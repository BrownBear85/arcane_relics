package bonker.arcane_relics.common;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.EntityGetter;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

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

    public static double distanceBetween(Vec3 start, Vec3 end) {
        return Math.sqrt(Math.pow(end.x - start.x, 2) + Math.pow(end.y - end.x, 2) + Math.pow(end.z - start.z, 2));
    }

    // from: https://math.stackexchange.com/questions/1735994/position-of-point-between-2-points-in-3d-space
    public static Vec3 pointBetween(Vec3 start, Vec3 end, double distance) {
        double s = distance / distanceBetween(start, end);
        return new Vec3(start.x + s * (end.x - start.x), start.y + s * (end.y - start.y), start.z + s * (end.z - start.z));
    }

    public static List<Vec3> pointsOnLine(Vec3 start, Vec3 end, int points) {
        ArrayList<Vec3> list = new ArrayList<>(points);

        double step = distanceBetween(start, end) / points;
        for (int i = 0; i < points; i++) {
            list.add(pointBetween(start, end, step * i));
        }
        return list;
    }

    public static Vector3f readVector3f(FriendlyByteBuf pBuffer) {
        return new Vector3f(pBuffer.readFloat(), pBuffer.readFloat(), pBuffer.readFloat());
    }

    public static Vec3 readVec3(FriendlyByteBuf pBuffer) {
        return new Vec3(pBuffer.readDouble(), pBuffer.readDouble(), pBuffer.readDouble());
    }

    public static Vector3f readVector3f(StringReader pReader) throws CommandSyntaxException {
        float f = pReader.readFloat();
        pReader.expect(' ');
        float f1 = pReader.readFloat();
        pReader.expect(' ');
        float f2 = pReader.readFloat();
        return new Vector3f(f, f1, f2);
    }

    public static Vec3 readVec3(StringReader pReader) throws CommandSyntaxException {
        double x = pReader.readDouble();
        pReader.expect(' ');
        double y = pReader.readDouble();
        pReader.expect(' ');
        double z = pReader.readDouble();
        return new Vec3(x, y, z);
    }

    public static final Codec<Vec3> VEC3_CODEC = Codec.DOUBLE.listOf().comapFlatMap(
            (list) -> net.minecraft.Util.fixedSize(list, 3).map(
                    (list1) -> new Vec3(list.get(0), list.get(1), list.get(2))),
            (vec3) -> ImmutableList.of(vec3.x(), vec3.y(), vec3.z()));

    public static <T extends Entity> List<T> nearestEntities(Vec3 pPos, List<T> pEntities, int limit) {
        BiConsumer<Vec3, List<? extends Entity>> order = EntitySelectorParser.ORDER_NEAREST;

        if (pEntities.size() > 1) {
            order.accept(pPos, pEntities);
        }

        return pEntities.subList(0, Math.min(limit, pEntities.size()));
    }

    public static Optional<Entity> getTargetedEntity(LivingEntity entity, double range, double assist) {
        for (double dist = 0; dist < range; dist += 0.2) {
            Vec3 vec3 = entity.getEyePosition().add(entity.getLookAngle().multiply(dist, dist, dist));
            Entity target = getNearestEntity(entity.level, Entity.class, e -> true, null, vec3.x, vec3.y, vec3.z, AABB.ofSize(vec3, assist, assist, assist));
            if (target != null && target != entity) {
                return Optional.of(target);
            }
        }
        return Optional.empty();
    }

    public static <T extends Entity> Optional<T> getTargetedEntity(LivingEntity entity, Class<? extends T> clazz, Predicate<Entity> predicate, double range, double assist) {
        for (double dist = 0; dist < range; dist += 0.2) {
            Vec3 vec3 = entity.getEyePosition().add(entity.getLookAngle().multiply(dist, dist, dist));
            T target = getNearestEntity(entity.level, clazz, predicate, null, vec3.x, vec3.y, vec3.z, AABB.ofSize(vec3, assist, assist, assist));
            if (target != null && target != entity) {
                return Optional.of(target);
            }
        }
        return Optional.empty();
    }

    // from EntityGetter.java, tweaked to allow for entity classes above LivingEntity
    // and replaced the TargetingConditions with an Entity Predicate
    @Nullable
    public static <T extends Entity> T getNearestEntity(EntityGetter getter, Class<? extends T> pEntityClazz, Predicate<Entity> pConditions, @Nullable LivingEntity pTarget, double pX, double pY, double pZ, AABB pBoundingBox) {
        return getNearestEntity(getter.getEntitiesOfClass(pEntityClazz, pBoundingBox, entity -> true), pConditions, pTarget, pX, pY, pZ);
    }

    // from EntityGetter.java, tweaked to allow for entity classes above LivingEntity
    // and replaced the TargetingConditions with an Entity Predicate
    @Nullable
    public static <T extends Entity> T getNearestEntity(List<? extends T> pEntities, Predicate<Entity> pPredicate, @Nullable LivingEntity pTarget, double pX, double pY, double pZ) {
        double d0 = -1.0D;
        T t = null;

        for(T t1 : pEntities) {
            if (t1 != pTarget && pPredicate.test(t1)) {
                double d1 = t1.distanceToSqr(pX, pY, pZ);
                if (d0 == -1.0D || d1 < d0) {
                    d0 = d1;
                    t = t1;
                }
            }
        }

        return t;
    }

    public static final Codec<ItemStack> ITEM_STACK_CODEC = RecordCodecBuilder.create((inst) -> inst.group(
            ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(ItemStack::getItem),
            Codec.INT.fieldOf("count").forGetter(ItemStack::getCount))
            .apply(inst, ItemStack::new));
}