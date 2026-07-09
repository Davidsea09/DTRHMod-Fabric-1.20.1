package net.emanueljdf09.dtrhmod.entity.ai.goals;

import net.emanueljdf09.dtrhmod.entity.custom.WhiteRabbitEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

public class RunToRabbitHoleGoal extends Goal {
    private final WhiteRabbitEntity rabbit;

    public RunToRabbitHoleGoal(WhiteRabbitEntity rabbit) {
        this.rabbit = rabbit;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        return rabbit.isReturning() && rabbit.getRabbitHolePos().isPresent();
    }

    @Override
    public void start() {
        rabbit.getRabbitHolePos().ifPresent(pos -> {
            rabbit.getNavigation().startMovingTo(pos.getX(), pos.getY(), pos.getZ(), 1.6D);
        });
    }

    @Override
    public boolean shouldContinue() {
        return rabbit.isReturning() && !rabbit.getNavigation().isIdle();
    }
}
