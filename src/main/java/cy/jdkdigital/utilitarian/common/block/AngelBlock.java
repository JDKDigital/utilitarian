package cy.jdkdigital.utilitarian.common.block;

import cy.jdkdigital.utilitarian.module.UtilityBlockModule;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;

public class AngelBlock extends Block
{
    public AngelBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void playerDestroy(Level pLevel, Player pPlayer, BlockPos pPos, BlockState pState, @Nullable BlockEntity pBlockEntity, ItemStack pTool) {
        super.playerDestroy(pLevel, pPlayer, pPos, pState, pBlockEntity, pTool);
        if (pPlayer.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
            pPlayer.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(UtilityBlockModule.ANGEL_BLOCK_ITEM.get()));
        } else {
            var cap = pPlayer.getCapability(Capabilities.ItemHandler.ENTITY);
            if (cap != null) {
                var leftOver = ItemHandlerHelper.insertItemStacked(cap, new ItemStack(UtilityBlockModule.ANGEL_BLOCK_ITEM.get()), false);
                if (!leftOver.isEmpty()) {
                    popResource(pLevel, pPos, leftOver);
                }
            }
        }
    }
}
