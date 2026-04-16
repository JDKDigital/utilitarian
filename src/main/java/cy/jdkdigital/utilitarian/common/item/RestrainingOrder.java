package cy.jdkdigital.utilitarian.common.item;

import cy.jdkdigital.utilitarian.module.NoSolicitingModule;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import java.util.function.Consumer;
import net.minecraft.world.level.Level;


public class RestrainingOrder extends Item
{
    public RestrainingOrder(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pPlayer.isShiftKeyDown()) {
            toggleActive(pPlayer.getItemInHand(pUsedHand));
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, TooltipDisplay pTooltipDisplay, Consumer<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTooltipDisplay, pTooltipComponents, pTooltipFlag);
        if (isActive(pStack)) {
            pTooltipComponents.accept(Component.translatable("utilitarian.restraining_order.status.active").withStyle(ChatFormatting.GOLD));
        } else {
            pTooltipComponents.accept(Component.translatable("utilitarian.restraining_order.status.inactive").withStyle(ChatFormatting.LIGHT_PURPLE));
        }
    }

    private void toggleActive(ItemStack stack) {
        if (stack.has(NoSolicitingModule.ACTIVE)) {
            stack.remove(NoSolicitingModule.ACTIVE);
        } else {
            stack.set(NoSolicitingModule.ACTIVE, Unit.INSTANCE);
        }
    }

    public static boolean isActive(ItemStack stack) {
        return stack.has(NoSolicitingModule.ACTIVE);
    }

    public static boolean isEnabledRestrainingOrder(ItemStack itemStack) {
        return itemStack.is(NoSolicitingModule.RESTRAINING_ORDER.get()) && isActive(itemStack);
    }
}
