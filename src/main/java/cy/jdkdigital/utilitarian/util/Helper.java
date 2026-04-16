package cy.jdkdigital.utilitarian.util;

import cy.jdkdigital.utilitarian.Config;
import cy.jdkdigital.utilitarian.common.item.RestrainingOrder;
import cy.jdkdigital.utilitarian.integration.CuriosIntegration;
import cy.jdkdigital.utilitarian.module.NoSolicitingModule;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.neoforged.fml.ModList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class Helper
{
    public static int locateNearbyNoSoliciting(ServerLevel level, BlockPos spawnPosition) {
        PoiManager poiManager = level.getPoiManager();
        Stream<PoiRecord> stream = poiManager.getInRange((poi) -> poi.is(NoSolicitingModule.NO_SOLICITING_POI_TAG), spawnPosition, Config.NO_SOLICITING_BANNER_CHUNK_RANGE.get() * 16, PoiManager.Occupancy.ANY);
        var posList = stream.map(PoiRecord::getPos).filter(blockPos -> level.getBlockState(blockPos).is(NoSolicitingModule.NO_SOLICITING_BANNER.get()) || level.getBlockState(blockPos).is(NoSolicitingModule.NO_SOLICITING_WALL_BANNER.get())).toList();
        if (!posList.isEmpty()) {
            return posList.size();
        }
        var range = Config.NO_SOLICITING_PLAYER_CHUNK_RANGE.get() * 16D;
        List<Player> players = level.getEntitiesOfClass(Player.class, (new AABB(new BlockPos(spawnPosition))).inflate(range, range, range)).stream().filter(player -> {
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) { ItemStack itemStack = player.getInventory().getItem(i);
                if (RestrainingOrder.isEnabledRestrainingOrder(itemStack)) {
                    return true;
                }
            }
            if (ModList.get().isLoaded("curios")) {
                return CuriosIntegration.hasRestrainingOrder(player);
            }
            return false;
        }).toList();
        return players.size();
    }

    public static List<BlockPos> locateNearbySoliciting(ServerLevel level, BlockPos sourcePos) {
        if (!level.isLoaded(sourcePos)) return new ArrayList<>();
        PoiManager poiManager = level.getPoiManager();
        Stream<PoiRecord> stream = poiManager.getInRange((poi) -> poi.is(NoSolicitingModule.SOLICITING_POI_TAG), sourcePos, Config.SOLICITING_CARPET_CHUNK_RANGE.get() * 16, PoiManager.Occupancy.ANY);
        return stream.map(PoiRecord::getPos)
                .sorted(Comparator.comparingDouble((vec) -> vec.distSqr(sourcePos))).toList();
    }

    public static List<BlockPos> locateNearbySoundMuffler(ServerLevel level, BlockPos sourcePos, TagKey<PoiType> poiTag) {
        if (!level.isLoaded(sourcePos)) return new ArrayList<>();
        PoiManager poiManager = level.getPoiManager();
        Stream<PoiRecord> stream = poiManager.getInRange((poi) -> poi.is(poiTag), sourcePos, Config.SOUND_MUFFLER_BLOCK_RANGE.get(), PoiManager.Occupancy.ANY);
        return stream.map(PoiRecord::getPos)
                .sorted(Comparator.comparingDouble((vec) -> vec.distSqr(sourcePos))).toList();
    }

    public static List<BlockPos> locateNearbyNoRaider(ServerLevel level, BlockPos sourcePos) {
        if (!level.isLoaded(sourcePos)) return new ArrayList<>();
        PoiManager poiManager = level.getPoiManager();
        Stream<PoiRecord> stream = poiManager.getInRange((poi) -> poi.is(NoSolicitingModule.NO_RAIDER_POI), sourcePos, 200, PoiManager.Occupancy.ANY);
        return stream.map(PoiRecord::getPos)
                .sorted(Comparator.comparingDouble((vec) -> vec.distSqr(sourcePos))).toList();
    }
}
