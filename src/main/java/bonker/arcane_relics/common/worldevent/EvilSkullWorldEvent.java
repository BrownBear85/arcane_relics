package bonker.arcane_relics.common.worldevent;

import bonker.arcane_relics.common.Util;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Vector3f;

import java.util.List;

public class EvilSkullWorldEvent extends WorldEvent {

    private static final Vector3f PARTICLE_COLOR = new Vector3f(0.25F, 0.1F, 0.1F);
    private static final DustParticleOptions PARTICLE_OPTIONS = new DustParticleOptions(PARTICLE_COLOR, 1.0F);

    public EvilSkullWorldEvent(ServerLevel level, Vec3 position) {
        super(level, position, 100);
    }

    EvilSkullWorldEvent(ServerLevel serverLevel, CompoundTag compoundTag) {
        super(serverLevel, compoundTag);
    }

    @Override
    protected void tick() {
        super.tick();
        if (age % 5 == 0) {
            for (double d = 0.0; d < 360; d += 360.0 / 35) {
                Vec2 point = Util.pointOnCircle(0.75, d, position.x, position.z);
                level.sendParticles(PARTICLE_OPTIONS, point.x, position.y, point.y, 1, 0, 0, 0, 0);
            }
        }
    }

    @Override
    protected String getId() {
        return "evil_skull";
    }

    @SubscribeEvent
    public static void projectileImpact(ProjectileImpactEvent event) {
        if (event.getProjectile() instanceof ThrownPotion thrownPotion) {

        }
    }

    private boolean handleThrownPotion(ThrownPotion thrownPotion) {
        List<MobEffectInstance> effects = PotionUtils.getMobEffects(thrownPotion.getItem());
        for (MobEffectInstance effect : effects) {
            if (effect.getEffect() == MobEffects.HARM) {

            }
        }
        return true;
    }
}
