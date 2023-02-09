package bonker.arcane_relics.client.particle;

import bonker.arcane_relics.ArcaneRelics;
import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ARParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, ArcaneRelics.MODID);

    public static final RegistryObject<ParticleType<EvilParticleOptions>> EVIL_PARTICLE = PARTICLE_TYPES.register("evil",
            () -> new ParticleType<>(false, EvilParticleOptions.DESERIALIZER) {
                @Override
                public Codec<EvilParticleOptions> codec() {
                    return EvilParticleOptions.CODEC;
                }
            });
}
