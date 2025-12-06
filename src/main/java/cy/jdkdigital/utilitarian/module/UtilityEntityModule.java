package cy.jdkdigital.utilitarian.module;

import cy.jdkdigital.utilitarian.Utilitarian;
import cy.jdkdigital.utilitarian.common.entity.RisingBlockEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;

public class UtilityEntityModule
{
    public static DeferredHolder<EntityType<?>, EntityType<RisingBlockEntity>> RISING_BLOCK;

    public static void register() {
        RISING_BLOCK = Utilitarian.ENTITIES.register(
                "rising_block",
                () -> EntityType.Builder.of(RisingBlockEntity::new, MobCategory.MISC).sized(0.98F, 0.98F).clientTrackingRange(10).updateInterval(20)
                        .build(Utilitarian.MODID + ":rising_block")
        );
    }
}
