package cy.jdkdigital.utilitarian.common.item;

import cy.jdkdigital.utilitarian.module.UtilityItemModule;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.WorldgenRandom;

import java.util.List;

public class SlimeBucketItem extends Item
{
    public SlimeBucketItem(Properties properties) {
        super(properties);
    }

    public static boolean isInSlime(ItemStack stack) {
        return stack.has(UtilityItemModule.SLIME_STATE) && (Boolean) stack.get(UtilityItemModule.SLIME_STATE);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide && level.getGameTime()%21 == 0) {
            ChunkPos chunkpos = new ChunkPos(entity.blockPosition());
            boolean isSlimeChunk = WorldgenRandom.seedSlimeChunk(chunkpos.x, chunkpos.z, ((WorldGenLevel)level).getSeed(), 987234911L).nextInt(10) == 0;
            stack.set(UtilityItemModule.SLIME_STATE, isSlimeChunk);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("item.utilitarian.slime_bucket_description").withStyle(ChatFormatting.GOLD));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        var entity = EntityType.SLIME.create(context.getLevel());
        if (entity instanceof Slime slime && context.getPlayer() != null) {
            if (!context.getLevel().isClientSide) {
                slime.setSize(1, true);
                slime.setPos(context.getClickLocation());
                if (context.getLevel().addFreshEntity(slime)) {
                    context.getItemInHand().shrink(1);
                    context.getPlayer().addItem(Items.BUCKET.getDefaultInstance());
                }
            }
            return InteractionResult.sidedSuccess(context.getLevel().isClientSide);
        }
        return super.useOn(context);
    }
}
