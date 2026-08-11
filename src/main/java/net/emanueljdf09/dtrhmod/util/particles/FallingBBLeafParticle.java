package net.emanueljdf09.dtrhmod.util.particles;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

public class FallingBBLeafParticle extends SpriteBillboardParticle {
    private float rotationSpeed;
    private final float sample;
    private final float rotSpeedMultiplier;

    protected FallingBBLeafParticle(ClientWorld world, double x, double y, double z,
                                    double velocityX, double velocityY, double velocityZ,
                                    SpriteProvider spriteProvider) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);
        this.setSprite(spriteProvider.getSprite(this.random.nextInt(12), 12));
        this.rotationSpeed = (float) Math.toRadians(this.random.nextBoolean() ? -30.0F : 30.0F);
        this.sample = this.random.nextFloat();
        this.rotSpeedMultiplier = (float) Math.toRadians(this.random.nextBoolean() ? -5.0F : 5.0F);
        this.maxAge = 300;
        this.gravityStrength = 7.5E-4F;
        float size = this.random.nextBoolean() ? 0.05F : 0.075F;
        this.scale = size;
        this.setBoundingBoxSpacing(size, size);
        this.velocityMultiplier = 1.0F;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.maxAge-- <= 0) {
            this.markDead();
        }

        if (!this.dead) {
            float f = (float) (300 - this.maxAge);
            float g = Math.min(f / 300.0F, 1.0F);
            double d = Math.cos(Math.toRadians((double) (this.sample * 60.0F))) * 2.0D * Math.pow((double) g, 1.25D);
            double e = Math.sin(Math.toRadians((double) (this.sample * 60.0F))) * 2.0D * Math.pow((double) g, 1.25D);
            this.velocityX += d * 0.0025D;
            this.velocityZ += e * 0.0025D;
            this.velocityY -= (double) this.gravityStrength;
            this.rotationSpeed += this.rotSpeedMultiplier / 20.0F;
            this.prevAngle = this.angle;
            this.angle += this.rotationSpeed / 20.0F;
            this.move(this.velocityX, this.velocityY, this.velocityZ);

            if (this.onGround || this.maxAge < 299 && (this.velocityX == 0.0D || this.velocityZ == 0.0D)) {
                this.markDead();
            }

            if (!this.dead) {
                this.velocityX *= (double) this.velocityMultiplier;
                this.velocityY *= (double) this.velocityMultiplier;
                this.velocityZ *= (double) this.velocityMultiplier;
            }
        }
    }

    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(DefaultParticleType parameters, ClientWorld world,
                                       double x, double y, double z,
                                       double velocityX, double velocityY, double velocityZ) {
            return new FallingBBLeafParticle(world, x, y, z, velocityX, velocityY, velocityZ, this.spriteProvider);
        }
    }
}
