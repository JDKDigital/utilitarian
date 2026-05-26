package cy.jdkdigital.utilitarian.data;

import cy.jdkdigital.utilitarian.Utilitarian;
import cy.jdkdigital.utilitarian.module.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RecipeProvider extends net.minecraft.data.recipes.RecipeProvider implements IConditionBuilder
{
    private final CompletableFuture<HolderLookup.Provider> registries;

    public RecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
        this.registries = registries;
    }

    @Override
    protected void buildRecipes(RecipeOutput pRecipeOutput, HolderLookup.Provider holderLookup) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, UtilityBlockModule.ANGEL_BLOCK_ITEM.get(), 1)
                .unlockedBy("has_gold", has(Tags.Items.INGOTS_GOLD))
                .unlockedBy("has_obsidian", has(Tags.Items.OBSIDIANS))
                .unlockedBy("has_feather", has(Tags.Items.FEATHERS))
                .pattern(" F ").pattern("#O#").pattern(" F ")
                .define('#', Ingredient.of(Tags.Items.INGOTS_GOLD))
                .define('O', Ingredient.of(Tags.Items.OBSIDIANS))
                .define('F', Ingredient.of(Tags.Items.FEATHERS))
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "angel_block"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, UtilityBlockModule.ANGEL_BLOCK_ITEM.get(), 1)
                .unlockedBy("has_gold", has(Tags.Items.INGOTS_GOLD))
                .unlockedBy("has_obsidian", has(Tags.Items.OBSIDIANS))
                .unlockedBy("has_feather", has(Tags.Items.FEATHERS))
                .pattern(" # ").pattern("FOF").pattern(" # ")
                .define('#', Ingredient.of(Tags.Items.INGOTS_GOLD))
                .define('O', Ingredient.of(Tags.Items.OBSIDIANS))
                .define('F', Ingredient.of(Tags.Items.FEATHERS))
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "angel_block_rot"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, UtilityBlockModule.INVERTED_REDSTONE_LAMP.get(), 1)
                .unlockedBy(getHasName(Items.LEVER), has(Items.LEVER))
                .unlockedBy(getHasName(Items.REDSTONE_LAMP), has(Items.REDSTONE_LAMP))
                .requires(Items.LEVER).requires(Items.REDSTONE_LAMP)
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "inverted_redstone_lamp"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, UtilityBlockModule.LAPIS_LAMP.get(), 1)
                .unlockedBy("has_lapis", has(Tags.Items.GEMS_LAPIS))
                .unlockedBy(getHasName(Items.REDSTONE_LAMP), has(Items.REDSTONE_LAMP))
                .pattern(" # ").pattern("#L#").pattern(" # ")
                .define('#', Ingredient.of(Tags.Items.GEMS_LAPIS))
                .define('L', Ingredient.of(Items.REDSTONE_LAMP))
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "lapis_lamp"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, UtilityBlockModule.INVERTED_LAPIS_LAMP.get(), 1)
                .unlockedBy(getHasName(Items.LEVER), has(Items.LEVER))
                .unlockedBy(getHasName(UtilityBlockModule.LAPIS_LAMP.get()), has(UtilityBlockModule.LAPIS_LAMP.get()))
                .requires(Items.LEVER).requires(UtilityBlockModule.LAPIS_LAMP.get())
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "inverted_lapis_lamp"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, UtilityBlockModule.SOUND_MUFFLER_ITEM.get(), 1)
                .unlockedBy("has_wool", has(ItemTags.WOOL))
                .unlockedBy(getHasName(Items.NOTE_BLOCK), has(Items.NOTE_BLOCK))
                .requires(ItemTags.WOOL).requires(Items.NOTE_BLOCK)
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "sound_muffler"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TPSMeterModule.TPS_METER_ITEM.get(), 1)
                .unlockedBy(getHasName(Items.COMPARATOR), has(Items.COMPARATOR))
                .unlockedBy("has_obsidian", has(Tags.Items.OBSIDIANS))
                .pattern(" # ").pattern("#O#").pattern(" # ")
                .define('O', Ingredient.of(Tags.Items.OBSIDIANS))
                .define('#', Ingredient.of(Items.COMPARATOR))
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "tps_meter"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, NoSolicitingModule.RESTRAINING_ORDER.get(), 1)
                .unlockedBy(getHasName(Items.PAPER), has(Items.PAPER))
                .unlockedBy(getHasName(Items.LEAD), has(Items.LEAD))
                .requires(Items.PAPER).requires(Items.LEAD)
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "no_soliciting/restraining_order"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NoSolicitingModule.NO_RAIDER_BLOCK.get(), 1)
                .unlockedBy("has_wool", has(Items.WHITE_WOOL))
                .unlockedBy("has_banner", has(ItemTags.BANNERS))
                .unlockedBy("has_crossbow", has(Items.CROSSBOW))
                .pattern(" C ").pattern("WBW").pattern(" W ")
                .define('W', Ingredient.of(Items.WHITE_WOOL))
                .define('B', DataComponentIngredient.of(true, Raid.getLeaderBannerInstance(holderLookup.lookupOrThrow(Registries.BANNER_PATTERN))))
                .define('C', Ingredient.of(Items.CROSSBOW))
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "no_soliciting/no_raider_block"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NoSolicitingModule.NO_SOLICITING_BANNER_ITEM.get(), 1)
                .group("banner")
                .unlockedBy(getHasName(Items.LEAD), has(Items.LEAD))
                .unlockedBy("has_wool", has(ItemTags.WOOL))
                .unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
                .pattern("###").pattern("#L#").pattern(" | ")
                .define('L', Ingredient.of(Items.LEAD))
                .define('#', Ingredient.of(ItemTags.WOOL))
                .define('|', Ingredient.of(Tags.Items.RODS_WOODEN))
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "no_soliciting/no_soliciting_banner"));
        Arrays.stream(DyeColor.values()).forEach(dyeColor -> {
            var carpet = BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(dyeColor.getSerializedName() + "_carpet"));
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, NoSolicitingModule.SOLICITING_CARPET_ITEM.get(dyeColor).get(), 1)
                    .unlockedBy(getHasName(carpet), has(carpet))
                    .unlockedBy("has_flower", has(ItemTags.SMALL_FLOWERS))
                    .requires(carpet).requires(ItemTags.SMALL_FLOWERS)
                    .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "no_soliciting/soliciting_carpets/" + dyeColor.getSerializedName() + "_soliciting_carpet"));
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, NoSolicitingModule.TRAPPED_SOLICITING_CARPET_ITEM.get(dyeColor).get(), 1)
                    .unlockedBy(getHasName(NoSolicitingModule.SOLICITING_CARPET_ITEM.get(dyeColor).get()), has(NoSolicitingModule.SOLICITING_CARPET_ITEM.get(dyeColor).get()))
                    .unlockedBy(getHasName(Items.TRIPWIRE_HOOK), has(Items.TRIPWIRE_HOOK))
                    .requires(NoSolicitingModule.SOLICITING_CARPET_ITEM.get(dyeColor).get()).requires(Items.TRIPWIRE_HOOK)
                    .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "no_soliciting/soliciting_carpets/" + dyeColor.getSerializedName() + "_trapped_soliciting_carpet"));
        });

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, UtilityBlockModule.FLUID_HOPPER_BLOCK.get(), 1)
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .pattern("IPI").pattern("I#I").pattern(" I ")
                .define('#', Ingredient.of(Items.BUCKET))
                .define('I', Ingredient.of(Tags.Items.INGOTS_IRON))
                .define('P', Ingredient.of(Tags.Items.DYES_WHITE))
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "fluid_hopper"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, UtilityBlockModule.MAGNET.get(), 1)
                .unlockedBy("has_iron", has(Tags.Items.STORAGE_BLOCKS_IRON))
                .pattern("CIC")
                .define('C', Ingredient.of(Tags.Items.INGOTS_COPPER))
                .define('I', Ingredient.of(Tags.Items.STORAGE_BLOCKS_IRON))
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "magnet"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, UtilityBlockModule.WELL_BEHAVED_DROPPER.get(), 1)
                .unlockedBy(getHasName(Items.DROPPER), has(Items.DROPPER))
                .pattern(" I ").pattern("I#I").pattern(" I ")
                .define('#', Ingredient.of(Items.DROPPER))
                .define('I', Ingredient.of(Tags.Items.NUGGETS_GOLD))
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "well_behaved_dropper"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, UtilityItemModule.TROWEL.get(), 1)
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .pattern("S  ").pattern(" II")
                .define('I', Ingredient.of(Tags.Items.INGOTS_IRON))
                .define('S', Ingredient.of(Tags.Items.RODS_WOODEN))
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "trowel"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, UtilityBlockModule.REDSTONE_CLOCK_BLOCK.get(), 1)
                .unlockedBy("has_redstone", has(Tags.Items.DUSTS_REDSTONE))
                .pattern(" R ").pattern("R#R").pattern(" R ")
                .define('#', Ingredient.of(Items.CHISELED_STONE_BRICKS))
                .define('R', Ingredient.of(Tags.Items.DUSTS_REDSTONE))
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "redstone_clock"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, UtilityItemModule.TINY_COAL.get(), 8)
                .unlockedBy(getHasName(Items.COAL), has(Items.COAL))
                .requires(Items.COAL)
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "tiny_fuel/tiny_coal"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.COAL, 1)
                .unlockedBy(getHasName(UtilityItemModule.TINY_COAL.get()), has(UtilityItemModule.TINY_COAL.get()))
                .requires(UtilityItemModule.TINY_COAL.get(), 8)
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "tiny_fuel/coal"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, UtilityItemModule.TINY_CHARCOAL.get(), 8)
                .unlockedBy(getHasName(Items.CHARCOAL), has(Items.CHARCOAL))
                .requires(Items.CHARCOAL)
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "tiny_fuel/tiny_charcoal"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.CHARCOAL, 1)
                .unlockedBy(getHasName(UtilityItemModule.TINY_CHARCOAL.get()), has(UtilityItemModule.TINY_CHARCOAL.get()))
                .requires(UtilityItemModule.TINY_CHARCOAL.get(), 8)
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "tiny_fuel/charcoal"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, UtilityItemModule.UNNAME_TAG.get(), 1)
                .unlockedBy(getHasName(Items.NAME_TAG), has(Items.NAME_TAG))
                .unlockedBy("has_white_dye", has(Tags.Items.DYES_WHITE))
                .requires(Items.NAME_TAG)
                .requires(Tags.Items.DYES_WHITE)
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "unname_tag"));

        // Snad
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SnadModule.SNAD_BLOCK_ITEM.get(), 1)
                .unlockedBy(getHasName(Items.SAND), has(Items.SAND))
                .requires(Items.SAND).requires(Items.SAND)
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "snad/snad"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SnadModule.RED_SNAD_BLOCK_ITEM.get(), 1)
                .unlockedBy(getHasName(Items.RED_SAND), has(Items.RED_SAND))
                .requires(Items.RED_SAND).requires(Items.RED_SAND)
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "snad/red_snad"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SnadModule.SOUL_SNAD_BLOCK_ITEM.get(), 1)
                .unlockedBy(getHasName(Items.SOUL_SAND), has(Items.SOUL_SAND))
                .requires(Items.SOUL_SAND).requires(Items.SOUL_SAND)
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "snad/soul_snad"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SnadModule.DRIT_BLOCK_ITEM.get(), 1)
                .unlockedBy(getHasName(Items.BONE), has(Items.BONE))
                .unlockedBy(getHasName(Items.DIRT), has(Items.DIRT))
                .pattern("###").pattern("#L#").pattern("###")
                .define('L', Ingredient.of(Items.BONE))
                .define('#', Ingredient.of(Items.DIRT))
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "snad/drit"));

        // Utility recipes
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.GLOW_INK_SAC, 1)
                .unlockedBy(getHasName(Items.INK_SAC), has(Items.INK_SAC))
                .unlockedBy(getHasName(Items.GLOWSTONE), has(Items.GLOWSTONE))
                .requires(Items.INK_SAC).requires(Tags.Items.DUSTS_GLOWSTONE)
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "utility/glow_ink_sac"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.QUARTZ, 4)
                .unlockedBy(getHasName(Items.QUARTZ), has(Items.QUARTZ))
                .requires(Items.QUARTZ_BLOCK)
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "utility/quartz"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.AMETHYST_SHARD, 4)
                .unlockedBy(getHasName(Items.AMETHYST_SHARD), has(Items.AMETHYST_SHARD))
                .requires(Items.AMETHYST_BLOCK)
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "utility/amethyst"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.NETHER_WART, 9)
                .unlockedBy(getHasName(Items.NETHER_WART), has(Items.NETHER_WART))
                .requires(Items.NETHER_WART_BLOCK)
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "utility/nether_wart"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.PACKED_ICE, 9)
                .unlockedBy(getHasName(Items.PACKED_ICE), has(Items.PACKED_ICE))
                .requires(Items.BLUE_ICE)
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "utility/packed_ice"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.ICE, 9)
                .unlockedBy(getHasName(Items.ICE), has(Items.ICE))
                .requires(Items.PACKED_ICE)
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "utility/ice"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.GREEN_DYE, 1)
                .unlockedBy(getHasName(Items.BLUE_DYE), has(Items.BLUE_DYE))
                .unlockedBy(getHasName(Items.YELLOW_DYE), has(Items.YELLOW_DYE))
                .requires(Tags.Items.DYES_BLUE).requires(Tags.Items.DYES_YELLOW)
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "utility/green_dye"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.CHEST, 4)
                .unlockedBy("has_log", has(ItemTags.LOGS))
                .pattern("###").pattern("# #").pattern("###")
                .define('#', Ingredient.of(ItemTags.LOGS))
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "utility/logs_to_chests"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.STICK, 16)
                .unlockedBy("has_log", has(ItemTags.LOGS))
                .pattern("#").pattern("#")
                .define('#', Ingredient.of(ItemTags.LOGS))
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "utility/logs_to_sticks"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.BOWL, 12)
                .unlockedBy("has_log", has(ItemTags.LOGS))
                .pattern("# #").pattern(" # ")
                .define('#', Ingredient.of(ItemTags.LOGS))
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "utility/logs_to_bowls"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.LADDER, 24)
                .unlockedBy("has_log", has(ItemTags.LOGS))
                .pattern("# #").pattern("###").pattern("# #")
                .define('#', Ingredient.of(ItemTags.LOGS))
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "utility/logs_to_ladders"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.HOPPER, 1)
                .unlockedBy("has_log", has(ItemTags.LOGS))
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .pattern("I#I").pattern("I#I").pattern(" I ")
                .define('#', Ingredient.of(ItemTags.LOGS))
                .define('I', Ingredient.of(Tags.Items.INGOTS_IRON))
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "utility/logs_to_hopper"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.DISPENSER, 1)
                .unlockedBy(getHasName(Items.DISPENSER), has(Items.DROPPER))
                .unlockedBy("has_string", has(Tags.Items.STRINGS))
                .unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
                .pattern(" RS").pattern("RDS").pattern(" RS")
                .define('R', Ingredient.of(Tags.Items.RODS_WOODEN))
                .define('S', Ingredient.of(Tags.Items.STRINGS))
                .define('D', Ingredient.of(Items.DROPPER))
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "utility/dispenser"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.REPEATER, 1)
                .unlockedBy(getHasName(Items.DISPENSER), has(Items.STONE))
                .unlockedBy("has_redstone", has(Tags.Items.DUSTS_REDSTONE))
                .unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
                .pattern("D D").pattern("RDR").pattern("SSS")
                .define('R', Ingredient.of(Tags.Items.RODS_WOODEN))
                .define('S', Ingredient.of(Items.STONE))
                .define('D', Ingredient.of(Tags.Items.DUSTS_REDSTONE))
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "utility/repeater"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.CHEST_MINECART, 1)
                .unlockedBy("has_chest", has(Tags.Items.CHESTS))
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .pattern("ICI").pattern("III")
                .define('C', Ingredient.of(Tags.Items.CHESTS))
                .define('I', Ingredient.of(Tags.Items.INGOTS_IRON))
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "utility/chest_minecart"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.HOPPER_MINECART, 1)
                .unlockedBy(getHasName(Items.HOPPER), has(Items.HOPPER))
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .pattern("IHI").pattern("III")
                .define('H', Ingredient.of(Items.HOPPER))
                .define('I', Ingredient.of(Tags.Items.INGOTS_IRON))
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "utility/hopper_minecart"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.TNT_MINECART, 1)
                .unlockedBy(getHasName(Items.TNT), has(Items.TNT))
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .pattern("ITI").pattern("III")
                .define('T', Ingredient.of(Items.TNT))
                .define('I', Ingredient.of(Tags.Items.INGOTS_IRON))
                .save (pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "utility/tnt_minecart"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.PAPER, 3)
                .unlockedBy(getHasName(Items.SUGAR_CANE), has(Items.SUGAR_CANE))
                .pattern("##").pattern("# ")
                .define('#', Ingredient.of(Items.SUGAR_CANE))
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "utility/paper"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.BREAD, 1)
                .unlockedBy(getHasName(Items.WHEAT), has(Items.WHEAT))
                .pattern("##").pattern("# ")
                .define('#', Ingredient.of(Items.WHEAT))
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "utility/bread"));

        Arrays.stream(DyeColor.values()).forEach(dyeColor -> {
            // concrete re-dying
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(dyeColor.getSerializedName() + "_concrete")), 1)
                    .unlockedBy("has_dye", has(dyeColor.getTag()))
                    .unlockedBy("has_concrete", has(Tags.Items.CONCRETES))
                    .requires(dyeColor.getTag()).requires(Tags.Items.CONCRETES)
                    .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "utility/redying/" + dyeColor.getSerializedName() + "_concrete_single"));

            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(dyeColor.getSerializedName() + "_concrete_powder")), 1)
                    .unlockedBy("has_dye", has(dyeColor.getTag()))
                    .unlockedBy("has_concrete", has(Tags.Items.CONCRETE_POWDERS))
                    .requires(dyeColor.getTag()).requires(Tags.Items.CONCRETE_POWDERS)
                    .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "utility/redying/" + dyeColor.getSerializedName() + "_concrete_powder_single"));

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(dyeColor.getSerializedName() + "_concrete")), 8)
                    .unlockedBy("has_dye", has(dyeColor.getTag()))
                    .unlockedBy("has_concrete", has(Tags.Items.CONCRETES))
                    .pattern("###").pattern("#D#").pattern("###")
                    .define('#', Ingredient.of(Tags.Items.CONCRETES))
                    .define('D', Ingredient.of(dyeColor.getTag()))
                    .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "utility/redying/" + dyeColor.getSerializedName() + "_concrete"));

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(dyeColor.getSerializedName() + "_concrete_powder")), 8)
                    .unlockedBy("has_dye", has(dyeColor.getTag()))
                    .unlockedBy("has_concrete", has(Tags.Items.CONCRETE_POWDERS))
                    .pattern("###").pattern("#D#").pattern("###")
                    .define('#', Ingredient.of(Tags.Items.CONCRETE_POWDERS))
                    .define('D', Ingredient.of(dyeColor.getTag()))
                    .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "utility/redying/" + dyeColor.getSerializedName() + "_concrete_powder"));

            // Glass re-dying
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(dyeColor.getSerializedName() + "_stained_glass")), 1)
                    .unlockedBy("has_dye", has(dyeColor.getTag()))
                    .unlockedBy("has_glass", has(Tags.Items.GLASS_BLOCKS_CHEAP))
                    .requires(dyeColor.getTag()).requires(Tags.Items.GLASS_BLOCKS_CHEAP)
                    .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "utility/redying/" + dyeColor.getSerializedName() + "_stained_glass_single"));

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(dyeColor.getSerializedName() + "_stained_glass")), 8)
                    .unlockedBy("has_dye", has(dyeColor.getTag()))
                    .unlockedBy("has_glass", has(Tags.Items.GLASS_BLOCKS_CHEAP))
                    .pattern("###").pattern("#D#").pattern("###")
                    .define('#', Ingredient.of(Tags.Items.GLASS_BLOCKS_CHEAP))
                    .define('D', Ingredient.of(dyeColor.getTag()))
                    .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "utility/redying/" + dyeColor.getSerializedName() + "_stained_glass"));

            // Glass pane re-dying
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(dyeColor.getSerializedName() + "_stained_glass_pane")), 1)
                    .unlockedBy("has_dye", has(dyeColor.getTag()))
                    .unlockedBy("has_glass_pane", has(Tags.Items.GLASS_PANES))
                    .requires(dyeColor.getTag()).requires(Tags.Items.GLASS_PANES)
                    .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "utility/redying/" + dyeColor.getSerializedName() + "_stained_glass_pane_single"));

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(dyeColor.getSerializedName() + "_stained_glass_pane")), 8)
                    .unlockedBy("has_dye", has(dyeColor.getTag()))
                    .unlockedBy("has_glass_pane", has(Tags.Items.GLASS_PANES))
                    .pattern("###").pattern("#D#").pattern("###")
                    .define('#', Ingredient.of(Tags.Items.GLASS_PANES))
                    .define('D', Ingredient.of(dyeColor.getTag()))
                    .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "utility/redying/" + dyeColor.getSerializedName() + "_stained_glass_pane"));

            // Terracotta re-dying
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(dyeColor.getSerializedName() + "_terracotta")), 1)
                    .unlockedBy("has_dye", has(dyeColor.getTag()))
                    .unlockedBy("has_terracotta", has(ItemTags.TERRACOTTA))
                    .requires(dyeColor.getTag()).requires(ItemTags.TERRACOTTA)
                    .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "utility/redying/" + dyeColor.getSerializedName() + "_terracotta_single"));

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(dyeColor.getSerializedName() + "_terracotta")), 8)
                    .unlockedBy("has_dye", has(dyeColor.getTag()))
                    .unlockedBy("has_terracotta", has(ItemTags.TERRACOTTA))
                    .pattern("###").pattern("#D#").pattern("###")
                    .define('#', Ingredient.of(ItemTags.TERRACOTTA))
                    .define('D', Ingredient.of(dyeColor.getTag()))
                    .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "utility/redying/" + dyeColor.getSerializedName() + "_terracotta"));

            // Candle re-dying
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(dyeColor.getSerializedName() + "_candle")), 1)
                    .unlockedBy("has_dye", has(dyeColor.getTag()))
                    .unlockedBy("has_terracotta", has(ItemTags.CANDLES))
                    .requires(dyeColor.getTag()).requires(ItemTags.CANDLES)
                    .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "utility/redying/" + dyeColor.getSerializedName() + "_candle"));

            // Overwrite wool, bed and carpet dying to use tags
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(dyeColor.getSerializedName() + "_wool")), 1)
                    .unlockedBy("has_dye", has(dyeColor.getTag()))
                    .unlockedBy("has_wool", has(ItemTags.WOOL))
                    .requires(dyeColor.getTag()).requires(ItemTags.WOOL)
                    .save(pRecipeOutput, ResourceLocation.withDefaultNamespace("dye_" + dyeColor.getSerializedName() + "_wool"));
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(dyeColor.getSerializedName() + "_bed")), 1)
                    .unlockedBy("has_dye", has(dyeColor.getTag()))
                    .unlockedBy("has_wool", has(ItemTags.BEDS))
                    .requires(dyeColor.getTag()).requires(ItemTags.BEDS)
                    .save(pRecipeOutput, ResourceLocation.withDefaultNamespace("dye_" + dyeColor.getSerializedName() + "_bed"));
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(dyeColor.getSerializedName() + "_carpet")), 1)
                    .unlockedBy("has_dye", has(dyeColor.getTag()))
                    .unlockedBy("has_wool", has(ItemTags.WOOL_CARPETS))
                    .requires(dyeColor.getTag()).requires(ItemTags.WOOL_CARPETS)
                    .save(pRecipeOutput, ResourceLocation.withDefaultNamespace("dye_" + dyeColor.getSerializedName() + "_carpet"));
        });

        WoodType.values().forEach(woodType -> {
            var isBamboo = woodType.name().equals("bamboo");
            var log = BuiltInRegistries.ITEM.get(ResourceLocation.parse(woodType.name() + (isBamboo ? "_block" : "_log")));
            if (woodType.name().equals("warped") || woodType.name().equals("crimson")) {
                log = BuiltInRegistries.ITEM.get(ResourceLocation.parse(woodType.name() + "_stem"));
            }
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BuiltInRegistries.ITEM.get(ResourceLocation.parse(woodType.name() + "_slab")), (isBamboo ? 12 : 24))
                    .unlockedBy("has_log", has(log))
                    .pattern("###")
                    .define('#', Ingredient.of(log))
                    .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "utility/" + woodType.name() + "_logs_to_slabs"));

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BuiltInRegistries.ITEM.get(ResourceLocation.parse(woodType.name() + "_stairs")), (isBamboo ? 8 : 16))
                    .unlockedBy("has_log", has(log))
                    .pattern("#  ").pattern("## ").pattern("###")
                    .define('#', Ingredient.of(log))
                    .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "utility/" + woodType.name() + "_logs_to_stairs"));

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BuiltInRegistries.ITEM.get(ResourceLocation.parse(woodType.name() + "_door")), (isBamboo ? 6 : 12))
                    .unlockedBy("has_log", has(log))
                    .pattern("##").pattern("##").pattern("##")
                    .define('#', Ingredient.of(log))
                    .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "utility/" + woodType.name() + "_logs_to_doors"));

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BuiltInRegistries.ITEM.get(ResourceLocation.parse(woodType.name() + "_trapdoor")), (isBamboo ? 6 : 12))
                    .unlockedBy("has_log", has(log))
                    .pattern("###").pattern("###")
                    .define('#', Ingredient.of(log))
                    .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "utility/" + woodType.name() + "_logs_to_trapdoors"));

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BuiltInRegistries.ITEM.get(ResourceLocation.parse(woodType.name() + "_pressure_plate")), (isBamboo ? 2 : 4))
                    .unlockedBy("has_log", has(log))
                    .pattern("##")
                    .define('#', Ingredient.of(log))
                    .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "utility/" + woodType.name() + "_logs_to_pressure_plates"));

            var slab = BuiltInRegistries.ITEM.get(ResourceLocation.parse(woodType.name() + "_slab"));
                    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(woodType.name() + "_planks")), 1)
                            .unlockedBy("has_slab", has(slab))
                            .pattern("# ").pattern(" #")
                            .define('#', Ingredient.of(slab))
                            .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "utility/" + woodType.name() + "_slab_to_block"));

