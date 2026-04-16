package cy.jdkdigital.utilitarian;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

@EventBusSubscriber(modid = Utilitarian.MODID)
public class Config
{

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
    }

    private static final ModConfigSpec.Builder CLIENT_BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.DoubleValue POTION_EFFECT_TRANSPARENCY = CLIENT_BUILDER
            .comment("Potions effect stink lines transparency in first person").defineInRange("potionEffectTransparency", 0.2d, 0d, 1d);

    static ModConfigSpec CLIENT_SPEC = CLIENT_BUILDER.build();

    private static final ModConfigSpec.Builder COMMON_BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue NO_STARTUP_MESSAGES_ENABLED = COMMON_BUILDER
            .comment("Enable No Startup Messages module").define("noStartupMessagesEnabled", true);
    public static final ModConfigSpec.ConfigValue<List<? extends String>> NO_STARTUP_MESSAGES_MESSAGE_STRINGS = COMMON_BUILDER
            .comment("A list of word sequences that if contained in a message will be stopped.")
            .defineListAllowEmpty("noStartupMessagesMessages", List.of("This game is using an alpha build of Ender IO"), String::new, o -> true);

    public static final ModConfigSpec.BooleanValue DISABLE_RECIPE_ADVANCEMENTS = COMMON_BUILDER
            .comment("Disable recipe advancements").define("disableRecipeAdvancements", true);

    public static final ModConfigSpec.BooleanValue DING_DONG_ENABLED = COMMON_BUILDER
            .comment("Enable startup ding sound").define("enableDingDongStartupSound", true);
    public static final ModConfigSpec.ConfigValue<String> DING_DONG_SOUND = COMMON_BUILDER
            .comment("Name of sound to play. Default is entity.experience_orb.pickup").define("dingDongSound", "entity.experience_orb.pickup");

    public static final ModConfigSpec.BooleanValue ENABLE_LOG_SUPPRESSOR = COMMON_BUILDER
            .comment("Enable log supressor").define("logSuppressorEnabled", false);
    public static final ModConfigSpec.ConfigValue<List<? extends String>> LOG_SUPPRESSOR_MESSAGES_STARTS_WITH_STRINGS = COMMON_BUILDER
            .comment("A list of word sequences that if a log message starts with will be stopped.")
            .defineListAllowEmpty("logSuppressorStartsWithMessages", List.of(), String::new, o -> true);
    public static final ModConfigSpec.ConfigValue<List<? extends String>> LOG_SUPPRESSOR_MESSAGES_ENDS_WITH_STRINGS = COMMON_BUILDER
            .comment("A list of word sequences that if a log message ends with will be stopped.")
            .defineListAllowEmpty("logSuppressorEndsWithMessages", List.of(), String::new, o -> true);
    public static final ModConfigSpec.ConfigValue<List<? extends String>> LOG_SUPPRESSOR_MESSAGES_CONTAINS_STRINGS = COMMON_BUILDER
            .comment("A list of word sequences that if contained in a log message will be stopped.")
            .defineListAllowEmpty("logSuppressorContainsMessages", List.of(), String::new, o -> true);
    public static final ModConfigSpec.ConfigValue<List<? extends String>> LOG_SUPPRESSOR_MESSAGES_REGEX_STRINGS = COMMON_BUILDER
            .comment("A list of regex that if a log message matches it will be stopped. Don't use this if you don't know what you're doing.")
            .defineListAllowEmpty("logSuppressorRegexMessages", List.of(), String::new, o -> true);

    static ModConfigSpec COMMON_SPEC = COMMON_BUILDER.build();


    private static final ModConfigSpec.Builder SERVER_BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue NO_SOLICITING_ENABLED = SERVER_BUILDER
            .comment("Enable No Soliciting module").define("noSolicitingEnabled", true);
    public static final ModConfigSpec.IntValue NO_SOLICITING_BANNER_CHUNK_RANGE = SERVER_BUILDER
            .comment("Range in chunks for no soliciting banner.")
            .defineInRange("noSolicitingChunkRangeBanner", 6, 1, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue NO_SOLICITING_PLAYER_CHUNK_RANGE = SERVER_BUILDER
            .comment("Range in chunks for players holding a restraining order.")
            .defineInRange("noSolicitingChunkRangePlayer", 6, 1, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue SOLICITING_CARPET_CHUNK_RANGE = SERVER_BUILDER
            .comment("Range in chunks for players holding a restraining order.")
            .defineInRange("noSolicitingChunkRangeCarpet", 6, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.BooleanValue NO_RAIDER_ENABLED = SERVER_BUILDER
            .comment("Enable No Raider module").define("noRaiderEnabled", true);

    public static final ModConfigSpec.IntValue SOUND_MUFFLER_BLOCK_RANGE = SERVER_BUILDER
            .comment("Block range for muffling sounds with the sound muffler.")
            .defineInRange("soundMufflerBlockRange", 8, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.BooleanValue HOE_PLANTING_ENABLED = SERVER_BUILDER
            .comment("Enable Hoe planting module").define("hoePlantingEnabled", true);

    public static final ModConfigSpec.IntValue SNAD_ADDITIONAL_HEIGHT = SERVER_BUILDER
            .comment("Additional height for sugar cane and cactus when growing on snad").defineInRange("additionalGrowthHeight", 3, 0, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue SNAD_GROWTH_MULTIPLIER = SERVER_BUILDER
            .comment("How many extra growth ticks to apply when on snad").defineInRange("additionalGrowthTicks", 5, 0, Integer.MAX_VALUE);
    public static final ModConfigSpec.DoubleValue SNAD_DRIT_DAMAGE = SERVER_BUILDER
            .comment("Damage done by drit when you step on it").defineInRange("dritDamage", 2.0, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue FLUID_HOPPER_TICK_RATE = SERVER_BUILDER
            .comment("Tick rate for the fluid hopper. Lower number is faster ticking.").defineInRange("fluidHopperTickRate", 10, 1, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue REDSTONE_CLOCK_MIN_FREQUENCY = SERVER_BUILDER
            .comment("Minimum tick rate for the redstone clock. Set this higher if you're worried about performance.").defineInRange("minimumRedstoneClockTick", 5, 1, 24);

    public static final ModConfigSpec.BooleanValue BETTER_SLEEP_ENABLED = SERVER_BUILDER
            .comment("Get rid of the \"too far away\" and \"there are monsters nearby\" errors when trying to sleep.").define("betterSleepEnabled", true);

    public static final ModConfigSpec.BooleanValue NO_TRAMPLE_ENABLED = SERVER_BUILDER
            .comment("Enable No Trampling module").define("noTramplingEnabled", true);

    public static final ModConfigSpec.BooleanValue FLOWER_DUPLICATION_ENABLED = SERVER_BUILDER
            .comment("Enable Flower Duplication. Duplicate small flowers when bonemealed just like tall flowers").define("flowerDuplicationEnabled", true);

    public static final ModConfigSpec.BooleanValue DESPAWN_WHEN_HOLDING_ITEMS_ENABLED = SERVER_BUILDER
            .comment("Enable to let mobs despawn despite them holding items").define("despawnWhenHoldingItems", true);

    public static final ModConfigSpec.BooleanValue WIND_CHARGE_AIR_SUPPLY_ENABLED = SERVER_BUILDER
            .comment("When true using a wind charge will replenish some of the players air supply").define("windChargeAirSupplyEnabled", true);
    public static final ModConfigSpec.IntValue WIND_CHARGE_AIR_AMOUNT = SERVER_BUILDER
            .comment("How much air should a charge refill. Full air supply for a player is 300.").defineInRange("windChargeAirAmount", 100, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.BooleanValue CURSED_EARTH_ENABLED = SERVER_BUILDER
            .comment("Enable Cursed Earth module").define("cursedEarthEnabled", true);

    static ModConfigSpec SERVER_SPEC = SERVER_BUILDER.build();
}
