package cy.jdkdigital.utilitarian.common.block;

import cy.jdkdigital.utilitarian.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SoulSandBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.IPlantable;
import net.neoforged.neoforge.common.PlantType;

public class SoulSnadBlock extends SoulSandBlock
{
    public SoulSnadBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean canSustainPlant(BlockState state, BlockGetter level, BlockPos pos, net.minecraft.core.Direction facing, IPlantable plantable) {
        return plantable.getPlantType(level, pos).equals(PlantType.NETHER);
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        super.tick(pState, pLevel, pPos, pRandom);

        var aboveBlock = pLevel.getBlockState(pPos.above()).getBlock();

        if (aboveBlock instanceof IPlantable) {
            // Find the first block above that's not the plant
            int i;
            for (i = 2; pLevel.getBlockState(pPos.above(i)).is(aboveBlock); ++i) {
            }

            var state = pLevel.getBlockState(pPos.above(i - 1));

            for (int u = 0; u < Config.SNAD_GROWTH_MULTIPLIER.get(); u++) {
                state.randomTick(pLevel, pPos.above(i), pRandom);
            }
        }
    }
}
