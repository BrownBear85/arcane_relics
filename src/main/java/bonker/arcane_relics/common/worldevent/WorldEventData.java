package bonker.arcane_relics.common.worldevent;

import bonker.arcane_relics.ArcaneRelics;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

public class WorldEventData extends SavedData {

    public final ServerLevel level;

    public static final Object2ObjectArrayMap<ServerLevel, WorldEventData> MAP = new Object2ObjectArrayMap<>();

    public WorldEventData(ServerLevel level) {
        this.level = level;
    }

    public static WorldEventData getOrCreate(ServerLevel level) {
        if (MAP.containsKey(level)) {
            return MAP.get(level);
        }
        return level.getDataStorage().computeIfAbsent((compoundTag) -> load(level, compoundTag), () -> create(level), ArcaneRelics.MODID);
    }

    public static WorldEventData create(ServerLevel level) {
        return new WorldEventData(level);
    }

    public static WorldEventData load(ServerLevel level, CompoundTag compoundTag) {
        WorldEventData data = new WorldEventData(level);
        ListTag list = compoundTag.getList("events", 10);
        for (Tag tag : list) {
            WorldEvent.fromNBT(level, (CompoundTag) tag);
        }
        return data;
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag pCompoundTag) {
        ListTag list = new ListTag();
        for (WorldEvent event : WorldEvent.getEvents(level)) {
            list.add(event.save());
        }
        pCompoundTag.put("events", list);
        return pCompoundTag;
    }
}
