package net.p3pp3rf1y.sophisticatedbackpacks.client;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.fabricators_of_create.porting_lib.event.client.KeyInputCallback;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.impl.client.screen.ScreenEventFactory;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackItem;
import net.p3pp3rf1y.sophisticatedbackpacks.client.gui.BackpackScreen;
import net.p3pp3rf1y.sophisticatedbackpacks.client.gui.SBPTranslationHelper;
import net.p3pp3rf1y.sophisticatedbackpacks.common.gui.BackpackContainer;
import net.p3pp3rf1y.sophisticatedbackpacks.network.BackpackCloseMessage;
import net.p3pp3rf1y.sophisticatedbackpacks.network.BackpackOpenMessage;
import net.p3pp3rf1y.sophisticatedbackpacks.network.BlockToolSwapMessage;
import net.p3pp3rf1y.sophisticatedbackpacks.network.EntityToolSwapMessage;
import net.p3pp3rf1y.sophisticatedbackpacks.network.InventoryInteractionMessage;
import net.p3pp3rf1y.sophisticatedbackpacks.network.SBPPacketHandler;
import net.p3pp3rf1y.sophisticatedbackpacks.network.UpgradeToggleMessage;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.GuiHelper;
import net.p3pp3rf1y.sophisticatedcore.util.WorldHelper;

import java.util.Map;
import java.util.Optional;


public class KeybindHandler {
	private KeybindHandler() {}

	private static final int KEY_B = 66;
	private static final int KEY_C = 67;
	private static final int KEY_Z = 90;
	private static final int KEY_X = 88;
	private static final int KEY_UNKNOWN = -1;
	private static final int MIDDLE_BUTTON = 2;
	private static final int CHEST_SLOT_INDEX = 6;
	private static final String KEYBIND_SOPHISTICATEDBACKPACKS_CATEGORY = "keybind.sophisticatedbackpacks.category";
	public static final KeyMapping BACKPACK_TOGGLE_UPGRADE_5 = new KeyMapping(SBPTranslationHelper.INSTANCE.translKeybind("toggle_upgrade_5"),
			KeyConflictContext.UNIVERSAL, InputConstants.Type.KEYSYM.getOrCreate(KEY_UNKNOWN), KEYBIND_SOPHISTICATEDBACKPACKS_CATEGORY);
/*	public static final KeyMapping BACKPACK_TOGGLE_UPGRADE_5 = new KeyMapping(SBPTranslationHelper.INSTANCE.translKeybind("toggle_upgrade_5"),
			KeyConflictContext.UNIVERSAL, InputConstants.Type.KEYSYM.getOrCreate(KEY_UNKNOWN), KEYBIND_SOPHISTICATEDBACKPACKS_CATEGORY);*/
	public static final KeyMapping BACKPACK_TOGGLE_UPGRADE_4 = new KeyMapping(SBPTranslationHelper.INSTANCE.translKeybind("toggle_upgrade_4"),
		KeyConflictContext.UNIVERSAL, InputConstants.Type.KEYSYM.getOrCreate(KEY_UNKNOWN), KEYBIND_SOPHISTICATEDBACKPACKS_CATEGORY);
/*	public static final KeyMapping BACKPACK_TOGGLE_UPGRADE_4 = new KeyMapping(SBPTranslationHelper.INSTANCE.translKeybind("toggle_upgrade_4"),
			KeyConflictContext.UNIVERSAL, InputConstants.Type.KEYSYM.getOrCreate(KEY_UNKNOWN), KEYBIND_SOPHISTICATEDBACKPACKS_CATEGORY);*/
	public static final KeyMapping BACKPACK_TOGGLE_UPGRADE_3 = new KeyMapping(SBPTranslationHelper.INSTANCE.translKeybind("toggle_upgrade_3"),
		KeyConflictContext.UNIVERSAL, InputConstants.Type.KEYSYM.getOrCreate(KEY_UNKNOWN), KEYBIND_SOPHISTICATEDBACKPACKS_CATEGORY);
/*	public static final KeyMapping BACKPACK_TOGGLE_UPGRADE_3 = new KeyMapping(SBPTranslationHelper.INSTANCE.translKeybind("toggle_upgrade_3"),
			KeyConflictContext.UNIVERSAL, InputConstants.Type.KEYSYM.getOrCreate(KEY_UNKNOWN), KEYBIND_SOPHISTICATEDBACKPACKS_CATEGORY);*/
	public static final KeyMapping BACKPACK_TOGGLE_UPGRADE_2 = new KeyMapping(SBPTranslationHelper.INSTANCE.translKeybind("toggle_upgrade_2"),
		KeyConflictContext.UNIVERSAL, KeyModifier.ALT, InputConstants.Type.KEYSYM.getOrCreate(KEY_X), KEYBIND_SOPHISTICATEDBACKPACKS_CATEGORY);
/*	public static final KeyMapping BACKPACK_TOGGLE_UPGRADE_2 = new KeyMapping(SBPTranslationHelper.INSTANCE.translKeybind("toggle_upgrade_2"),
			KeyConflictContext.UNIVERSAL, KeyModifier.ALT, InputConstants.Type.KEYSYM.getOrCreate(KEY_X), KEYBIND_SOPHISTICATEDBACKPACKS_CATEGORY);*/
	public static final KeyMapping BACKPACK_TOGGLE_UPGRADE_1 = new KeyMapping(SBPTranslationHelper.INSTANCE.translKeybind("toggle_upgrade_1"),
		KeyConflictContext.UNIVERSAL, KeyModifier.ALT, InputConstants.Type.KEYSYM.getOrCreate(KEY_Z), KEYBIND_SOPHISTICATEDBACKPACKS_CATEGORY);
/*	public static final KeyMapping BACKPACK_TOGGLE_UPGRADE_1 = new KeyMapping(SBPTranslationHelper.INSTANCE.translKeybind("toggle_upgrade_1"),
			KeyConflictContext.UNIVERSAL, KeyModifier.ALT, InputConstants.Type.KEYSYM.getOrCreate(KEY_Z), KEYBIND_SOPHISTICATEDBACKPACKS_CATEGORY);*/

