package net.p3pp3rf1y.sophisticatedbackpacks.data;

import me.alphamode.forgetags.Tags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.p3pp3rf1y.sophisticatedbackpacks.SophisticatedBackpacks;
import net.p3pp3rf1y.sophisticatedbackpacks.crafting.SmithingBackpackUpgradeRecipe;
import net.p3pp3rf1y.sophisticatedbackpacks.init.ModItems;
import net.p3pp3rf1y.sophisticatedcore.crafting.ShapeBasedRecipeBuilder;
import net.p3pp3rf1y.sophisticatedcore.init.ModRecipes;
import net.p3pp3rf1y.sophisticatedcore.util.RegistryHelper;

import java.util.function.Consumer;

public class SBPRecipeProvider extends FabricRecipeProvider {
	private static final String HAS_UPGRADE_BASE = "has_upgrade_base";
	private static final String HAS_SMELTING_UPGRADE = "has_smelting_upgrade";

	public SBPRecipeProvider(FabricDataOutput output) {
		super(output);
	}

	@Override
	public void buildRecipes(Consumer<FinishedRecipe> consumer) {
		ShapeBasedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BACKPACK.get(), ModItems.BASIC_BACKPACK_RECIPE_SERIALIZER.get())
				.pattern("SLS")
				.pattern("SCS")
				.pattern("LLL")
				.define('L', Tags.Items.LEATHER)
				.define('C', Tags.Items.CHESTS_WOODEN)
				.define('S', Tags.Items.STRING)
				.unlockedBy("has_leather", hasLeather())
				.save(consumer);

		SpecialRecipeBuilder.special(ModItems.BACKPACK_DYE_RECIPE_SERIALIZER.get()).save(consumer, SophisticatedBackpacks.getRegistryName("backpack_dye"));

		ShapeBasedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.DIAMOND_BACKPACK.get(), ModItems.BACKPACK_UPGRADE_RECIPE_SERIALIZER.get())
				.pattern("DDD")
				.pattern("DBD")
				.pattern("DDD")
				.define('D', Tags.Items.GEMS_DIAMOND)
				.define('B', ModItems.GOLD_BACKPACK.get())
				.unlockedBy("has_gold_backpack", has(ModItems.GOLD_BACKPACK.get()))
				.save(consumer);

		ShapeBasedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.GOLD_BACKPACK.get(), ModItems.BACKPACK_UPGRADE_RECIPE_SERIALIZER.get())
				.pattern("GGG")
				.pattern("GBG")
				.pattern("GGG")
				.define('G', Tags.Items.INGOTS_GOLD)
				.define('B', ModItems.IRON_BACKPACK.get())
				.unlockedBy("has_iron_backpack", has(ModItems.IRON_BACKPACK.get()))
				.save(consumer);

		ShapeBasedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.IRON_BACKPACK.get(), ModItems.BACKPACK_UPGRADE_RECIPE_SERIALIZER.get())
				.pattern("III")
				.pattern("IBI")
				.pattern("III")
				.define('I', Tags.Items.INGOTS_IRON)
				.define('B', ModItems.BACKPACK.get())
				.unlockedBy("has_backpack", has(ModItems.BACKPACK.get()))
				.save(consumer);

		ShapeBasedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PICKUP_UPGRADE.get())
				.pattern(" P ")
				.pattern("SBS")
				.pattern("RRR")
				.define('B', ModItems.UPGRADE_BASE.get())
				.define('R', Tags.Items.DUSTS_REDSTONE)
				.define('S', Tags.Items.STRING)
				.define('P', Blocks.STICKY_PISTON)
				.unlockedBy(HAS_UPGRADE_BASE, has(ModItems.UPGRADE_BASE.get()))
				.save(consumer);

		ShapeBasedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.UPGRADE_BASE.get())
				.pattern("SIS")
				.pattern("ILI")
				.pattern("SIS")
				.define('L', Tags.Items.LEATHER)
				.define('I', Tags.Items.INGOTS_IRON)
				.define('S', Tags.Items.STRING)
				.unlockedBy("has_leather", hasLeather())
				.save(consumer);

		ShapeBasedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ADVANCED_PICKUP_UPGRADE.get(), ModRecipes.UPGRADE_NEXT_TIER_SERIALIZER.get())
				.pattern(" D ")
				.pattern("GPG")
				.pattern("RRR")
				.define('D', Tags.Items.GEMS_DIAMOND)
				.define('G', Tags.Items.INGOTS_GOLD)
				.define('R', Tags.Items.DUSTS_REDSTONE)
				.define('P', ModItems.PICKUP_UPGRADE.get())
				.unlockedBy("has_pickup_upgrade", has(ModItems.PICKUP_UPGRADE.get()))
				.save(consumer);

		ShapeBasedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.FILTER_UPGRADE.get())
				.pattern("RSR")
				.pattern("SBS")
				.pattern("RSR")
				.define('B', ModItems.UPGRADE_BASE.get())
				.define('R', Tags.Items.DUSTS_REDSTONE)
				.define('S', Tags.Items.STRING)
				.unlockedBy(HAS_UPGRADE_BASE, has(ModItems.UPGRADE_BASE.get()))
				.save(consumer);

		ShapeBasedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ADVANCED_FILTER_UPGRADE.get(), ModRecipes.UPGRADE_NEXT_TIER_SERIALIZER.get())
				.pattern("GPG")
				.pattern("RRR")
				.define('G', Tags.Items.INGOTS_GOLD)
				.define('R', Tags.Items.DUSTS_REDSTONE)
				.define('P', ModItems.FILTER_UPGRADE.get())
				.unlockedBy("has_filter_upgrade", has(ModItems.FILTER_UPGRADE.get()))
				.save(consumer);

		ShapeBasedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.MAGNET_UPGRADE.get(), ModRecipes.UPGRADE_NEXT_TIER_SERIALIZER.get())
				.pattern("EIE")
				.pattern("IPI")
				.pattern("R L")
				.define('E', Tags.Items.ENDER_PEARLS)
				.define('I', Tags.Items.INGOTS_IRON)
				.define('R', Tags.Items.DUSTS_REDSTONE)
				.define('L', Tags.Items.GEMS_LAPIS)
				.define('P', ModItems.PICKUP_UPGRADE.get())
				.unlockedBy("has_pickup_upgrade", has(ModItems.PICKUP_UPGRADE.get()))
				.save(consumer);

		ShapeBasedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ADVANCED_MAGNET_UPGRADE.get(), ModRecipes.UPGRADE_NEXT_TIER_SERIALIZER.get())
				.pattern("EIE")
				.pattern("IPI")
				.pattern("R L")
				.define('E', Tags.Items.ENDER_PEARLS)
				.define('I', Tags.Items.INGOTS_IRON)
				.define('R', Tags.Items.DUSTS_REDSTONE)
				.define('L', Tags.Items.GEMS_LAPIS)
				.define('P', ModItems.ADVANCED_PICKUP_UPGRADE.get())
				.unlockedBy("has_advanced_pickup_upgrade", has(ModItems.ADVANCED_PICKUP_UPGRADE.get()))
				.save(consumer);

		ShapeBasedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ADVANCED_MAGNET_UPGRADE.get(), ModRecipes.UPGRADE_NEXT_TIER_SERIALIZER.get())
				.pattern(" D ")
				.pattern("GMG")
				.pattern("RRR")
				.define('D', Tags.Items.GEMS_DIAMOND)
				.define('G', Tags.Items.INGOTS_GOLD)
				.define('R', Tags.Items.DUSTS_REDSTONE)
				.define('M', ModItems.MAGNET_UPGRADE.get())
				.unlockedBy("has_magnet_upgrade", has(ModItems.MAGNET_UPGRADE.get()))
				.save(consumer, new ResourceLocation(SophisticatedBackpacks.getRegistryName("advanced_magnet_upgrade_from_basic")));

		ShapeBasedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.FEEDING_UPGRADE.get())
				.pattern(" C ")
				.pattern("ABM")
				.pattern(" E ")
				.define('B', ModItems.UPGRADE_BASE.get())
				.define('C', Items.GOLDEN_CARROT)
				.define('A', Items.GOLDEN_APPLE)
				.define('M', Items.GLISTERING_MELON_SLICE)
				.define('E', Tags.Items.ENDER_PEARLS)
				.unlockedBy(HAS_UPGRADE_BASE, has(ModItems.UPGRADE_BASE.get()))
				.save(consumer);

		ShapeBasedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.COMPACTING_UPGRADE.get())
				.pattern("IPI")
				.pattern("PBP")
				.pattern("RPR")
				.define('B', ModItems.UPGRADE_BASE.get())
				.define('I', Tags.Items.INGOTS_IRON)
				.define('P', Items.PISTON)
				.define('R', Tags.Items.DUSTS_REDSTONE)
				.unlockedBy(HAS_UPGRADE_BASE, has(ModItems.UPGRADE_BASE.get()))
				.save(consumer);

		ShapeBasedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ADVANCED_COMPACTING_UPGRADE.get(), ModRecipes.UPGRADE_NEXT_TIER_SERIALIZER.get())
				.pattern(" D ")
				.pattern("GCG")
				.pattern("RRR")
				.define('D', Tags.Items.GEMS_DIAMOND)
				.define('G', Tags.Items.INGOTS_GOLD)
				.define('R', Tags.Items.DUSTS_REDSTONE)
				.define('C', ModItems.COMPACTING_UPGRADE.get())
				.unlockedBy("has_compacting_upgrade", has(ModItems.COMPACTING_UPGRADE.get()))
				.save(consumer);

		ShapeBasedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.VOID_UPGRADE.get())
				.pattern(" E ")
				.pattern("OBO")
				.pattern("ROR")
				.define('B', ModItems.UPGRADE_BASE.get())
				.define('E', Tags.Items.ENDER_PEARLS)
				.define('O', Tags.Items.OBSIDIAN)
				.define('R', Tags.Items.DUSTS_REDSTONE)
				.unlockedBy(HAS_UPGRADE_BASE, has(ModItems.UPGRADE_BASE.get()))
				.save(consumer);

		ShapeBasedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ADVANCED_VOID_UPGRADE.get(), ModRecipes.UPGRADE_NEXT_TIER_SERIALIZER.get())
				.pattern(" D ")
				.pattern("GVG")
				.pattern("RRR")
				.define('D', Tags.Items.GEMS_DIAMOND)
				.define('G', Tags.Items.INGOTS_GOLD)
				.define('R', Tags.Items.DUSTS_REDSTONE)
				.define('V', ModItems.VOID_UPGRADE.get())
				.unlockedBy("has_void_upgrade", has(ModItems.VOID_UPGRADE.get()))
				.save(consumer);

		ShapeBasedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.RESTOCK_UPGRADE.get())
				.pattern(" P ")
				.pattern("IBI")
				.pattern("RCR")
				.define('B', ModItems.UPGRADE_BASE.get())
				.define('C', Tags.Items.CHESTS_WOODEN)
				.define('I', Tags.Items.INGOTS_IRON)
				.define('R', Tags.Items.DUSTS_REDSTONE)
				.define('P', Items.STICKY_PISTON)
				.unlockedBy(HAS_UPGRADE_BASE, has(ModItems.UPGRADE_BASE.get()))
				.save(consumer);

		ShapeBasedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ADVANCED_RESTOCK_UPGRADE.get(), ModRecipes.UPGRADE_NEXT_TIER_SERIALIZER.get())
				.pattern(" D ")
				.pattern("GVG")
				.pattern("RRR")
				.define('D', Tags.Items.GEMS_DIAMOND)
				.define('G', Tags.Items.INGOTS_GOLD)
				.define('R', Tags.Items.DUSTS_REDSTONE)
				.define('V', ModItems.RESTOCK_UPGRADE.get())
				.unlockedBy("has_restock_upgrade", has(ModItems.RESTOCK_UPGRADE.get()))
				.save(consumer);

		ShapeBasedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.DEPOSIT_UPGRADE.get())
				.pattern(" P ")
				.pattern("IBI")
				.pattern("RCR")
				.define('B', ModItems.UPGRADE_BASE.get())
				.define('C', Tags.Items.CHESTS_WOODEN)
				.define('I', Tags.Items.INGOTS_IRON)
				.define('R', Tags.Items.DUSTS_REDSTONE)
				.define('P', Items.PISTON)
				.unlockedBy(HAS_UPGRADE_BASE, has(ModItems.UPGRADE_BASE.get()))
				.save(consumer);

		ShapeBasedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ADVANCED_DEPOSIT_UPGRADE.get(), ModRecipes.UPGRADE_NEXT_TIER_SERIALIZER.get())
				.pattern(" D ")
				.pattern("GVG")
				.pattern("RRR")
				.define('D', Tags.Items.GEMS_DIAMOND)
				.define('G', Tags.Items.INGOTS_GOLD)
				.define('R', Tags.Items.DUSTS_REDSTONE)
				.define('V', ModItems.DEPOSIT_UPGRADE.get())
				.unlockedBy("has_deposit_upgrade", has(ModItems.DEPOSIT_UPGRADE.get()))
				.save(consumer);

		ShapeBasedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.REFILL_UPGRADE.get())
				.pattern(" E ")
				.pattern("IBI")
				.pattern("RCR")
				.define('B', ModItems.UPGRADE_BASE.get())
				.define('C', Tags.Items.CHESTS_WOODEN)
				.define('I', Tags.Items.INGOTS_IRON)
				.define('R', Tags.Items.DUSTS_REDSTONE)
				.define('E', Tags.Items.ENDER_PEARLS)
				.unlockedBy(HAS_UPGRADE_BASE, has(ModItems.UPGRADE_BASE.get()))
				.save(consumer);

		ShapeBasedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ADVANCED_REFILL_UPGRADE.get(), ModRecipes.UPGRADE_NEXT_TIER_SERIALIZER.get())
				.pattern(" D ")
				.pattern("GFG")
				.pattern("RRR")
				.define('D', Tags.Items.GEMS_DIAMOND)
				.define('G', Tags.Items.INGOTS_GOLD)
				.define('R', Tags.Items.DUSTS_REDSTONE)
				.define('F', ModItems.REFILL_UPGRADE.get())
				.unlockedBy("has_refill_upgrade", has(ModItems.REFILL_UPGRADE.get()))
				.save(consumer);

		ShapeBasedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.INCEPTION_UPGRADE.get())
				.pattern("ESE")
				.pattern("DBD")
				.pattern("EDE")
				.define('B', ModItems.UPGRADE_BASE.get())
				.define('S', Tags.Items.NETHER_STARS)
				.define('D', Tags.Items.GEMS_DIAMOND)
				.define('E', Items.ENDER_EYE)
				.unlockedBy(HAS_UPGRADE_BASE, has(ModItems.UPGRADE_BASE.get()))
				.save(consumer);

		ShapeBasedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.EVERLASTING_UPGRADE.get())
				.pattern("CSC")
				.pattern("SBS")
				.pattern("CSC")
				.define('B', ModItems.UPGRADE_BASE.get())
				.define('S', Tags.Items.NETHER_STARS)
				.define('C', Items.END_CRYSTAL)
				.unlockedBy(HAS_UPGRADE_BASE, has(ModItems.UPGRADE_BASE.get()))
				.save(consumer);

		ShapeBasedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SMELTING_UPGRADE.get())
				.pattern("RIR")
				.pattern("IBI")
				.pattern("RFR")
				.define('B', ModItems.UPGRADE_BASE.get())
				.define('R', Tags.Items.DUSTS_REDSTONE)
				.define('I', Tags.Items.INGOTS_IRON)
				.define('F', Items.FURNACE)
				.unlockedBy(HAS_UPGRADE_BASE, has(ModItems.UPGRADE_BASE.get()))
				.save(consumer);

		ShapeBasedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.AUTO_SMELTING_UPGRADE.get(), ModRecipes.UPGRADE_NEXT_TIER_SERIALIZER.get())
				.pattern("DHD")
				.pattern("RSH")
				.pattern("GHG")
				.define('D', Tags.Items.GEMS_DIAMOND)
				.define('G', Tags.Items.INGOTS_GOLD)
				.define('R', Tags.Items.DUSTS_REDSTONE)
				.define('H', Items.HOPPER)
				.define('S', ModItems.SMELTING_UPGRADE.get())
				.unlockedBy(HAS_SMELTING_UPGRADE, has(ModItems.SMELTING_UPGRADE.get()))
				.save(consumer);

		ShapeBasedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.CRAFTING_UPGRADE.get())
				.pattern(" T ")
				.pattern("IBI")
				.pattern(" C ")
				.define('B', ModItems.UPGRADE_BASE.get())
				.define('C', Tags.Items.CHESTS)
				.define('I', Tags.Items.INGOTS_IRON)
				.define('T', Items.CRAFTING_TABLE)
				.unlockedBy(HAS_UPGRADE_BASE, has(ModItems.UPGRADE_BASE.get()))
				.save(consumer);

		ShapeBasedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.STONECUTTER_UPGRADE.get())
				.pattern(" S ")
				.pattern("IBI")
				.pattern(" R ")
				.define('B', ModItems.UPGRADE_BASE.get())
				.define('R', Tags.Items.DUSTS_REDSTONE)
				.define('I', Tags.Items.INGOTS_IRON)
				.define('S', Items.STONECUTTER)
				.unlockedBy(HAS_UPGRADE_BASE, has(ModItems.UPGRADE_BASE.get()))
				.save(consumer);

		ShapeBasedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.STACK_UPGRADE_TIER_1.get())
				.pattern("III")
				.pattern("IBI")
				.pattern("III")
				.define('B', ModItems.UPGRADE_BASE.get())
				.define('I', Tags.Items.STORAGE_BLOCKS_IRON)
				.unlockedBy(HAS_UPGRADE_BASE, has(ModItems.UPGRADE_BASE.get()))
				.save(consumer);

		ShapeBasedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.STACK_UPGRADE_TIER_2.get())
				.pattern("GGG")
				.pattern("GSG")
				.pattern("GGG")
				.define('S', ModItems.STACK_UPGRADE_TIER_1.get())
				.define('G', Tags.Items.STORAGE_BLOCKS_GOLD)
				.unlockedBy(HAS_UPGRADE_BASE, has(ModItems.STACK_UPGRADE_TIER_1.get()))
				.save(consumer);

		ShapeBasedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.STACK_UPGRADE_TIER_3.get())
				.pattern("DDD")
				.pattern("DSD")
				.pattern("DDD")
				.define('S', ModItems.STACK_UPGRADE_TIER_2.get())
				.define('D', Tags.Items.STORAGE_BLOCKS_DIAMOND)
				.unlockedBy(HAS_UPGRADE_BASE, has(ModItems.STACK_UPGRADE_TIER_2.get()))
				.save(consumer);

		ShapeBasedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.STACK_UPGRADE_TIER_4.get())
				.pattern("NNN")
				.pattern("NSN")
				.pattern("NNN")
				.define('S', ModItems.STACK_UPGRADE_TIER_3.get())
				.define('N', Tags.Items.STORAGE_BLOCKS_NETHERITE)
				.unlockedBy(HAS_UPGRADE_BASE, has(ModItems.STACK_UPGRADE_TIER_3.get()))
				.save(consumer);

		ShapeBasedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.JUKEBOX_UPGRADE.get())
				.pattern(" J ")
				.pattern("IBI")
				.pattern(" R ")
				.define('B', ModItems.UPGRADE_BASE.get())
				.define('R', Tags.Items.DUSTS_REDSTONE)
				.define('I', Tags.Items.INGOTS_IRON)
				.define('J', Items.JUKEBOX)
				.unlockedBy(HAS_UPGRADE_BASE, has(ModItems.UPGRADE_BASE.get()))
				.save(consumer);

		ShapeBasedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.TOOL_SWAPPER_UPGRADE.get())
				.pattern("RWR")
				.pattern("PBA")
				.pattern("ISI")
				.define('B', ModItems.UPGRADE_BASE.get())
				.define('S', Items.WOODEN_SHOVEL)
				.define('P', Items.WOODEN_PICKAXE)
				.define('A', Items.WOODEN_AXE)
				.define('W', Items.WOODEN_SWORD)
				.define('I', Tags.Items.INGOTS_IRON)
				.define('R', Tags.Items.DUSTS_REDSTONE)
				.unlockedBy(HAS_UPGRADE_BASE, has(ModItems.UPGRADE_BASE.get()))
				.save(consumer);

		ShapeBasedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ADVANCED_TOOL_SWAPPER_UPGRADE.get(), ModRecipes.UPGRADE_NEXT_TIER_SERIALIZER.get())
				.pattern(" D ")
				.pattern("GVG")
				.pattern("RRR")
				.define('D', Tags.Items.GEMS_DIAMOND)
				.define('G', Tags.Items.INGOTS_GOLD)
				.define('R', Tags.Items.DUSTS_REDSTONE)
				.define('V', ModItems.TOOL_SWAPPER_UPGRADE.get())
				.unlockedBy("has_tool_swapper_upgrade", has(ModItems.TOOL_SWAPPER_UPGRADE.get()))
				.save(consumer);

