package cy.jdkdigital.utilitarian.event;

import cy.jdkdigital.utilitarian.Config;
import cy.jdkdigital.utilitarian.Utilitarian;
import cy.jdkdigital.utilitarian.client.gui.ColorfulHeartsLayer;
import cy.jdkdigital.utilitarian.client.gui.OverloadedArmorLayer;
import cy.jdkdigital.utilitarian.client.render.block.NoSolicitingBannerRenderer;
import cy.jdkdigital.utilitarian.module.NoSolicitingModule;
import cy.jdkdigital.utilitarian.module.SnadModule;
import cy.jdkdigital.utilitarian.module.UtilityEntityModule;
import net.minecraft.client.color.block.BlockTintSources;
import net.minecraft.client.renderer.entity.FallingBlockRenderer;

import java.util.List;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@EventBusSubscriber(modid = Utilitarian.MODID, value = Dist.CLIENT)
public class ClientModEvents
{
    @SubscribeEvent
    public static void registerGuiLayers(RegisterGuiLayersEvent event) {
        if (Config.COLORFUL_HEARTS_ENABLED.get()) {
            event.replaceLayer(VanillaGuiLayers.PLAYER_HEALTH, new ColorfulHeartsLayer());
        }
        if (Config.OVERLOADED_ARMOR_BAR_ENABLED.get()) {
            event.replaceLayer(VanillaGuiLayers.ARMOR_LEVEL, new OverloadedArmorLayer());
        }
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(NoSolicitingModule.NO_SOLICITING_BANNER_BLOCK_ENTITY.get(), NoSolicitingBannerRenderer::new);
        event.registerEntityRenderer(UtilityEntityModule.RISING_BLOCK.get(), FallingBlockRenderer::new);

    }

    @SubscribeEvent
    public static void init(final FMLClientSetupEvent event) {
        // TODO MC 26.1: ItemProperties and ItemBlockRenderTypes removed.
        // Item model predicates and render type overrides are now data-driven via JSON model files.
        // ItemProperties.register(UtilityItemModule.TROWEL.get(), Identifier.withDefaultNamespace("extended"), ...);
        // ItemProperties.register(UtilityItemModule.SLIME_BUCKET.get(), Identifier.withDefaultNamespace("slimed"), ...);
    }

    // TODO MC 26.1: Item color handlers are now data-driven via model JSON tint sources.
    // Block tint source registration uses RegisterColorHandlersEvent.BlockTintSources.
    @SubscribeEvent
    public static void registerBlockColors(final RegisterColorHandlersEvent.BlockTintSources event) {
        event.register(List.of(BlockTintSources.grassBlock()), SnadModule.GRRASS_BLOCK.get());
    }
}
