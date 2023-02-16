package bonker.arcane_relics.common.worldevent;

import bonker.arcane_relics.ArcaneRelics;
import bonker.arcane_relics.common.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class WorldEvent {

    protected final ServerLevel level;
    protected Vec3 position;
    protected BlockPos blockPos;
    protected final int lifetime;
    protected int age = 0;

    private static final ArrayList<WorldEvent> events = new ArrayList<>();

    protected abstract String getId();

    public WorldEvent(ServerLevel level, Vec3 position, int lifetime) {
        this.level = level;
        this.position = position;
        this.blockPos = new BlockPos(position);
        this.lifetime = lifetime;

        events.add(this);
    }

    WorldEvent(ServerLevel level, CompoundTag tag) {
        this(level, Util.readVec3(tag, "position"), tag.getInt("lifetime"));
        this.age = tag.getInt("age");
    }

    protected void tick() {
        if (lifetime > 0 && ++age >= lifetime) {
            end();
        }
    }

    protected CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        Util.putVec3(tag, "position", position);
        tag.putInt("age", age);
        tag.putInt("lifetime", lifetime);
        tag.putString("id", getId());
        return tag;
    }

    public void end() {
        events.remove(this);
    }

    public void fail() {
        events.remove(this);
    }

    public static List<WorldEvent> getEvents(ServerLevel level) {
        List<WorldEvent> list = new ArrayList<>();
        for (WorldEvent event : events) {
            if (event.level == level) {
                list.add(event);
            }
        }
        return list;
    }

    public static <T extends WorldEvent> List<T> getOfClass(ServerLevel level, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        for (WorldEvent event : events) {
            if (event.getClass() == clazz && event.level == level) {
                list.add(clazz.cast(event));
            }
        }
        return list;
    }

    public static void clearEvents(ServerLevel level) {
        for (WorldEvent event : getEvents(level)) {
            event.fail();
        }
    }

    static void fromNBT(ServerLevel level, CompoundTag tag) {
        WorldEventCreator creator = WORLD_EVENT_CREATORS.get(tag.getString("id"));
        if (creator != null) {
            creator.create(level, tag);
        }
    }

    @Mod.EventBusSubscriber(modid = ArcaneRelics.MODID)
    public static class Events {

        @SubscribeEvent
        public static void levelTick(TickEvent.LevelTickEvent event) {
            if (event.level instanceof ServerLevel serverLevel && event.phase == TickEvent.Phase.END) {
                for (WorldEvent worldEvent : getEvents(serverLevel)) {
                    if (serverLevel.isLoaded(worldEvent.blockPos)) {
                        worldEvent.tick();
                        WorldEventData.getOrCreate(serverLevel).setDirty();
                    }
                }
            }
        }

        @SubscribeEvent
        public static void levelLoaded(LevelEvent.Load event) {
            if (event.getLevel() instanceof ServerLevel serverLevel) {
                WorldEventData.getOrCreate(serverLevel);
            }
        }
    }

    private static final Map<String, WorldEventCreator> WORLD_EVENT_CREATORS = new HashMap<>();

    public static void loadWorldEventCreators() {
        WORLD_EVENT_CREATORS.put(SummonUndeadWorldEvent.ID, SummonUndeadWorldEvent::new);
        WORLD_EVENT_CREATORS.put(EvilSkullCraftingWorldEvent.ID, EvilSkullCraftingWorldEvent::new);
    }

    @FunctionalInterface
    public interface WorldEventCreator {
        WorldEvent create(ServerLevel serverLevel, CompoundTag tag);
    }
}