/*		ShapeBasedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.TANK_UPGRADE.get(), ModRecipes.UPGRADE_NEXT_TIER_SERIALIZER.get())
				.pattern("GGG")
				.pattern("GBG")
				.pattern("GGG")
				.define('G', Tags.Items.GLASS)
				.define('B', ModItems.UPGRADE_BASE.get())
				.unlockedBy(HAS_UPGRADE_BASE, has(ModItems.UPGRADE_BASE.get()))
				.save(consumer);*/

		ShapeBasedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ADVANCED_FEEDING_UPGRADE.get(), ModRecipes.UPGRADE_NEXT_TIER_SERIALIZER.get())
				.pattern(" D ")
				.pattern("GVG")
				.pattern("RRR")
				.define('D', Tags.Items.GEMS_DIAMOND)
				.define('G', Tags.Items.INGOTS_GOLD)
				.define('R', Tags.Items.DUSTS_REDSTONE)
				.define('V', ModItems.FEEDING_UPGRADE.get())
				.unlockedBy("has_feeding_upgrade", has(ModItems.FEEDING_UPGRADE.get()))
				.save(consumer);

		ShapeBasedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BATTERY_UPGRADE.get(), ModRecipes.UPGRADE_NEXT_TIER_SERIALIZER.get())
				.pattern("GRG")
				.pattern("RBR")
				.pattern("GRG")
				.define('R', Tags.Items.STORAGE_BLOCKS_REDSTONE)
				.define('G', Tags.Items.INGOTS_GOLD)
				.define('B', ModItems.UPGRADE_BASE.get())
				.unlockedBy(HAS_UPGRADE_BASE, has(ModItems.UPGRADE_BASE.get()))
				.save(consumer);

