package cy.jdkdigital.utilitarian.common.block.entity;

import cy.jdkdigital.utilitarian.Config;
import cy.jdkdigital.utilitarian.module.UtilityBlockModule;
import mezz.jei.core.util.function.LazySupplier;
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
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FluidHopperBlockEntity extends BlockEntity
{
    int tick = 0;
    public LazyOptional<IFluidHandler> internalTank = LazyOptional.of(() -> new FluidTank(1000, p -> true));

    public FluidHopperBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(UtilityBlockModule.FLUID_HOPPER_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap.equals(ForgeCapabilities.FLUID_HANDLER)) {
            return internalTank.cast();
        }
        return super.getCapability(cap, side);
    }

    public static <E extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState blockState, FluidHopperBlockEntity blockEntity) {
        if (blockState.getValue(BlockStateProperties.ENABLED) && ++blockEntity.tick >= Config.SERVER.FLUID_HOPPER_TICK_RATE.get()) {
            blockEntity.internalTank.ifPresent(internalTank -> {
                blockEntity.tick = 0;
                var hasInserted = false;
                var internalFluid = internalTank.getFluidInTank(0);

                var hasSpaceForBucket = internalFluid.getAmount() == 0;

                // Check waterlogged
                var blockStateAbove = level.getBlockState(blockPos.above());
                if (hasSpaceForBucket && blockStateAbove.hasProperty(BlockStateProperties.WATERLOGGED) && blockStateAbove.getValue(BlockStateProperties.WATERLOGGED)) {
                    level.setBlockAndUpdate(blockPos.above(), blockStateAbove.setValue(BlockStateProperties.WATERLOGGED, false));
                    internalTank.fill(new FluidStack(Fluids.WATER, 1000), IFluidHandler.FluidAction.EXECUTE);
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
                        internalTank.fill(new FluidStack(fluidStateAbove.getType(), 1000), IFluidHandler.FluidAction.EXECUTE);
                        hasInserted = true;
                    }
                }

                // Check for fluid handler
                if (!hasInserted) {
                    var handler = FluidUtil.getFluidHandler(level, blockPos.above(), Direction.DOWN);
                    if (handler.isPresent()) {
                        handler.ifPresent(iFluidHandler -> {
                            if (internalFluid.isEmpty() || internalFluid.equals(iFluidHandler.getFluidInTank(0))) {
                                FluidUtil.tryFluidTransfer(internalTank, iFluidHandler, 1000, true);
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
                            internalTank.fill(new FluidStack(cauldronFluid, 1000), IFluidHandler.FluidAction.EXECUTE);
                        }
                    }
                }

                // Insert into connected fluid handler
                Direction direction = blockState.getValue(HopperBlock.FACING);
                var facingHandler = FluidUtil.getFluidHandler(level, blockPos.relative(direction), direction.getOpposite());
                facingHandler.ifPresent(iFluidHandler -> {
                    FluidUtil.tryFluidTransfer(iFluidHandler, internalTank, Math.min(internalTank.getFluidInTank(0).getAmount(), 1000), true);
                });
            });
        }
    }
}
