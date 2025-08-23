package cy.jdkdigital.utilitarian.module;

import cy.jdkdigital.utilitarian.Utilitarian;
import cy.jdkdigital.utilitarian.common.block.*;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ColorRGBA;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredHolder;

public class SnadModule
{
    public static final ResourceKey<DamageType> DRIT_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "drit"));
    public static final ResourceKey<DamageType> GRRASS_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "grrass"));

    public static final TagKey<Block> SAND_GROWABLES = BlockTags.create(ResourceLocation.fromNamespaceAndPath("c", "sand_growables"));
    public static final TagKey<Block> SOUL_SAND_GROWABLES = BlockTags.create(ResourceLocation.fromNamespaceAndPath("c", "soul_sand_growables"));
    public static final TagKey<EntityType<?>> CURSED_GRRASS_BLACKLIST = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "cursed_grrass_blacklist"));

    public static DeferredHolder<Block, Block> SNAD_BLOCK;
    public static DeferredHolder<Block, Block> RED_SNAD_BLOCK;
    public static DeferredHolder<Block, Block> SOUL_SNAD_BLOCK;
    public static DeferredHolder<Block, Block> DRIT_BLOCK;
    public static DeferredHolder<Block, Block> GRRASS_BLOCK;
    public static DeferredHolder<Block, Block> CURSED_GRRASS_BLOCK;

    public static DeferredHolder<Item, Item> SNAD_BLOCK_ITEM;
    public static DeferredHolder<Item, Item> RED_SNAD_BLOCK_ITEM;
    public static DeferredHolder<Item, Item> SOUL_SNAD_BLOCK_ITEM;
    public static DeferredHolder<Item, Item> DRIT_BLOCK_ITEM;
    public static DeferredHolder<Item, Item> GRRASS_BLOCK_ITEM;
    public static DeferredHolder<Item, Item> CURSED_GRRASS_BLOCK_ITEM;

    public static void register() {
        SNAD_BLOCK = Utilitarian.BLOCKS.register("snad", () -> new SnadBlock(new ColorRGBA(14406560), BlockBehaviour.Properties.ofFullCopy(Blocks.SAND).randomTicks()));
        RED_SNAD_BLOCK = Utilitarian.BLOCKS.register("red_snad", () -> new SnadBlock(new ColorRGBA(11098145), BlockBehaviour.Properties.ofFullCopy(Blocks.SAND).randomTicks()));
        SOUL_SNAD_BLOCK = Utilitarian.BLOCKS.register("soul_snad", () -> new SoulSnadBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SOUL_SAND).randomTicks()));
        DRIT_BLOCK = Utilitarian.BLOCKS.register("drit", () -> new DritBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT).randomTicks()));
        GRRASS_BLOCK = Utilitarian.BLOCKS.register("grrass", () -> new GrrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK).randomTicks()));
        CURSED_GRRASS_BLOCK = Utilitarian.BLOCKS.register("cursed_grrass", () -> new CursedGrrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK).randomTicks()));

        SNAD_BLOCK_ITEM = Utilitarian.ITEMS.register("snad", () -> new BlockItem(SNAD_BLOCK.get(), new Item.Properties()));
        RED_SNAD_BLOCK_ITEM = Utilitarian.ITEMS.register("red_snad", () -> new BlockItem(RED_SNAD_BLOCK.get(), new Item.Properties()));
        SOUL_SNAD_BLOCK_ITEM = Utilitarian.ITEMS.register("soul_snad", () -> new BlockItem(SOUL_SNAD_BLOCK.get(), new Item.Properties()));
        DRIT_BLOCK_ITEM = Utilitarian.ITEMS.register("drit", () -> new BlockItem(DRIT_BLOCK.get(), new Item.Properties()));
        GRRASS_BLOCK_ITEM = Utilitarian.ITEMS.register("grrass", () -> new BlockItem(GRRASS_BLOCK.get(), new Item.Properties()));
        CURSED_GRRASS_BLOCK_ITEM = Utilitarian.ITEMS.register("cursed_grrass", () -> new BlockItem(CURSED_GRRASS_BLOCK.get(), new Item.Properties()));
    }
}
