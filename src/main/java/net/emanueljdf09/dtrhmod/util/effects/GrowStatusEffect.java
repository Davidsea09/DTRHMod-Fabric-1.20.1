package net.emanueljdf09.dtrhmod.util.effects;

import net.emanueljdf09.dtrhmod.util.ModEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import org.jetbrains.annotations.Nullable;
import virtuoel.pehkui.api.ScaleTypes;

public class GrowStatusEffect extends StatusEffect {
    public GrowStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if (!entity.getWorld().isClient) {

            if (entity.hasStatusEffect(ModEffects.SHRINK)) {
                entity.removeStatusEffect(ModEffects.SHRINK);
            }

            ScaleTypes.BASE.getScaleData(entity).setTargetScale(2.5f);
            ScaleTypes.MOTION.getScaleData(entity).setTargetScale(0.65f);
            ScaleTypes.HEALTH.getScaleData(entity).setTargetScale(1.5f);
            ScaleTypes.ATTACK.getScaleData(entity).setTargetScale(1.5f);
            ScaleTypes.JUMP_HEIGHT.getScaleData(entity).setTargetScale(1.0f);

        }
        super.onApplied(entity, attributes, amplifier);
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if (!entity.getWorld().isClient) {
            if (!entity.hasStatusEffect(ModEffects.SHRINK)) {

                ScaleTypes.BASE.getScaleData(entity).setTargetScale(1.0f);
                ScaleTypes.MOTION.getScaleData(entity).setTargetScale(1.0f);
                ScaleTypes.HEALTH.getScaleData(entity).setTargetScale(1.0f);
                ScaleTypes.ATTACK.getScaleData(entity).setTargetScale(1.0f);
            }
        }
        super.onRemoved(entity, attributes, amplifier);
    }


    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

}
