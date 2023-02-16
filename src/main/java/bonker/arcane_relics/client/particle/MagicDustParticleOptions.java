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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import org.joml.Vector3f;

import java.util.Locale;

public class MagicDustParticleOptions implements ParticleOptions {

    protected final Vec3 moveTo;
    protected final Vector3f color;
    protected final float scale;

    public static final Vector3f SKULL_COLOR = new Vector3f(0.15F, 0.0F, 0.0F);

    public static final Codec<MagicDustParticleOptions> CODEC = RecordCodecBuilder.create((inst) -> inst.group(
            Util.VEC3_CODEC.fieldOf("moveTo").forGetter((m -> m.moveTo)),
            ExtraCodecs.VECTOR3F.fieldOf("color").forGetter(m -> m.color),
            Codec.FLOAT.fieldOf("scale").forGetter(m -> m.scale)
    ).apply(inst, MagicDustParticleOptions::new));

    public static MagicDustParticleOptions to(Vec3 moveTo, Vector3f color, float scale) {
        return new MagicDustParticleOptions(moveTo, color, scale);
    }

    public static MagicDustParticleOptions toEntity(Entity moveTo, Vector3f color, float scale) {
        return new MagicDustParticleOptions(new Vec3(Double.MAX_VALUE, Double.MIN_VALUE, moveTo.getId()), color, scale);
    }

    protected MagicDustParticleOptions(Vec3 moveTo, Vector3f color, float scale) {
        this.moveTo = moveTo;
        this.color = color;
        this.scale = scale;
    }

    @Override
    public ParticleType<?> getType() {
        return ARParticles.MAGIC_DUST_PARTICLE.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf pBuffer) {
        pBuffer.writeDouble(moveTo.x);
        pBuffer.writeDouble(moveTo.y);
        pBuffer.writeDouble(moveTo.z);
        pBuffer.writeFloat(color.x);
        pBuffer.writeFloat(color.y);
        pBuffer.writeFloat(color.z);
        pBuffer.writeFloat(scale);
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %.2f %.2f %.2f", ForgeRegistries.PARTICLE_TYPES.getKey(getType()), moveTo.x(), moveTo.y(), moveTo.z(), color.x(), color.y(), color.z(), scale);
    }

    public static final ParticleOptions.Deserializer<MagicDustParticleOptions> DESERIALIZER = new ParticleOptions.Deserializer<>() {
        public MagicDustParticleOptions fromCommand(ParticleType<MagicDustParticleOptions> type, StringReader stringReader) throws CommandSyntaxException {
            stringReader.expect(' ');
            Vec3 moveTo = Util.readVec3(stringReader);
            stringReader.expect(' ');
            Vector3f color = Util.readVector3f(stringReader);
            stringReader.expect(' ');
            float scale = stringReader.readFloat();
            return new MagicDustParticleOptions(moveTo, color, scale);
        }

        public MagicDustParticleOptions fromNetwork(ParticleType<MagicDustParticleOptions> type, FriendlyByteBuf buf) {
            return new MagicDustParticleOptions(Util.readVec3(buf), Util.readVector3f(buf), buf.readFloat());
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
