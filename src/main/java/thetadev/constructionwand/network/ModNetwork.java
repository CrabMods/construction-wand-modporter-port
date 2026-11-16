package thetadev.constructionwand.network;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class ModNetwork {

    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("constructionwand").versioned("1");

        registrar.playToClient(
                PacketUndoBlocks.TYPE,
                PacketUndoBlocks.STREAM_CODEC,
                (payload, context) -> PacketUndoBlocks.Handler.handle(payload, context)
        );

        registrar.playToServer(
                PacketQueryUndo.TYPE,
                PacketQueryUndo.STREAM_CODEC,
                (payload, context) -> PacketQueryUndo.Handler.handle(payload, context)
        );

        registrar.playToServer(
                PacketWandOption.TYPE,
                PacketWandOption.STREAM_CODEC,
                (payload, context) -> PacketWandOption.Handler.handle(payload, context)
        );
    }
}
