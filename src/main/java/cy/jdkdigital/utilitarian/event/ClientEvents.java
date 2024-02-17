package cy.jdkdigital.utilitarian.event;

import cy.jdkdigital.utilitarian.Config;
import cy.jdkdigital.utilitarian.Utilitarian;
import cy.jdkdigital.utilitarian.client.render.block.NoSolicitingBannerRenderer;
import cy.jdkdigital.utilitarian.common.item.RestrainingOrder;
import cy.jdkdigital.utilitarian.module.AngelBlockModule;
import cy.jdkdigital.utilitarian.module.NoSolicitingModule;
import cy.jdkdigital.utilitarian.module.TPSBreakerModule;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BannerPatterns;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Utilitarian.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents
{
    @SubscribeEvent
    public static void tabContents(BuildCreativeModeTabContentsEvent event) {
        if (Config.SERVER.NO_SOLICITING_ENABLED.get()) {
            if (event.getTabKey().equals(CreativeModeTabs.FUNCTIONAL_BLOCKS)) {
                ItemStack noSolicitingBanner = new ItemStack(NoSolicitingModule.NO_SOLICITING_BANNER_ITEM.get());
                CompoundTag compoundtag = new CompoundTag();
                compoundtag.put("Patterns", (new BannerPattern.Builder()).addPattern(BannerPatterns.CROSS, DyeColor.RED).toListTag());
                BlockItem.setBlockEntityData(noSolicitingBanner, NoSolicitingModule.NO_SOLICITING_BANNER_BLOCK_ENTITY.get(), compoundtag);
                event.accept(noSolicitingBanner);
            }
            if (event.getTabKey().equals(CreativeModeTabs.TOOLS_AND_UTILITIES)) {
                event.accept(NoSolicitingModule.RESTRAINING_ORDER.get());
            }
            if (event.getTabKey().equals(CreativeModeTabs.TOOLS_AND_UTILITIES) || event.getTabKey().equals(CreativeModeTabs.COLORED_BLOCKS)) {
                event.accept(NoSolicitingModule.RESTRAINING_ORDER.get());

                for (DyeColor color: DyeColor.values()) {
                    event.accept(NoSolicitingModule.SOLICITING_CARPET_ITEM.get(color).get());
                    event.accept(NoSolicitingModule.TRAPPED_SOLICITING_CARPET_ITEM.get(color).get());
                }
            }
        }
        if (event.getTabKey().equals(CreativeModeTabs.REDSTONE_BLOCKS)) {
            event.accept(TPSBreakerModule.TPS_METER_ITEM.get());
        }
        if (event.getTabKey().equals(CreativeModeTabs.FUNCTIONAL_BLOCKS)) {
            event.accept(AngelBlockModule.ANGEL_BLOCK_ITEM.get());
            event.accept(TPSBreakerModule.TPS_METER_ITEM.get());
        }
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(NoSolicitingModule.NO_SOLICITING_BANNER_BLOCK_ENTITY.get(), NoSolicitingBannerRenderer::new);
    }

    @SubscribeEvent
    public static void init(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(NoSolicitingModule.RESTRAINING_ORDER.get(), new ResourceLocation("active"), (stack, world, entity, i) -> RestrainingOrder.isActive(stack) ? 1.0F : 0.0F);
        });
    }
}
