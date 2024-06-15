package cy.jdkdigital.utilitarian.module;

import com.mojang.serialization.Codec;
import cy.jdkdigital.utilitarian.Config;
import cy.jdkdigital.utilitarian.Utilitarian;
import cy.jdkdigital.utilitarian.common.block.NoSolicitingBanner;
import cy.jdkdigital.utilitarian.common.block.NoSolicitingWallBanner;
import cy.jdkdigital.utilitarian.common.block.SolicitingCarpet;
import cy.jdkdigital.utilitarian.common.block.entity.NoSolicitingBannerBlockEntity;
import cy.jdkdigital.utilitarian.common.item.RestrainingOrder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class NoSolicitingModule
{
    public static DeferredHolder<Block, Block> NO_SOLICITING_BANNER;
    public static DeferredHolder<Block, Block> NO_SOLICITING_WALL_BANNER;
    public static Map<DyeColor, DeferredHolder<Block, Block>> SOLICITING_CARPET = new HashMap<>();
    public static Map<DyeColor, DeferredHolder<Block, Block>> TRAPPED_SOLICITING_CARPET = new HashMap<>();
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<NoSolicitingBannerBlockEntity>> NO_SOLICITING_BANNER_BLOCK_ENTITY;
    public static DeferredHolder<Item, Item> NO_SOLICITING_BANNER_ITEM;
    public static Map<DyeColor, DeferredHolder<Item, Item>> SOLICITING_CARPET_ITEM = new HashMap<>();
    public static Map<DyeColor, DeferredHolder<Item, Item>> TRAPPED_SOLICITING_CARPET_ITEM = new HashMap<>();
    public static DeferredHolder<Item, Item> RESTRAINING_ORDER;
    public static DeferredHolder<PoiType, PoiType> NO_SOLICITING_POI;

    public static TagKey<EntityType<?>> ENTITY_BLACKLIST = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "traders"));
    public static TagKey<PoiType> NO_SOLICITING_POI_TAG = TagKey.create(Registries.POINT_OF_INTEREST_TYPE, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "no_soliciting"));
    public static TagKey<PoiType> SOLICITING_POI_TAG = TagKey.create(Registries.POINT_OF_INTEREST_TYPE, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "soliciting"));
    public static TagKey<Block> TRAPPED_SOLICITING_CARPETS = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "trapped_soliciting_carpets"));

    public static final Supplier<DataComponentType<Boolean>> ACTIVE = Utilitarian.DATA_COMPONENT_TYPES.register("active", () -> DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build());

    public static void register() {
        NO_SOLICITING_BANNER = Utilitarian.BLOCKS.register("no_soliciting_banner", () -> new NoSolicitingBanner(DyeColor.WHITE, BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_BANNER)));
        NO_SOLICITING_WALL_BANNER = Utilitarian.BLOCKS.register("no_soliciting_wall_banner", () -> new NoSolicitingWallBanner(DyeColor.WHITE, BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WALL_BANNER)));
        NO_SOLICITING_BANNER_BLOCK_ENTITY = Utilitarian.BLOCK_ENTITY.register("no_soliciting_banner", () -> BlockEntityType.Builder.of(NoSolicitingBannerBlockEntity::new, NO_SOLICITING_BANNER.get(), NO_SOLICITING_WALL_BANNER.get()).build(null));
        NO_SOLICITING_BANNER_ITEM = Utilitarian.ITEMS.register("no_soliciting_banner", () -> new BannerItem(NO_SOLICITING_BANNER.get(), NO_SOLICITING_WALL_BANNER.get(), new Item.Properties()));

        for (DyeColor color : DyeColor.values()) {
            SOLICITING_CARPET.put(color, Utilitarian.BLOCKS.register(color.getSerializedName() + "_soliciting_carpet", () -> new SolicitingCarpet(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_CARPET))));
            TRAPPED_SOLICITING_CARPET.put(color, Utilitarian.BLOCKS.register(color.getSerializedName() + "_trapped_soliciting_carpet", () -> new SolicitingCarpet(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_CARPET))));

            SOLICITING_CARPET_ITEM.put(color, Utilitarian.ITEMS.register(color.getSerializedName() + "_soliciting_carpet", () -> new BlockItem(SOLICITING_CARPET.get(color).get(), new Item.Properties())));
            TRAPPED_SOLICITING_CARPET_ITEM.put(color, Utilitarian.ITEMS.register(color.getSerializedName() + "_trapped_soliciting_carpet", () -> new BlockItem(TRAPPED_SOLICITING_CARPET.get(color).get(), new Item.Properties())));
        }

        RESTRAINING_ORDER = Utilitarian.ITEMS.register("restraining_order", () -> new RestrainingOrder(new Item.Properties().stacksTo(1)));

        NO_SOLICITING_POI = Utilitarian.POI_TYPES.register("no_soliciting", () -> {
            Set<BlockState> blockStates = new HashSet<>();
            blockStates.addAll(NO_SOLICITING_BANNER.get().getStateDefinition().getPossibleStates());
            blockStates.addAll(NO_SOLICITING_WALL_BANNER.get().getStateDefinition().getPossibleStates());
            return new PoiType(blockStates, 1, 1);
        });
        NO_SOLICITING_POI = Utilitarian.POI_TYPES.register("soliciting", () -> {
            Set<BlockState> blockStates = new HashSet<>();
            for (DyeColor color : DyeColor.values()) {
                blockStates.addAll(SOLICITING_CARPET.get(color).get().getStateDefinition().getPossibleStates());
                blockStates.addAll(TRAPPED_SOLICITING_CARPET.get(color).get().getStateDefinition().getPossibleStates());
            }
            return new PoiType(blockStates, 1, 1);
        });
    }

    public static int locateNearbyNoSoliciting(ServerLevel level, BlockPos spawnPosition) {
        PoiManager poiManager = level.getPoiManager();
        Stream<PoiRecord> stream = poiManager.getInRange((poi) -> poi.is(NoSolicitingModule.NO_SOLICITING_POI_TAG), spawnPosition, Config.NO_SOLICITING_BANNER_CHUNK_RANGE.get() * 16, PoiManager.Occupancy.ANY);
        var posList = stream.map(PoiRecord::getPos).filter(blockPos -> level.getBlockState(blockPos).is(NO_SOLICITING_BANNER.get()) || level.getBlockState(blockPos).is(NO_SOLICITING_WALL_BANNER.get())).toList();
        if (!posList.isEmpty()) {
            return posList.size();
        }
        var range = Config.NO_SOLICITING_BANNER_CHUNK_RANGE.get() * 16D;
        List<Player> players = level.getEntitiesOfClass(Player.class, (new AABB(new BlockPos(spawnPosition))).inflate(range, range, range)).stream().filter(player -> {
            for (ItemStack itemStack : player.getInventory().items) {
                if (RestrainingOrder.isEnabledRestrainingOrder(itemStack)) {
                    return true;
                }
            }
            return false;
        }).toList();
        return players.size();
    }

    public static List<BlockPos> locateNearbySoliciting(ServerLevel level, BlockPos spawnPosition) {
        PoiManager poiManager = level.getPoiManager();
        Stream<PoiRecord> stream = poiManager.getInRange((poi) -> poi.is(NoSolicitingModule.SOLICITING_POI_TAG), spawnPosition, Config.SOLICITING_CARPET_CHUNK_RANGE.get() * 16, PoiManager.Occupancy.ANY);
        return stream.map(PoiRecord::getPos)
                .sorted(Comparator.comparingDouble((vec) -> vec.distSqr(spawnPosition))).toList();
    }
}
