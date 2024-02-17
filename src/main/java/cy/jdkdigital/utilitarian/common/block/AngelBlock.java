package cy.jdkdigital.utilitarian.common.block;

import cy.jdkdigital.utilitarian.module.AngelBlockModule;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.ItemHandlerHelper;
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
            pPlayer.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(AngelBlockModule.ANGEL_BLOCK_ITEM.get()));
        } else {
            pPlayer.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {
                var leftOver = ItemHandlerHelper.insertItemStacked(iItemHandler, new ItemStack(AngelBlockModule.ANGEL_BLOCK_ITEM.get()), false);
                if (!leftOver.isEmpty()) {
                    popResource(pLevel, pPos, leftOver);
                }
            });
        }
    }
}
