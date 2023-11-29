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
import org.jetbrains.annotations.Nullable;

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
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(stack, level, pTooltipComponents, pIsAdvanced);
        if (isActive(stack)) {
            pTooltipComponents.add(Component.translatable("utilitarian.restraining_order.status.active").withStyle(ChatFormatting.GOLD));
        } else {
            pTooltipComponents.add(Component.translatable("utilitarian.restraining_order.status.inactive").withStyle(ChatFormatting.LIGHT_PURPLE));
        }
    }

    private void toggleActive(ItemStack stack) {
        var tag = stack.getOrCreateTag();
        if (tag.contains("active")) {
            tag.putBoolean("active", !tag.getBoolean("active"));
        } else {
            tag.putBoolean("active", true);
        }
    }

    public static boolean isActive(ItemStack stack) {
        var tag = stack.getTag();
        if (tag != null && tag.contains("active")) {
            return tag.getBoolean("active");
        }
        return true;
    }

    public static boolean isEnabledRestrainingOrder(ItemStack itemStack) {
        return itemStack.is(NoSolicitingModule.RESTRAINING_ORDER.get()) && isActive(itemStack);
    }
}
