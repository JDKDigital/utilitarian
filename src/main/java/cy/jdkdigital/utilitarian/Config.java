package cy.jdkdigital.utilitarian;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

@EventBusSubscriber(modid = Utilitarian.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config
{

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
    }

    private static final ModConfigSpec.Builder COMMON_BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue NO_STARTUP_MESSAGES_ENABLED = COMMON_BUILDER
            .comment("Enable No Startup Messages module").define("noStartupMessagesEnabled", true);
    public static final ModConfigSpec.ConfigValue<List<? extends String>> NO_STARTUP_MESSAGES_MESSAGE_STRINGS = COMMON_BUILDER
            .comment("A list of word sequences that if contained in a message will be stopped.")
            .defineListAllowEmpty("noStartupMessagesMessages", List.of("This game is using an alpha build of Ender IO"), o -> true);

    public static final ModConfigSpec.BooleanValue DISABLE_RECIPE_ADVANCEMENTS = COMMON_BUILDER
            .comment("Disable recipe advancements").define("disableRecipeAdvancements", true);

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

    public static final ModConfigSpec.BooleanValue HOE_PLANTING_ENABLED = SERVER_BUILDER
            .comment("Enable Hoe planting module").define("hoePlantingEnabled", true);

    public static final ModConfigSpec.IntValue SNAD_ADDITIONAL_HEIGHT = SERVER_BUILDER
            .comment("Additional height for sugar cane and cactus when growing on snad").defineInRange("additionalGrowthHeight", 3, 0, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue SNAD_GROWTH_MULTIPLIER = SERVER_BUILDER
            .comment("How many extra growth ticks to apply when on snad").defineInRange("additionalGrowthTicks", 1, 0, Integer.MAX_VALUE);
    public static final ModConfigSpec.DoubleValue SNAD_DRIT_DAMAGE = SERVER_BUILDER
            .comment("Damage done by drit when you step on it").defineInRange("dritDamage", 2.0, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue FLUID_HOPPER_TICK_RATE = SERVER_BUILDER
            .comment("Tick rate for the fluid hopper. Lower number is faster ticking.").defineInRange("fluidHopperTickRate", 10, 1, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue REDSTONE_CLOCK_MIN_FREQUENCY = SERVER_BUILDER
            .comment("Minimum tick rate for the redstone clock. Set this higher if you're worried about performance.").defineInRange("minimumRedstoneClockTick", 5, 1, 24);

    public static final ModConfigSpec.BooleanValue BETTER_SLEEP_ENABLED = SERVER_BUILDER
            .comment("Get rid of the \"too far away\" and \"there are monsters nearby\" errors when trying to sleep.").define("betterSleepEnabled", true);

    static ModConfigSpec SERVER_SPEC = SERVER_BUILDER.build();
}
