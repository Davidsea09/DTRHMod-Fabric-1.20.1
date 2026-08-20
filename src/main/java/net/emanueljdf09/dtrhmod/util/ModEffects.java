package net.emanueljdf09.dtrhmod.util;

import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.emanueljdf09.dtrhmod.item.ModItems;
import net.emanueljdf09.dtrhmod.util.effects.GrowStatusEffect;
import net.emanueljdf09.dtrhmod.util.effects.ShrinkStatusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEffects {

    public static final StatusEffect GROW = registerStatusEffect("grow",
            new GrowStatusEffect(StatusEffectCategory.NEUTRAL, 0x90329f));

    public static final StatusEffect SHRINK = registerStatusEffect("shrink",
            new ShrinkStatusEffect(StatusEffectCategory.NEUTRAL, 0x3399FF));

    public static final Potion SHRINK_POTION = registerPotions("shrink_potion",
            new Potion(new StatusEffectInstance(ModEffects.SHRINK, 3600, 0)));

    public static final Potion GROW_POTION = registerPotions("grow_potion",
            new Potion(new StatusEffectInstance(ModEffects.GROW, 3600, 0)));


    public static Potion registerPotions(String name, Potion potion) {
        return Registry.register(Registries.POTION, Identifier.of(DownTheRabbitHole.MOD_ID, name), potion);
    }

    public static StatusEffect registerStatusEffect(String name, StatusEffect statusEffect) {
        return Registry.register(Registries.STATUS_EFFECT, Identifier.of(DownTheRabbitHole.MOD_ID, name), statusEffect);
    }

    public static void registerBrewingRecipes() {
        BrewingRecipeRegistry.registerPotionRecipe(Potions.AWKWARD, ModItems.POCKETWATCH, ModEffects.SHRINK_POTION);
        BrewingRecipeRegistry.registerPotionRecipe(Potions.AWKWARD, ModItems.AURORA_STORYBOOK, ModEffects.GROW_POTION);
    }

    public static void registerModEffects() {
        DownTheRabbitHole.LOGGER.info("Registering ModEffects for " + DownTheRabbitHole.MOD_ID);
    }
}
