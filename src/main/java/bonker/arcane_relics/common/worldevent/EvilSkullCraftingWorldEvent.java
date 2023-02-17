package bonker.arcane_relics.common.worldevent;

import bonker.arcane_relics.ArcaneRelics;
import bonker.arcane_relics.common.Util;
import bonker.arcane_relics.common.item.ARItems;
import bonker.arcane_relics.common.sound.ARSounds;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.*;

@Mod.EventBusSubscriber(modid = ArcaneRelics.MODID)
public class EvilSkullCraftingWorldEvent extends WorldEvent {

    public static final String ID = "evil_skull_crafting";

    private static final double RANGE = 1.5;
    private static final float SIZE = 0.75F;

    public static final Map<MobEffect, DustParticleOptions> WANTS = Maps.toMap(
            Set.of(MobEffects.HARM, MobEffects.POISON, MobEffects.WEAKNESS, MobEffects.MOVEMENT_SLOWDOWN),
            (effect) -> new DustParticleOptions(Vec3.fromRGB24(effect.getColor()).toVector3f(), SIZE));

    @Nullable
    private ItemEntity skull;
    @Nullable
    private UUID skullUUID;
    private MobEffect want;
    private int wantsSatisfied;
    private double radius = RANGE / 2;
    private int endTime = -1;

    public EvilSkullCraftingWorldEvent(ServerLevel level, ItemEntity skull) {
        super(level, skull.position(), -1);
        this.skull = skull;
        skull.setUnlimitedLifetime();
        skull.setNeverPickUp();
        this.wantsSatisfied = 0;
        this.want = randomWant();

        level.playSound(null, position.x, position.y, position.z, ARSounds.EVIL_WHOOSH.get(), SoundSource.MASTER, 1.0F, 1.0F);
    }

    @Override
    protected void tick() {
        super.tick();
        if (skull == null) {
            if (skullUUID != null) {
                skull = (ItemEntity) level.getEntity(skullUUID);
            }
        }
        if (skull != null && !skull.isRemoved()) {
            position = skull.position();
            blockPos = new BlockPos(position);
            if (level.getBlockState(blockPos).is(Blocks.FIRE)) {
                level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
                skull.extinguishFire();
            }
            if (endTime > 0 && --endTime == 0) {
                end();
            } else if (age % 5 == 0) {
                for (double d = 0.0; d < 360; d += 360.0 / 35) {
                    Vec2 point = Util.pointOnCircle(radius, d, position.x, position.z);
                    level.sendParticles(WANTS.get(want), point.x, position.y, point.y, 1, 0, 0, 0, 0);
                }
                radius -= RANGE / 2 / 55;
                if (radius <= 0) {
                    fail();
                }
            }
        } else {
            fail();
        }
    }

    private boolean handleThrownPotion(ThrownPotion thrownPotion) {
        List<MobEffectInstance> effects = PotionUtils.getMobEffects(thrownPotion.getItem());
        for (MobEffectInstance effect : effects) {
            if (effect.getEffect() == want) {
                wantsSatisfied++;
                radius = RANGE / 2;
                if (wantsSatisfied > 3 && level.random.nextBoolean()) {
                    end();
                    level.playSound(null, position.x, position.y, position.z, ARSounds.SPOOKY_BREATH.get(), SoundSource.MASTER, 1.0F, 1.0F);
                } else {
                    level.playSound(null, position.x, position.y, position.z, ARSounds.EVIL_WHOOSH.get(), SoundSource.MASTER, 1.0F, 1.0F);
                    want = randomWant();
                }
            } else {
                fail();
            }
        }
        return true;
    }

    @Override
    public void end() {
        if (skull != null) {
            ItemStack stack = skull.getItem();
            stack.shrink(1);
            skull.setItem(stack);
            skull.setNoPickUpDelay();
            skull.setExtendedLifetime();
        }
        EntityType.ITEM.spawn(level, null, (entity) -> {
            entity.setItem(new ItemStack(ARItems.EVIL_SKULL.get()));
            entity.setPos(position);
            entity.addDeltaMovement(new Vec3(0, 0.3, 0));
        }, BlockPos.ZERO, MobSpawnType.COMMAND, false, false);
        super.end();
    }

    @Override
    public void fail() {
        if (skull != null) {
            ItemStack stack = skull.getItem();
            stack.shrink(1);
            skull.setItem(stack);
            skull.setNoPickUpDelay();
            skull.setExtendedLifetime();
        }
        level.playSound(null, position.x, position.y, position.z, ARSounds.EVIL_IMPACT.get(), SoundSource.MASTER, 1.0F, 1.0F);
        super.fail();
    }

    private MobEffect randomWant() {
        List<MobEffect> list = new ArrayList<>(WANTS.keySet());
        return list.get(level.random.nextInt(list.size()));
    }

    @Override
    protected CompoundTag save() {
        CompoundTag tag = super.save();
        if (skull != null) {
            tag.putUUID("skull", skull.getUUID());
        }
        return tag;
    }

    EvilSkullCraftingWorldEvent(ServerLevel serverLevel, CompoundTag compoundTag) {
        super(serverLevel, compoundTag);

        skullUUID = compoundTag.getUUID("skull");
        skull = (ItemEntity) serverLevel.getEntity(skullUUID);
    }

    @Override
    protected String getId() {
        return ID;
    }

    @Nullable
    public static EvilSkullCraftingWorldEvent fromItemEntity(ServerLevel serverLevel, ItemEntity itemEntity) {
        for (WorldEvent event : WorldEvent.getEvents(serverLevel)) {
            if (event instanceof EvilSkullCraftingWorldEvent skullEvent &&
                    (itemEntity.equals(skullEvent.skull) || itemEntity.getUUID().equals(skullEvent.skullUUID))) {
                return skullEvent;
            }
        }
        return null;
    }

    @SubscribeEvent
    public static void projectileImpact(ProjectileImpactEvent event) {
        if (event.getProjectile() instanceof ThrownPotion thrownPotion && thrownPotion.level instanceof ServerLevel serverLevel) {
            for (EvilSkullCraftingWorldEvent worldEvent : WorldEvent.getOfClass(serverLevel, EvilSkullCraftingWorldEvent.class)) {
                if (worldEvent.position.distanceTo(thrownPotion.position()) <= RANGE && worldEvent.handleThrownPotion(thrownPotion)) {
                    return;
                }
            }
        }
    }
}
