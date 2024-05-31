package cy.jdkdigital.utilitarian.common.block;

import cy.jdkdigital.utilitarian.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SandBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

public class SnadBlock extends SandBlock
{
    public SnadBlock(int pDustColor, Properties pProperties) {
        super(pDustColor, pProperties);
    }

    @Override
    public boolean canSustainPlant(BlockState state, BlockGetter level, BlockPos pos, Direction facing, IPlantable plantable) {
        if (plantable.getPlantType(level, pos).equals(PlantType.DESERT)) {
            return true;
        } else if (plantable.getPlantType(level, pos).equals(PlantType.BEACH)) {
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                boolean isWater = level.getFluidState(pos.relative(direction)).is(FluidTags.WATER);
                boolean isFrostedIce = level.getBlockState(pos.relative(direction)).is(Blocks.FROSTED_ICE);
                if (!isWater && !isFrostedIce) {
                    continue;
                }
                return true;
            }
        }
        return super.canSustainPlant(state, level, pos, facing, plantable);
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        super.tick(pState, pLevel, pPos, pRandom);

        var plantBlock = pLevel.getBlockState(pPos.above()).getBlock();

        if (plantBlock instanceof IPlantable) {
            // Find the first block above that's not the plant
            int i;
            for(i = 2; pLevel.getBlockState(pPos.above(i)).is(plantBlock); ++i) {}

            var state = pLevel.getBlockState(pPos.above(i - 1));

            if (
                state.hasProperty(BlockStateProperties.AGE_15) &&
                i < 4 + Config.SERVER.SNAD_ADDITIONAL_HEIGHT.get() &&
                (pPos.above(i).getY()) < pLevel.getMaxBuildHeight() &&
                pLevel.getBlockState(pPos.above(i)).canBeReplaced()
            ) {
                // additional growth to configured height
                if (state.getValue(BlockStateProperties.AGE_15) == 15) {
                    if (ForgeHooks.onCropsGrowPre(pLevel, pPos, pState, true)) {
                        pLevel.setBlockAndUpdate(pPos.above(i), plantBlock.defaultBlockState());
                        pLevel.setBlock(pPos.above(i - 1), state.setValue(BlockStateProperties.AGE_15, 0), Block.UPDATE_INVISIBLE);
                        ForgeHooks.onCropsGrowPost(pLevel, pPos.above(), plantBlock.defaultBlockState());
                    }
                } else {
                    // Set growth
                    pLevel.setBlock(pPos.above(i - 1), state.setValue(BlockStateProperties.AGE_15, Math.min(15, state.getValue(BlockStateProperties.AGE_15) + Config.SERVER.SNAD_GROWTH_MULTIPLIER.get())), Block.UPDATE_INVISIBLE);
                }
            } else {
                // When below default max height, just tick
                for (int u = 0; u < Config.SERVER.SNAD_GROWTH_MULTIPLIER.get(); u++) {
                    state.getBlock().randomTick(state, pLevel, pPos.above(i), pRandom);
                }
            }
        }
    }
}