/*		ShapeBasedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PUMP_UPGRADE.get(), ModRecipes.UPGRADE_NEXT_TIER_SERIALIZER.get())
				.pattern("GUG")
				.pattern("PBS")
				.pattern("GUG")
				.define('U', Items.BUCKET)
				.define('G', Tags.Items.GLASS)
				.define('P', Items.PISTON)
				.define('S', Items.STICKY_PISTON)
				.define('B', ModItems.UPGRADE_BASE.get())
				.unlockedBy(HAS_UPGRADE_BASE, has(ModItems.UPGRADE_BASE.get()))
				.save(consumer);

		ShapeBasedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ADVANCED_PUMP_UPGRADE.get(), ModRecipes.UPGRADE_NEXT_TIER_SERIALIZER.get())
				.pattern("DID")
				.pattern("GPG")
				.pattern("RRR")
				.define('I', Items.DISPENSER)
				.define('D', Tags.Items.GEMS_DIAMOND)
				.define('G', Tags.Items.INGOTS_GOLD)
				.define('R', Tags.Items.DUSTS_REDSTONE)
				.define('P', ModItems.PUMP_UPGRADE.get())
				.unlockedBy("has_pump_upgrade", has(ModItems.PUMP_UPGRADE.get()))
				.save(consumer);

		ShapeBasedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.XP_PUMP_UPGRADE.get())
				.pattern("RER")
				.pattern("CPC")
				.pattern("RER")
				.define('R', Tags.Items.DUSTS_REDSTONE)
				.define('E', Items.ENDER_EYE)
				.define('C', Items.EXPERIENCE_BOTTLE)
				.define('P', ModItems.ADVANCED_PUMP_UPGRADE.get())
				.unlockedBy("has_advanced_pump_upgrade", has(ModItems.ADVANCED_PUMP_UPGRADE.get()))
				.save(consumer);*/

		ShapeBasedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SMOKING_UPGRADE.get())
				.pattern("RIR")
				.pattern("IBI")
				.pattern("RSR")
				.define('B', ModItems.UPGRADE_BASE.get())
				.define('R', Tags.Items.DUSTS_REDSTONE)
				.define('I', Tags.Items.INGOTS_IRON)
				.define('S', Items.SMOKER)
				.unlockedBy(HAS_UPGRADE_BASE, has(ModItems.UPGRADE_BASE.get()))
				.save(consumer);

		ShapeBasedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SMOKING_UPGRADE.get())
				.pattern(" L ")
				.pattern("LSL")
				.pattern(" L ")
				.define('S', ModItems.SMELTING_UPGRADE.get())
				.define('L', ItemTags.LOGS)
				.unlockedBy(HAS_SMELTING_UPGRADE, has(ModItems.SMELTING_UPGRADE.get()))
				.save(consumer, SophisticatedBackpacks.getRL("smoking_upgrade_from_smelting_upgrade"));

		ShapeBasedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.AUTO_SMOKING_UPGRADE.get(), ModRecipes.UPGRADE_NEXT_TIER_SERIALIZER.get())
				.pattern("DHD")
				.pattern("RSH")
				.pattern("GHG")
				.define('D', Tags.Items.GEMS_DIAMOND)
				.define('G', Tags.Items.INGOTS_GOLD)
				.define('R', Tags.Items.DUSTS_REDSTONE)
				.define('H', Items.HOPPER)
				.define('S', ModItems.SMOKING_UPGRADE.get())
				.unlockedBy("has_smoking_upgrade", has(ModItems.SMOKING_UPGRADE.get()))
				.save(consumer);

		ShapeBasedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.AUTO_SMOKING_UPGRADE.get())
				.pattern(" L ")
				.pattern("LSL")
				.pattern(" L ")
				.define('S', ModItems.AUTO_SMELTING_UPGRADE.get())
				.define('L', ItemTags.LOGS)
				.unlockedBy("has_auto_smelting_upgrade", has(ModItems.AUTO_SMELTING_UPGRADE.get()))
				.save(consumer, SophisticatedBackpacks.getRL("auto_smoking_upgrade_from_auto_smelting_upgrade"));

		ShapeBasedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BLASTING_UPGRADE.get())
				.pattern("RIR")
				.pattern("IBI")
				.pattern("RFR")
				.define('B', ModItems.UPGRADE_BASE.get())
				.define('R', Tags.Items.DUSTS_REDSTONE)
				.define('I', Tags.Items.INGOTS_IRON)
				.define('F', Items.BLAST_FURNACE)
				.unlockedBy(HAS_UPGRADE_BASE, has(ModItems.UPGRADE_BASE.get()))
				.save(consumer);

		ShapeBasedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BLASTING_UPGRADE.get())
				.pattern("III")
				.pattern("ISI")
				.pattern("TTT")
				.define('S', ModItems.SMELTING_UPGRADE.get())
				.define('I', Tags.Items.INGOTS_IRON)
				.define('T', Items.SMOOTH_STONE)
				.unlockedBy(HAS_SMELTING_UPGRADE, has(ModItems.SMELTING_UPGRADE.get()))
				.save(consumer, SophisticatedBackpacks.getRL("blasting_upgrade_from_smelting_upgrade"));

		ShapeBasedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.AUTO_BLASTING_UPGRADE.get(), ModRecipes.UPGRADE_NEXT_TIER_SERIALIZER.get())
				.pattern("DHD")
				.pattern("RSH")
				.pattern("GHG")
				.define('D', Tags.Items.GEMS_DIAMOND)
				.define('G', Tags.Items.INGOTS_GOLD)
				.define('R', Tags.Items.DUSTS_REDSTONE)
				.define('H', Items.HOPPER)
				.define('S', ModItems.BLASTING_UPGRADE.get())
				.unlockedBy("has_blasting_upgrade", has(ModItems.BLASTING_UPGRADE.get()))
				.save(consumer);

		ShapeBasedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.AUTO_BLASTING_UPGRADE.get())
				.pattern("III")
				.pattern("ISI")
				.pattern("TTT")
				.define('S', ModItems.AUTO_SMELTING_UPGRADE.get())
				.define('I', Tags.Items.INGOTS_IRON)
				.define('T', Items.SMOOTH_STONE)
				.unlockedBy("has_auto_smelting_upgrade", has(ModItems.AUTO_SMELTING_UPGRADE.get()))
				.save(consumer, SophisticatedBackpacks.getRL("auto_blasting_upgrade_from_auto_smelting_upgrade"));

		new LegacyUpgradeRecipeBuilder(ModItems.SMITHING_BACKPACK_UPGRADE_RECIPE_SERIALIZER.get(), Ingredient.of(ModItems.DIAMOND_BACKPACK.get()),
				Ingredient.of(Items.NETHERITE_INGOT), RecipeCategory.MISC, ModItems.NETHERITE_BACKPACK.get())
				.unlocks("has_diamond_backpack", has(ModItems.DIAMOND_BACKPACK.get()))
				.save(consumer, RegistryHelper.getItemKey(ModItems.NETHERITE_BACKPACK.get()));
	}

	private static InventoryChangeTrigger.TriggerInstance hasLeather() {
		return inventoryTrigger(ItemPredicate.Builder.item().of(Tags.Items.LEATHER).build());
	}
}
