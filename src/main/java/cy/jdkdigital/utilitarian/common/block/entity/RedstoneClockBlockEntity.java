package cy.jdkdigital.utilitarian.common.block.entity;

import cy.jdkdigital.utilitarian.Config;
import cy.jdkdigital.utilitarian.module.UtilityBlockModule;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class RedstoneClockBlockEntity extends BlockEntity
{
    private int tickCounter = 0;
    private int poweredCounter = 0;
    public int rate = 10;

    public RedstoneClockBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(UtilityBlockModule.REDSTONE_CLOCK_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    public static <E extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState blockState, RedstoneClockBlockEntity blockEntity) {
        if (level instanceof ServerLevel && blockState.getValue(BlockStateProperties.ENABLED)) {
            if (blockEntity.rate < Config.REDSTONE_CLOCK_MIN_FREQUENCY.get()) {
                blockEntity.rate = Config.REDSTONE_CLOCK_MIN_FREQUENCY.get();
            }
            if (!blockState.getValue(BlockStateProperties.POWERED) && ++blockEntity.tickCounter >= blockEntity.rate) {
                blockEntity.tickCounter = blockEntity.poweredCounter = 0;
                level.setBlockAndUpdate(blockPos, blockState.setValue(BlockStateProperties.POWERED, true));
                for(Direction direction : Direction.values()) {
                    level.updateNeighborsAt(blockPos.relative(direction), blockState.getBlock());
                }
            } else if (blockState.getValue(BlockStateProperties.POWERED) && ++blockEntity.poweredCounter >= blockEntity.rate) {
                level.setBlockAndUpdate(blockPos, blockState.setValue(BlockStateProperties.POWERED, false));
                for(Direction direction : Direction.values()) {
                    level.updateNeighborsAt(blockPos.relative(direction), blockState.getBlock());
                }
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        pTag.putInt("rate", this.rate);
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        if (pTag.contains("rate")) {
            this.rate = pTag.getInt("rate");
        } else {
            this.rate = Config.REDSTONE_CLOCK_MIN_FREQUENCY.get();
        }
    }
}
