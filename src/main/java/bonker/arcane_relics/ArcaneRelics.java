package bonker.arcane_relics;

import bonker.arcane_relics.client.particle.ARParticles;
import bonker.arcane_relics.common.item.ARItems;
import bonker.arcane_relics.common.loot.ARGlobalLootModifiers;
import bonker.arcane_relics.common.recipe.ARRecipes;
import bonker.arcane_relics.common.sound.ARSounds;
import bonker.arcane_relics.common.worldevent.WorldEvent;
import com.mojang.logging.LogUtils;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(ArcaneRelics.MODID)
public class ArcaneRelics {

    public static final String MODID = "arcane_relics";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ArcaneRelics() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        ARItems.ITEMS.register(bus);

        ARSounds.SOUND_EVENTS.register(bus);
        ARParticles.PARTICLE_TYPES.register(bus);

        ARGlobalLootModifiers.GLOBAL_LOOT_MODIFIER_SERIALIZERS.register(bus);

        ARRecipes.RecipeTypes.RECIPE_TYPES.register(bus);
        ARRecipes.RecipeSerializers.RECIPE_SERIALIZERS.register(bus);

        WorldEvent.loadWorldEventCreators();
    }
}
