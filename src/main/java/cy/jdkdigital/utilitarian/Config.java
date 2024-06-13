package cy.jdkdigital.utilitarian;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = Utilitarian.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    public static final Common COMMON;
    public static final Server SERVER;
    public static final ForgeConfigSpec COMMON_SPEC, SERVER_SPEC;

    static {
        final var commonPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = commonPair.getRight();
        COMMON = commonPair.getLeft();

        final var serverPair = new ForgeConfigSpec.Builder().configure(Server::new);
        SERVER_SPEC = serverPair.getRight();
        SERVER = serverPair.getLeft();
    }

    public static class Common
    {
        public final ForgeConfigSpec.BooleanValue NO_STARTUP_MESSAGES_ENABLED;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> NO_STARTUP_MESSAGES_MESSAGE_STRINGS;

        public final ForgeConfigSpec.BooleanValue DISABLE_RECIPE_ADVANCEMENTS;

        Common(ForgeConfigSpec.Builder builder) {
            builder.push("No Startup Messages");
            NO_STARTUP_MESSAGES_ENABLED = builder
                    .comment("Enable No Startup Messages module").define("noStartupMessagesEnabled", true);
            NO_STARTUP_MESSAGES_MESSAGE_STRINGS = builder
                    .comment("A list of word sequences that if contained in a message will be stopped.")
                    .defineListAllowEmpty("noStartupMessagesMessages", List.of("This game is using an alpha build of Ender IO"), o -> true);
            builder.pop();

            builder.push("Recipes");
            DISABLE_RECIPE_ADVANCEMENTS = builder
                    .comment("Disable recipe advancements").define("disableRecipeAdvancements", true);
            builder.pop();
        }
    }

    public static class Server
    {
        public final ForgeConfigSpec.BooleanValue NO_SOLICITING_ENABLED;
        public final ForgeConfigSpec.IntValue NO_SOLICITING_BANNER_CHUNK_RANGE;
        public final ForgeConfigSpec.IntValue NO_SOLICITING_PLAYER_CHUNK_RANGE;
        public final ForgeConfigSpec.IntValue SOLICITING_CARPET_CHUNK_RANGE;

        public final ForgeConfigSpec.BooleanValue HOE_PLANTING_ENABLED;

        public final ForgeConfigSpec.IntValue SNAD_ADDITIONAL_HEIGHT;
        public final ForgeConfigSpec.IntValue SNAD_GROWTH_MULTIPLIER;
        public final ForgeConfigSpec.DoubleValue SNAD_DRIT_DAMAGE;

        public final ForgeConfigSpec.IntValue FLUID_HOPPER_TICK_RATE;
        public final ForgeConfigSpec.IntValue REDSTONE_CLOCK_MIN_FREQUENCY;

        public final ForgeConfigSpec.BooleanValue BETTER_SLEEP_ENABLED;

        Server(ForgeConfigSpec.Builder builder) {
            Utilitarian.LOGGER.info("setting up server config");
            builder.push("No Soliciting");
            NO_SOLICITING_ENABLED = builder
                    .comment("Enable No Soliciting module").define("noSolicitingEnabled", true);
            NO_SOLICITING_BANNER_CHUNK_RANGE = builder
                    .comment("Range in chunks for no soliciting banner.")
                    .defineInRange("noSolicitingChunkRangeBanner", 6, 1, Integer.MAX_VALUE);
            NO_SOLICITING_PLAYER_CHUNK_RANGE = builder
                    .comment("Range in chunks for players holding a restraining order.")
                    .defineInRange("noSolicitingChunkRangePlayer", 6, 1, Integer.MAX_VALUE);
            SOLICITING_CARPET_CHUNK_RANGE = builder
                    .comment("Range in chunks for players holding a restraining order.")
                    .defineInRange("noSolicitingChunkRangeCarpet", 6, 1, Integer.MAX_VALUE);
            builder.pop();

            builder.push("Hoe planting");
            HOE_PLANTING_ENABLED = builder
                    .comment("Enable Hoe planting module").define("hoePlantingEnabled", true);
            builder.pop();

            builder.push("Snad");
            SNAD_ADDITIONAL_HEIGHT = builder
                    .comment("Additional height for sugar cane and cactus when growing on snad").defineInRange("additionalGrowthHeight", 3, 0, Integer.MAX_VALUE);
            SNAD_GROWTH_MULTIPLIER = builder
                    .comment("How many extra growth ticks to apply when on snad").defineInRange("additionalGrowthTicks", 1, 0, Integer.MAX_VALUE);
            SNAD_DRIT_DAMAGE = builder
                    .comment("Damage done by drit when you step on it").defineInRange("dritDamage", 2.0, 0, Integer.MAX_VALUE);
            builder.pop();

            builder.push("Utility blocks");
            FLUID_HOPPER_TICK_RATE = builder
                    .comment("Tick rate for the fluid hopper. Lower number is faster ticking.").defineInRange("fluidHopperTickRate", 10, 1, Integer.MAX_VALUE);
            REDSTONE_CLOCK_MIN_FREQUENCY = builder
                    .comment("Minimum tick rate for the redstone clock. Set this higher if you're worried about performance.").defineInRange("minimumRedstoneClockTick", 5, 1, 24);
            builder.pop();

            builder.push("Better sleep");
            BETTER_SLEEP_ENABLED = builder
                    .comment("Get rid of the \"too far away\" and \"there are monsters nearby\" errors when trying to sleep.").define("betterSleepEnabled", true);
            builder.pop();
        }
    }
}
