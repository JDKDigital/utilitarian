package cy.jdkdigital.utilitarian.module;

import cy.jdkdigital.utilitarian.Utilitarian;
import cy.jdkdigital.utilitarian.common.block.*;
import cy.jdkdigital.utilitarian.common.block.entity.FluidHopperBlockEntity;
import cy.jdkdigital.utilitarian.common.block.entity.RedstoneClockBlockEntity;
import cy.jdkdigital.utilitarian.common.block.entity.WellBehavedDropperBlockEntity;
import cy.jdkdigital.utilitarian.common.item.AngelBlockItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.HashSet;
import java.util.Set;

public class UtilityBlockModule
{
    public static TagKey<Block> MUFFLERS = BlockTags.create(Identifier.fromNamespaceAndPath(Utilitarian.MODID, "mufflers"));

    public static DeferredHolder<Block, Block> ANGEL_BLOCK;
    public static DeferredHolder<Item, Item> ANGEL_BLOCK_ITEM;

    public static DeferredHolder<Block, Block> SOUND_MUFFLER;
    public static DeferredHolder<Item, BlockItem> SOUND_MUFFLER_ITEM;

    public static DeferredHolder<Block, Block> LAPIS_LAMP;
    public static DeferredHolder<Item, BlockItem> LAPIS_LAMP_ITEM;

    public static DeferredHolder<Block, Block> INVERTED_LAPIS_LAMP;
    public static DeferredHolder<Item, BlockItem> INVERTED_LAPIS_LAMP_ITEM;

    public static DeferredHolder<Block, Block> INVERTED_REDSTONE_LAMP;
    public static DeferredHolder<Item, BlockItem> INVERTED_REDSTONE_LAMP_ITEM;

    public static DeferredHolder<Block, Block> FLUID_HOPPER_BLOCK;
    public static DeferredHolder<Item, BlockItem> FLUID_HOPPER_BLOCK_ITEM;
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<FluidHopperBlockEntity>> FLUID_HOPPER_BLOCK_ENTITY;

    public static DeferredHolder<Block, Block> REDSTONE_CLOCK_BLOCK;
    public static DeferredHolder<Item, BlockItem> REDSTONE_CLOCK_BLOCK_ITEM;
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<RedstoneClockBlockEntity>> REDSTONE_CLOCK_BLOCK_ENTITY;

    public static DeferredHolder<Block, Block> WELL_BEHAVED_DROPPER;
    public static DeferredHolder<Item, BlockItem> WELL_BEHAVED_DROPPER_ITEM;
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<WellBehavedDropperBlockEntity>> WELL_BEHAVED_DROPPER_BLOCK_ENTITY;

    public static DeferredHolder<Block, Block> MAGNET;
    public static DeferredHolder<Item, BlockItem> MAGNET_ITEM;

    public static TagKey<PoiType> SOUND_MUFFLER_POI_TAG = TagKey.create(Registries.POINT_OF_INTEREST_TYPE, Identifier.fromNamespaceAndPath(Utilitarian.MODID, "sound_muffler"));
    public static DeferredHolder<PoiType, PoiType> SOUND_MUFFLER_POI;

    // TODO particle preventer block

    public static void register() {
        ANGEL_BLOCK = Utilitarian.BLOCKS.registerBlock("angel_block", p -> new AngelBlock(p.mapColor(MapColor.COLOR_BLACK).noCollision().instabreak().pushReaction(PushReaction.DESTROY)));
        ANGEL_BLOCK_ITEM = Utilitarian.ITEMS.registerItem("angel_block", p -> new AngelBlockItem(ANGEL_BLOCK.get(), p.useBlockDescriptionPrefix()));

        LAPIS_LAMP = Utilitarian.BLOCKS.registerBlock("lapis_lamp", p -> new LapisLampBlock(p.lightLevel(state -> 0), false), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.REDSTONE_LAMP));
        LAPIS_LAMP_ITEM = Utilitarian.ITEMS.registerSimpleBlockItem(LAPIS_LAMP);

        INVERTED_LAPIS_LAMP = Utilitarian.BLOCKS.registerBlock("inverted_lapis_lamp", p -> new LapisLampBlock(p, true), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.REDSTONE_LAMP).lightLevel(state -> 0));
        INVERTED_LAPIS_LAMP_ITEM = Utilitarian.ITEMS.registerSimpleBlockItem(INVERTED_LAPIS_LAMP);

        INVERTED_REDSTONE_LAMP = Utilitarian.BLOCKS.registerBlock("inverted_redstone_lamp", RedstoneLampBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.REDSTONE_LAMP).lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? 0 : 15));
        INVERTED_REDSTONE_LAMP_ITEM = Utilitarian.ITEMS.registerSimpleBlockItem(INVERTED_REDSTONE_LAMP);

        SOUND_MUFFLER = Utilitarian.BLOCKS.registerBlock("sound_muffler", MufflerBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL));
        SOUND_MUFFLER_ITEM = Utilitarian.ITEMS.registerSimpleBlockItem(SOUND_MUFFLER);

        FLUID_HOPPER_BLOCK = Utilitarian.BLOCKS.registerBlock("fluid_hopper", FluidHopperBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.HOPPER));
        FLUID_HOPPER_BLOCK_ITEM = Utilitarian.ITEMS.registerSimpleBlockItem(FLUID_HOPPER_BLOCK);
        FLUID_HOPPER_BLOCK_ENTITY = Utilitarian.BLOCK_ENTITY.register("fluid_hopper", () -> new BlockEntityType<>(FluidHopperBlockEntity::new, FLUID_HOPPER_BLOCK.get()));

        REDSTONE_CLOCK_BLOCK = Utilitarian.BLOCKS.registerBlock("redstone_clock", RedstoneClockBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).isRedstoneConductor((pState, pLevel, pPos) -> false));
        REDSTONE_CLOCK_BLOCK_ITEM = Utilitarian.ITEMS.registerSimpleBlockItem(REDSTONE_CLOCK_BLOCK);
        REDSTONE_CLOCK_BLOCK_ENTITY = Utilitarian.BLOCK_ENTITY.register("redstone_clock", () -> new BlockEntityType<>(RedstoneClockBlockEntity::new, REDSTONE_CLOCK_BLOCK.get()));

        WELL_BEHAVED_DROPPER = Utilitarian.BLOCKS.registerBlock("well_behaved_dropper", WellBehavedDropperBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.DROPPER).isRedstoneConductor((pState, pLevel, pPos) -> false));
        WELL_BEHAVED_DROPPER_ITEM = Utilitarian.ITEMS.registerSimpleBlockItem(WELL_BEHAVED_DROPPER);
        WELL_BEHAVED_DROPPER_BLOCK_ENTITY = Utilitarian.BLOCK_ENTITY.register("well_behaved_dropper", () -> new BlockEntityType<>(WellBehavedDropperBlockEntity::new, WELL_BEHAVED_DROPPER.get()));

        MAGNET = Utilitarian.BLOCKS.registerBlock("magnet", MagnetBlock::new, () -> BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).noOcclusion());
        MAGNET_ITEM = Utilitarian.ITEMS.registerSimpleBlockItem(MAGNET);

        SOUND_MUFFLER_POI = Utilitarian.POI_TYPES.register("sound_muffler", () -> {
            Set<BlockState> blockStates = new HashSet<>(SOUND_MUFFLER.get().getStateDefinition().getPossibleStates());
            return new PoiType(blockStates, 1, 1);
        });
    }
}
