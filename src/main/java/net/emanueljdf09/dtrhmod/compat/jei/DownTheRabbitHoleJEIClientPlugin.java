package net.emanueljdf09.dtrhmod.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.emanueljdf09.dtrhmod.item.ModItemGroups;
import net.emanueljdf09.dtrhmod.item.ModItems;
import net.emanueljdf09.dtrhmod.menu.screen.TeapotScreen;
import net.emanueljdf09.dtrhmod.recipe.TeapotRecipe;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
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
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.useNbtForSubtypes(ModItems.FILLED_TEA_CUP);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) return;

        List<TeapotRecipe> baseRecipes = client.world.getRecipeManager()
                .listAllOfType(TeapotRecipe.Type.INSTANCE);

        List<TeapotRecipeVariant> expandedVariants = new ArrayList<>();
        for (TeapotRecipe recipe : baseRecipes) {
            expandedVariants.add(new TeapotRecipeVariant(recipe, TeapotRecipeVariant.LiquidType.WATER));
            expandedVariants.add(new TeapotRecipeVariant(recipe, TeapotRecipeVariant.LiquidType.LAVA));
            expandedVariants.add(new TeapotRecipeVariant(recipe, TeapotRecipeVariant.LiquidType.MILK));
        }

        registration.addRecipes(TeapotCategory.TEAPOT_RECIPE_TYPE, expandedVariants); }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(TeapotScreen.class, 91, 35, 24, 17,
                TeapotCategory.TEAPOT_RECIPE_TYPE);
    }
}
