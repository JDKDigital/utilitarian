package cy.jdkdigital.utilitarian.event;

import com.mojang.datafixers.util.Pair;
import cy.jdkdigital.utilitarian.Config;
import cy.jdkdigital.utilitarian.Utilitarian;
import cy.jdkdigital.utilitarian.module.NoSolicitingModule;
import cy.jdkdigital.utilitarian.module.SnadModule;
import cy.jdkdigital.utilitarian.module.UtilityBlockModule;
import cy.jdkdigital.utilitarian.network.SyncSoundMufflerData;
import cy.jdkdigital.utilitarian.util.Helper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.windcharge.WindCharge;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.LogicalSide;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientChatReceivedEvent;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.util.LogicalSidedProvider;
import net.neoforged.neoforge.event.PlayLevelSoundEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.*;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.ChunkWatchEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@EventBusSubscriber(modid = Utilitarian.MODID)
public class EventHandler
{
    @SubscribeEvent
    static void onTooltip(ItemTooltipEvent event) {
        if (event.getItemStack().is(Items.WIND_CHARGE)) {
            event.getToolTip().add(Component.translatable(Utilitarian.MODID + ".wind_charge.tooltip").withStyle(ChatFormatting.AQUA));
        }
        if (event.getItemStack().is(UtilityBlockModule.SOUND_MUFFLER_ITEM)) {
            event.getToolTip().add(Component.translatable(Utilitarian.MODID + ".sound_muffler.tooltip", Config.SOUND_MUFFLER_BLOCK_RANGE.get()).withStyle(ChatFormatting.AQUA));
        }
    }

    @SubscribeEvent
    static void onEntitySpawn(EntityJoinLevelEvent event) {
        if (Config.NO_SOLICITING_ENABLED.get()) {
            if (!event.loadedFromDisk() && event.getLevel() instanceof ServerLevel serverLevel && event.getEntity() instanceof LivingEntity entity) {
                if (entity.getType().is(NoSolicitingModule.TRADER_BLACKLIST)) {
                    var executor = LogicalSidedProvider.WORKQUEUE.get(LogicalSide.SERVER);
                    executor.tell(new TickTask(0, () -> {
                        var nearbySoliciting = Helper.locateNearbySoliciting(serverLevel, event.getEntity().blockPosition());
                        if (!nearbySoliciting.isEmpty()) {
                            // TP to nearest soliciting carpet
                            var pos = nearbySoliciting.getFirst();
                            entity.setPos(pos.getX(), pos.getY(), pos.getZ());
                            if (serverLevel.getBlockState(pos).is(NoSolicitingModule.TRAPPED_SOLICITING_CARPETS)) {
                                // and die
                                entity.kill();
                            }
                        } else {
                            var nearbyNoSolicitingCount = Helper.locateNearbyNoSoliciting(serverLevel, event.getEntity().blockPosition());
                            if (nearbyNoSolicitingCount > 0) {
                                entity.discard();
                            }
                        }
                    }));
                }
            }
        }
        if (Config.WIND_CHARGE_AIR_SUPPLY_ENABLED.get() && !event.getLevel().isClientSide) {
            if (event.getEntity() instanceof WindCharge windCharge) {
                if (windCharge.getOwner() instanceof Player player && player.getAirSupply() < player.getMaxAirSupply()) {
                    player.setAirSupply(Math.min(player.getMaxAirSupply(), player.getAirSupply() + Config.WIND_CHARGE_AIR_AMOUNT.get()));
                }
            }
        }
        if (Config.CURSED_EARTH_ENABLED.get() && event.getLevel() instanceof ServerLevel serverLevel && event.getEntity() instanceof LightningBolt lightningBolt) {
            Vec3 vec3 = lightningBolt.position();
            var hitPosition = BlockPos.containing(vec3.x, vec3.y - 1.0E-6, vec3.z);
            var hitState = serverLevel.getBlockState(hitPosition);
            if (hitState.is(Blocks.LIGHTNING_ROD)) {
                hitState = serverLevel.getBlockState(hitPosition.below());
            }

            if (hitState.is(SnadModule.GRRASS_BLOCK)) {
                BlockPos.betweenClosedStream(new AABB(hitPosition).inflate(2)).forEach(pos -> {
                    if (serverLevel.getBlockState(pos).is(SnadModule.GRRASS_BLOCK)) {
                        int weight = Math.max(0, 15  - (int)pos.distSqr(hitPosition));
                        if (serverLevel.getRandom().nextInt(15) < weight) {
                            serverLevel.setBlockAndUpdate(pos, SnadModule.CURSED_GRRASS_BLOCK.get().defaultBlockState());
                        }
                    }
                });
            }
        }
        if (Config.NO_RAIDER_ENABLED.get() && event.getLevel() instanceof ServerLevel serverLevel && event.getEntity().getType().is(NoSolicitingModule.RAIDER_BLACKLIST)) {
            Registry<Structure> registry = serverLevel.registryAccess().registryOrThrow(Registries.STRUCTURE);
            registry.getTag(NoSolicitingModule.RAIDER_OUTPOSTS).ifPresent(holders -> {
                Pair<BlockPos, Holder<Structure>> pair = serverLevel.getChunkSource()
                        .getGenerator()
                        .findNearestMapStructure(serverLevel, holders, event.getEntity().blockPosition(), 4, false);

                if (pair != null) {
                    var nearbySoliciting = Helper.locateNearbyNoRaider(serverLevel, pair.getFirst());
                    if (!nearbySoliciting.isEmpty()) {
                        var executor = LogicalSidedProvider.WORKQUEUE.get(LogicalSide.SERVER);
                        executor.tell(new TickTask(0, () -> {
                            event.getEntity().discard();
                        }));
                    }
                }
            });
        }
    }

