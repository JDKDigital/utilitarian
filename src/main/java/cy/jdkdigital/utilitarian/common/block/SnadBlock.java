package cy.jdkdigital.utilitarian.common.block;

import cy.jdkdigital.utilitarian.Config;
import cy.jdkdigital.utilitarian.Utilitarian;
import cy.jdkdigital.utilitarian.module.SnadModule;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ColorRGBA;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.common.CommonHooks;

import java.util.Comparator;

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
            var headBlock = plantBlock.getBlock() instanceof GrowingPlantBlock growBlock ? growBlock.getHeadBlock() : plantBlock.getBlock();
            var bodyBlock = plantBlock.getBlock() instanceof GrowingPlantBlock growBlock ? growBlock.getBodyBlock() : plantBlock.getBlock();
            int i;
            for (i = 2; pLevel.getBlockState(pPos.above(i)).is(headBlock) || pLevel.getBlockState(pPos.above(i)).is(plantBlock.getBlock()); ++i) {}

            var state = pLevel.getBlockState(pPos.above(i - 1));
            var ageProperty = state.getBlock() instanceof GrowingPlantHeadBlock ? BlockStateProperties.AGE_25 : BlockStateProperties.AGE_15;
            int maxAge = ageProperty.getAllValues().max(Comparator.comparingInt(Property.Value::value)).get().value();
            if (
                    state.hasProperty(ageProperty) &&
                    i < 4 + Config.SNAD_ADDITIONAL_HEIGHT.get() &&
                    (pPos.above(i).getY()) < pLevel.getMaxBuildHeight() &&
                    pLevel.getBlockState(pPos.above(i)).canBeReplaced()
            ) {
                // additional growth to configured height
                if (state.getValue(ageProperty) == maxAge) {
                    if (CommonHooks.canCropGrow(pLevel, pPos, pState, true)) {
                        // Set the new plant block
                        pLevel.setBlockAndUpdate(pPos.above(i), headBlock.defaultBlockState());
                        // Set the fully grown block state
                        var grownState = bodyBlock.defaultBlockState();
                        pLevel.setBlock(pPos.above(i - 1), grownState.hasProperty(ageProperty) ? grownState.setValue(ageProperty, 0) : grownState, Block.UPDATE_CLIENTS);
                        CommonHooks.fireCropGrowPost(pLevel, pPos.above(), plantBlock.getBlock().defaultBlockState());
                    }
                } else {
                    // Set growth
                    pLevel.setBlock(pPos.above(i - 1), state.setValue(ageProperty, Math.min(maxAge, state.getValue(ageProperty) + Config.SNAD_GROWTH_MULTIPLIER.get())), Block.UPDATE_INVISIBLE);
                }
            } else {
                // When below default max height, just tick
                for (int u = 0; u < Config.SNAD_GROWTH_MULTIPLIER.get(); u++) {
                    state.randomTick(pLevel, pPos.above(i-1), pRandom);
                }
            }
        }
    }
}
