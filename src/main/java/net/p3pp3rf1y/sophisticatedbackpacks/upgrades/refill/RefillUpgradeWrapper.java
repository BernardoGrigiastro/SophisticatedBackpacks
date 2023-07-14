package net.p3pp3rf1y.sophisticatedbackpacks.upgrades.refill;

import com.google.common.collect.ImmutableMap;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemHandlerHelper;
import io.github.fabricators_of_create.porting_lib.transfer.item.SlotExposedStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.item.PlayerInventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.p3pp3rf1y.sophisticatedbackpacks.api.IBlockPickResponseUpgrade;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackItem;
import net.p3pp3rf1y.sophisticatedbackpacks.client.gui.SBPTranslationHelper;
import net.p3pp3rf1y.sophisticatedcore.api.IStorageWrapper;
import net.p3pp3rf1y.sophisticatedcore.inventory.InventoryHandler;
import net.p3pp3rf1y.sophisticatedcore.inventory.SlotExposedStorageWrapper;
import net.p3pp3rf1y.sophisticatedcore.upgrades.FilterLogic;
import net.p3pp3rf1y.sophisticatedcore.upgrades.IFilteredUpgrade;
import net.p3pp3rf1y.sophisticatedcore.upgrades.ITickableUpgrade;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeWrapperBase;
import net.p3pp3rf1y.sophisticatedcore.util.InventoryHelper;
import net.p3pp3rf1y.sophisticatedcore.util.NBTHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class RefillUpgradeWrapper extends UpgradeWrapperBase<RefillUpgradeWrapper, RefillUpgradeItem>
		implements IFilteredUpgrade, ITickableUpgrade, IBlockPickResponseUpgrade {
	private static final int COOLDOWN = 5;

	private final Map<Integer, TargetSlot> targetSlots;

	private final FilterLogic filterLogic;

	public RefillUpgradeWrapper(IStorageWrapper backpackWrapper, ItemStack upgrade, Consumer<ItemStack> upgradeSaveHandler) {
		super(backpackWrapper, upgrade, upgradeSaveHandler);
		filterLogic = new FilterLogic(upgrade, upgradeSaveHandler, upgradeItem.getFilterSlotCount());
		targetSlots = NBTHelper.getMap(upgrade.getOrCreateTag(), "targetSlots", Integer::valueOf, (k, tag) -> Optional.of(TargetSlot.fromName(tag.getAsString()))).orElseGet(HashMap::new);
		if (upgradeItem.allowsTargetSlotSelection()) {
			FilterLogic.ObservableFilterItemStackHandler filterHandler = filterLogic.getFilterHandler();
			filterHandler.setOnSlotChange(s -> onFilterChange(filterHandler, s));
		}
		filterLogic.setAllowByDefault(true);
	}

	private void onFilterChange(FilterLogic.ObservableFilterItemStackHandler filterHandler, int slot) {
		if (filterHandler.getStackInSlot(slot).isEmpty()) {
			targetSlots.remove(slot);
			saveTargetSlots();
		} else {
			if (!targetSlots.containsKey(slot)) {
				setTargetSlot(slot, TargetSlot.ANY);
			}
		}
	}

	public Map<Integer, TargetSlot> getTargetSlots() {
		return targetSlots;
	}

	public void setTargetSlot(int slot, TargetSlot targetSlot) {
		targetSlots.put(slot, targetSlot);
		saveTargetSlots();
	}

	private void saveTargetSlots() {
		NBTHelper.putMap(upgrade.getOrCreateTag(), "targetSlots", targetSlots, String::valueOf, t -> StringTag.valueOf(t.getSerializedName()));
		save();
	}

	@Override
	public FilterLogic getFilterLogic() {
		return filterLogic;
	}

	@Override
	public void tick(@Nullable LivingEntity entity, Level world, BlockPos pos) {
		if (entity == null /*not supported in block form*/ || isInCooldown(world)) {
			return;
		}
		if (entity instanceof Player player) {

			InventoryHelper.iterate(filterLogic.getFilterHandler(), (slot, filter) -> {
				if (filter.isEmpty()) {
					return;
				}

				tryRefillFilter(entity, new SlotExposedStorageWrapper(PlayerInventoryStorage.of(player)), filter, getTargetSlots().getOrDefault(slot, TargetSlot.ANY));
			});
		}
		setCooldown(world, COOLDOWN);
	}

	private void tryRefillFilter(@Nonnull LivingEntity entity, SlotExposedStorage playerInvHandler, ItemStack filter, TargetSlot targetSlot) {
		if (!(entity instanceof Player player)) {
			return;
		}
		int missingCount = targetSlot.missingCountGetter.getMissingCount(player, playerInvHandler, filter);
		if (ItemHandlerHelper.canItemStacksStack(player.containerMenu.getCarried(), filter)) {
			missingCount -= Math.min(missingCount, player.containerMenu.getCarried().getCount());
		}
		if (missingCount == 0) {
			return;
		}
		SlotExposedStorage extractFromHandler = storageWrapper.getInventoryForUpgradeProcessing();
		ItemStack toMove = filter.copy();
		toMove.setCount(missingCount);
		ItemStack extracted = InventoryHelper.simulateExtractFromInventory(toMove, extractFromHandler, null);
		if (extracted.isEmpty()) {
			return;
		}
		ItemStack remaining = targetSlot.filler.fill(player, playerInvHandler, extracted);
		if (remaining.getCount() != extracted.getCount()) {
			ItemStack toExtract = extracted.copy();
			toExtract.setCount(extracted.getCount() - remaining.getCount());
			InventoryHelper.extractFromInventory(toExtract, extractFromHandler, null);
		}
	}

	public boolean allowsTargetSlotSelection() {
		return upgradeItem.allowsTargetSlotSelection();
	}

	@Override
	public boolean pickBlock(Player player, ItemStack filter) {
		if (!upgradeItem.supportsBlockPick()) {
			return false;
		}

		AtomicInteger stashSlot = new AtomicInteger(-1);
		AtomicBoolean hasItemInBackpack = new AtomicBoolean(false);

		InventoryHandler inventoryHandler = storageWrapper.getInventoryHandler();
		InventoryHelper.iterate(inventoryHandler, (slot, stack) -> {
			if (ItemHandlerHelper.canItemStacksStack(stack, filter)) {
				hasItemInBackpack.set(true);
				if (stack.getCount() <= stack.getMaxStackSize()) {
					stashSlot.set(slot);
				}
			}
		}, () -> stashSlot.get() > -1);

		ItemStack mainHandItem = player.getMainHandItem();
		ItemVariant resource = ItemVariant.of(mainHandItem);
		int count = mainHandItem.getCount();
		if (hasItemInBackpack.get() && !(mainHandItem.getItem() instanceof BackpackItem) &&
				(mainHandItem.isEmpty() || (stashSlot.get() > -1 && inventoryHandler.isItemValid(stashSlot.get(), resource, count)) || inventoryHandler.simulateInsert(resource, count, null) == 0)) {
			ItemStack toExtract = filter.copy();
			toExtract.setCount(filter.getMaxStackSize());
			ItemStack extracted = InventoryHelper.extractFromInventory(toExtract, inventoryHandler, null);
			if (!extracted.isEmpty()) {
				inventoryHandler.insert(resource, count, null);
				player.setItemInHand(InteractionHand.MAIN_HAND, extracted);
				return true;
			}
		}
		return false;
	}

	public enum TargetSlot implements StringRepresentable {
		ANY("any", SBPTranslationHelper.INSTANCE.translUpgrade("refill.target_slot.any"), SBPTranslationHelper.INSTANCE.translUpgrade("refill.target_slot.any.tooltip").withStyle(ChatFormatting.DARK_GREEN),
				(player, playerInvHandler, filter) -> InventoryHelper.getCountMissingInHandler(playerInvHandler, filter, filter.getMaxStackSize()),
				(player, playerInvHandler, stackToAdd) -> refillAnywhereInInventory(playerInvHandler, stackToAdd)),
		MAIN_HAND("main_hand", SBPTranslationHelper.INSTANCE.translUpgrade("refill.target_slot.main_hand"), SBPTranslationHelper.INSTANCE.translUpgrade("refill.target_slot.main_hand.tooltip").withStyle(ChatFormatting.DARK_GREEN),
				(player, playerInvHandler, filter) -> getMissingCount(player.getMainHandItem(), filter),
				(player, playerInvHandler, stackToAdd) -> refillSlot(player::getMainHandItem, stackToAdd, stack -> player.setItemInHand(InteractionHand.MAIN_HAND, stack))),
		OFF_HAND("off_hand", SBPTranslationHelper.INSTANCE.translUpgrade("refill.target_slot.off_hand"), SBPTranslationHelper.INSTANCE.translUpgrade("refill.target_slot.off_hand.tooltip").withStyle(ChatFormatting.DARK_GREEN),
				(player, playerInvHandler, filter) -> getMissingCount(player.getOffhandItem(), filter),
				(player, playerInvHandler, stackToAdd) -> refillSlot(player::getOffhandItem, stackToAdd, stack -> player.setItemInHand(InteractionHand.OFF_HAND, stack))),
		TOOLBAR_1("toolbar_1", Component.literal("1"), SBPTranslationHelper.INSTANCE.translUpgrade(Constants.HOTBAR_TRANSL, 1).withStyle(ChatFormatting.DARK_GREEN),
				(player, playerInvHandler, filter) -> getMissingCount(player.getInventory().getItem(0), filter),
				(player, playerInvHandler, stackToAdd) -> refillSlot(() -> player.getInventory().getItem(0), stackToAdd, stack -> player.getInventory().setItem(0, stack))),
		TOOLBAR_2("toolbar_2", Component.literal("2"), SBPTranslationHelper.INSTANCE.translUpgrade(Constants.HOTBAR_TRANSL, 2).withStyle(ChatFormatting.DARK_GREEN),
				(player, playerInvHandler, filter) -> getMissingCount(player.getInventory().getItem(1), filter),
				(player, playerInvHandler, stackToAdd) -> refillSlot(() -> player.getInventory().getItem(1), stackToAdd, stack -> player.getInventory().setItem(1, stack))),
		TOOLBAR_3("toolbar_3", Component.literal("3"), SBPTranslationHelper.INSTANCE.translUpgrade(Constants.HOTBAR_TRANSL, 3).withStyle(ChatFormatting.DARK_GREEN),
				(player, playerInvHandler, filter) -> getMissingCount(player.getInventory().getItem(2), filter),
				(player, playerInvHandler, stackToAdd) -> refillSlot(() -> player.getInventory().getItem(2), stackToAdd, stack -> player.getInventory().setItem(2, stack))),
		TOOLBAR_4("toolbar_4", Component.literal("4"), SBPTranslationHelper.INSTANCE.translUpgrade(Constants.HOTBAR_TRANSL, 4).withStyle(ChatFormatting.DARK_GREEN),
				(player, playerInvHandler, filter) -> getMissingCount(player.getInventory().getItem(3), filter),
				(player, playerInvHandler, stackToAdd) -> refillSlot(() -> player.getInventory().getItem(3), stackToAdd, stack -> player.getInventory().setItem(3, stack))),
		TOOLBAR_5("toolbar_5", Component.literal("5"), SBPTranslationHelper.INSTANCE.translUpgrade(Constants.HOTBAR_TRANSL, 5).withStyle(ChatFormatting.DARK_GREEN),
				(player, playerInvHandler, filter) -> getMissingCount(player.getInventory().getItem(4), filter),
				(player, playerInvHandler, stackToAdd) -> refillSlot(() -> player.getInventory().getItem(4), stackToAdd, stack -> player.getInventory().setItem(4, stack))),
		TOOLBAR_6("toolbar_6", Component.literal("6"), SBPTranslationHelper.INSTANCE.translUpgrade(Constants.HOTBAR_TRANSL, 6).withStyle(ChatFormatting.DARK_GREEN),
				(player, playerInvHandler, filter) -> getMissingCount(player.getInventory().getItem(5), filter),
				(player, playerInvHandler, stackToAdd) -> refillSlot(() -> player.getInventory().getItem(5), stackToAdd, stack -> player.getInventory().setItem(5, stack))),
		TOOLBAR_7("toolbar_7", Component.literal("7"), SBPTranslationHelper.INSTANCE.translUpgrade(Constants.HOTBAR_TRANSL, 7).withStyle(ChatFormatting.DARK_GREEN),
				(player, playerInvHandler, filter) -> getMissingCount(player.getInventory().getItem(6), filter),
				(player, playerInvHandler, stackToAdd) -> refillSlot(() -> player.getInventory().getItem(6), stackToAdd, stack -> player.getInventory().setItem(6, stack))),
		TOOLBAR_8("toolbar_8", Component.literal("8"), SBPTranslationHelper.INSTANCE.translUpgrade(Constants.HOTBAR_TRANSL, 8).withStyle(ChatFormatting.DARK_GREEN),
				(player, playerInvHandler, filter) -> getMissingCount(player.getInventory().getItem(7), filter),
				(player, playerInvHandler, stackToAdd) -> refillSlot(() -> player.getInventory().getItem(7), stackToAdd, stack -> player.getInventory().setItem(7, stack))),
		TOOLBAR_9("toolbar_9", Component.literal("9"), SBPTranslationHelper.INSTANCE.translUpgrade(Constants.HOTBAR_TRANSL, 9).withStyle(ChatFormatting.DARK_GREEN),
				(player, playerInvHandler, filter) -> getMissingCount(player.getInventory().getItem(8), filter),
				(player, playerInvHandler, stackToAdd) -> refillSlot(() -> player.getInventory().getItem(8), stackToAdd, stack -> player.getInventory().setItem(8, stack)));

		private final String name;

		private final Component acronym;
		private final Component description;
		private final MissingCountGetter missingCountGetter;
		private final Filler filler;

		TargetSlot(String name, Component acronym, Component description, MissingCountGetter missingCountGetter, Filler filler) {
			this.name = name;
			this.acronym = acronym;
			this.description = description;
			this.missingCountGetter = missingCountGetter;
			this.filler = filler;
		}

		@Override
		public String getSerializedName() {
			return name;
		}

		public TargetSlot next() {
			return VALUES[(ordinal() + 1) % VALUES.length];
		}

		public TargetSlot previous() {
			return VALUES[Math.floorMod(ordinal() - 1, VALUES.length)];
		}

		private static final Map<String, TargetSlot> NAME_VALUES;
		private static final TargetSlot[] VALUES;

		static {
			ImmutableMap.Builder<String, TargetSlot> builder = new ImmutableMap.Builder<>();
			for (TargetSlot value : TargetSlot.values()) {
				builder.put(value.getSerializedName(), value);
			}
			NAME_VALUES = builder.build();
			VALUES = values();
		}

		public static TargetSlot fromName(String name) {
			return NAME_VALUES.getOrDefault(name, ANY);
		}

		public Component getAcronym() {
			return acronym;
		}

		public Component getDescription() {
			return description;
		}

		private static class Constants {
			private static final String HOTBAR_TRANSL = "refill.target_slot.hotbar.tooltip";
		}

		private interface MissingCountGetter {
			int getMissingCount(Player player, SlotExposedStorage playerInventory, ItemStack filter);
		}

		private interface Filler {
			ItemStack fill(Player player, SlotExposedStorage playerInventory, ItemStack stackToAdd);
		}

		// TODO: refactor
		private static ItemStack refillAnywhereInInventory(SlotExposedStorage playerInvHandler, ItemStack extracted) {
			ItemVariant resource = ItemVariant.of(extracted);
			AtomicReference<ItemVariant> atomicResource = new AtomicReference<>(resource);
			AtomicLong maxAmount = new AtomicLong(extracted.getCount());

			InventoryHelper.iterate(playerInvHandler, (slot, stack) -> {
				if (ItemHandlerHelper.canItemStacksStack(stack, atomicResource.get().toStack(maxAmount.intValue()))) {
					try (Transaction outer = Transaction.openOuter()) {
						maxAmount.set(maxAmount.get() - playerInvHandler.insertSlot(slot, atomicResource.get(), maxAmount.get(), outer));
						outer.commit();
					}
				}
			}, () -> maxAmount.get() == 0);

			long remaining = maxAmount.get();
			if (remaining > 0) {
				try (Transaction outer = Transaction.openOuter()) {
					long inserted = InventoryHelper.insertIntoInventory(resource, remaining, playerInvHandler, outer);
					if (inserted <= 0) {
						return resource.toStack((int) remaining);
					}
				}

				try (Transaction outer = Transaction.openOuter()) {
					ItemStack result = resource.toStack((int) (remaining - InventoryHelper.insertIntoInventory(resource, remaining, playerInvHandler, outer)));
					outer.commit();
					return result;
				}
			}
			return resource.toStack((int) remaining);
		}

		private static int getMissingCount(ItemStack stack, ItemStack filter) {
			if (ItemHandlerHelper.canItemStacksStack(stack, filter)) {
				return filter.getMaxStackSize() - stack.getCount();
			}
			return filter.getMaxStackSize();
		}

		private static ItemStack refillSlot(Supplier<ItemStack> getSlotContents, ItemStack stackToAdd, Consumer<ItemStack> setSlotContents) {
			ItemStack contents = getSlotContents.get();
			if (contents.isEmpty()) {
				setSlotContents.accept(stackToAdd);
				return ItemStack.EMPTY;
			}
			if (ItemHandlerHelper.canItemStacksStack(contents, stackToAdd)) {
				contents.grow(stackToAdd.getCount());
				return ItemStack.EMPTY;
			}
			return stackToAdd;
		}
	}
}
