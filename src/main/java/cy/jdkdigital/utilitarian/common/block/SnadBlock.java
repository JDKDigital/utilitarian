package cy.jdkdigital.utilitarian.common.block;

import cy.jdkdigital.utilitarian.Config;
import cy.jdkdigital.utilitarian.module.SnadModule;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ColorRGBA;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ColoredFallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.common.CommonHooks;

public class SnadBlock extends ColoredFallingBlock
{
    public SnadBlock(ColorRGBA pDustColor, Properties pProperties) {
        super(pDustColor, pProperties);
    }

    @Override
    protected void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        super.randomTick(pState, pLevel, pPos, pRandom);
        tick(pState, pLevel, pPos, pRandom);
    }

    @Override
    protected void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        super.tick(pState, pLevel, pPos, pRandom);

        var plantBlock = pLevel.getBlockState(pPos.above());

        if (plantBlock.is(SnadModule.SAND_GROWABLES)) {
            // Find the first block above that's not the plant
            int i;
            for (i = 2; pLevel.getBlockState(pPos.above(i)).is(plantBlock.getBlock()); ++i) {
            }

            var state = pLevel.getBlockState(pPos.above(i - 1));

            if (
                    state.hasProperty(BlockStateProperties.AGE_15) &&
                    i < 4 + Config.SNAD_ADDITIONAL_HEIGHT.get() &&
                    (pPos.above(i).getY()) < pLevel.getMaxBuildHeight() &&
                    pLevel.getBlockState(pPos.above(i)).canBeReplaced()
            ) {
                // additional growth to configured height
                if (state.getValue(BlockStateProperties.AGE_15) == 15) {
                    if (CommonHooks.canCropGrow(pLevel, pPos, pState, true)) {
                        pLevel.setBlockAndUpdate(pPos.above(i), plantBlock.getBlock().defaultBlockState());
                        pLevel.setBlock(pPos.above(i - 1), state.setValue(BlockStateProperties.AGE_15, 0), Block.UPDATE_INVISIBLE);
                        CommonHooks.fireCropGrowPost(pLevel, pPos.above(), plantBlock.getBlock().defaultBlockState());
                    }
                } else {
                    // Set growth
                    pLevel.setBlock(pPos.above(i - 1), state.setValue(BlockStateProperties.AGE_15, Math.min(15, state.getValue(BlockStateProperties.AGE_15) + Config.SNAD_GROWTH_MULTIPLIER.get())), Block.UPDATE_INVISIBLE);
                }
            } else {
                // When below default max height, just tick
                for (int u = 0; u < Config.SNAD_GROWTH_MULTIPLIER.get(); u++) {
                    state.randomTick(pLevel, pPos.above(i), pRandom);
                }
            }
        }
    }
}
