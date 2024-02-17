package cy.jdkdigital.utilitarian.common.block.entity;

import cy.jdkdigital.utilitarian.module.TPSBreakerModule;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class TPSMeterBlockEntity extends BlockEntity
{
    private int tickCounter = 80;

    public TPSMeterBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(TPSBreakerModule.TPS_METER_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    public static <E extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState blockState, TPSMeterBlockEntity blockEntity) {
        if (++blockEntity.tickCounter >= 100 && level instanceof ServerLevel serverLevel) {
            blockEntity.tickCounter = 0;
            int power = Mth.lerpInt((float) (TPSBreakerModule.getTPS(serverLevel)/20.0), 0, 15);
            if (blockState.getValue(BlockStateProperties.POWER) != power) {
                level.setBlockAndUpdate(blockPos, blockState.setValue(BlockStateProperties.POWER, power));
            }
        }
    }
}