	public static final Map<Integer, KeyMapping> UPGRADE_SLOT_TOGGLE_KEYBINDS = Map.of(
			0, BACKPACK_TOGGLE_UPGRADE_1,
			1, BACKPACK_TOGGLE_UPGRADE_2,
			2, BACKPACK_TOGGLE_UPGRADE_3,
			3, BACKPACK_TOGGLE_UPGRADE_4,
			4, BACKPACK_TOGGLE_UPGRADE_5
	);
	public static final KeyMapping SORT_KEYBIND = new KeyMapping(SBPTranslationHelper.INSTANCE.translKeybind("sort"),
			BackpackGuiKeyConflictContext.INSTANCE, InputConstants.Type.MOUSE.getOrCreate(MIDDLE_BUTTON), KEYBIND_SOPHISTICATEDBACKPACKS_CATEGORY);
/*	public static final KeyMapping SORT_KEYBIND = new KeyMapping(SBPTranslationHelper.INSTANCE.translKeybind("sort"),
			BackpackGuiKeyConflictContext.INSTANCE, InputConstants.Type.MOUSE.getOrCreate(MIDDLE_BUTTON), KEYBIND_SOPHISTICATEDBACKPACKS_CATEGORY);*/
	public static final KeyMapping TOOL_SWAP_KEYBIND = new KeyMapping(SBPTranslationHelper.INSTANCE.translKeybind("tool_swap"),
		KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM.getOrCreate(KEY_UNKNOWN), KEYBIND_SOPHISTICATEDBACKPACKS_CATEGORY);
/*	public static final KeyMapping TOOL_SWAP_KEYBIND = new KeyMapping(SBPTranslationHelper.INSTANCE.translKeybind("tool_swap"),
			KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM.getOrCreate(KEY_UNKNOWN), KEYBIND_SOPHISTICATEDBACKPACKS_CATEGORY);*/
	public static final KeyMapping INVENTORY_INTERACTION_KEYBIND = new KeyMapping(SBPTranslationHelper.INSTANCE.translKeybind("inventory_interaction"),
			KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM.getOrCreate(KEY_C), KEYBIND_SOPHISTICATEDBACKPACKS_CATEGORY);
	public static final KeyMapping BACKPACK_OPEN_KEYBIND = new KeyMapping(SBPTranslationHelper.INSTANCE.translKeybind("open_backpack"),
			BackpackKeyConflictContext.INSTANCE, InputConstants.Type.KEYSYM.getOrCreate(KEY_B), KEYBIND_SOPHISTICATEDBACKPACKS_CATEGORY);

