package bonker.arcane_relics.common.networking;

import bonker.arcane_relics.common.Util;
import bonker.arcane_relics.common.item.ARItems;
import bonker.arcane_relics.common.sound.ARSounds;
import bonker.arcane_relics.common.worldevent.SummonUndeadWorldEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.function.Supplier;

public class UndeadSwordC2SPacket { // TODO: validate packet to make sure they can cast legitimately

    private final int targetID;
    private final InteractionHand hand;

    public UndeadSwordC2SPacket(LivingEntity target, InteractionHand hand) {
        this.targetID = target.getId();
        this.hand = hand;
    }

    public UndeadSwordC2SPacket(FriendlyByteBuf friendlyByteBuf) {
        this.targetID = friendlyByteBuf.readInt();
        this.hand = Util.readInteractionHand(friendlyByteBuf);
    }

    public void encode(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeInt(targetID);
        Util.writeInteractionHand(friendlyByteBuf, hand);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        contextSupplier.get().enqueueWork(() -> {

            ServerPlayer player = context.getSender();
            if (player == null) {
                return;
            }

            ServerLevel level = player.getLevel();

            if (level.getEntity(targetID) instanceof LivingEntity target) {
                ArrayList<Vec3> positions = new ArrayList<>(); // empty list of positions to spawn monsters at
                for (int x = -1; x <= 1; x++) { // make 3, each one is one block apart
                    Vec3 pos0 = Util.relativeView(player.position(), player.getRotationVector(), 2.5, 0, x * 1.2); // get spawn position based on player look angle
                    Vec3 pos1 = new Vec3(pos0.x, player.getY(), pos0.z); // spawn at player's feet height
                    boolean foundValidSpawn = false;
                    for (int y = -1; y <= 1; y++) { // check 3 places if they are valid spawn positions (1 block down, player feet level, 1 block up)
                        Vec3 pos2 = pos1.add(0, y, 0);
                        if (!level.getBlockState(new BlockPos(pos2)).getMaterial().isSolid() && // block is valid
                                level.getBlockState(new BlockPos(pos2).below()).getMaterial().isSolid() && // block to stand on / come out of
                                !level.getBlockState(new BlockPos(pos2).above()).getMaterial().isSolid()) // head space is empty
                        {
                            positions.add(pos2); // if its valid, add it to the list and stop searching
                            foundValidSpawn = true;
                            break;
                        }
                    }
                    if (!foundValidSpawn) { // fail if it couldn't find a valid spawn position at that location
                        player.sendSystemMessage(Component.translatable("item.arcane_relics.undead_sword.fail").withStyle(ChatFormatting.RED));
                        return;
                    }
                }
                level.playSound(null, player.blockPosition(), ARSounds.UNDEAD_CAST.get(), SoundSource.PLAYERS, 2.0F, 1.0F);
                for (Vec3 pos : positions) { // spawn monsters
                    new SummonUndeadWorldEvent(level, new Vec3(pos.x, Math.floor(pos.y), pos.z), target);
                }
                player.getCooldowns().addCooldown(ARItems.UNDEAD_SWORD.get(), 40);
                if (!player.getAbilities().instabuild)
                    ARItems.UNDEAD_SWORD.get().subtractProgress(player.getItemInHand(hand), 6);
            }
        });
    }
}
