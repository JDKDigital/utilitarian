package cy.jdkdigital.utilitarian.common.item;

import cy.jdkdigital.utilitarian.module.NoSolicitingModule;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class RestrainingOrder extends Item
{
    public RestrainingOrder(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pPlayer.isShiftKeyDown()) {
            toggleActive(pPlayer.getItemInHand(pUsedHand));
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
        if (isActive(pStack)) {
            pTooltipComponents.add(Component.translatable("utilitarian.restraining_order.status.active").withStyle(ChatFormatting.GOLD));
        } else {
            pTooltipComponents.add(Component.translatable("utilitarian.restraining_order.status.inactive").withStyle(ChatFormatting.LIGHT_PURPLE));
        }
    }

    private void toggleActive(ItemStack stack) {
        var active = stack.get(NoSolicitingModule.ACTIVE);
        if (active != null) {
            stack.set(NoSolicitingModule.ACTIVE, !active);
        } else {
            stack.set(NoSolicitingModule.ACTIVE, true);
        }
    }

    public static boolean isActive(ItemStack stack) {
        var active = stack.get(NoSolicitingModule.ACTIVE);
        return active != null && active;
    }

    public static boolean isEnabledRestrainingOrder(ItemStack itemStack) {
        return itemStack.is(NoSolicitingModule.RESTRAINING_ORDER.get()) && isActive(itemStack);
    }
}
