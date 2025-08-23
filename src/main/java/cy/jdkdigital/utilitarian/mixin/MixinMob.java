package cy.jdkdigital.utilitarian.mixin;

import cy.jdkdigital.utilitarian.Config;
import cy.jdkdigital.utilitarian.Utilitarian;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Debug(export = true)
@Mixin(value = Mob.class)
public abstract class MixinMob extends Entity
{
    @Shadow private boolean persistenceRequired;
    @Unique
    private boolean utilitarian$existingPersistenceRequired = false;

    public MixinMob(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(at = {@At("HEAD")}, method = {"setItemSlotAndDropWhenKilled"})
    public void setItemSlotAndDropWhenKilledHead(EquipmentSlot slot, ItemStack stack, CallbackInfo ci) {
        if (Config.DESPAWN_WHEN_HOLDING_ITEMS_ENABLED.get()) {
            utilitarian$existingPersistenceRequired = this.persistenceRequired;
        }
    }
    @Inject(at = {@At("TAIL")}, method = {"setItemSlotAndDropWhenKilled"})
    public void setItemSlotAndDropWhenKilledTail(EquipmentSlot slot, ItemStack stack, CallbackInfo ci) {
        if (Config.DESPAWN_WHEN_HOLDING_ITEMS_ENABLED.get()) {
            // entities in the tag will have default despawn prevention
            this.persistenceRequired = utilitarian$existingPersistenceRequired || this.getType().is(Utilitarian.ALWAYS_PERSIST_WITH_EQUIPMENT);
        }
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;discard()V"), method = {"checkDespawn()V"})
    private void dropOnDespawn(Mob instance) {
        Mob mob = (Mob) (Object) this;
        if (Config.DESPAWN_WHEN_HOLDING_ITEMS_ENABLED.get()) {
            for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
                ItemStack itemStack = mob.getItemBySlot(equipmentSlot);
                if (!itemStack.isEmpty() && !itemStack.is(Utilitarian.EQUIPMENT_DESPAWN_BLACKLIST) && !EnchantmentHelper.has(itemStack, EnchantmentEffectComponents.PREVENT_EQUIPMENT_DROP)) {
                    this.spawnAtLocation(itemStack);
                    mob.setItemSlot(equipmentSlot, ItemStack.EMPTY);
                }
            }
        }

        this.discard();
    }
}
