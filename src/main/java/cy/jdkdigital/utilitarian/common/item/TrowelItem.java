package cy.jdkdigital.utilitarian.common.item;

import cy.jdkdigital.utilitarian.module.UtilityItemModule;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.phys.BlockHitResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrowelItem extends Item
{
    public TrowelItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        var level  = pContext.getLevel();
        var pos = level.getBlockState(pContext.getClickedPos()).canBeReplaced() ? pContext.getClickedPos() : pContext.getClickedPos().relative(pContext.getClickedFace());
        if (pContext.getPlayer() != null) {
            boolean extended = isExtended(pContext.getPlayer().getItemInHand(pContext.getHand()));
            Map<ItemStack, Integer> placeables = new HashMap<>();
            int total = 0;
            for (int i = 0; i < (extended ? pContext.getPlayer().getInventory().getContainerSize() : 9); i++) {
                var stack = pContext.getPlayer().getInventory().getItem(i);
                if (!stack.is(UtilityItemModule.TROWEL_BLACKLIST) && stack.getItem() instanceof BlockItem) {
                    total = total + stack.getCount();
                    placeables.put(stack, stack.getCount());
                }
            }
            if (!placeables.isEmpty()) {
                if (!level.isClientSide()) {
                    ItemStack randomItem = ItemStack.EMPTY;
                    var randomWeight = level.getRandom().nextInt(total);
                    for (Map.Entry<ItemStack, Integer> entry : placeables.entrySet()) {
                        randomWeight -= entry.getValue();
                        if (randomWeight <= 0) {
                            randomItem = entry.getKey();
                            break;
                        }
                    }
                    if (!randomItem.isEmpty() && randomItem.getItem() instanceof BlockItem blockItem) {
                        var result = blockItem.place(new BlockPlaceContext(level, pContext.getPlayer(), pContext.getHand(), randomItem, new BlockHitResult(pContext.getClickLocation(), pContext.getPlayer().getDirection(), pos, false)));
                        if (result.consumesAction()) {
                            var blockState = blockItem.getBlock().defaultBlockState();
                            SoundType soundtype = blockState.getSoundType(level, pos, pContext.getPlayer());
                            level.playSound(
                                    null,
                                    pos,
                                    blockState.getSoundType(level, pos, pContext.getPlayer()).getPlaceSound(),
                                    SoundSource.BLOCKS,
                                    (soundtype.getVolume() + 1.0F) / 2.0F,
                                    soundtype.getPitch() * 0.8F
                            );
                        }
                    }
                }
                return InteractionResult.SUCCESS_NO_ITEM_USED;
            }
        }
        return super.useOn(pContext);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pPlayer.isShiftKeyDown()) {
            toggleState(pPlayer.getItemInHand(pUsedHand));
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        boolean extended = isExtended(pStack);
        pTooltipComponents.add(Component.translatable("utilitarian.trowel.state", Component.translatable("utilitarian.trowel.state." + (extended ? "extended" : "normal")).getString()).withStyle(ChatFormatting.LIGHT_PURPLE));
        pTooltipComponents.add(Component.translatable("utilitarian.trowel.tooltip").withStyle(ChatFormatting.GREEN));
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
    }

    private void toggleState(ItemStack pStack) {
        boolean extended = isExtended(pStack);
        if (!extended) {
            pStack.set(UtilityItemModule.TROWEL_STATE, true);
        } else {
            pStack.set(UtilityItemModule.TROWEL_STATE, false);
        }
    }

    public static boolean isExtended(ItemStack pStack) {
        return (boolean) pStack.getOrDefault(UtilityItemModule.TROWEL_STATE, false);
    }
}
