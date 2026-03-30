package cy.jdkdigital.utilitarian.common.block;

import cy.jdkdigital.utilitarian.Config;
import cy.jdkdigital.utilitarian.module.SnadModule;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;

import java.util.List;

public class CursedGrrassBlock extends Block
{
    public CursedGrrassBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void stepOn(Level pLevel, BlockPos pPos, BlockState pState, Entity pEntity) {
        super.stepOn(pLevel, pPos, pState, pEntity);
        if (pEntity instanceof Player || pEntity instanceof Animal) {
            Holder.Reference<DamageType> damageType = pEntity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(SnadModule.DRIT_DAMAGE);
            pEntity.hurt(new DamageSource(damageType), Config.SNAD_DRIT_DAMAGE.get().floatValue());
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("utilitarian.cursed_grrass.description").withStyle(ChatFormatting.DARK_RED).withStyle(ChatFormatting.ITALIC));
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);
        if (!level.isAreaLoaded(pos, 3)) return;
        if (level instanceof ServerLevel serverLevel) {
            // set on fire and convert to grrass
            boolean isLight = (serverLevel.hasChunkAt(pos.getX(), pos.getZ()) ? serverLevel.getLightLevelDependentMagicValue(pos.above()) : 0.0F)  > 0.5f;

            if (isLight && serverLevel.isDay() && !serverLevel.isRaining() && serverLevel.canSeeSky(pos.above())) {
                if (serverLevel.getBlockState(pos.above()).canBeReplaced()) {
                    serverLevel.setBlockAndUpdate(pos, SnadModule.GRRASS_BLOCK.get().defaultBlockState());
                    serverLevel.setBlockAndUpdate(pos.above(), Blocks.FIRE.defaultBlockState());
                }
            }
            // spawn mob
            if (!isLight) {
                MobCategory category = level.getFluidState(pos.above()).is(Tags.Fluids.WATER) ? MobCategory.WATER_CREATURE : MobCategory.MONSTER;
                List<MobSpawnSettings.SpawnerData> spawnOptions = serverLevel.getChunkSource().getGenerator().getMobsAt(serverLevel.getBiome(pos.above()), serverLevel.structureManager(), category, pos)
                        .unwrap().stream().filter(spawners -> !spawners.type.is(SnadModule.CURSED_GRRASS_BLACKLIST)).toList();
                if (!spawnOptions.isEmpty()) {
                    var spawnData = spawnOptions.get(serverLevel.getRandom().nextInt(spawnOptions.size()));
                    if (SpawnPlacements.checkSpawnRules(spawnData.type, serverLevel, MobSpawnType.NATURAL, pos.above(), serverLevel.getRandom())) {
                        Entity entity = spawnData.type.create(serverLevel);
                        if (entity != null) {
                            entity.moveTo(pos.getX() + 0.5f, pos.getY() + 1, pos.getZ() + 0.5f);
                            if (!serverLevel.noCollision(entity) || !serverLevel.isUnobstructed(entity)) return;
                            serverLevel.addFreshEntity(entity);
                        }
                    }
                }
            }
        }
    }
}