//            var boat = BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(woodType.name() + (isBamboo ? "_raft" : "_boat")));
//            if (!boat.equals(Items.AIR)) {
//                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, boat, (isBamboo ? 2 : 4))
//                        .unlockedBy("has_log", has(log))
//                        .pattern("# #").pattern("###")
//                        .define('#', Ingredient.of(log))
//                        .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "utility/" + woodType.name() + "_logs_to_boats"));
//            } else {
//                boat = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("fireproofboats", woodType.name() + "_boat"));
//                if (!boat.equals(Items.AIR)) {
//                    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, boat, 1)
//                            .unlockedBy("has_log", has(log))
//                            .pattern("# #").pattern("###")
//                            .define('#', Ingredient.of(log))
//                            .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "utility/" + woodType.name() + "_logs_to_boats"));
//                }
//            }
        });

        // Smelting recipes
        campfire(ItemTags.LOGS_THAT_BURN, Items.CHARCOAL, pRecipeOutput);

        List<String> metals = List.of("iron", "copper", "gold"); // , "aluminum", "bismuth", "iridium", "lead", "netherite", "nickel", "osmium", "platinum", "uranium", "silver", "tin", "titanium", "tungsten", "zinc"
        metals.forEach(s -> {
            blockSmelt(s, pRecipeOutput);
        });
    }

    void blockSmelt(String resource, RecipeOutput pRecipeOutput) {
        var rawItem = ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "raw_materials/" + resource));
        var rawBlock = ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/raw_" + resource));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(rawBlock), RecipeCategory.MISC, BuiltInRegistries.ITEM.get(ResourceLocation.parse(resource + "_block")), 6.3F, 1800)
                .unlockedBy("has_raw_" + resource, has(rawItem))
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "utility/" + resource + "_block_from_smelting"));

        SimpleCookingRecipeBuilder.blasting(Ingredient.of(rawBlock), RecipeCategory.MISC, BuiltInRegistries.ITEM.get(ResourceLocation.parse(resource + "_block")), 6.3F, 900)
                .unlockedBy("has_raw_" + resource, has(rawItem))
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "utility/" + resource + "_block_from_blasting"));
    }

    static void campfire(TagKey<Item> item, ItemLike cookedItem, RecipeOutput pRecipeOutput) {
        var name = BuiltInRegistries.ITEM.getKey(cookedItem.asItem());
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(item), RecipeCategory.FOOD, cookedItem, 0.35F, 600).unlockedBy("has_" + item.location().getPath(), has(item)).save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "utility/" + name.getPath() + "_from_campfire"));
    }
}
