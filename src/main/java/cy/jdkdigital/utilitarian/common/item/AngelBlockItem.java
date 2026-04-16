package cy.jdkdigital.utilitarian.common.item;

import cy.jdkdigital.utilitarian.module.UtilityBlockModule;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.util.BlockSnapshot;
import net.neoforged.neoforge.event.EventHooks;

public class AngelBlockItem extends BlockItem
{
    public AngelBlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    public InteractionResult use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        var pos = pPlayer.blockPosition().relative(pPlayer.getDirection(), 2).above();
        if (pPlayer.getXRot() < -70) {
            pos = pPlayer.blockPosition().above(3);
        } else if (pPlayer.getXRot() > 70) {
            pos = pPlayer.blockPosition().below(1);
        }
        if (pLevel.getBlockState(pos).isAir() && !pLevel.isOutsideBuildHeight(pos) && !EventHooks.onBlockPlace(pPlayer, BlockSnapshot.create(pLevel.dimension(), pLevel, pos), pPlayer.getDirection())) {
            if (!pLevel.isClientSide()) {
                pLevel.setBlockAndUpdate(pos, UtilityBlockModule.ANGEL_BLOCK.get().defaultBlockState());
                if (!pPlayer.isCreative()) {
                    pPlayer.getItemInHand(pUsedHand).shrink(1);
                }
            }
            return InteractionResult.SUCCESS;
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
