package bonker.arcane_relics.common.item.custom;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public interface ICastOnEntityItem {
    @Nullable
    default LivingEntity getTargetedEntity(Player player, double range, double assist) {
        for (double dist = 0; dist < range; dist += 0.2) {
            Vec3 vec3 = player.getEyePosition().add(player.getLookAngle().multiply(dist, dist, dist));
            LivingEntity target = player.level.getNearestEntity(LivingEntity.class, TargetingConditions.DEFAULT, null, vec3.x, vec3.y, vec3.z, AABB.ofSize(vec3, assist, assist, assist));
            if (target != null && target != player) {
                return target;
            }
        }
        return null;
    }
}
