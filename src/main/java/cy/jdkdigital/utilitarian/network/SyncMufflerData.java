package cy.jdkdigital.utilitarian.network;

import com.mojang.serialization.Codec;
import cy.jdkdigital.utilitarian.Utilitarian;
import cy.jdkdigital.utilitarian.event.EventHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public record SyncMufflerData(List<String> data, BlockPos chunkPos) implements CustomPacketPayload
{
    public static final Type<SyncMufflerData> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Utilitarian.MODID, "sync_muffler_data"));

    public static final StreamCodec<ByteBuf, SyncMufflerData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodec(Codec.STRING.listOf()),
            SyncMufflerData::data,
            ByteBufCodecs.fromCodec(BlockPos.CODEC),
            SyncMufflerData::chunkPos,
            SyncMufflerData::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void clientHandle(final SyncMufflerData data, final IPayloadContext context) {
        EventHandler.CLIENT_MUFFLER_LIST.addAll(data.data);
    }

    public static void serverHandle(final SyncMufflerData data, final IPayloadContext context) {
    }
}
