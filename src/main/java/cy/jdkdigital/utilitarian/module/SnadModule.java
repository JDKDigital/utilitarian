package cy.jdkdigital.utilitarian.module;

import cy.jdkdigital.utilitarian.Utilitarian;
import cy.jdkdigital.utilitarian.common.block.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
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
    public static final ResourceKey<DamageType> DRIT_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(Utilitarian.MODID, "drit"));
    public static final ResourceKey<DamageType> GRRASS_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(Utilitarian.MODID, "grrass"));

    public static final TagKey<Block> SAND_GROWABLES = BlockTags.create(Identifier.fromNamespaceAndPath("c", "sand_growables"));
    public static final TagKey<Block> SOUL_SAND_GROWABLES = BlockTags.create(Identifier.fromNamespaceAndPath("c", "soul_sand_growables"));
    public static final TagKey<EntityType<?>> CURSED_GRRASS_BLACKLIST = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(Utilitarian.MODID, "cursed_grrass_blacklist"));

    public static DeferredHolder<Block, Block> SNAD_BLOCK;
    public static DeferredHolder<Block, Block> RED_SNAD_BLOCK;
    public static DeferredHolder<Block, Block> SOUL_SNAD_BLOCK;
    public static DeferredHolder<Block, Block> DRIT_BLOCK;
    public static DeferredHolder<Block, Block> GRRASS_BLOCK;
    public static DeferredHolder<Block, Block> CURSED_GRRASS_BLOCK;

    public static DeferredHolder<Item, BlockItem> SNAD_BLOCK_ITEM;
    public static DeferredHolder<Item, BlockItem> RED_SNAD_BLOCK_ITEM;
    public static DeferredHolder<Item, BlockItem> SOUL_SNAD_BLOCK_ITEM;
    public static DeferredHolder<Item, BlockItem> DRIT_BLOCK_ITEM;
    public static DeferredHolder<Item, BlockItem> GRRASS_BLOCK_ITEM;
    public static DeferredHolder<Item, BlockItem> CURSED_GRRASS_BLOCK_ITEM;

    public static void register() {
        SNAD_BLOCK = Utilitarian.BLOCKS.registerBlock("snad", p -> new SnadBlock(new ColorRGBA(14406560), p), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.SAND).randomTicks());
        RED_SNAD_BLOCK = Utilitarian.BLOCKS.registerBlock("red_snad", p -> new SnadBlock(new ColorRGBA(11098145), p), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.SAND).randomTicks());
        SOUL_SNAD_BLOCK = Utilitarian.BLOCKS.registerBlock("soul_snad", SoulSnadBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.SOUL_SAND).randomTicks());
        DRIT_BLOCK = Utilitarian.BLOCKS.registerBlock("drit", DritBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT).randomTicks());
        GRRASS_BLOCK = Utilitarian.BLOCKS.registerBlock("grrass", GrrassBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK).randomTicks());
        CURSED_GRRASS_BLOCK = Utilitarian.BLOCKS.registerBlock("cursed_grrass", CursedGrrassBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK).randomTicks());

        SNAD_BLOCK_ITEM = Utilitarian.ITEMS.registerSimpleBlockItem(SNAD_BLOCK);
        RED_SNAD_BLOCK_ITEM = Utilitarian.ITEMS.registerSimpleBlockItem(RED_SNAD_BLOCK);
        SOUL_SNAD_BLOCK_ITEM = Utilitarian.ITEMS.registerSimpleBlockItem(SOUL_SNAD_BLOCK);
        DRIT_BLOCK_ITEM = Utilitarian.ITEMS.registerSimpleBlockItem(DRIT_BLOCK);
        GRRASS_BLOCK_ITEM = Utilitarian.ITEMS.registerSimpleBlockItem(GRRASS_BLOCK);
        CURSED_GRRASS_BLOCK_ITEM = Utilitarian.ITEMS.registerSimpleBlockItem(CURSED_GRRASS_BLOCK);
    }
}
