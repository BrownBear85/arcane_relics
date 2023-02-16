package bonker.arcane_relics.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class MagicDustParticle extends TextureSheetParticle {

    private final SpriteSet sprites;
    private boolean isMovingToEntity = false;
    private Entity entity;
    private Vec3 moveTo;

    protected MagicDustParticle(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, SpriteSet pSpriteSet, MagicDustParticleOptions pOptions) {
        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);

        Vec3 dest = pOptions.getDest();
        if (dest.x == Double.MAX_VALUE && dest.y == Double.MIN_VALUE) {
            entity = pLevel.getEntity((int) dest.z);
            if (entity != null) {
                this.isMovingToEntity = true;
            }
        }

        if (!this.isMovingToEntity) {
            this.moveTo = dest;
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
        setSpriteFromAge(sprites);
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.sprites);
        setDelta();
    }

    protected void setDelta() {
        if (isMovingToEntity) {
            this.moveTo = entity.position().add(0, entity.getBbHeight() / 2, 0);
        }
        Vec3 pos = this.getBoundingBox().getCenter();
        Vec3 velocity = moveTo.subtract(pos);
        double m = Math.max(Math.min(pos.distanceTo(moveTo), 1) * 0.1, 0.1);
        this.xd = velocity.x * m;
        this.yd = velocity.y * m;
        this.zd = velocity.z * m;
    }

    // stolen from DustParticleBase.java
    protected float randomizeColor(float pCoordMultiplier, float pMultiplier) {
        return (this.random.nextFloat() * 0.2F + 0.8F) * pCoordMultiplier * pMultiplier;
    }

    @Override
    public void setSpriteFromAge(SpriteSet pSprite) {
        super.setSpriteFromAge(pSprite);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public static class Provider implements ParticleProvider<MagicDustParticleOptions> {
        private final SpriteSet sprites;

        public Provider(SpriteSet pSprites) {
            this.sprites = pSprites;
        }

        public Particle createParticle(MagicDustParticleOptions options, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new MagicDustParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, this.sprites, options);
        }
    }
}
