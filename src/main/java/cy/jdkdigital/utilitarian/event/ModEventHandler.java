package cy.jdkdigital.utilitarian.event;

import cy.jdkdigital.utilitarian.Config;
import cy.jdkdigital.utilitarian.Utilitarian;
import cy.jdkdigital.utilitarian.module.*;
import cy.jdkdigital.utilitarian.network.SyncMufflerData;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@EventBusSubscriber(modid = Utilitarian.MODID)
public class ModEventHandler
{
    private static final Map<ResourceKey<Level>, LongOpenHashSet> VISITED_THIS_TICK = new HashMap<>();
    private static final List<DecayEntry> DECAY_QUEUE = new ArrayList<>();

    private record DecayEntry(ServerLevel level, BlockPos pos, long dueTick) {}

    @SubscribeEvent
    public static void onLogBreak(BreakBlockEvent event) {
        if (!Config.FAST_LEAF_DECAY_ENABLED.get()) {
            return;
        }
        if (!event.getState().is(BlockTags.LOGS)) {
            return;
        }
        if (event.getLevel().isClientSide() || !(event.getLevel() instanceof ServerLevel level)) {
            return;
        }

        LongOpenHashSet visited = VISITED_THIS_TICK.computeIfAbsent(level.dimension(), k -> new LongOpenHashSet());

        int radius = Config.FAST_LEAF_DECAY_RADIUS.get();
        int maxDelay = Config.FAST_LEAF_DECAY_MAX_DELAY.get();
        int baseDelay = radius + 2;
        long now = level.getGameTime();
        BlockPos origin = event.getPos();
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    cursor.setWithOffset(origin, dx, dy, dz);
                    if (!visited.add(cursor.asLong())) {
                        continue;
                    }
                    BlockState state = level.getBlockState(cursor);
                    if (state.is(BlockTags.LEAVES)
                            && state.hasProperty(LeavesBlock.PERSISTENT)
                            && !state.getValue(LeavesBlock.PERSISTENT)) {
                        long dueTick = now + baseDelay + level.getRandom().nextInt(maxDelay);
                        DECAY_QUEUE.add(new DecayEntry(level, cursor.immutable(), dueTick));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onServerTickPost(ServerTickEvent.Post event) {
        if (!VISITED_THIS_TICK.isEmpty()) {
            VISITED_THIS_TICK.clear();
        }
        if (DECAY_QUEUE.isEmpty()) {
            return;
        }
        Iterator<DecayEntry> iter = DECAY_QUEUE.iterator();
        while (iter.hasNext()) {
            DecayEntry entry = iter.next();
            if (entry.level.getGameTime() < entry.dueTick) {
                continue;
            }
            BlockState state = entry.level.getBlockState(entry.pos);
            if (state.is(BlockTags.LEAVES)
                    && state.hasProperty(LeavesBlock.PERSISTENT)
                    && state.hasProperty(LeavesBlock.DISTANCE)
                    && !state.getValue(LeavesBlock.PERSISTENT)
                    && state.getValue(LeavesBlock.DISTANCE) == 7) {
                Block.dropResources(state, entry.level, entry.pos);
                entry.level.removeBlock(entry.pos, false);
            }
            iter.remove();
        }
    }

    @SubscribeEvent
    public static void registerBlockEntityCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.Item.BLOCK,
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
                SyncMufflerData::clientHandle
        );
    }

    @SubscribeEvent
    public static void tabContents(BuildCreativeModeTabContentsEvent event) {
        if (Config.NO_SOLICITING_ENABLED.get()) {
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
            event.accept(UtilityBlockModule.MAGNET.get());
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
