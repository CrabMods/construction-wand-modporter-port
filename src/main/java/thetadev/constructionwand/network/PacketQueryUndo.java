package thetadev.constructionwand.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import thetadev.constructionwand.ConstructionWand;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;


public class PacketQueryUndo implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<PacketQueryUndo> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("constructionwand", "packet_query_undo"));

    public static final StreamCodec<FriendlyByteBuf, PacketQueryUndo> STREAM_CODEC =
            StreamCodec.of((buf, message) -> PacketQueryUndo.encode(message, buf), PacketQueryUndo::decode);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public boolean undoPressed;

    public PacketQueryUndo(boolean undoPressed) {
        this.undoPressed = undoPressed;
    }

    public static void encode(PacketQueryUndo msg, FriendlyByteBuf buffer) {
        buffer.writeBoolean(msg.undoPressed);
    }

    public static PacketQueryUndo decode(FriendlyByteBuf buffer) {
        return new PacketQueryUndo(buffer.readBoolean());
    }

    public static class Handler
    {
        public static void handle(final PacketQueryUndo msg, final IPayloadContext ctx) {
            ServerPlayer player = (ServerPlayer) ctx.player();
            if(player == null) return;

            ConstructionWand.instance.undoHistory.updateClient(player, msg.undoPressed);

            //ConstructionWand.LOGGER.debug("Undo queried");
        }
    }
}
