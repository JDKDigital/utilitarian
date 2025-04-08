package cy.jdkdigital.utilitarian.event;

import cy.jdkdigital.utilitarian.Config;
import cy.jdkdigital.utilitarian.Utilitarian;
import cy.jdkdigital.utilitarian.client.render.block.NoSolicitingBannerRenderer;
import cy.jdkdigital.utilitarian.common.item.RestrainingOrder;
import cy.jdkdigital.utilitarian.common.item.TrowelItem;
import cy.jdkdigital.utilitarian.module.*;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@EventBusSubscriber(modid = Utilitarian.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ClientModEvents
{
    @SubscribeEvent
    public static void tabContents(BuildCreativeModeTabContentsEvent event) {
        if (Config.NO_SOLICITING_ENABLED.get()) {
//            if (event.getTabKey().equals(CreativeModeTabs.FUNCTIONAL_BLOCKS)) {
//                HolderLookup.RegistryLookup<BannerPattern> registrylookup = Minecraft.getInstance().level.registryAccess().lookupOrThrow(Registries.BANNER_PATTERN);
//                ItemStack noSolicitingBanner = new ItemStack(NoSolicitingModule.NO_SOLICITING_BANNER_ITEM.get());
//                CompoundTag compoundtag = new CompoundTag();
//                compoundtag.put("Patterns", (new BannerPatternLayers.Builder()).addIfRegistered(registrylookup, BannerPatterns.CROSS, DyeColor.RED).toListTag());
//                BlockItem.setBlockEntityData(noSolicitingBanner, NoSolicitingModule.NO_SOLICITING_BANNER_BLOCK_ENTITY.get(), compoundtag);
//                event.accept(noSolicitingBanner);
//            }
            if (event.getTabKey().equals(CreativeModeTabs.TOOLS_AND_UTILITIES)) {
                event.accept(NoSolicitingModule.RESTRAINING_ORDER.get());
                event.accept(NoSolicitingModule.SOLICITING_CARPET_ITEM.get(DyeColor.WHITE).get());
                event.accept(NoSolicitingModule.TRAPPED_SOLICITING_CARPET_ITEM.get(DyeColor.WHITE).get());
                event.accept(UtilityBlockModule.FLUID_HOPPER_BLOCK.get());
                event.accept(UtilityBlockModule.ANGEL_BLOCK.get());
                event.accept(UtilityBlockModule.REDSTONE_CLOCK_BLOCK.get());
                event.accept(UtilityItemModule.TROWEL.get());
                event.accept(UtilityItemModule.TINY_COAL.get());
                event.accept(UtilityItemModule.TINY_CHARCOAL.get());
            }
            if (event.getTabKey().equals(CreativeModeTabs.COLORED_BLOCKS)) {
                for (DyeColor color: DyeColor.values()) {
                    event.accept(NoSolicitingModule.SOLICITING_CARPET_ITEM.get(color).get());
                    event.accept(NoSolicitingModule.TRAPPED_SOLICITING_CARPET_ITEM.get(color).get());
                }
            }
        }
        if (event.getTabKey().equals(CreativeModeTabs.REDSTONE_BLOCKS)) {
            event.accept(TPSMeterModule.TPS_METER_ITEM.get());
            event.accept(UtilityBlockModule.REDSTONE_CLOCK_BLOCK.get());
            event.accept(UtilityBlockModule.LAPIS_LAMP.get());
        }
        if (event.getTabKey().equals(CreativeModeTabs.FUNCTIONAL_BLOCKS)) {
            event.accept(UtilityBlockModule.ANGEL_BLOCK_ITEM.get());
            event.accept(UtilityBlockModule.SOUND_MUFFLER_ITEM.get());
            event.accept(TPSMeterModule.TPS_METER_ITEM.get());
            event.accept(SnadModule.SNAD_BLOCK_ITEM.get());
            event.accept(SnadModule.RED_SNAD_BLOCK_ITEM.get());
            event.accept(SnadModule.SOUL_SNAD_BLOCK_ITEM.get());
            event.accept(SnadModule.DRIT_BLOCK_ITEM.get());
            event.accept(SnadModule.GRRASS_BLOCK_ITEM.get());
        }
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(NoSolicitingModule.NO_SOLICITING_BANNER_BLOCK_ENTITY.get(), NoSolicitingBannerRenderer::new);
    }

    @SubscribeEvent
    public static void init(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(NoSolicitingModule.RESTRAINING_ORDER.get(), ResourceLocation.withDefaultNamespace("active"), (stack, world, entity, i) -> RestrainingOrder.isActive(stack) ? 1.0F : 0.0F);
            ItemProperties.register(UtilityItemModule.TROWEL.get(), ResourceLocation.withDefaultNamespace("extended"), (stack, world, entity, i) -> TrowelItem.isExtended(stack) ? 1.0F : 0.0F);
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