    public static Set<String> CLIENT_MUFFLER_LIST = new HashSet<>();
    @SubscribeEvent
    static void onSoundAtPosition(PlayLevelSoundEvent.AtPosition event) {
        var position = new BlockPos((int) event.getPosition().x, (int) event.getPosition().y, (int) event.getPosition().z);
        if (event.getLevel() instanceof ServerLevel level) {
            var nearbySoundMufflers = Helper.locateNearbySoundMuffler(level, position, UtilityBlockModule.SOUND_MUFFLER_POI_TAG);
            event.setCanceled(!nearbySoundMufflers.isEmpty());
        } else {
            var list = new ArrayList<>(CLIENT_MUFFLER_LIST);
            for (String s : list) {
                var mufflerPos = BlockPos.of(Long.parseLong(s));
                if (!event.getLevel().getBlockState(mufflerPos).is(UtilityBlockModule.SOUND_MUFFLER)) {
                    CLIENT_MUFFLER_LIST.remove(s);
                } else if (mufflerPos.distToCenterSqr(position.getX(), position.getY(), position.getZ()) <= Config.SOUND_MUFFLER_BLOCK_RANGE.get()) {
                    event.setCanceled(true);
                    break;
                }
            }
        }
    }

    @SubscribeEvent
    static void onSoundAtEntity(PlayLevelSoundEvent.AtEntity event) {
        var position = event.getEntity().blockPosition();
        if (event.getLevel() instanceof ServerLevel level) {
            var nearbySoundMuffler = Helper.locateNearbySoundMuffler(level, position, UtilityBlockModule.SOUND_MUFFLER_POI_TAG);
            if (!nearbySoundMuffler.isEmpty()) {
                event.setCanceled(true);
            }
        } else {
            var list = new ArrayList<>(CLIENT_MUFFLER_LIST);
            for (String s : list) {
                var mufflerPos = BlockPos.of(Long.parseLong(s));
                if (!event.getLevel().getBlockState(mufflerPos).is(UtilityBlockModule.SOUND_MUFFLER)) {
                    CLIENT_MUFFLER_LIST.remove(s);
                } else if (mufflerPos.distToCenterSqr(position.getX(), position.getY(), position.getZ()) <= Config.SOUND_MUFFLER_BLOCK_RANGE.get()) {
                    event.setCanceled(true);
                    break;
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
            if (!event.isSimulated() && event.getItemAbility().equals(ItemAbilities.HOE_TILL) && event.getLevel() instanceof ServerLevel level) {
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
                                var blockState = blockItem.place(new BlockPlaceContext(level, event.getPlayer(), event.getContext().getHand(), usedSeedStack, hitResult));
                                if (blockState.consumesAction()) {
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

    @SubscribeEvent
    public static void onBoneMeal(BonemealEvent event) {
        if (Config.FLOWER_DUPLICATION_ENABLED.get()) {
            if (event.getState().is(BlockTags.SMALL_FLOWERS)) {
                if (!event.getLevel().isClientSide) {
                    if (event.getPlayer() != null) {
                        event.getPlayer().gameEvent(GameEvent.ITEM_INTERACT_FINISH);
                    }
                    event.getLevel().levelEvent(2011, event.getPos(), 15);
                    Block.popResource(event.getLevel(), event.getPos(), event.getState().getBlock().getCloneItemStack(event.getState(), new BlockHitResult(event.getPos().getCenter(), Direction.DOWN, event.getPos(), false), event.getLevel(), event.getPos(), event.getPlayer()));
                    event.getStack().shrink(1);
                }
                event.setSuccessful(true);
            }
        }
    }

    @SubscribeEvent
    public static void chunkWatch(ChunkWatchEvent.Sent event) {
        PacketDistributor.sendToPlayersTrackingChunk(event.getLevel(), event.getPos(), new SyncSoundMufflerData(event.getChunk().getData(Utilitarian.SOUND_MUFFLER_BLOCK_LIST), event.getPos().getWorldPosition()));
    }

    @SubscribeEvent
    public static void onEntityDamaged(LivingDamageEvent.Post event) {
        if (event.getEntity().level() instanceof ServerLevel && event.getEntity() instanceof Villager villager && event.getSource().is(DamageTypes.FALLING_ANVIL)) {
            if (!villager.getVillagerData().getProfession().equals(VillagerProfession.NITWIT)) {
                villager.setVillagerData(villager.getVillagerData().setProfession(VillagerProfession.NITWIT));
                villager.releasePoi(MemoryModuleType.JOB_SITE);
            }
        }
    }

    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.EntityInteract event) {
        if (event.getItemStack().is(Utilitarian.NITWIT_CONVERT) && event.getTarget() instanceof Villager villager && villager.getVillagerData().getProfession().equals(VillagerProfession.NITWIT)) {
            villager.setVillagerData(villager.getVillagerData().setProfession(VillagerProfession.NONE));
        }
    }
}
