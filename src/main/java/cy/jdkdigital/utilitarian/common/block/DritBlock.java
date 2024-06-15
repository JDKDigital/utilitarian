package cy.jdkdigital.utilitarian.common.block;

import cy.jdkdigital.utilitarian.Config;
import cy.jdkdigital.utilitarian.module.SnadModule;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class DritBlock extends Block
{
    public DritBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void stepOn(Level pLevel, BlockPos pPos, BlockState pState, Entity pEntity) {
        super.stepOn(pLevel, pPos, pState, pEntity);
        if (pEntity instanceof LivingEntity livingEntity) {
            Holder.Reference<DamageType> damageType = pEntity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(SnadModule.DRIT_DAMAGE);
            livingEntity.hurt(new DamageSource(damageType), Config.SNAD_DRIT_DAMAGE.get().floatValue());
        }
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (!pLevel.isAreaLoaded(pPos, 3)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light and spreading
        if (pLevel.getMaxLocalRawBrightness(pPos.above()) >= 9) {
            BlockState blockstate = SnadModule.GRRASS_BLOCK.get().defaultBlockState();

            for(int i = 0; i < 4; ++i) {
                BlockPos blockpos = pPos.offset(pRandom.nextInt(3) - 1, pRandom.nextInt(5) - 3, pRandom.nextInt(3) - 1);
                if (pLevel.getBlockState(blockpos).is(Blocks.GRASS_BLOCK)) {
                    pLevel.setBlockAndUpdate(pPos, blockstate.setValue(BlockStateProperties.SNOWY, pLevel.getBlockState(blockpos.above()).is(Blocks.SNOW)));
                    break;
                }
            }
        }
    }
}
