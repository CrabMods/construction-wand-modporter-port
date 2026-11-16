package thetadev.constructionwand.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import thetadev.constructionwand.basics.WandUtil;
import thetadev.constructionwand.basics.option.IOption;
import thetadev.constructionwand.basics.option.WandOptions;
import thetadev.constructionwand.items.wand.ItemWand;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;


public class PacketWandOption implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<PacketWandOption> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("constructionwand", "packet_wand_option"));

    public static final StreamCodec<FriendlyByteBuf, PacketWandOption> STREAM_CODEC =
            StreamCodec.of((buf, message) -> PacketWandOption.encode(message, buf), PacketWandOption::decode);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public final String key;
    public final String value;
    public final boolean notify;

    public PacketWandOption(IOption<?> option, boolean notify) {
        this(option.getKey(), option.getValueString(), notify);
    }

    private PacketWandOption(String key, String value, boolean notify) {
        this.key = key;
        this.value = value;
        this.notify = notify;
    }

    public static void encode(PacketWandOption msg, FriendlyByteBuf buffer) {
        buffer.writeUtf(msg.key);
        buffer.writeUtf(msg.value);
        buffer.writeBoolean(msg.notify);
    }

    public static PacketWandOption decode(FriendlyByteBuf buffer) {
        return new PacketWandOption(buffer.readUtf(100), buffer.readUtf(100), buffer.readBoolean());
    }

    public static class Handler
    {
        public static void handle(final PacketWandOption msg, final IPayloadContext ctx) {
            ServerPlayer player = (ServerPlayer) ctx.player();
            if(player == null) return;

            ItemStack wand = WandUtil.holdingWand(player);
            if(wand == null) return;
            WandOptions options = new WandOptions(wand);

            IOption<?> option = options.get(msg.key);
            if(option == null) return;
            option.setValueString(msg.value);

            if(msg.notify) ItemWand.optionMessage(player, option);
            player.getInventory().setChanged();
        }
    }
}
