package bonker.arcane_relics.common.worldevent;

import bonker.arcane_relics.ArcaneRelics;
import bonker.arcane_relics.common.entity.goal.SummonedMonsterDespawnGoal;
import bonker.arcane_relics.common.sound.ARSounds;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingConversionEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = ArcaneRelics.MODID)
public class SummonUndeadWorldEvent extends WorldEvent {

    public static final String ID = "summon_undead";

    private static final Vector3f PARTICLE_COLOR = new Vector3f(0.1F, 0.1F, 0.1F);
    private static final DustParticleOptions PARTICLE_OPTIONS = new DustParticleOptions(PARTICLE_COLOR, 1.0F);
    private static final UUID NULL = new UUID(0, 0);

    private LivingEntity target;
    private Monster monster;

    private boolean setLate = false;
    private UUID targetUUID;
    private UUID monsterUUID;

    public static final String SUMMONED_TAG = "arcane_relics.summoned";

    @Override
    protected String getId() {
        return ID;
    }

    public SummonUndeadWorldEvent(ServerLevel level, Vec3 position, LivingEntity target) {
        super(level, position, 2000);

        level.playSound(null, position.x, position.y, position.z, ARSounds.RUMBLE.get(), SoundSource.HOSTILE, 0.4F, 1.0F);

        this.target = target;

        this.monster = (level.random.nextDouble() < 0.8 ? EntityType.ZOMBIE : EntityType.SKELETON)
                .spawn(level, null, this::addMonsterData, BlockPos.ZERO, MobSpawnType.COMMAND, false, false);
        if (this.monster == null) {
            fail();
        }
    }

    private void addMonsterData(Monster mob) {
        mob.setItemSlot(EquipmentSlot.HEAD, new ItemStack(mob.getType() == EntityType.ZOMBIE ? Items.LEATHER_HELMET : Items.CHAINMAIL_HELMET));
        mob.setDropChance(EquipmentSlot.HEAD, 0.0F);
        mob.setNoActionTime(20);
        mob.setCanPickUpLoot(false);
        mob.addTag(SUMMONED_TAG);
        mob.setPos(position.x, position.y - 2, position.z);
        mob.lookAt(EntityAnchorArgument.Anchor.EYES, target.position());
        mob.setInvulnerable(true);
        mob.setNoAi(true);
        mob.setSilent(true);
    }

    @Override
    protected void tick() {
        if (setLate) {
            this.target = (LivingEntity) level.getEntity(targetUUID);
            this.monster = (Monster) level.getEntity(monsterUUID);
            if (monster != null) {
                super.tick();
                if (target != null) {
                    setLate = false;
                }
            } else {
                return;
            }
        } else {
            super.tick();
        }

        if (age < 50) {
            if (monster != null) {
                monster.setPos(monster.position().add(0, 0.04, 0));
            }

            if (age % 5 == 0) {
                level.sendParticles(PARTICLE_OPTIONS, position.x, position.y, position.z, 25, 0.35, 0, 0.35, 0);
            }
        } else if (age == 50) {
            monster.goalSelector.addGoal(1, new SummonedMonsterDespawnGoal(monster));
            monster.setTarget(target);

            monster.setPos(position.x, position.y, position.z);
            monster.setInvulnerable(false);
            monster.setNoAi(false);
            monster.setSilent(false);

            monster.playAmbientSound();

            monster.setDeltaMovement(0, 0, 0);

            level.sendParticles(PARTICLE_OPTIONS, position.x, position.y + 1, position.z, 50, 0.7, 1, 0.7, 0);
        }

        if (monster != null && !monster.isAlive()) {
            end();
        }
    }

    @Override
    public void end() {
        super.end();

        if (monster != null) {
            monster.discard();
            level.playSound(null, monster.getX(), monster.getY(), monster.getZ(), ARSounds.MONSTER_DISAPPEAR.get(), SoundSource.HOSTILE, 1.0F, 1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.2F);
            level.sendParticles(PARTICLE_OPTIONS, monster.getX(), monster.getY() + 1, monster.getZ(), 50, 0.4, 1, 0.4, 0);
        }
    }

    @SubscribeEvent
    public static void entityJoinLevel(EntityJoinLevelEvent event) {
        if (event.getLevel() instanceof ServerLevel serverLevel &&
                event.loadedFromDisk() &&
                event.getEntity() instanceof Monster monster
                && monster.getTags().contains(SUMMONED_TAG)) {
            monster.goalSelector.addGoal(1, new SummonedMonsterDespawnGoal(monster));
            SummonUndeadWorldEvent worldEvent = fromMonster(serverLevel, monster);
            if (worldEvent != null) {
                if (worldEvent.target != null) {
                    monster.setTarget(worldEvent.target);
                    return;
                }
                if (worldEvent.targetUUID != null)
                    monster.setTarget((LivingEntity) serverLevel.getEntity(worldEvent.targetUUID));
            }
        }
    }

    @SubscribeEvent
    public static void entityConverts(LivingConversionEvent event) {
        if (event.getEntity().getTags().contains(SUMMONED_TAG)) {
            event.setCanceled(true);
            event.getEntity().discard();
        }
    }

    @SubscribeEvent
    public static void entityDrops(LivingDropsEvent event) {
        if (event.getEntity().getTags().contains(SUMMONED_TAG)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void entityDropsEXP(LivingExperienceDropEvent event) {
        if (event.getEntity().getTags().contains(SUMMONED_TAG)) {
            event.setCanceled(true);
        }
    }

    @Nullable
    private static SummonUndeadWorldEvent fromMonster(ServerLevel serverLevel, Monster monster) {
        for (WorldEvent event : WorldEvent.getEvents(serverLevel)) {
            if (event instanceof SummonUndeadWorldEvent summonEvent &&
                    (monster.equals(summonEvent.monster) || monster.getUUID().equals(summonEvent.monsterUUID))) {
                return summonEvent;
            }
        }
        return null;
    }

    SummonUndeadWorldEvent(ServerLevel serverLevel, CompoundTag tag) {
        super(serverLevel, tag);

        this.targetUUID = tag.getUUID("target");
        this.monsterUUID = tag.getUUID("monster");

        this.target = (LivingEntity) serverLevel.getEntity(targetUUID);
        this.monster = (Monster) serverLevel.getEntity(monsterUUID);

        if (target == null || monster == null) {
            setLate = true;
        }
    }

    @Override
    protected CompoundTag save() {
        if (monster == null) {
            return new CompoundTag();
        }
        CompoundTag tag = super.save();
        tag.putUUID("target", target == null ? NULL : target.getUUID());
        tag.putUUID("monster", monster.getUUID());
        return tag;
    }
}
