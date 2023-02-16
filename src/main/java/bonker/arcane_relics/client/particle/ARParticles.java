package bonker.arcane_relics.client.particle;

import bonker.arcane_relics.ArcaneRelics;
import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ARParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, ArcaneRelics.MODID);

    public static final RegistryObject<ParticleType<MagicDustParticleOptions>> MAGIC_DUST_PARTICLE = PARTICLE_TYPES.register("magic_dust",
            () -> new ParticleType<>(false, MagicDustParticleOptions.DESERIALIZER) {
                @Override
                public Codec<MagicDustParticleOptions> codec() {
                    return MagicDustParticleOptions.CODEC;
                }
            });

    public static final RegistryObject<ParticleType<MagicParticleOptions>> MAGIC_PARTICLE = PARTICLE_TYPES.register("magic",
            () -> new ParticleType<MagicParticleOptions>(false, MagicParticleOptions.DESERIALIZER) {
                @Override
                public Codec<MagicParticleOptions> codec() {
                    return MagicParticleOptions.CODEC;
                }
            });
}
