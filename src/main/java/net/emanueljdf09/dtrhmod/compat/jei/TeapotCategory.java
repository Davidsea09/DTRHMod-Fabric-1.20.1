package net.emanueljdf09.dtrhmod.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.emanueljdf09.dtrhmod.block.ModBlocks;
import net.emanueljdf09.dtrhmod.item.ModItems;
import net.emanueljdf09.dtrhmod.recipe.TeapotRecipe;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TeapotCategory implements IRecipeCategory<TeapotRecipeVariant> {
    public static final Identifier UID = new Identifier(DownTheRabbitHole.MOD_ID, "teapot");
    public static final Identifier TEXTURE = new Identifier(DownTheRabbitHole.MOD_ID, "textures/gui/teapot_jei_screen.png");

    public static final RecipeType<TeapotRecipeVariant> TEAPOT_RECIPE_TYPE =
            new RecipeType<>(UID, TeapotRecipeVariant.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;

    public TeapotCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.TEAPOT_BLOCK));

        IDrawableStatic staticArrow = helper.createDrawable(TEXTURE, 176, 14, 24, 17);

        // 2. Wrap it into an animated drawable (e.g., 80 ticks / 4 seconds to fill up from LEFT to right)
        this.arrow = helper.createAnimatedDrawable(staticArrow, 80, IDrawableAnimated.StartDirection.LEFT, false);

    }

    @Override
    public RecipeType<TeapotRecipeVariant> getRecipeType() {
        return TEAPOT_RECIPE_TYPE;
    }

    @Override
    public Text getTitle() {
        return ModBlocks.TEAPOT_BLOCK.getName();
    }

    @Override
    public int getWidth() {
        return 176;
    }

    @Override
    public int getHeight() {
        return 87;
    }

    @Override
    public void draw(TeapotRecipeVariant recipeVariant, IRecipeSlotsView recipeSlotsView, DrawContext guiGraphics, double mouseX, double mouseY) {
        this.background.draw(guiGraphics, 0, 0);

        this.arrow.draw(guiGraphics, 91, 35);
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, TeapotRecipeVariant variant, IFocusGroup focuses) {
        TeapotRecipe recipe = variant.getRecipe();

        List<Ingredient> leafIngredients = recipe.getIngredients();
        if (!leafIngredients.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 27, 34).addIngredients(leafIngredients.get(0));
        }

        ItemStack emptyCupDisplay = new ItemStack(ModItems.EMPTY_CUP, 1);
        builder.addSlot(RecipeIngredientRole.INPUT, 63, 56).addItemStack(emptyCupDisplay);

        // Render just the specific liquid required for this isolated variant row
        builder.addSlot(RecipeIngredientRole.INPUT, 27, 12).addItemStack(variant.getLiquidType().getStack());

        // Render the exact tailored output with modified nbt effects
        DynamicRegistryManager rm = MinecraftClient.getInstance().world.getRegistryManager();
        ItemStack specificOutput = variant.getOutput(rm);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 130, 35).addItemStack(specificOutput);
    }
}