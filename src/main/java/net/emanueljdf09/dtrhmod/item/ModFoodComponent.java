package net.emanueljdf09.dtrhmod.item;

import net.emanueljdf09.dtrhmod.util.ModEffects;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.FoodComponents;

public class ModFoodComponent {
    public static final FoodComponent EAT_ME = new FoodComponent.Builder()
            .hunger(1)
            .saturationModifier(0.1f)
            .snack()
            .alwaysEdible()
            .statusEffect(new StatusEffectInstance(ModEffects.GROW, 1800, 0), 1.0f)
            .build();

    public static final FoodComponent DRINK_ME = new FoodComponent.Builder()
            .hunger(1)
            .saturationModifier(0.1f)
            .snack()
            .alwaysEdible()
            .statusEffect(new StatusEffectInstance(ModEffects.SHRINK, 1800, 0), 1.0f)
            .build();
}