	public static void register() {
		ScreenEventFactory.createAfterMouseClickEvent().register(KeybindHandler::handleGuiMouseKeyPress);
		ScreenEventFactory.createBeforeKeyPressEvent().register(KeybindHandler::handleGuiKeyPress);

		KeyInputCallback.EVENT.register(KeybindHandler::handleKeyInputEvent);
	}

	public static void registerKeyMappings() {
		KeyBindingHelper.registerKeyBinding(BACKPACK_OPEN_KEYBIND);
		KeyBindingHelper.registerKeyBinding(INVENTORY_INTERACTION_KEYBIND);
		KeyBindingHelper.registerKeyBinding(TOOL_SWAP_KEYBIND);
		KeyBindingHelper.registerKeyBinding(SORT_KEYBIND);

		UPGRADE_SLOT_TOGGLE_KEYBINDS.forEach((slot, keybind) -> KeyBindingHelper.registerKeyBinding(keybind));
	}

	public static void handleGuiKeyPress(Screen screen, int key, int scancode, int modifiers) {
		if (SORT_KEYBIND.matchesMouse(InputConstants.getKey(key, scancode).getValue()) && tryCallSort(screen)) {
			event.setCanceled(true);
		}
	}

	public static void handleGuiMouseKeyPress(Screen screen, double mouseX, double mouseY, int button) {
		InputConstants.Key input = InputConstants.Type.MOUSE.getOrCreate(button);
		if (SORT_KEYBIND.matchesMouse(input.getValue()) && tryCallSort(screen)) {
			event.setCanceled(true);
		} else if (BACKPACK_OPEN_KEYBIND.matchesMouse(input.getValue())) {
			sendBackpackOpenOrCloseMessage();
		}
	}

	public static void handleKeyInputEvent(int key, int scancode, int action, int mods) {
		if (BACKPACK_OPEN_KEYBIND.consumeClick()) {
			sendBackpackOpenOrCloseMessage();
		} else if (INVENTORY_INTERACTION_KEYBIND.consumeClick()) {
			sendInteractWithInventoryMessage();
		} else if (TOOL_SWAP_KEYBIND.consumeClick()) {
			sendToolSwapMessage();
		} else {
			for (Map.Entry<Integer, KeyMapping> slotKeybind : UPGRADE_SLOT_TOGGLE_KEYBINDS.entrySet()) {
				if (slotKeybind.getValue().consumeClick()) {
					SBPPacketHandler.sendToServer(new UpgradeToggleMessage(slotKeybind.getKey()));
				}
			}
		}
	}

