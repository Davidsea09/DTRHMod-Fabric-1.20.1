package net.emanueljdf09.dtrhmod.entity.ai.goals;

import net.emanueljdf09.dtrhmod.entity.custom.WhiteRabbitEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;

public class WanderAroundHoleGoal extends Goal {
    private final WhiteRabbitEntity rabbit;
    private final double maxDistance;

    // Idle break tracking
    private int idleTimeRemaining = 0;

    public WanderAroundHoleGoal(WhiteRabbitEntity rabbit, double maxDistance) {
        this.rabbit = rabbit;
        this.maxDistance = maxDistance;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        // Decrement idle time if it's currently resting
        if (this.idleTimeRemaining > 0) {
            this.idleTimeRemaining--;
            return false;
        }

        return !rabbit.isReturning() && !rabbit.isGreeting() && rabbit.getNavigation().isIdle();
    }

    @Override
    public boolean shouldContinue() {
        return !rabbit.isReturning() && !rabbit.isGreeting() && !rabbit.getNavigation().isIdle();
    }

    @Override
    public void start() {
        rabbit.getRabbitHolePos().ifPresent(origin -> {
            Vec3d target = findTargetNearEdge(origin);
            if (target != null) {
                this.rabbit.getNavigation().startMovingTo(target.x, target.y, target.z, 0.6D);
            }
        });
    }

    @Override
    public void stop() {
        // This runs the exact moment the rabbit arrives at its destination or stops moving
        super.stop();

        // Schedule an idle break!
        // 40 to 100 ticks means it will stand still randomly between 2 and 5 seconds
        if (!rabbit.isReturning() && !rabbit.isGreeting()) {
            this.idleTimeRemaining = this.rabbit.getRandom().nextBetween(40, 100);
        }
    }

    private Vec3d findTargetNearEdge(BlockPos origin) {
        double angle = this.rabbit.getRandom().nextDouble() * 2.0 * Math.PI;
        double minRadius = maxDistance * 0.6;
        double distance = minRadius + (this.rabbit.getRandom().nextDouble() * (maxDistance - minRadius));

        double xOffset = Math.cos(angle) * distance;
        double zOffset = Math.sin(angle) * distance;

        BlockPos targetPos = origin.add((int) xOffset, 0, (int) zOffset);

        for (int yOffset = 3; yOffset >= -3; yOffset--) {
            BlockPos checkFloor = targetPos.up(yOffset);

            // Check if the block is air, the block below it isn't air, AND there is no water/fluid at its feet
            if (this.rabbit.getWorld().getBlockState(checkFloor).isAir() &&
                    !this.rabbit.getWorld().getBlockState(checkFloor.down()).isAir() &&
                    this.rabbit.getWorld().getFluidState(checkFloor).isEmpty() &&
                    this.rabbit.getWorld().getFluidState(checkFloor.down()).isEmpty()) { // Also ensures it doesn't walk on water surfaces

                return Vec3d.ofBottomCenter(checkFloor);
            }
        }

        // Fallback block mapping via heightmap - let's make sure our absolute backup position isn't water either!
        BlockPos fallbackPos = this.rabbit.getWorld().getTopPosition(net.minecraft.world.Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, targetPos);
        if (!this.rabbit.getWorld().getFluidState(fallbackPos).isEmpty() || !this.rabbit.getWorld().getFluidState(fallbackPos.down()).isEmpty()) {
            return null; // Return null if the destination is water, forcing the AI to try a new angle on the next attempt
        }

        return Vec3d.ofBottomCenter(fallbackPos);
    }
}