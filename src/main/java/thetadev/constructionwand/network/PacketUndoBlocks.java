package thetadev.constructionwand.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import thetadev.constructionwand.ConstructionWand;

import java.util.HashSet;
import java.util.Set;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;


public class PacketUndoBlocks implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<PacketUndoBlocks> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("constructionwand", "packet_undo_blocks"));

    public static final StreamCodec<FriendlyByteBuf, PacketUndoBlocks> STREAM_CODEC =
            StreamCodec.of((buf, message) -> PacketUndoBlocks.encode(message, buf), PacketUndoBlocks::decode);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public HashSet<BlockPos> undoBlocks;

    public PacketUndoBlocks(Set<BlockPos> undoBlocks) {
        this.undoBlocks = new HashSet<>(undoBlocks);
    }

    private PacketUndoBlocks(HashSet<BlockPos> undoBlocks) {
        this.undoBlocks = undoBlocks;
    }

    public static void encode(PacketUndoBlocks msg, FriendlyByteBuf buffer) {
        for(BlockPos pos : msg.undoBlocks) {
            buffer.writeBlockPos(pos);
        }
    }

    public static PacketUndoBlocks decode(FriendlyByteBuf buffer) {
        HashSet<BlockPos> undoBlocks = new HashSet<>();

        while(buffer.isReadable()) {
            undoBlocks.add(buffer.readBlockPos());
        }
        return new PacketUndoBlocks(undoBlocks);
    }

    public static class Handler
    {
        public static void handle(final PacketUndoBlocks msg, final IPayloadContext ctx) {
            ConstructionWand.instance.renderBlockPreview.undoBlocks = msg.undoBlocks;
}
    }
}
