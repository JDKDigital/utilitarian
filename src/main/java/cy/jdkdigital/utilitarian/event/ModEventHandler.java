package cy.jdkdigital.utilitarian.event;

import cy.jdkdigital.utilitarian.Config;
import cy.jdkdigital.utilitarian.Utilitarian;
import cy.jdkdigital.utilitarian.module.*;
import cy.jdkdigital.utilitarian.network.SyncMufflerData;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = Utilitarian.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventHandler
{
    @SubscribeEvent
    public static void registerBlockEntityCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                UtilityBlockModule.WELL_BEHAVED_DROPPER_BLOCK_ENTITY.get(),
                (myBlockEntity, side) -> myBlockEntity.inventoryHandler
        );
    }

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(Utilitarian.MODID).versioned("1");
        registrar.playToClient(
                SyncMufflerData.TYPE,
                SyncMufflerData.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        SyncMufflerData::clientHandle,
                        SyncMufflerData::serverHandle
                )
        );
    }

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
                event.accept(NoSolicitingModule.NO_SOLICITING_BANNER_ITEM.get());
                event.accept(NoSolicitingModule.SOLICITING_CARPET_ITEM.get(DyeColor.WHITE).get());
                event.accept(NoSolicitingModule.TRAPPED_SOLICITING_CARPET_ITEM.get(DyeColor.WHITE).get());
            }
            if (event.getTabKey().equals(CreativeModeTabs.COLORED_BLOCKS)) {
                for (DyeColor color: DyeColor.values()) {
                    event.accept(NoSolicitingModule.SOLICITING_CARPET_ITEM.get(color).get());
                    event.accept(NoSolicitingModule.TRAPPED_SOLICITING_CARPET_ITEM.get(color).get());
                }
            }
        }

        if (event.getTabKey().equals(CreativeModeTabs.TOOLS_AND_UTILITIES)) {
            event.accept(NoSolicitingModule.NO_RAIDER_BLOCK_ITEM.get());
            event.accept(UtilityBlockModule.FLUID_HOPPER_BLOCK.get());
            event.accept(UtilityBlockModule.ANGEL_BLOCK.get());
            event.accept(UtilityBlockModule.REDSTONE_CLOCK_BLOCK.get());
            event.accept(UtilityItemModule.TROWEL.get());
            event.accept(UtilityItemModule.TINY_COAL.get());
            event.accept(UtilityItemModule.TINY_CHARCOAL.get());
            event.accept(UtilityItemModule.UNNAME_TAG.get());
            event.accept(UtilityItemModule.SLIME_BUCKET.get());
        }

        if (event.getTabKey().equals(CreativeModeTabs.REDSTONE_BLOCKS)) {
            event.accept(TPSMeterModule.TPS_METER_ITEM.get());
            event.accept(UtilityBlockModule.REDSTONE_CLOCK_BLOCK.get());
            event.accept(UtilityBlockModule.LAPIS_LAMP.get());
            event.accept(UtilityBlockModule.INVERTED_LAPIS_LAMP.get());
            event.accept(UtilityBlockModule.INVERTED_REDSTONE_LAMP.get());
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
            event.accept(SnadModule.CURSED_GRRASS_BLOCK_ITEM.get());
        }
    }
}
