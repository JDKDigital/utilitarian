package cy.jdkdigital.utilitarian.module;

import cy.jdkdigital.utilitarian.Utilitarian;
import cy.jdkdigital.utilitarian.common.block.*;
import cy.jdkdigital.utilitarian.common.block.entity.FluidHopperBlockEntity;
import cy.jdkdigital.utilitarian.common.block.entity.RedstoneClockBlockEntity;
import cy.jdkdigital.utilitarian.common.block.entity.WellBehavedDropperBlockEntity;
import cy.jdkdigital.utilitarian.common.item.AngelBlockItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.HashSet;
import java.util.Set;

public class UtilityBlockModule
{
    public static DeferredHolder<Block, Block> ANGEL_BLOCK;
    public static DeferredHolder<Item, Item> ANGEL_BLOCK_ITEM;

    public static DeferredHolder<Block, Block> SOUND_MUFFLER;
    public static DeferredHolder<Item, Item> SOUND_MUFFLER_ITEM;

    public static DeferredHolder<Block, Block> ENTITY_SOUND_MUFFLER;
    public static DeferredHolder<Item, Item> ENTITY_SOUND_MUFFLER_ITEM;

    public static DeferredHolder<Block, Block> LAPIS_LAMP;
    public static DeferredHolder<Item, Item> LAPIS_LAMP_ITEM;

    public static DeferredHolder<Block, Block> FLUID_HOPPER_BLOCK;
    public static DeferredHolder<Item, Item> FLUID_HOPPER_BLOCK_ITEM;
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<FluidHopperBlockEntity>> FLUID_HOPPER_BLOCK_ENTITY;

    public static DeferredHolder<Block, Block> REDSTONE_CLOCK_BLOCK;
    public static DeferredHolder<Item, Item> REDSTONE_CLOCK_BLOCK_ITEM;
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<RedstoneClockBlockEntity>> REDSTONE_CLOCK_BLOCK_ENTITY;

    public static DeferredHolder<Block, Block> WELL_BEHAVED_DROPPER;
    public static DeferredHolder<Item, Item> WELL_BEHAVED_DROPPER_ITEM;
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<WellBehavedDropperBlockEntity>> WELL_BEHAVED_DROPPER_BLOCK_ENTITY;

    public static TagKey<PoiType> SOUND_MUFFLER_POI_TAG = TagKey.create(Registries.POINT_OF_INTEREST_TYPE, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "sound_muffler"));
    public static DeferredHolder<PoiType, PoiType> SOUND_MUFFLER_POI;

    public static void register() {
        ANGEL_BLOCK = Utilitarian.BLOCKS.register("angel_block", () -> new AngelBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).noCollission().instabreak().pushReaction(PushReaction.DESTROY)));
        ANGEL_BLOCK_ITEM = Utilitarian.ITEMS.register("angel_block", () -> new AngelBlockItem(ANGEL_BLOCK.get(), new Item.Properties()));

        LAPIS_LAMP = Utilitarian.BLOCKS.register("lapis_lamp", () -> new LapisLampBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.REDSTONE_LAMP).lightLevel(state -> 0)));
        LAPIS_LAMP_ITEM = Utilitarian.ITEMS.register("lapis_lamp", () -> new BlockItem(LAPIS_LAMP.get(), new Item.Properties()));

        SOUND_MUFFLER = Utilitarian.BLOCKS.register("sound_muffler", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL)));
        SOUND_MUFFLER_ITEM = Utilitarian.ITEMS.register("sound_muffler", () -> new AngelBlockItem(SOUND_MUFFLER.get(), new Item.Properties()));

        FLUID_HOPPER_BLOCK = Utilitarian.BLOCKS.register("fluid_hopper", () -> new FluidHopperBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.HOPPER)));
        FLUID_HOPPER_BLOCK_ITEM = Utilitarian.ITEMS.register("fluid_hopper", () -> new BlockItem(FLUID_HOPPER_BLOCK.get(), new Item.Properties()));
        FLUID_HOPPER_BLOCK_ENTITY = Utilitarian.BLOCK_ENTITY.register("fluid_hopper", () -> BlockEntityType.Builder.of(FluidHopperBlockEntity::new, FLUID_HOPPER_BLOCK.get()).build(null));

        REDSTONE_CLOCK_BLOCK = Utilitarian.BLOCKS.register("redstone_clock", () -> new RedstoneClockBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).isRedstoneConductor((pState, pLevel, pPos) -> false)));
        REDSTONE_CLOCK_BLOCK_ITEM = Utilitarian.ITEMS.register("redstone_clock", () -> new BlockItem(REDSTONE_CLOCK_BLOCK.get(), new Item.Properties()));
        REDSTONE_CLOCK_BLOCK_ENTITY = Utilitarian.BLOCK_ENTITY.register("redstone_clock", () -> BlockEntityType.Builder.of(RedstoneClockBlockEntity::new, REDSTONE_CLOCK_BLOCK.get()).build(null));

        WELL_BEHAVED_DROPPER = Utilitarian.BLOCKS.register("well_behaved_dropper", () -> new WellBehavedDropperBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DROPPER).isRedstoneConductor((pState, pLevel, pPos) -> false)));
        WELL_BEHAVED_DROPPER_ITEM = Utilitarian.ITEMS.register("well_behaved_dropper", () -> new BlockItem(WELL_BEHAVED_DROPPER.get(), new Item.Properties()));
        WELL_BEHAVED_DROPPER_BLOCK_ENTITY = Utilitarian.BLOCK_ENTITY.register("well_behaved_dropper", () -> BlockEntityType.Builder.of(WellBehavedDropperBlockEntity::new, WELL_BEHAVED_DROPPER.get()).build(null));

        SOUND_MUFFLER_POI = Utilitarian.POI_TYPES.register("sound_muffler", () -> {
            Set<BlockState> blockStates = new HashSet<>(SOUND_MUFFLER.get().getStateDefinition().getPossibleStates());
            return new PoiType(blockStates, 1, 1);
        });
    }
}
