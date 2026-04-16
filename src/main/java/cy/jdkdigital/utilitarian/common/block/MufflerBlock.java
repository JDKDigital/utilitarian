package cy.jdkdigital.utilitarian.common.block;

import cy.jdkdigital.utilitarian.Utilitarian;
import cy.jdkdigital.utilitarian.module.UtilityBlockModule;
import cy.jdkdigital.utilitarian.network.SyncMufflerData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;

public class MufflerBlock extends Block
{
    public MufflerBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (level instanceof ServerLevel serverLevel) {
            syncToClient(serverLevel, pos);
        }
    }

    private void syncToClient(ServerLevel serverLevel, BlockPos pos) {
        var chunk = serverLevel.getChunkAt(pos);
        var mufflerData = new ArrayList<>(chunk.getData(Utilitarian.MUFFLER_BLOCK_LIST));
        // clear invalid entries in the list
        mufflerData.removeIf(s -> !serverLevel.getBlockState(BlockPos.of(Long.parseLong(s))).is(UtilityBlockModule.MUFFLERS));
        // add new muffler position
        mufflerData.add(String.valueOf(pos.asLong()));
        chunk.setData(Utilitarian.MUFFLER_BLOCK_LIST, mufflerData);
        // sync to client
        PacketDistributor.sendToPlayersTrackingChunk(serverLevel, ChunkPos.containing(pos), new SyncMufflerData(mufflerData, pos));
    }
}
