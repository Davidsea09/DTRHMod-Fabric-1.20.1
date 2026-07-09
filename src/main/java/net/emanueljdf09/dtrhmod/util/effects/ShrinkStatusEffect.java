package net.emanueljdf09.dtrhmod.util.effects;

import net.emanueljdf09.dtrhmod.util.ModEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleTypes;

public class ShrinkStatusEffect extends StatusEffect {

    public ShrinkStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if (!entity.getWorld().isClient) {

            if (entity.hasStatusEffect(ModEffects.GROW)) {
                entity.removeStatusEffect(ModEffects.GROW);
            }

            ScaleTypes.BASE.getScaleData(entity).setTargetScale(0.5f);
            ScaleTypes.MOTION.getScaleData(entity).setTargetScale(1.3f);
            ScaleTypes.HEALTH.getScaleData(entity).setTargetScale(0.80f);
            ScaleTypes.JUMP_HEIGHT.getScaleData(entity).setTargetScale(1.3f);
            ScaleTypes.ATTACK.getScaleData(entity).setTargetScale(0.80f);

        }
        super.onApplied(entity, attributes, amplifier);
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if (!entity.getWorld().isClient) {

            if (!entity.hasStatusEffect(ModEffects.GROW)) {
                ScaleTypes.BASE.getScaleData(entity).setTargetScale(1.0f);
                ScaleTypes.MOTION.getScaleData(entity).setTargetScale(1.0f);
                ScaleTypes.HEALTH.getScaleData(entity).setTargetScale(1.0f);
                ScaleTypes.JUMP_HEIGHT.getScaleData(entity).setTargetScale(1.0f);
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
