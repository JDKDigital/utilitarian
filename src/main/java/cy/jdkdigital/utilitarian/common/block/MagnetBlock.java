package cy.jdkdigital.utilitarian.common.block;

import cy.jdkdigital.utilitarian.Utilitarian;
import cy.jdkdigital.utilitarian.common.entity.RisingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;

public class MagnetBlock extends Block
{
    public MagnetBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(BlockStateProperties.ENABLED, true));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(BlockStateProperties.ENABLED);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        this.checkPoweredState(level, pos, state, Block.UPDATE_CLIENTS);
    }

    private void checkPoweredState(Level level, BlockPos pos, BlockState state, int flags) {
        boolean noSignal = !level.hasNeighborSignal(pos);
        if (noSignal != state.getValue(BlockStateProperties.ENABLED)) {
            // Switch state
            level.setBlock(pos, state.setValue(BlockStateProperties.ENABLED, noSignal), flags);
            if (noSignal) {
                // release any RisingBlockEntity underneath
                level.getEntitiesOfClass(RisingBlockEntity.class, (new AABB(pos)).setMinY(pos.getY()-10)).forEach(risingBlockEntity -> {
                    FallingBlockEntity fallingBlockEntity = FallingBlockEntity.fall(level, risingBlockEntity.blockPosition(), risingBlockEntity.blockState);
                    if (risingBlockEntity.blockState.getBlock() instanceof FallingBlock fallingBlock) {
                        fallingBlock.falling(fallingBlockEntity);
                    }
                    risingBlockEntity.discard();
                });
            } else {
                // pick up any FallingBlockEntity underneath
                level.getEntitiesOfClass(FallingBlockEntity.class, (new AABB(pos)).setMinY(pos.getY()-10)).forEach(fallingBlockEntity -> {
                    RisingBlockEntity.rise(level, fallingBlockEntity.blockPosition(), fallingBlockEntity.blockState);
                    fallingBlockEntity.discard();
                });

                // if no RisingBlockEntity underneath, create one from first block found (from block tag)
                for (int i = 1; i < 10; i++) { // TODO config for magnet range
                    var stateBelow = level.getBlockState(pos.below(i));
                    if (stateBelow.is(Utilitarian.MAGNET_VALID_BLOCKS)) {
                        RisingBlockEntity.rise(level, pos.below(i), stateBelow);
                        break;
                    } else if (!stateBelow.isAir()) {
                        break;
                    }
                }
            }
        }
    }
}
