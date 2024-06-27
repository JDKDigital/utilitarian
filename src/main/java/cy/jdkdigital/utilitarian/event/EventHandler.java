package cy.jdkdigital.utilitarian.event;

import cy.jdkdigital.utilitarian.Config;
import cy.jdkdigital.utilitarian.Utilitarian;
import cy.jdkdigital.utilitarian.module.NoSolicitingModule;
import net.minecraft.core.Direction;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.LogicalSide;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientChatReceivedEvent;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.ToolActions;
import net.neoforged.neoforge.common.util.LogicalSidedProvider;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.CanPlayerSleepEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

@EventBusSubscriber(modid = Utilitarian.MODID)
public class EventHandler
{
    @SubscribeEvent
    static void onEntitySpawn(EntityJoinLevelEvent event) {
        if (Config.NO_SOLICITING_ENABLED.get()) {
            if (!event.loadedFromDisk() && event.getLevel() instanceof ServerLevel serverLevel && event.getEntity() instanceof LivingEntity entity) {
                if (entity.getType().is(NoSolicitingModule.ENTITY_BLACKLIST)) {
                    var executor = LogicalSidedProvider.WORKQUEUE.get(LogicalSide.SERVER);
                    executor.tell(new TickTask(0, () -> {
                        var nearbySoliciting = NoSolicitingModule.locateNearbySoliciting(serverLevel, event.getEntity().blockPosition());
                        if (nearbySoliciting.size() > 0) {
                            // TP to nearest soliciting carpet
                            var pos = nearbySoliciting.get(0);
                            entity.setPos(pos.getX(), pos.getY(), pos.getZ());
                            if (serverLevel.getBlockState(pos).is(NoSolicitingModule.TRAPPED_SOLICITING_CARPETS)) {
                                // and die
                                entity.kill();
                            }
                        } else {
                            var nearbyNoSolicitingCount = NoSolicitingModule.locateNearbyNoSoliciting(serverLevel, event.getEntity().blockPosition());
                            if (nearbyNoSolicitingCount > 0) {
                                entity.discard();
                            }
                        }
                    }));
                }
            }
        }
    }

    @SubscribeEvent
    public static void canSleep(CanPlayerSleepEvent canPlayerSleepEvent) {
        if (Config.BETTER_SLEEP_ENABLED.get()) {
            if (canPlayerSleepEvent.getVanillaProblem() != null) {
                if (canPlayerSleepEvent.getVanillaProblem().equals(Player.BedSleepingProblem.TOO_FAR_AWAY)) {
                    canPlayerSleepEvent.setProblem(null);
                } else if (canPlayerSleepEvent.getVanillaProblem().equals(Player.BedSleepingProblem.NOT_SAFE)) {
                    canPlayerSleepEvent.setProblem(null);
                }
            }
        }
    }

    @SubscribeEvent
    public static void blockToolModified(BlockEvent.BlockToolModificationEvent event) {
        if (Config.HOE_PLANTING_ENABLED.get()) {
            if (!event.isSimulated() && event.getToolAction().equals(ToolActions.HOE_TILL) && event.getLevel() instanceof ServerLevel level) {
                if (event.getPlayer() != null && level.getBlockState(event.getPos().above()).canBeReplaced()) {
                    ItemStack seedStack = ItemStack.EMPTY;
                    if (event.getPlayer().getOffhandItem().is(Tags.Items.SEEDS)) {
                        seedStack = event.getPlayer().getOffhandItem();
                        event.getPlayer().swing(InteractionHand.OFF_HAND);
                    } else {
                        for (int i = 0; i <= 9; i++) {
                            var slotItem = event.getPlayer().getInventory().getItem(i);
                            if (!slotItem.isEmpty() && slotItem.is(Tags.Items.SEEDS)) {
                                seedStack = slotItem;
                                event.getPlayer().swing(InteractionHand.MAIN_HAND);
                                break;
                            }
                        }
                    }
                    if (!seedStack.isEmpty() && !seedStack.is(Utilitarian.BLACKLISTED_SEEDS) && seedStack.getItem() instanceof BlockItem blockItem) {
                        final ItemStack usedSeedStack = seedStack;
                        var executor = LogicalSidedProvider.WORKQUEUE.get(LogicalSide.SERVER);
                        executor.tell(new TickTask(0, () -> {
                            if (level.getBlockState(event.getPos()).getBlock() instanceof FarmBlock) {
                                var hitResult = new BlockHitResult(Vec3.ZERO, Direction.UP, event.getPos(), false);
                                var blockState = blockItem.getBlock().getStateForPlacement(new BlockPlaceContext(level, event.getPlayer(), event.getContext().getHand(), usedSeedStack, hitResult));
                                if (blockState != null) {
                                    level.setBlock(event.getPos().above(), blockItem.getBlock().defaultBlockState(), Block.UPDATE_ALL);
                                    if (!event.getPlayer().isCreative()) {
                                        usedSeedStack.shrink(1);
                                    }
                                }
                            }
                        }));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onSystemMessage(ClientChatReceivedEvent.System event) {
        if (Config.NO_STARTUP_MESSAGES_ENABLED.get()) {
            Config.NO_STARTUP_MESSAGES_MESSAGE_STRINGS.get().forEach(s -> {
                if (event.getMessage().getString().contains(s)) {
                    Utilitarian.LOGGER.debug("Blocked message: \"" + event.getMessage().getString() + "\"");
                    event.setCanceled(true);
                }
            });
        }
    }

    @SubscribeEvent
    public static void onTrample(BlockEvent.FarmlandTrampleEvent event) {
        if (Config.NO_TRAMPLE_ENABLED.get()) {
            if (!event.getEntity().getType().is(Utilitarian.TRAMPLING_ENTITIES)) {
                event.setCanceled(true);
            }
        }
    }
}
