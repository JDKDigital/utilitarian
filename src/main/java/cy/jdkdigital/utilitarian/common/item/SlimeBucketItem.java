package cy.jdkdigital.utilitarian.common.item;

import cy.jdkdigital.utilitarian.module.NoSolicitingModule;
import cy.jdkdigital.utilitarian.module.UtilityItemModule;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.WorldgenRandom;

import java.util.function.Consumer;

public class SlimeBucketItem extends Item
{
    public SlimeBucketItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, EquipmentSlot slot) {
        if (level.getGameTime() % 21 == 0) {
            ChunkPos chunkpos = ChunkPos.containing(entity.blockPosition());
            boolean isSlimeChunk = WorldgenRandom.seedSlimeChunk(chunkpos.x(), chunkpos.z(), level.getSeed(), 987234911L).nextInt(10) == 0;
            if (isSlimeChunk) {
                stack.set(NoSolicitingModule.ACTIVE, Unit.INSTANCE);
            } else {
                stack.remove(NoSolicitingModule.ACTIVE);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipDisplay, tooltipComponents, tooltipFlag);
        tooltipComponents.accept(Component.translatable("item.utilitarian.slime_bucket_description").withStyle(ChatFormatting.GOLD));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getPlayer() != null && context.getLevel() instanceof ServerLevel serverLevel) {
            var entity = EntityType.SLIME.create(serverLevel, EntitySpawnReason.TRIGGERED);
            if (entity instanceof Slime slime) {
                slime.setSize(1, true);
                slime.setPos(context.getClickLocation());
                if (serverLevel.addFreshEntity(slime)) {
                    context.getItemInHand().shrink(1);
                    context.getPlayer().addItem(Items.BUCKET.getDefaultInstance());
                }
                return InteractionResult.SUCCESS_SERVER;
            }
        }
        return super.useOn(context);
    }
}
