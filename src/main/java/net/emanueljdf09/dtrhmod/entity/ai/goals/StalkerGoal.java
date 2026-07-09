package net.emanueljdf09.dtrhmod.entity.ai.goals;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;

public class StalkerGoal extends Goal {
    protected final PathAwareEntity mob;
    protected PlayerEntity targetPlayer;
    private boolean isFrozen = false;

    private int stalkDelayTimer = 0;
    private int attackDelayTimer = 0;
    private static final int TWO_SECONDS_IN_TICKS = 40;

    public StalkerGoal(PathAwareEntity mob) {
        this.mob = mob;
        this.setControls(EnumSet.of(Goal.Control.MOVE));
    }

    @Override
    public boolean canStart() {
        this.targetPlayer = this.mob.getWorld().getClosestPlayer(this.mob, 32.0);
        return this.targetPlayer != null && this.targetPlayer.isAlive();
    }

    @Override
    public void start() {
        this.stalkDelayTimer = 0;
        this.attackDelayTimer = 0;
    }

    @Override
    public void tick() {
        if (this.targetPlayer == null) return;

        // --- CONDITION 1: PLAYER IS LOOKING AT THE STATUE ---
        if (isPlayerLooking(this.targetPlayer)) {
            this.isFrozen = true;
            this.mob.getNavigation().stop();
            this.mob.setVelocity(Vec3d.ZERO);
            this.stalkDelayTimer = 0;
            this.attackDelayTimer = 0;
            return;
        }

        // --- CONDITION 2: PLAYER IS NOT LOOKING (STATUE IS ACTIVE) ---
        this.isFrozen = false;

        // Handle the 2-second initial stalk delay before moving
        if (this.stalkDelayTimer < TWO_SECONDS_IN_TICKS) {
            this.stalkDelayTimer++;
            this.mob.getNavigation().stop();
            this.mob.setVelocity(Vec3d.ZERO);
            return;
        }

        double distanceSq = this.mob.squaredDistanceTo(this.targetPlayer);

        // Check if within attack reach (4.0D equals roughly 2 blocks away)
        if (distanceSq <= 4.0D) {
            this.mob.getNavigation().stop();
            this.mob.setVelocity(Vec3d.ZERO);

            // Handle the 2-second attack windup delay
            if (this.attackDelayTimer < TWO_SECONDS_IN_TICKS) {
                this.attackDelayTimer++;
            } else {
                // Execute attack!
                float damage = (float) this.mob.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
                this.targetPlayer.damage(this.mob.getDamageSources().mobAttack(this.mob), damage);
                this.targetPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 40));

                this.attackDelayTimer = 0;
            }
        } else {
            this.attackDelayTimer = 0;

            // --- CIRCLING LOGIC ---
            // Distribute up to 8 statues perfectly around the player using their unique Entity ID
            // Multiplying by 45 degrees gives us 8 distinct directions (0, 45, 90, 135...)
            double angleDegrees = (this.mob.getId() * 45) % 360;
            double angleRadians = Math.toRadians(angleDegrees);

            // Set the target radius (how far from the player they want to stand in the circle)
            double radius = 1.2D;

            // Calculate the exact offset coordinate
            double targetX = this.targetPlayer.getX() + (Math.cos(angleRadians) * radius);
            double targetZ = this.targetPlayer.getZ() + (Math.sin(angleRadians) * radius);
            double targetY = this.targetPlayer.getY();

            // Pathfind to their assigned slot in the circle instead of the player's exact center
            this.mob.getNavigation().startMovingTo(targetX, targetY, targetZ, 1.4D);
        }
    }

    public boolean isFrozen() {
        return this.isFrozen;
    }

    private boolean isPlayerLooking(PlayerEntity player) {
        Vec3d lookVec = player.getRotationVec(1.0F).normalize();
        Vec3d toMobVec = new Vec3d(
                this.mob.getX() - player.getX(),
                this.mob.getEyeY() - player.getEyeY(),
                this.mob.getZ() - player.getZ()
        );

        double distance = toMobVec.length();
        toMobVec = toMobVec.normalize();
        double dotProduct = lookVec.dotProduct(toMobVec);

        if (dotProduct > 0.7) {
            return player.canSee(this.mob);
        }
        return false;
    }

    @Override
    public void stop() {
        this.targetPlayer = null;
        this.isFrozen = false;
    }
}