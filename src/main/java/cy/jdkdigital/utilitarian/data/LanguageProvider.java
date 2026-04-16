package cy.jdkdigital.utilitarian.data;

import cy.jdkdigital.utilitarian.Utilitarian;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.WallBannerBlock;

public class LanguageProvider extends net.neoforged.neoforge.common.data.LanguageProvider
{
    public LanguageProvider(PackOutput output, String locale) {
        super(output, Utilitarian.MODID, locale);
        add("death.attack.utilitarian.drit", "%1$s was killed by drit");
        add("death.attack.utilitarian.drit.player", "%1$s was killed by drit while playing with %2$s");
        add("death.attack.utilitarian.grrass", "%1$s was killed by angry grass");
        add("death.attack.utilitarian.grrass.player", "%1$s was killed by angry grass while playing with %2$s");
        add("utilitarian.soliciting_carpet.tooltip", "Arriving traders will end up on this block instead of in your face.");
        add("utilitarian.soliciting_carpet.tooltip_trapped", "This carpet is trapped and will kill the trader...you monster ;D");
        add("utilitarian.restraining_order.status.inactive", "Draft. Sneak right click to activate.");
        add("utilitarian.restraining_order.status.active", "Stamped and enforced. Sneak right click to deactivate.");
        add("utilitarian.wind_charge.tooltip", "Replenishes your breath under water.");
        add("utilitarian.sound_muffler.tooltip", "Mutes sounds within a %s block radius.");
        add("utilitarian.trowel.tooltip", "Shift right click to change pull setting");
        add("utilitarian.trowel.state", "Pulls from, %s");
        add("utilitarian.trowel.state.normal", "Hotbar");
        add("utilitarian.trowel.state.extended", "Inventory and hotbar");
        add("utilitarian.no_raider.description", "Place in an outpost to stop raiders from returning");
        add("utilitarian.drit.description", "It hurts. Grass can grow on this.");
        add("utilitarian.grrass.description", "It also hurts. Don't let it be struck by lightning!");
        add("utilitarian.cursed_grrass.description", "Spawns extra mobs.");
        add("item.utilitarian.redstone_clock.description", "Right click the redstone clock block with an empty hand to change pulse frequency, sneak to increase faster.");
        add("block.utilitarian.redstone_clock.message", "Clock frequency: %s");
        add("item.utilitarian.tps_meter_description", "Outputs a redstone signal based on the dimensions TPS");
        add("item.utilitarian.slime_bucket_description", "Gets visibly excited to see its friends when visiting a slime chunk.");
        add("block.utilitarian.lapis_lamp.tooltip", "This is blue right?");
        add("block.utilitarian.lapis_lamp.tooltip2", "Lights up an area on the client side only, it's still dark, you just can't see it.");

        add("utilitarian.configuration.potionEffectTransparency", "Potion Effect Transparency");
        add("utilitarian.configuration.noStartupMessagesEnabled", "Enable NoStartup Messages");
        add("utilitarian.configuration.noStartupMessagesMessages", "No Startup Messages");
        add("utilitarian.configuration.disableRecipeAdvancements", "Disable Recipe Advancements");
        add("utilitarian.configuration.enableDingDongStartupSound", "Enable DingDong Startup Sound");
        add("utilitarian.configuration.dingDongSound", "DingDong Sound");
        add("utilitarian.configuration.logSuppressorEnabled", "Enable log Suppressor");
        add("utilitarian.configuration.logSuppressorStartsWithMessages", "Log Suppressor StartsWith Messages");
        add("utilitarian.configuration.logSuppressorEndsWithMessages", "Log Suppressor EndsWith Messages");
        add("utilitarian.configuration.logSuppressorContainsMessages", "Log Suppressor Contains Messages");
        add("utilitarian.configuration.logSuppressorRegexMessages", "Log Suppressor Regex Messages");
        add("utilitarian.configuration.noSolicitingEnabled", "Enable No-Soliciting");
        add("utilitarian.configuration.noSolicitingChunkRangeBanner", "No Soliciting Banner ChunkRange");
        add("utilitarian.configuration.noSolicitingChunkRangePlayer", "No Soliciting Player ChunkRange");
        add("utilitarian.configuration.noSolicitingChunkRangeCarpet", "No Soliciting Carpet ChunkRange");
        add("utilitarian.configuration.noRaiderEnabled", "Enable No-Raider");
        add("utilitarian.configuration.soundMufflerBlockRange", "Sound Muffler BlockRange");
        add("utilitarian.configuration.hoePlantingEnabled", "Enable Hoe Planting");
        add("utilitarian.configuration.additionalGrowthHeight", "Snad Additional Growth Height");
        add("utilitarian.configuration.additionalGrowthTicks", "Snad Additional Growth Ticks");
        add("utilitarian.configuration.dritDamage", "Drit Damage");
        add("utilitarian.configuration.fluidHopperTickRate", "Fluid Hopper Tick Rate");
        add("utilitarian.configuration.minimumRedstoneClockTick", "Minimum Redstone Clock Tick Rate");
        add("utilitarian.configuration.betterSleepEnabled", "Enable Better Sleep");
        add("utilitarian.configuration.noTramplingEnabled", "Enable No Trampling");
        add("utilitarian.configuration.flowerDuplicationEnabled", "Enable Flower Duplication");
        add("utilitarian.configuration.despawnWhenHoldingItems", "Enable Despawn When Holding Items");
        add("utilitarian.configuration.windChargeAirSupplyEnabled", "Enable Wind Charge Air Supply");
        add("utilitarian.configuration.windChargeAirAmount", "Wind Charge Air Amount");
        add("utilitarian.configuration.cursedEarthEnabled", "Enable Cursed Earth");
    }

    @Override
    protected void addTranslations() {
        Utilitarian.BLOCKS.getEntries().forEach(holder -> {
            if (!(holder.get() instanceof WallBannerBlock)) {
                add(holder.get(), capName(BuiltInRegistries.BLOCK.getKey(holder.get()).getPath()));
            }
        });
        Utilitarian.ITEMS.getEntries().forEach(holder -> {
            if (!(holder.get() instanceof BlockItem)) {
                add(holder.get(), capName(BuiltInRegistries.ITEM.getKey(holder.get()).getPath()));
            }
        });
    }

    public static String capName(String name) {
        String[] nameParts = name.split("_");

        for (int i = 0; i < nameParts.length; i++) {
            nameParts[i] = nameParts[i].substring(0, 1).toUpperCase() + nameParts[i].substring(1);
        }

        return String.join(" ", nameParts);
    }
}
