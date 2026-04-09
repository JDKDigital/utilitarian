package cy.jdkdigital.utilitarian.event;

import cy.jdkdigital.utilitarian.Utilitarian;
import cy.jdkdigital.utilitarian.client.render.block.NoSolicitingBannerRenderer;
import cy.jdkdigital.utilitarian.common.item.RestrainingOrder;
import cy.jdkdigital.utilitarian.common.item.SlimeBucketItem;
import cy.jdkdigital.utilitarian.common.item.TrowelItem;
import cy.jdkdigital.utilitarian.module.NoSolicitingModule;
import cy.jdkdigital.utilitarian.module.SnadModule;
import cy.jdkdigital.utilitarian.module.UtilityEntityModule;
import cy.jdkdigital.utilitarian.module.UtilityItemModule;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.FallingBlockRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

@EventBusSubscriber(modid = Utilitarian.MODID, value = Dist.CLIENT)
public class ClientModEvents
{
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(NoSolicitingModule.NO_SOLICITING_BANNER_BLOCK_ENTITY.get(), NoSolicitingBannerRenderer::new);
        event.registerEntityRenderer(UtilityEntityModule.RISING_BLOCK.get(), FallingBlockRenderer::new);

    }

    @SubscribeEvent
    public static void init(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(NoSolicitingModule.RESTRAINING_ORDER.get(), ResourceLocation.withDefaultNamespace("active"), (stack, world, entity, i) -> RestrainingOrder.isActive(stack) ? 1.0F : 0.0F);
            ItemProperties.register(UtilityItemModule.TROWEL.get(), ResourceLocation.withDefaultNamespace("extended"), (stack, world, entity, i) -> TrowelItem.isExtended(stack) ? 1.0F : 0.0F);
            ItemProperties.register(UtilityItemModule.SLIME_BUCKET.get(), ResourceLocation.withDefaultNamespace("slimed"), (stack, world, entity, i) -> SlimeBucketItem.isInSlime(stack) ? 1.0F : 0.0F);
            ItemBlockRenderTypes.setRenderLayer(SnadModule.GRRASS_BLOCK.get(), RenderType.cutoutMipped());
        });
    }

    @SubscribeEvent
    public static void registerItemColors(final RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> {
            BlockState blockstate = ((BlockItem)stack.getItem()).getBlock().defaultBlockState();
            return event.getBlockColors().getColor(blockstate, null, null, tintIndex);
        }, SnadModule.GRRASS_BLOCK.get());
    }

    @SubscribeEvent
    public static void registerBlockColors(final RegisterColorHandlersEvent.Block event) {
        event.register((blockState, lightReader, pos, tintIndex) -> {
            return lightReader != null && pos != null ? BiomeColors.getAverageGrassColor(lightReader, pos) : GrassColor.get(0.5D, 1.0D);
        }, SnadModule.GRRASS_BLOCK.get());
    }
}
