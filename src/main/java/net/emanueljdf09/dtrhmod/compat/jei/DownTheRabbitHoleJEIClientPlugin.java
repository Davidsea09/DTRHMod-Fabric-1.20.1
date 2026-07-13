package net.emanueljdf09.dtrhmod.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.emanueljdf09.dtrhmod.menu.screen.TeapotScreen;
import net.emanueljdf09.dtrhmod.recipe.TeapotRecipe;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class DownTheRabbitHoleJEIClientPlugin implements IModPlugin {


    @Override
    public Identifier getPluginUid() {
        return new Identifier(DownTheRabbitHole.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new TeapotCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) return;

        List<TeapotRecipe> baseRecipes = client.world.getRecipeManager()
                .listAllOfType(TeapotRecipe.Type.INSTANCE);
        registration.addRecipes(TeapotCategory.TEAPOT_RECIPE_TYPE, baseRecipes);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(TeapotScreen.class, 91, 35, 24, 17,
                TeapotCategory.TEAPOT_RECIPE_TYPE);
    }
}
