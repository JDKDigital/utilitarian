package cy.jdkdigital.utilitarian.data;

import cy.jdkdigital.utilitarian.Utilitarian;
import cy.jdkdigital.utilitarian.module.AngelBlockModule;
import cy.jdkdigital.utilitarian.module.NoSolicitingModule;
import cy.jdkdigital.utilitarian.module.TPSBreakerModule;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class RecipeProvider extends net.minecraft.data.recipes.RecipeProvider implements IConditionBuilder
{
    public RecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AngelBlockModule.ANGEL_BLOCK_ITEM.get(), 1)
                .unlockedBy("has_gold", has(Tags.Items.INGOTS_GOLD))
                .unlockedBy("has_obsidian", has(Tags.Items.OBSIDIAN))
                .unlockedBy("has_feather", has(Tags.Items.FEATHERS))
                .pattern(" F ").pattern("#O#").pattern(" F ")
                .define('#', Ingredient.of(Tags.Items.INGOTS_GOLD))
                .define('O', Ingredient.of(Tags.Items.OBSIDIAN))
                .define('F', Ingredient.of(Tags.Items.FEATHERS))
                .save(consumer, new ResourceLocation(Utilitarian.MODID, "angel_block"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AngelBlockModule.ANGEL_BLOCK_ITEM.get(), 1)
                .unlockedBy("has_gold", has(Tags.Items.INGOTS_GOLD))
                .unlockedBy("has_obsidian", has(Tags.Items.OBSIDIAN))
                .unlockedBy("has_feather", has(Tags.Items.FEATHERS))
                .pattern(" # ").pattern("FOF").pattern(" # ")
                .define('#', Ingredient.of(Tags.Items.INGOTS_GOLD))
                .define('O', Ingredient.of(Tags.Items.OBSIDIAN))
                .define('F', Ingredient.of(Tags.Items.FEATHERS))
                .save(consumer, new ResourceLocation(Utilitarian.MODID, "angel_block_rot"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TPSBreakerModule.TPS_METER_ITEM.get(), 1)
                .unlockedBy(getHasName(Items.COMPARATOR), has(Items.COMPARATOR))
                .unlockedBy("has_obsidian", has(Tags.Items.OBSIDIAN))
                .pattern(" # ").pattern("#O#").pattern(" # ")
                .define('O', Ingredient.of(Tags.Items.OBSIDIAN))
                .define('#', Ingredient.of(Items.COMPARATOR))
                .save(consumer, new ResourceLocation(Utilitarian.MODID, "tps_meter"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, NoSolicitingModule.RESTRAINING_ORDER.get(), 1)
                .unlockedBy(getHasName(Items.PAPER), has(Items.PAPER))
                .unlockedBy(getHasName(Items.LEAD), has(Items.LEAD))
                .requires(Items.PAPER).requires(Items.LEAD)
                .save(consumer, new ResourceLocation(Utilitarian.MODID, "no_soliciting/restraining_order"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NoSolicitingModule.NO_SOLICITING_BANNER_ITEM.get(), 1)
                .group("banner")
                .unlockedBy(getHasName(Items.LEAD), has(Items.LEAD))
                .unlockedBy("has_wool", has(ItemTags.WOOL))
                .unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
                .pattern("###").pattern("#L#").pattern(" | ")
                .define('L', Ingredient.of(Items.LEAD))
                .define('#', Ingredient.of(ItemTags.WOOL))
                .define('|', Ingredient.of(Tags.Items.RODS_WOODEN))
                .save(consumer, new ResourceLocation(Utilitarian.MODID, "no_soliciting/no_soliciting_banner"));
        Arrays.stream(DyeColor.values()).forEach(dyeColor -> {
            var carpet = ForgeRegistries.ITEMS.getValue(new ResourceLocation(dyeColor.getSerializedName() + "_carpet"));
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, NoSolicitingModule.SOLICITING_CARPET_ITEM.get(dyeColor).get(), 1)
                    .unlockedBy(getHasName(carpet), has(carpet))
                    .unlockedBy("has_flower", has(ItemTags.SMALL_FLOWERS))
                    .requires(carpet).requires(ItemTags.SMALL_FLOWERS)
                    .save(consumer, new ResourceLocation(Utilitarian.MODID, "no_soliciting/soliciting_carpets/" + dyeColor.getSerializedName() + "_soliciting_carpet"));
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, NoSolicitingModule.TRAPPED_SOLICITING_CARPET_ITEM.get(dyeColor).get(), 1)
                    .unlockedBy(getHasName(NoSolicitingModule.SOLICITING_CARPET_ITEM.get(dyeColor).get()), has(NoSolicitingModule.SOLICITING_CARPET_ITEM.get(dyeColor).get()))
                    .unlockedBy(getHasName(Items.TRIPWIRE_HOOK), has(Items.TRIPWIRE_HOOK))
                    .requires(NoSolicitingModule.SOLICITING_CARPET_ITEM.get(dyeColor).get()).requires(Items.TRIPWIRE_HOOK)
                    .save(consumer, new ResourceLocation(Utilitarian.MODID, "no_soliciting/soliciting_carpets/" + dyeColor.getSerializedName() + "_trapped_soliciting_carpet"));
        });

        // Utility recipes
        ConditionalRecipe.builder().addCondition(not(modLoaded("quark"))).addRecipe(
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.CHEST, 4)
                        .unlockedBy("has_log", has(ItemTags.LOGS))
                        .pattern("###").pattern("# #").pattern("###")
                        .define('#', Ingredient.of(ItemTags.LOGS))
                        ::save).build(consumer, new ResourceLocation(Utilitarian.MODID, "utility/logs_to_chests"));

        ConditionalRecipe.builder().addCondition(not(modLoaded("quark"))).addRecipe(
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.STICK, 16)
                        .unlockedBy("has_log", has(ItemTags.LOGS))
                        .pattern("#").pattern("#")
                        .define('#', Ingredient.of(ItemTags.LOGS))
                        ::save).build(consumer, new ResourceLocation(Utilitarian.MODID, "utility/logs_to_sticks"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.BOWL, 12)
                .unlockedBy("has_log", has(ItemTags.LOGS))
                .pattern("# #").pattern(" # ")
                .define('#', Ingredient.of(ItemTags.LOGS))
                .save(consumer, new ResourceLocation(Utilitarian.MODID, "utility/logs_to_bowls"));

        ConditionalRecipe.builder().addCondition(not(modLoaded("quark"))).addRecipe(
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.LADDER, 24)
                        .unlockedBy("has_log", has(ItemTags.LOGS))
                        .pattern("# #").pattern("###").pattern("# #")
                        .define('#', Ingredient.of(ItemTags.LOGS))
                        ::save).build(consumer, new ResourceLocation(Utilitarian.MODID, "utility/logs_to_ladders"));

        ConditionalRecipe.builder().addCondition(not(modLoaded("quark"))).addRecipe(
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.HOPPER, 1)
                        .unlockedBy("has_log", has(ItemTags.LOGS))
                        .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                        .pattern("I#I").pattern("I#I").pattern(" I ")
                        .define('#', Ingredient.of(ItemTags.LOGS))
                        .define('I', Ingredient.of(Tags.Items.INGOTS_IRON))
                        ::save).build(consumer, new ResourceLocation(Utilitarian.MODID, "utility/logs_to_hopper"));

        ConditionalRecipe.builder().addCondition(not(modLoaded("quark"))).addRecipe(
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.DISPENSER, 1)
                        .unlockedBy(getHasName(Items.DISPENSER), has(Items.DROPPER))
                        .unlockedBy("has_string", has(Tags.Items.STRING))
                        .unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
                        .pattern(" RS").pattern("RDS").pattern(" RS")
                        .define('R', Ingredient.of(Tags.Items.RODS_WOODEN))
                        .define('S', Ingredient.of(Tags.Items.STRING))
                        .define('D', Ingredient.of(Items.DROPPER))
                        ::save).build(consumer, new ResourceLocation(Utilitarian.MODID, "utility/dispenser"));

        ConditionalRecipe.builder().addCondition(not(modLoaded("quark"))).addRecipe(
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.REPEATER, 1)
                        .unlockedBy(getHasName(Items.DISPENSER), has(Items.STONE))
                        .unlockedBy("has_redstone", has(Tags.Items.DUSTS_REDSTONE))
                        .unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
                        .pattern("D D").pattern("RDR").pattern("SSS")
                        .define('R', Ingredient.of(Tags.Items.RODS_WOODEN))
                        .define('S', Ingredient.of(Items.STONE))
                        .define('D', Ingredient.of(Tags.Items.DUSTS_REDSTONE))
                        ::save).build(consumer, new ResourceLocation(Utilitarian.MODID, "utility/repeater"));

        ConditionalRecipe.builder().addCondition(not(modLoaded("quark"))).addRecipe(
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.CHEST_MINECART, 1)
                        .unlockedBy("has_chest", has(Tags.Items.CHESTS))
                        .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                        .pattern("ICI").pattern("III")
                        .define('C', Ingredient.of(Tags.Items.CHESTS))
                        .define('I', Ingredient.of(Tags.Items.INGOTS_IRON))
                        ::save).build(consumer, new ResourceLocation(Utilitarian.MODID, "utility/chest_minecart"));

        ConditionalRecipe.builder().addCondition(not(modLoaded("quark"))).addRecipe(
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.CHEST_MINECART, 1)
                        .unlockedBy(getHasName(Items.HOPPER), has(Items.HOPPER))
                        .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                        .pattern("IHI").pattern("III")
                        .define('H', Ingredient.of(Items.HOPPER))
                        .define('I', Ingredient.of(Tags.Items.INGOTS_IRON))
                        ::save).build(consumer, new ResourceLocation(Utilitarian.MODID, "utility/hopper_minecart"));

        ConditionalRecipe.builder().addCondition(not(modLoaded("quark"))).addRecipe(
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.CHEST_MINECART, 1)
                        .unlockedBy(getHasName(Items.TNT), has(Items.TNT))
                        .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                        .pattern("ITI").pattern("III")
                        .define('T', Ingredient.of(Items.TNT))
                        .define('I', Ingredient.of(Tags.Items.INGOTS_IRON))
                        ::save).build(consumer, new ResourceLocation(Utilitarian.MODID, "utility/tnt_minecart"));

        ConditionalRecipe.builder().addCondition(not(modLoaded("quark"))).addRecipe(
                ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.PAPER, 3)
                        .unlockedBy(getHasName(Items.SUGAR_CANE), has(Items.SUGAR_CANE))
                        .requires(Items.SUGAR_CANE).requires(Items.SUGAR_CANE).requires(Items.SUGAR_CANE)
                        ::save).build(consumer, new ResourceLocation(Utilitarian.MODID, "utility/paper"));


        WoodType.values().forEach(woodType -> {
            var isBamboo = woodType.name().equals("bamboo");
            var log = ForgeRegistries.ITEMS.getValue(new ResourceLocation(woodType.name() + (isBamboo ? "_block" : "_log")));
            if (log == null) {
                log = ForgeRegistries.ITEMS.getValue(new ResourceLocation(woodType.name() + "_stem"));
            }
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ForgeRegistries.ITEMS.getValue(new ResourceLocation(woodType.name() + "_slab")), (isBamboo ? 12 : 24))
                    .unlockedBy("has_log", has(log))
                    .pattern("###")
                    .define('#', Ingredient.of(log))
                    .save(consumer, new ResourceLocation(Utilitarian.MODID, "utility/" + woodType.name() + "_logs_to_slabs"));

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ForgeRegistries.ITEMS.getValue(new ResourceLocation(woodType.name() + "_stairs")), (isBamboo ? 8 : 16))
                    .unlockedBy("has_log", has(log))
                    .pattern("#  ").pattern("## ").pattern("###")
                    .define('#', Ingredient.of(log))
                    .save(consumer, new ResourceLocation(Utilitarian.MODID, "utility/" + woodType.name() + "_logs_to_stairs"));

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ForgeRegistries.ITEMS.getValue(new ResourceLocation(woodType.name() + "_door")), (isBamboo ? 6 : 12))
                    .unlockedBy("has_log", has(log))
                    .pattern("##").pattern("##").pattern("##")
                    .define('#', Ingredient.of(log))
                    .save(consumer, new ResourceLocation(Utilitarian.MODID, "utility/" + woodType.name() + "_logs_to_doors"));

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ForgeRegistries.ITEMS.getValue(new ResourceLocation(woodType.name() + "_trapdoor")), (isBamboo ? 6 : 12))
                    .unlockedBy("has_log", has(log))
                    .pattern("###").pattern("###")
                    .define('#', Ingredient.of(log))
                    .save(consumer, new ResourceLocation(Utilitarian.MODID, "utility/" + woodType.name() + "_logs_to_trapdoors"));

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ForgeRegistries.ITEMS.getValue(new ResourceLocation(woodType.name() + "_pressure_plate")), (isBamboo ? 2 : 4))
                    .unlockedBy("has_log", has(log))
                    .pattern("##")
                    .define('#', Ingredient.of(log))
                    .save(consumer, new ResourceLocation(Utilitarian.MODID, "utility/" + woodType.name() + "_logs_to_pressure_plates"));

            var slab = ForgeRegistries.ITEMS.getValue(new ResourceLocation(woodType.name() + "_slab"));
            ConditionalRecipe.builder().addCondition(not(modLoaded("quark"))).addRecipe(
                    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ForgeRegistries.ITEMS.getValue(new ResourceLocation(woodType.name() + "_planks")), (isBamboo ? 2 : 4))
                            .unlockedBy("has_slab", has(slab))
                            .pattern("#").pattern("#")
                            .define('#', Ingredient.of(slab))
                            ::save).build(consumer, new ResourceLocation(Utilitarian.MODID, "utility/" + woodType.name() + "_slab_to_block"));

            var boat = ForgeRegistries.ITEMS.getValue(new ResourceLocation(woodType.name() + (isBamboo ? "_raft" : "_boat")));
            if (boat != null) {
                    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, boat, (isBamboo ? 2 : 4))
                            .unlockedBy("has_log", has(log))
                            .pattern("# #").pattern("###")
                            .define('#', Ingredient.of(log))
                            .save(consumer, new ResourceLocation(Utilitarian.MODID, "utility/" + woodType.name() + "_logs_to_boats"));
            } else {
                boat = ForgeRegistries.ITEMS.getValue(new ResourceLocation("fireproofboats", woodType.name() + "_boat"));
                ConditionalRecipe.builder().addCondition(modLoaded("fireproofboats")).addRecipe(
                        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, boat, 4)
                                .unlockedBy("has_log", has(log))
                                .pattern("# #").pattern("###")
                                .define('#', Ingredient.of(log))
                                ::save).build(consumer, new ResourceLocation(Utilitarian.MODID, "utility/" + woodType.name() + "_logs_to_boats"));
            }
        });

        // Smelting recipes
        campfire(ItemTags.LOGS, Items.CHARCOAL, consumer);

        List<String> metals = List.of("iron", "copper", "gold"); // , "aluminum", "bismuth", "iridium", "lead", "netherite", "nickel", "osmium", "platinum", "uranium", "silver", "tin", "titanium", "tungsten", "zinc"
        metals.forEach(s -> {
            blockSmelt(s, consumer);
        });
    }

    void blockSmelt(String resource, Consumer<FinishedRecipe> consumer) {
        var rawItem = ItemTags.create(new ResourceLocation("forge", "raw_materials/" + resource));
        var rawBlock = ItemTags.create(new ResourceLocation("forge", "storage_blocks/raw_" + resource));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(rawBlock), RecipeCategory.MISC, ForgeRegistries.ITEMS.getValue(new ResourceLocation(resource + "_block")), 6.3F, 1800)
                .unlockedBy("has_raw_" + resource, has(rawItem))
                .save(consumer, new ResourceLocation(Utilitarian.MODID, "utility/" + resource + "_block_from_smelting"));

        SimpleCookingRecipeBuilder.blasting(Ingredient.of(rawBlock), RecipeCategory.MISC, ForgeRegistries.ITEMS.getValue(new ResourceLocation(resource + "_block")), 6.3F, 900)
                .unlockedBy("has_raw_" + resource, has(rawItem))
                .save(consumer, new ResourceLocation(Utilitarian.MODID, "utility/" + resource + "_block_from_blasting"));
    }

    static void campfire(TagKey<Item> item, ItemLike cookedItem, Consumer<FinishedRecipe> consumer) {
        var name = ForgeRegistries.ITEMS.getKey(cookedItem.asItem());
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(item), RecipeCategory.FOOD, cookedItem, 0.35F, 600).unlockedBy("has_" + item.location().getPath(), has(item)).save(consumer, new ResourceLocation(Utilitarian.MODID, "utility/" + name.getPath() + "_from_campfire"));
    }
}
