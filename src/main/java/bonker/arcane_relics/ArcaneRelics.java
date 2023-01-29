package bonker.arcane_relics;

import bonker.arcane_relics.common.item.ARItems;
import bonker.arcane_relics.common.loot.ARGlobalLootModifiers;
import bonker.arcane_relics.common.sound.ARSounds;
import bonker.arcane_relics.common.worldevent.WorldEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ArcaneRelics.MODID)
public class ArcaneRelics {

    public static final String MODID = "arcane_relics";

    public ArcaneRelics() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        ARItems.ITEMS.register(bus);

        ARSounds.SOUND_EVENTS.register(bus);

        ARGlobalLootModifiers.GLOBAL_LOOT_MODIFIER_SERIALIZERS.register(bus);

        WorldEvent.loadWorldEventCreators();
    }
}
