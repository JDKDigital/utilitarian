package cy.jdkdigital.utilitarian.common.item;

import cy.jdkdigital.utilitarian.Utilitarian;
import cy.jdkdigital.utilitarian.module.AngelBlockModule;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class AngelBlockItem extends BlockItem
{
    public AngelBlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide) {
            var pos = pPlayer.blockPosition().relative(pPlayer.getDirection(), 2).above();
            if (pPlayer.getXRot() < -70) {
                pos = pPlayer.blockPosition().above(3);
            } else if (pPlayer.getXRot() > 70) {
                pos = pPlayer.blockPosition().below(1);
            }
            if (pLevel.getBlockState(pos).isAir()) {
                pLevel.setBlockAndUpdate(pos, AngelBlockModule.ANGEL_BLOCK.get().defaultBlockState());
                if (!pPlayer.isCreative()) {
                    pPlayer.getItemInHand(pUsedHand).shrink(1);
                }
                return InteractionResultHolder.consume(pPlayer.getItemInHand(pUsedHand));
            }
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
