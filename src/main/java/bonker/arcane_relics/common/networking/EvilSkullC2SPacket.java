package bonker.arcane_relics.common.networking;

import bonker.arcane_relics.common.worldevent.UseEvilSkullWorldEvent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class EvilSkullC2SPacket {

    private final boolean starting;

    public EvilSkullC2SPacket(boolean starting) {
        this.starting = starting;
    }

    public EvilSkullC2SPacket(FriendlyByteBuf buf) {
        this.starting = buf.readBoolean();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(starting);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        ServerPlayer player = contextSupplier.get().getSender();
        if (player != null) {
            if (starting) {
                new UseEvilSkullWorldEvent(player);
            } else {
                UseEvilSkullWorldEvent event = UseEvilSkullWorldEvent.fromPlayer(player);
                if (event != null) {
                    event.end();
                }
            }
        }
    }
}