	private static boolean tryCallSort(Screen gui) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.player != null && mc.player.containerMenu instanceof BackpackContainer container && gui instanceof BackpackScreen screen) {
			MouseHandler mh = mc.mouseHandler;
			double mouseX = mh.xpos() * mc.getWindow().getGuiScaledWidth() / mc.getWindow().getScreenWidth();
			double mouseY = mh.ypos() * mc.getWindow().getGuiScaledHeight() / mc.getWindow().getScreenHeight();
			Slot selectedSlot = screen.findSlot(mouseX, mouseY);
			if (selectedSlot != null && container.isNotPlayersInventorySlot(selectedSlot.index)) {
				container.sort();
				return true;
			}
		}
		return false;
	}

	private static void sendToolSwapMessage() {
		Minecraft mc = Minecraft.getInstance();
		LocalPlayer player = mc.player;
		if (player == null || mc.hitResult == null) {
			return;
		}
		if (player.getMainHandItem().getItem() instanceof BackpackItem) {
			player.displayClientMessage(Component.translatable("gui.sophisticatedbackpacks.status.unable_to_swap_tool_for_backpack"), true);
			return;
		}
		HitResult rayTrace = mc.hitResult;
		if (rayTrace.getType() == HitResult.Type.BLOCK) {
			BlockHitResult blockRayTraceResult = (BlockHitResult) rayTrace;
			BlockPos pos = blockRayTraceResult.getBlockPos();
			SBPPacketHandler.sendToServer(new BlockToolSwapMessage(pos));
		} else if (rayTrace.getType() == HitResult.Type.ENTITY) {
			EntityHitResult entityRayTraceResult = (EntityHitResult) rayTrace;
			SBPPacketHandler.sendToServer(new EntityToolSwapMessage(entityRayTraceResult.getEntity().getId()));
		}
	}

	private static void sendInteractWithInventoryMessage() {
		Minecraft mc = Minecraft.getInstance();
		HitResult rayTrace = mc.hitResult;
		if (rayTrace == null || rayTrace.getType() != HitResult.Type.BLOCK) {
			return;
		}
		BlockHitResult blockraytraceresult = (BlockHitResult) rayTrace;
		BlockPos pos = blockraytraceresult.getBlockPos();

		if (!WorldHelper.getBlockEntity(mc.level, pos, BlockEntity.class).map(te -> te.getCapability(ForgeCapabilities.ITEM_HANDLER).isPresent()).orElse(false)) {
			return;
		}

		SBPPacketHandler.sendToServer(new InventoryInteractionMessage(pos, blockraytraceresult.getDirection()));
	}

	@SuppressWarnings({"java:S2440", "InstantiationOfUtilityClass"})
	private static void sendBackpackOpenOrCloseMessage() {
		if (!GUI.isActive()) {
			SBPPacketHandler.sendToServer(new BackpackOpenMessage());
			return;
		}

		Screen screen = Minecraft.getInstance().screen;
		if (screen instanceof BackpackScreen backpackScreen) {
			Optional<Slot> slot = GuiHelper.getSlotUnderMouse(backpackScreen);
			if (slot.isPresent() && slot.get().getItem().getItem() instanceof BackpackItem) {
				if (slot.get().getItem().getCount() == 1) {
					SBPPacketHandler.sendToServer(new BackpackOpenMessage(slot.get().index));
				}
			} else {
				SBPPacketHandler.sendToServer(new BackpackCloseMessage());
			}
		} else if (screen instanceof InventoryScreen inventoryScreen) {
			Optional<Slot> slot = GuiHelper.getSlotUnderMouse(inventoryScreen);

			if (slot.isPresent() && isSupportedPlayerInventorySlot(slot.get().index) && slot.get().getItem().getItem() instanceof BackpackItem) {
				SBPPacketHandler.sendToServer(new BackpackOpenMessage(slot.get().getSlotIndex()));
			}
		}
	}

	private static boolean isSupportedPlayerInventorySlot(int slotIndex) {
		return slotIndex == CHEST_SLOT_INDEX || (slotIndex > 8 && slotIndex < 46);
	}

	private static class BackpackKeyConflictContext implements IKeyConflictContext {
		public static final BackpackKeyConflictContext INSTANCE = new BackpackKeyConflictContext();

		@Override
		public boolean isActive() {
			if (!GUI.isActive()) {
				return true;
			}
			Screen screen = Minecraft.getInstance().screen;
			return screen instanceof BackpackScreen || screen instanceof InventoryScreen;
		}

		@Override
		public boolean conflicts(IKeyConflictContext other) {
			return this == other;
		}
	}

	private static class BackpackGuiKeyConflictContext implements IKeyConflictContext {
		public static final BackpackGuiKeyConflictContext INSTANCE = new BackpackGuiKeyConflictContext();

		@Override
		public boolean isActive() {
			return GUI.isActive() && Minecraft.getInstance().screen instanceof BackpackScreen;
		}

		@Override
		public boolean conflicts(IKeyConflictContext other) {
			return this == other;
		}
	}
}
