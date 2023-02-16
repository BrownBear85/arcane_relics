package bonker.arcane_relics.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.particle.ParticleProvider;

public class MagicParticle extends TextureSheetParticle {

    private final SpriteSet sprites;

    protected MagicParticle(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, MagicParticleOptions pOptions, SpriteSet pSprites) {
        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);

        this.xd = pXSpeed;
        this.yd = pYSpeed;
        this.zd = pZSpeed;

        float colorRand = this.random.nextFloat() * 0.4F + 0.6F;
        this.rCol = this.randomizeColor(pOptions.getColor().x, colorRand);
        this.gCol = this.randomizeColor(pOptions.getColor().y, colorRand);
        this.bCol = this.randomizeColor(pOptions.getColor().z, colorRand);

        this.lifetime = 60 + (int) Math.round((pLevel.random.nextDouble() * 10));
        this.scale(pOptions.getScale());

        this.sprites = pSprites;
        setSprite(sprites.get(age, lifetime));
    }

    @Override
    public void tick() {
        super.tick();

        setSprite(sprites.get(age, lifetime));
    }

    // stolen from DustParticleBase.java
    protected float randomizeColor(float pCoordMultiplier, float pMultiplier) {
        return (this.random.nextFloat() * 0.2F + 0.8F) * pCoordMultiplier * pMultiplier;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    public static class Provider implements ParticleProvider<MagicParticleOptions> {
        private final SpriteSet sprites;

        public Provider(SpriteSet pSprites) {
            this.sprites = pSprites;
        }

        public Particle createParticle(MagicParticleOptions pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new MagicParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, pType, sprites);
        }
    }
}
