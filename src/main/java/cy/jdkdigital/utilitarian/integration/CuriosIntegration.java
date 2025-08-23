package cy.jdkdigital.utilitarian.integration;

import cy.jdkdigital.utilitarian.common.item.RestrainingOrder;
import cy.jdkdigital.utilitarian.module.NoSolicitingModule;
import net.minecraft.world.entity.player.Player;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.concurrent.atomic.AtomicBoolean;

public class CuriosIntegration
{
    public static boolean hasRestrainingOrder(Player player) {
        AtomicBoolean hasRestrainingOrder = new AtomicBoolean(false);
        CuriosApi.getCuriosInventory(player).ifPresent(curiosInventory -> {
            var restrainingOrder = curiosInventory.findFirstCurio(stack -> {
                return stack.is(NoSolicitingModule.RESTRAINING_ORDER) && RestrainingOrder.isEnabledRestrainingOrder(stack);
            });
            hasRestrainingOrder.set(restrainingOrder.isPresent());
        });
        return hasRestrainingOrder.get();
    }
}
