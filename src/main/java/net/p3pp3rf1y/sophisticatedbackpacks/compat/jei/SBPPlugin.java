package net.p3pp3rf1y.sophisticatedbackpacks.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.helpers.IStackHelper;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.LegacyUpgradeRecipe;
import net.p3pp3rf1y.sophisticatedbackpacks.SophisticatedBackpacks;
import net.p3pp3rf1y.sophisticatedbackpacks.common.components.IBackpackWrapper;
import net.p3pp3rf1y.sophisticatedbackpacks.client.gui.BackpackScreen;
import net.p3pp3rf1y.sophisticatedbackpacks.client.gui.BackpackSettingsScreen;
import net.p3pp3rf1y.sophisticatedbackpacks.common.gui.BackpackContainer;
import net.p3pp3rf1y.sophisticatedbackpacks.crafting.BackpackUpgradeRecipe;
import net.p3pp3rf1y.sophisticatedbackpacks.crafting.SmithingBackpackUpgradeRecipe;
import net.p3pp3rf1y.sophisticatedbackpacks.init.ModItems;
import net.p3pp3rf1y.sophisticatedcore.client.gui.SettingsScreen;
import net.p3pp3rf1y.sophisticatedcore.compat.jei.ClientRecipeHelper;
import net.p3pp3rf1y.sophisticatedcore.compat.jei.CraftingContainerRecipeTransferHandlerBase;
import net.p3pp3rf1y.sophisticatedcore.compat.jei.SettingsGhostIngredientHandler;
import net.p3pp3rf1y.sophisticatedcore.compat.jei.StorageGhostIngredientHandler;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@JeiPlugin
public class SBPPlugin implements IModPlugin {
	@Override
	public ResourceLocation getPluginUid() {
		return SophisticatedBackpacks.getRL("default");
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistration registration) {
		IIngredientSubtypeInterpreter<ItemStack> backpackNbtInterpreter = (itemStack, context) -> IBackpackWrapper.maybeGet(itemStack)
				.map(wrapper -> "{clothColor:" + wrapper.getMainColor() + ",borderColor:" + wrapper.getAccentColor() + "}")
				.orElse(IIngredientSubtypeInterpreter.NONE);
		registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, ModItems.BACKPACK.get(), backpackNbtInterpreter);
		registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, ModItems.IRON_BACKPACK.get(), backpackNbtInterpreter);
		registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, ModItems.GOLD_BACKPACK.get(), backpackNbtInterpreter);
		registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, ModItems.DIAMOND_BACKPACK.get(), backpackNbtInterpreter);
		registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, ModItems.NETHERITE_BACKPACK.get(), backpackNbtInterpreter);
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		registration.addGuiContainerHandler(BackpackScreen.class, new IGuiContainerHandler<>() {
			@Override
			public List<Rect2i> getGuiExtraAreas(BackpackScreen gui) {
				List<Rect2i> ret = new ArrayList<>();
				gui.getUpgradeSlotsRectangle().ifPresent(ret::add);
				ret.addAll(gui.getUpgradeSettingsControl().getTabRectangles());
				gui.getSortButtonsRectangle().ifPresent(ret::add);
				return ret;
			}
		});

		registration.addGuiContainerHandler(BackpackSettingsScreen.class, new IGuiContainerHandler<>() {
			@Override
			public List<Rect2i> getGuiExtraAreas(BackpackSettingsScreen gui) {
				return new ArrayList<>(gui.getSettingsTabControl().getTabRectangles());
			}
		});

		registration.addGhostIngredientHandler(BackpackScreen.class, new StorageGhostIngredientHandler<>());
		registration.addGhostIngredientHandler(SettingsScreen.class, new SettingsGhostIngredientHandler<>());
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		registration.addRecipes(RecipeTypes.CRAFTING, DyeRecipesMaker.getRecipes());
		registration.addRecipes(RecipeTypes.CRAFTING, ClientRecipeHelper.getAndTransformAvailableRecipes(BackpackUpgradeRecipe.REGISTERED_RECIPES, ShapedRecipe.class, ClientRecipeHelper::copyShapedRecipe));
		registration.addRecipes(RecipeTypes.SMITHING, ClientRecipeHelper.getAndTransformAvailableRecipes(SmithingBackpackUpgradeRecipe.REGISTERED_RECIPES, LegacyUpgradeRecipe.class, this::copyUpgradeRecipe));
	}

	private LegacyUpgradeRecipe copyUpgradeRecipe(LegacyUpgradeRecipe recipe) {
		return new LegacyUpgradeRecipe(recipe.getId(), recipe.base, recipe.addition, recipe.getResultItem(null));
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(new ItemStack(ModItems.CRAFTING_UPGRADE.get()), RecipeTypes.CRAFTING);
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
		IRecipeTransferHandlerHelper handlerHelper = registration.getTransferHelper();
		IStackHelper stackHelper = registration.getJeiHelpers().getStackHelper();
		registration.addRecipeTransferHandler(new CraftingContainerRecipeTransferHandlerBase<BackpackContainer>(handlerHelper, stackHelper) {
			@Override
			public Class<BackpackContainer> getContainerClass() {
				return BackpackContainer.class;
			}
		}, RecipeTypes.CRAFTING);
	}

}

