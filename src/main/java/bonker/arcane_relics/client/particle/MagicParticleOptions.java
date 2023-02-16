package bonker.arcane_relics.client.particle;

import bonker.arcane_relics.common.Util;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraftforge.registries.ForgeRegistries;
import org.joml.Vector3f;

import java.util.Locale;

public class MagicParticleOptions implements ParticleOptions {

    protected final Vector3f color;
    protected final float scale;

    public MagicParticleOptions(Vector3f color, float scale) {
        this.color = color;
        this.scale = scale;
    }

    public static MagicParticleOptions create(Vector3f color, float scale) {
        return new MagicParticleOptions(color, scale);
    }

    public static final Codec<MagicParticleOptions> CODEC = RecordCodecBuilder.create((inst) -> inst.group(
            ExtraCodecs.VECTOR3F.fieldOf("color").forGetter(m -> m.color),
            Codec.FLOAT.fieldOf("scale").forGetter(m -> m.scale)
    ).apply(inst, MagicParticleOptions::new));

    public static final ParticleOptions.Deserializer<MagicParticleOptions> DESERIALIZER = new ParticleOptions.Deserializer<>() {
        public MagicParticleOptions fromCommand(ParticleType<MagicParticleOptions> type, StringReader stringReader) throws CommandSyntaxException {
            stringReader.expect(' ');
            Vector3f color = Util.readVector3f(stringReader);
            stringReader.expect(' ');
            float scale = stringReader.readFloat();
            return new MagicParticleOptions(color, scale);
        }

        public MagicParticleOptions fromNetwork(ParticleType<MagicParticleOptions> type, FriendlyByteBuf buf) {
            return new MagicParticleOptions(Util.readVector3f(buf), buf.readFloat());
        }
    };

    @Override
    public void writeToNetwork(FriendlyByteBuf pBuffer) {
        pBuffer.writeFloat(color.x);
        pBuffer.writeFloat(color.y);
        pBuffer.writeFloat(color.z);
        pBuffer.writeFloat(scale);
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f", ForgeRegistries.PARTICLE_TYPES.getKey(getType()), color.x(), color.y(), color.z(), scale);
    }

    @Override
    public ParticleType<?> getType() {
        return ARParticles.MAGIC_PARTICLE.get();
    }

    public Vector3f getColor() {
        return color;
    }

    public float getScale() {
        return scale;
    }
}
