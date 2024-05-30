package cy.jdkdigital.utilitarian.module;

import cy.jdkdigital.utilitarian.Utilitarian;
import cy.jdkdigital.utilitarian.common.block.AngelBlock;
import cy.jdkdigital.utilitarian.common.block.FluidHopperBlock;
import cy.jdkdigital.utilitarian.common.block.RedstoneClockBlock;
import cy.jdkdigital.utilitarian.common.block.entity.FluidHopperBlockEntity;
import cy.jdkdigital.utilitarian.common.block.entity.RedstoneClockBlockEntity;
import cy.jdkdigital.utilitarian.common.block.entity.TPSMeterBlockEntity;
import cy.jdkdigital.utilitarian.common.item.AngelBlockItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.RegistryObject;

public class UtilityBlockModule
{
    public static RegistryObject<Block> ANGEL_BLOCK;
    public static RegistryObject<Item> ANGEL_BLOCK_ITEM;

    public static RegistryObject<Block> FLUID_HOPPER_BLOCK;
    public static RegistryObject<Item> FLUID_HOPPER_BLOCK_ITEM;
    public static RegistryObject<BlockEntityType<FluidHopperBlockEntity>> FLUID_HOPPER_BLOCK_ENTITY;

    public static RegistryObject<Block> REDSTONE_CLOCK_BLOCK;
    public static RegistryObject<Item> REDSTONE_CLOCK_BLOCK_ITEM;
    public static RegistryObject<BlockEntityType<RedstoneClockBlockEntity>> REDSTONE_CLOCK_BLOCK_ENTITY;

    public static void register() {
        ANGEL_BLOCK = Utilitarian.BLOCKS.register("angel_block", () -> new AngelBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).noCollission().instabreak().pushReaction(PushReaction.DESTROY)));
        ANGEL_BLOCK_ITEM = Utilitarian.ITEMS.register("angel_block", () -> new AngelBlockItem(ANGEL_BLOCK.get(), new Item.Properties()));

        FLUID_HOPPER_BLOCK = Utilitarian.BLOCKS.register("fluid_hopper", () -> new FluidHopperBlock(BlockBehaviour.Properties.copy(Blocks.HOPPER)));
        FLUID_HOPPER_BLOCK_ITEM = Utilitarian.ITEMS.register("fluid_hopper", () -> new BlockItem(FLUID_HOPPER_BLOCK.get(), new Item.Properties()));
        FLUID_HOPPER_BLOCK_ENTITY = Utilitarian.BLOCK_ENTITY.register("fluid_hopper", () -> BlockEntityType.Builder.of(FluidHopperBlockEntity::new, FLUID_HOPPER_BLOCK.get()).build(null));

        REDSTONE_CLOCK_BLOCK = Utilitarian.BLOCKS.register("redstone_clock", () -> new RedstoneClockBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS).isRedstoneConductor((pState, pLevel, pPos) -> false)));
        REDSTONE_CLOCK_BLOCK_ITEM = Utilitarian.ITEMS.register("redstone_clock", () -> new BlockItem(REDSTONE_CLOCK_BLOCK.get(), new Item.Properties()));
        REDSTONE_CLOCK_BLOCK_ENTITY = Utilitarian.BLOCK_ENTITY.register("redstone_clock", () -> BlockEntityType.Builder.of(RedstoneClockBlockEntity::new, REDSTONE_CLOCK_BLOCK.get()).build(null));
    }
}
