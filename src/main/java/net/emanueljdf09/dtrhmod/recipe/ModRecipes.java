package net.emanueljdf09.dtrhmod.recipe;

import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipes {
    public static void registerRecipes() {
        Registry.register(Registries.RECIPE_SERIALIZER,
                new Identifier(DownTheRabbitHole.MOD_ID, "teapot"),
                TeapotRecipe.Serializer.INSTANCE);

        Registry.register(Registries.RECIPE_TYPE,
                new Identifier(DownTheRabbitHole.MOD_ID, "teapot"),
                TeapotRecipe.Type.INSTANCE);

        Registry.register(Registries.RECIPE_SERIALIZER,
                new Identifier(DownTheRabbitHole.MOD_ID, "hat_ritual"),
                HatRitualRecipe.Serializer.INSTANCE);

        Registry.register(Registries.RECIPE_TYPE,
                new Identifier(DownTheRabbitHole.MOD_ID, "hat_ritual"),
                HatRitualRecipe.Type.INSTANCE);
    }
}
