package net.emanueljdf09.dtrhmod.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.emanueljdf09.dtrhmod.block.ModBlocks;
import net.emanueljdf09.dtrhmod.recipe.TeapotRecipe;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TeapotCategory implements IRecipeCategory<TeapotRecipe> {
    public static final Identifier UID = new Identifier(DownTheRabbitHole.MOD_ID, "teapot");
    public static final Identifier TEXTURE = new Identifier(DownTheRabbitHole.MOD_ID, "textures/gui/teapot_jei_screen.png");

    public static final RecipeType<TeapotRecipe> TEAPOT_RECIPE_TYPE =
            new RecipeType<>(UID, TeapotRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public TeapotCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.TEAPOT_BLOCK));

    }

    @Override
    public RecipeType<TeapotRecipe> getRecipeType() {
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
    public void draw(TeapotRecipe recipe, IRecipeSlotsView recipeSlotsView, DrawContext guiGraphics, double mouseX, double mouseY) {
        this.background.draw(guiGraphics, 0, 0);
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, TeapotRecipe recipe, IFocusGroup focuses) {

        List<Ingredient> leafIngredients = recipe.getIngredients();
        if (!leafIngredients.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 27, 34).addIngredients(leafIngredients.get(0));
        }

        ItemStack emptyCupDisplay = new ItemStack(net.emanueljdf09.dtrhmod.item.ModItems.EMPTY_CUP, 1);
        builder.addSlot(RecipeIngredientRole.INPUT, 63, 56).addItemStack(emptyCupDisplay);

        List<ItemStack> rotatingFluids = List.of(
                new ItemStack(Items.WATER_BUCKET),
                new ItemStack(Items.LAVA_BUCKET),
                new ItemStack(Items.MILK_BUCKET)
        );
        builder.addSlot(RecipeIngredientRole.INPUT, 27, 12).addItemStacks(rotatingFluids);

        List<ItemStack> rotatingOutputs = generateDisplayOutputs(recipe);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 130, 35).addItemStacks(rotatingOutputs);
    }

    private List<ItemStack> generateDisplayOutputs(TeapotRecipe recipe) {
        List<ItemStack> outputs = new ArrayList<>();
        DynamicRegistryManager rm = MinecraftClient.getInstance().world.getRegistryManager();

        ItemStack waterCup = recipe.getOutput(rm).copy();
        waterCup.getOrCreateNbt().putString("FluidUsed", "minecraft:water_bucket");
        outputs.add(waterCup);

        ItemStack lavaCup = recipe.getOutput(rm).copy();
        NbtCompound lavaNbt = lavaCup.getOrCreateNbt();
        lavaNbt.putString("FluidUsed", "minecraft:lava_bucket");
        if (lavaNbt.contains("Effects", 9)) {
            NbtList list = lavaNbt.getList("Effects", 10);
            for (int i = 0; i < list.size(); i++) {
                NbtCompound effectNbt = list.getCompound(i);
                int currentAmp = effectNbt.getInt("Amplifier");
                effectNbt.putInt("Amplifier", Math.max(currentAmp, 1));
            }
        }
        outputs.add(lavaCup);

        ItemStack milkCup = recipe.getOutput(rm).copy();
        NbtCompound milkNbt = milkCup.getOrCreateNbt();
        milkNbt.putString("FluidUsed", "minecraft:milk_bucket");
        if (milkNbt.contains("Effects", 9)) {
            NbtList list = milkNbt.getList("Effects", 10);
            for (int i = 0; i < list.size(); i++) {
                NbtCompound effectNbt = list.getCompound(i);
                effectNbt.putInt("Duration", 600);
            }
        }
        outputs.add(milkCup);

        return outputs;
    }
}
