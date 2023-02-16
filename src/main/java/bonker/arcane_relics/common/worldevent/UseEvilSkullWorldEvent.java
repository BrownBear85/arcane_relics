package bonker.arcane_relics.common.worldevent;

import bonker.arcane_relics.client.particle.MagicDustParticleOptions;
import bonker.arcane_relics.client.particle.MagicParticleOptions;
import bonker.arcane_relics.common.Util;
import bonker.arcane_relics.common.item.ARItems;
import bonker.arcane_relics.common.recipe.ARRecipes;
import bonker.arcane_relics.common.recipe.EvilSkullRecipe;
import bonker.arcane_relics.common.sound.ARSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class UseEvilSkullWorldEvent extends WorldEvent {

    private static final int MAX_PROGRESS = 30;

    private final Player player;
    private List<Entity> suckingFrom = Collections.emptyList();
    @Nullable
    private Entity selectedEntity, previousEntity = null;
    private int charge, progress = 0;
    private EvilSkullRecipe recipe;

    public UseEvilSkullWorldEvent(ServerPlayer player) {
        super((ServerLevel) player.level, player.position(), 1200);
        this.player = player;
    }

    @Override
    protected void tick() {
        super.tick();

        if (!player.getUseItem().is(ARItems.EVIL_SKULL.get())) {
            end();
            return;
        }

        this.position = player.position();

        if (age % 10 == 0) {
            suckingFrom = Util.nearestEntities(position, level.getEntities(player, AABB.ofSize(position, 15, 15, 15), (e) -> e instanceof LivingEntity && e.isAlive()), 3);
            for (Entity entity : suckingFrom) {
                entity.hurt(DamageSource.indirectMagic(player, null), 0.75F);
                charge++;
            }
        }

        if (age % 12 == 0 && suckingFrom.size() > 0) {
            level.playSound(null, previousEntity == null ? player : previousEntity, ARSounds.EVIL_LOOP.get(), SoundSource.AMBIENT, recipe == null ? 0.3F : 1.0F, (charge / 2F + progress) / (float) MAX_PROGRESS * 2);
        }

        if (suckingFrom.size() > 0 && previousEntity != null && !previousEntity.isRemoved() && recipe != null) {
            Vec3 entityPos = previousEntity.position().add(0, 0.25, 0);
            Util.pointsOnLine(Util.relativeView(player.getEyePosition(), player.getRotationVector(), 0.6, -0.1, -0.4), entityPos, 10).forEach(point -> {
                MagicDustParticleOptions particleOptions = MagicDustParticleOptions.to(entityPos, MagicDustParticleOptions.SKULL_COLOR, 0.15F * suckingFrom.size());
                level.sendParticles(particleOptions, point.x, point.y, point.z, 1, 0, 0, 0, 0);
            });
        }

        for (Entity entity : suckingFrom) {
            Vec3 pos = entity.position().add(0, entity.getBbHeight() / 2, 0);
            level.sendParticles(MagicDustParticleOptions.toEntity(player, MagicDustParticleOptions.SKULL_COLOR, 0.75F), pos.x, pos.y, pos.z, 1, 0.2, 0.2, 0.2, 0);
        }

        Util.getTargetedEntity(player, ItemEntity.class, e -> true, 5, 0.5).ifPresentOrElse(entity -> {
            EvilSkullRecipe newRecipe = level.getRecipeManager().getRecipeFor(ARRecipes.RecipeTypes.EVIL_SKULL_TYPE.get(), new SimpleContainer(entity.getItem()), level).orElse(null);
            if (newRecipe != null) {
                recipe = newRecipe;
                selectedEntity = entity;
                if (selectedEntity != previousEntity) {
                    progress = 0;
                    previousEntity = entity;
                    recipe = level.getRecipeManager().getRecipeFor(ARRecipes.RecipeTypes.EVIL_SKULL_TYPE.get(), new SimpleContainer(((ItemEntity) previousEntity).getItem()), level).orElse(null);
                }
            }
        }, () -> selectedEntity = null);

        if (previousEntity != null && recipe != null && !previousEntity.isRemoved()) {
            if (charge >= 2) {
                charge -= 2;

                if (++progress >= MAX_PROGRESS) {
                    ItemStack stack = ((ItemEntity) previousEntity).getItem();
                    stack.shrink(1);
                    ((ItemEntity) previousEntity).setItem(stack);
                    Vec3 pos = new Vec3(previousEntity.getX(), previousEntity.getY(), previousEntity.getZ());
                    ItemEntity newItem = new ItemEntity(level, pos.x, pos.y, pos.z, recipe.output());
                    newItem.setPickUpDelay(40);
                    newItem.setDeltaMovement(new Vec3(0, 0, 0));
                    level.addFreshEntity(newItem);
                    level.sendParticles(ParticleTypes.FLASH, pos.x, pos.y, pos.z, 1, 0, 0, 0, 0);
                    level.playSound(null, newItem, ARSounds.EVIL_CAST.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                    end();
                }
            }
        } else {
            progress = 0;
        }

        if (progress > 0) {
            float percent = progress / (float) MAX_PROGRESS;
            Vec3 pos = previousEntity.position().add(0, 0.2, 0);
            level.sendParticles(MagicParticleOptions.create(MagicDustParticleOptions.SKULL_COLOR, 0.1F + 1.1F * percent), pos.x, pos.y, pos.z, 2, 0.05, 0.05, 0.05, 0.02);
        }
    }

    @Override
    public void end() {
        super.end();
        player.stopUsingItem();
    }

    @Override
    protected CompoundTag save() {
        return WorldEventData.NO_SAVE;
    }

    @Override
    protected String getId() {
        return "evil_skull";
    }

    @Nullable
    public static UseEvilSkullWorldEvent fromPlayer(Player player) {
        for (WorldEvent event : WorldEvent.getOfClass((ServerLevel) player.level, UseEvilSkullWorldEvent.class)) {
            if (event instanceof UseEvilSkullWorldEvent skullEvent && player.equals(skullEvent.player)) {
                return skullEvent;
            }
        }
        return null;
    }
}
