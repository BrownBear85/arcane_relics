package bonker.arcane_relics.common;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class Util {
    public static ItemStack itemStack(Item item) {
        return itemStack(item, 1);
    }

    public static ItemStack itemStack(Item item, int count) {
        ItemStack stack = new ItemStack(item, count);
        stack.getOrCreateTag();
        return stack;
    }

    // stolen from net.minecraft.commands.arguments.coordinates.LocalCoordinates.java
    /** emulates ^ ^ ^ coordinates in commands */
    public static Vec3 relativeView(Vec3 pos, Vec2 rot, double forwards, double up, double left) {
        float f = Mth.cos((rot.y + 90.0F) * ((float)Math.PI / 180F));
        float f1 = Mth.sin((rot.y + 90.0F) * ((float)Math.PI / 180F));
        float f2 = Mth.cos(-rot.x * ((float)Math.PI / 180F));
        float f3 = Mth.sin(-rot.x * ((float)Math.PI / 180F));
        float f4 = Mth.cos((-rot.x + 90.0F) * ((float)Math.PI / 180F));
        float f5 = Mth.sin((-rot.x + 90.0F) * ((float)Math.PI / 180F));
        Vec3 vec31 = new Vec3(f * f2, f3, f1 * f2);
        Vec3 vec32 = new Vec3(f * f4, f5, f1 * f4);
        Vec3 vec33 = vec31.cross(vec32).scale(-1.0D);
        double d0 = vec31.x * forwards + vec32.x * up + vec33.x * left;
        double d1 = vec31.y * forwards + vec32.y * up + vec33.y * left;
        double d2 = vec31.z * forwards + vec32.z * up + vec33.z * left;
        return new Vec3(pos.x + d0, pos.y + d1, pos.z + d2);
    }

    public static Vec3 readVec3(CompoundTag tag, String key) {
        CompoundTag vec = tag.getCompound(key);
        double x = vec.getDouble("x");
        double y = vec.getDouble("y");
        double z = vec.getDouble("z");
        return new Vec3(x, y, z);
    }

    public static void putVec3(CompoundTag tag, String key, Vec3 data) {
        CompoundTag vec = new CompoundTag();
        vec.putDouble("x", data.x);
        vec.putDouble("y", data.y);
        vec.putDouble("z", data.z);
        tag.put(key, vec);
    }

    public static AABB chunkAABB(ChunkAccess chunk) {
        int x = chunk.getPos().x;
        int z = chunk.getPos().z;
        return new AABB(x, chunk.getMinBuildHeight(), z, x + 16, chunk.getMaxBuildHeight(), z + 16);
    }

    public static void writeInteractionHand(FriendlyByteBuf buf, InteractionHand hand) {
        buf.writeBoolean(hand == InteractionHand.MAIN_HAND);
    }

    public static InteractionHand readInteractionHand(FriendlyByteBuf buf) {
        return buf.getBoolean(0) ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
    }

    public static Vec2 pointOnCircle(float radius, float angleDegrees, float originX, float originY) {
        return new Vec2(originX + radius * (float) Math.cos(Math.toRadians(Mth.wrapDegrees(angleDegrees))), originY + radius * (float) Math.sin(Math.toRadians(Mth.wrapDegrees(angleDegrees))));
    }

    public static Vec2 pointOnCircle(double radius, double angleDegrees, double originX, double originY) {
        return new Vec2((float) (originX + radius * (float) Math.cos(Math.toRadians(Mth.wrapDegrees(angleDegrees)))), (float) (originY + radius * (float) Math.sin(Math.toRadians(Mth.wrapDegrees(angleDegrees)))));
    }
}