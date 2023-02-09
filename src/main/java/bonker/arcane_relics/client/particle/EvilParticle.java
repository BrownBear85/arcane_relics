package bonker.arcane_relics.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.world.phys.Vec3;

public class EvilParticle extends TextureSheetParticle {

    private final SpriteSet sprites;
    private final Vec3 moveTo;

    protected EvilParticle(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, SpriteSet pSpriteSet, EvilParticleOptions pOptions) {
        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);

        this.moveTo = pOptions.getDest();
        if (moveTo == null) {
            this.xd = pXSpeed;
            this.yd = pYSpeed;
            this.zd = pZSpeed;
        }
        setDelta();

        this.lifetime = 80;
        this.gravity = 0.1F;

        float colorRand = this.random.nextFloat() * 0.4F + 0.6F;
        this.rCol = this.randomizeColor(pOptions.getColor().x, colorRand);
        this.gCol = this.randomizeColor(pOptions.getColor().y, colorRand);
        this.bCol = this.randomizeColor(pOptions.getColor().z, colorRand);

        this.scale(pOptions.getScale());

        this.sprites = pSpriteSet;
        setSpriteFromAge(pSpriteSet);
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.sprites);
        setDelta();
    }

    protected void setDelta() {
        if (moveTo != null) {
            Vec3 pos = this.getBoundingBox().getCenter();
            Vec3 velocity = moveTo.subtract(pos);
            double m = Math.max(Math.min(pos.distanceTo(moveTo), 1) * 0.1, 0.1);
            this.xd = velocity.x * m;
            this.yd = velocity.y * m;
            this.zd = velocity.z * m;
        }
    }

    // stolen from DustParticleBase.java
    protected float randomizeColor(float pCoordMultiplier, float pMultiplier) {
        return (this.random.nextFloat() * 0.2F + 0.8F) * pCoordMultiplier * pMultiplier;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public static class Provider implements ParticleProvider<EvilParticleOptions> {
        private final SpriteSet sprites;

        public Provider(SpriteSet pSprites) {
            this.sprites = pSprites;
        }

        public Particle createParticle(EvilParticleOptions options, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new EvilParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, this.sprites, options);
        }
    }
}
