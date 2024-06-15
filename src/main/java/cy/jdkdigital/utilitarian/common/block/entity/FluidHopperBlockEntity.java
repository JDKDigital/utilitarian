package cy.jdkdigital.utilitarian.common.block.entity;

import cy.jdkdigital.utilitarian.Config;
import cy.jdkdigital.utilitarian.module.UtilityBlockModule;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public class FluidHopperBlockEntity extends BlockEntity
{
    int tick = 0;
    public IFluidHandler internalTank = new FluidTank(1000, p -> true);

    public FluidHopperBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(UtilityBlockModule.FLUID_HOPPER_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    public static <E extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState blockState, FluidHopperBlockEntity blockEntity) {
        if (blockState.getValue(BlockStateProperties.ENABLED) && ++blockEntity.tick >= Config.FLUID_HOPPER_TICK_RATE.get()) {
            blockEntity.tick = 0;
            var hasInserted = false;
            var internalFluid = blockEntity.internalTank.getFluidInTank(0);

            var hasSpaceForBucket = internalFluid.getAmount() == 0;

            // Check waterlogged
            var blockStateAbove = level.getBlockState(blockPos.above());
            if (hasSpaceForBucket && blockStateAbove.hasProperty(BlockStateProperties.WATERLOGGED) && blockStateAbove.getValue(BlockStateProperties.WATERLOGGED)) {
                level.setBlockAndUpdate(blockPos.above(), blockStateAbove.setValue(BlockStateProperties.WATERLOGGED, false));
                blockEntity.internalTank.fill(new FluidStack(Fluids.WATER, 1000), IFluidHandler.FluidAction.EXECUTE);
                hasInserted = true;
            }

            // Check for fluid source block
            if (!hasInserted) {
                var fluidStateAbove = level.getFluidState(blockPos.above());
                if (
                        hasSpaceForBucket &&
                                !fluidStateAbove.is(Fluids.EMPTY) &&
                                fluidStateAbove.isSource() &&
                                (internalFluid.isEmpty() || fluidStateAbove.isSourceOfType(internalFluid.getFluid()))
                ) {
                    level.setBlockAndUpdate(blockPos.above(), Blocks.AIR.defaultBlockState());
                    blockEntity.internalTank.fill(new FluidStack(fluidStateAbove.getType(), 1000), IFluidHandler.FluidAction.EXECUTE);
                    hasInserted = true;
                }
            }

            // Check for fluid handler
            if (!hasInserted) {
                var handler = FluidUtil.getFluidHandler(level, blockPos.above(), Direction.DOWN);
                if (handler.isPresent()) {
                    handler.ifPresent(iFluidHandler -> {
                        if (internalFluid.isEmpty() || internalFluid.equals(iFluidHandler.getFluidInTank(0))) {
                            FluidUtil.tryFluidTransfer(blockEntity.internalTank, iFluidHandler, 1000, true);
                        }
                    });
                    hasInserted = true;
                }
            }

            if (!hasInserted) {
                if (blockStateAbove.is(BlockTags.CAULDRONS)) {
                    var cauldronFluid = blockStateAbove.is(Blocks.WATER_CAULDRON) ? Fluids.WATER : blockStateAbove.is(Blocks.LAVA_CAULDRON) ? Fluids.LAVA : Fluids.EMPTY;
                    if (!cauldronFluid.isSame(Fluids.EMPTY) && hasSpaceForBucket) {
                        level.setBlockAndUpdate(blockPos.above(), Blocks.CAULDRON.defaultBlockState());
                        blockEntity.internalTank.fill(new FluidStack(cauldronFluid, 1000), IFluidHandler.FluidAction.EXECUTE);
                    }
                }
            }

            // Insert into connected fluid handler
            Direction direction = blockState.getValue(HopperBlock.FACING);
            var facingHandler = FluidUtil.getFluidHandler(level, blockPos.relative(direction), direction.getOpposite());
            facingHandler.ifPresent(iFluidHandler -> {
                FluidUtil.tryFluidTransfer(iFluidHandler, blockEntity.internalTank, Math.min(blockEntity.internalTank.getFluidInTank(0).getAmount(), 1000), true);
            });
        }
    }
}
