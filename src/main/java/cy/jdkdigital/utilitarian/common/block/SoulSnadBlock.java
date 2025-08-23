package cy.jdkdigital.utilitarian.common.block;

import cy.jdkdigital.utilitarian.Config;
import cy.jdkdigital.utilitarian.module.SnadModule;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.SoulSandBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.TriState;

public class SoulSnadBlock extends SoulSandBlock
{
    public SoulSnadBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public TriState canSustainPlant(BlockState state, BlockGetter level, BlockPos soilPosition, net.minecraft.core.Direction facing, BlockState plant) {
        return plant.is(SnadModule.SOUL_SAND_GROWABLES) ? TriState.TRUE : TriState.DEFAULT;
    }

    @Override
    protected void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        super.randomTick(pState, pLevel, pPos, pRandom);
        tick(pState, pLevel, pPos, pRandom);
    }

    @Override
    protected void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        super.tick(pState, pLevel, pPos, pRandom);

        var aboveBlock = pLevel.getBlockState(pPos.above());
        if (aboveBlock.is(SnadModule.SOUL_SAND_GROWABLES)) {
            for (int u = 0; u < Config.SNAD_GROWTH_MULTIPLIER.get(); u++) {
                aboveBlock.randomTick(pLevel, pPos.above(), pRandom);
            }
        }
    }

    @Override
    protected BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        pLevel.scheduleTick(pCurrentPos, this, 2);
        return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }
}
