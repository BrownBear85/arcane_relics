package bonker.arcane_relics.common.networking;

import bonker.arcane_relics.ArcaneRelics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ARNetworking {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        INSTANCE = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(ArcaneRelics.MODID, "packets"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE.messageBuilder(UndeadSwordC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(UndeadSwordC2SPacket::new)
                .encoder(UndeadSwordC2SPacket::encode)
                .consumerMainThread(UndeadSwordC2SPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }
}
