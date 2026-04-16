package cy.jdkdigital.utilitarian.common.item;

import cy.jdkdigital.utilitarian.Utilitarian;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class UnnameTagItem extends Item
{
    public UnnameTagItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (!(target instanceof Player)) {
            if (!player.level().isClientSide() && target.getType().canSerialize()) {
                target.setCustomName(null);
                if (target instanceof Mob mob) {
                    mob.setPersistenceRequired();
                }

                stack.shrink(1);
            }

            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }
}
