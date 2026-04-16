package cy.jdkdigital.utilitarian.common.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.neoforged.neoforge.common.Tags;

public class YankingRopeItem extends Item
{
    public YankingRopeItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {
        // "capture" entity and create an entity balloon
        if (!pInteractionTarget.getType().builtInRegistryHolder().is(Tags.EntityTypes.CAPTURING_NOT_SUPPORTED)) { // TODO use own tag
            pInteractionTarget.setNoGravity(true);
            // set DataCompo
            if (pInteractionTarget instanceof Leashable leashable) {
                leashable.setLeashedTo(pPlayer, true);
            }
            var pos = pInteractionTarget.position();
            pInteractionTarget.setPos(pos.add(0, 3, 0));
        }
        return super.interactLivingEntity(pStack, pPlayer, pInteractionTarget, pUsedHand);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        // release mob on clicked fence
        return super.useOn(pContext);
    }
}
