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
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.Locale;

public class EvilParticleOptions implements ParticleOptions {
    @Nullable
    private final Vec3 moveTo;
    private final Vector3f color;
    private final float scale;

    public static final Codec<EvilParticleOptions> CODEC = RecordCodecBuilder.create((inst) -> inst.group(
            Util.VEC3_CODEC.fieldOf("moveTo").forGetter((m -> m.moveTo)),
            ExtraCodecs.VECTOR3F.fieldOf("color").forGetter(m -> m.color),
            Codec.FLOAT.fieldOf("scale").forGetter(m -> m.scale)
    ).apply(inst, EvilParticleOptions::new));

    public EvilParticleOptions(@Nullable Vec3 moveTo, Vector3f color, float scale) {
        this.moveTo = moveTo;
        this.color = color;
        this.scale = scale;
    }

    @Override
    public ParticleType<?> getType() {
        return ARParticles.EVIL_PARTICLE.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf pBuffer) {
        if (moveTo == null) {
            pBuffer.writeDouble(0);
            pBuffer.writeDouble(0);
            pBuffer.writeDouble(0);
        } else {
            pBuffer.writeDouble(moveTo.x);
            pBuffer.writeDouble(moveTo.y);
            pBuffer.writeDouble(moveTo.z);
        }
        pBuffer.writeFloat(color.x);
        pBuffer.writeFloat(color.y);
        pBuffer.writeFloat(color.z);
        pBuffer.writeFloat(scale);
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %.2f %.2f %.2f", ForgeRegistries.PARTICLE_TYPES.getKey(this.getType()), this.moveTo == null ? 0 : this.moveTo.x(), this.moveTo == null ? 0 : this.moveTo.y(), this.moveTo == null ? 0 : this.moveTo.z(), this.color.x(), this.color.y(), this.color.z(), this.scale);
    }

    public static final ParticleOptions.Deserializer<EvilParticleOptions> DESERIALIZER = new ParticleOptions.Deserializer<>() {
        public EvilParticleOptions fromCommand(ParticleType<EvilParticleOptions> type, StringReader stringReader) throws CommandSyntaxException {
            Vec3 moveTo = Util.readVec3(stringReader);
            Vector3f color = Util.readVector3f(stringReader);
            stringReader.expect(' ');
            float scale = stringReader.readFloat();
            return new EvilParticleOptions(moveTo, color, scale);
        }

        public EvilParticleOptions fromNetwork(ParticleType<EvilParticleOptions> type, FriendlyByteBuf buf) {
            return new EvilParticleOptions(Util.readVec3(buf), Util.readVector3f(buf), buf.readFloat());
        }
    };

    public Vec3 getDest() {
        return moveTo;
    }

    public Vector3f getColor() {
        return color;
    }

    public float getScale() {
        return scale;
    }
}
