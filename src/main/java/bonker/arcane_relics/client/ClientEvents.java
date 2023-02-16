package bonker.arcane_relics.client;

import bonker.arcane_relics.ArcaneRelics;
import bonker.arcane_relics.client.particle.ARParticles;
import bonker.arcane_relics.client.particle.MagicDustParticle;
import bonker.arcane_relics.client.particle.MagicParticle;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid = ArcaneRelics.MODID, value = Dist.CLIENT)
    public static class ClientForgeEvents {

        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {

        }
    }

    @Mod.EventBusSubscriber(modid = ArcaneRelics.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
            event.register(ARParticles.MAGIC_DUST_PARTICLE.get(), MagicDustParticle.Provider::new);
            event.register(ARParticles.MAGIC_PARTICLE.get(), MagicParticle.Provider::new);
        }
    }
}
