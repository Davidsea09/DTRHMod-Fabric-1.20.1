package net.emanueljdf09.dtrhmod.datagen;

import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.emanueljdf09.dtrhmod.block.ModBlocks;
import net.emanueljdf09.dtrhmod.item.ModItems;
import net.emanueljdf09.dtrhmod.recipe.HatRitualRecipeJsonBuilder;
import net.emanueljdf09.dtrhmod.recipe.TeapotRecipeJsonBuilder;
import net.emanueljdf09.dtrhmod.util.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

import static net.minecraft.data.server.recipe.RecipeProvider.*;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> consumer) {

        TeapotRecipeJsonBuilder.create(ModItems.FILLED_TEA_CUP)
                .addIngredient(Items.SPRUCE_LEAVES)
                .fluid(Items.WATER_BUCKET)
                .cupCount(1)
                .addEffect(new Identifier("minecraft", "speed"), 200, 0)
                .criterion(hasItem(Items.SPRUCE_LEAVES), conditionsFromItem(Items.SPRUCE_LEAVES))
                .offerTo(consumer, new Identifier(DownTheRabbitHole.MOD_ID, "spruce_tea"));

        TeapotRecipeJsonBuilder.create(ModItems.FILLED_TEA_CUP)
                .addIngredient(Items.ACACIA_LEAVES)
                .fluid(Items.WATER_BUCKET)
                .cupCount(1)
                .addEffect(new Identifier("minecraft", "strength"), 200, 0)
                .criterion(hasItem(Items.ACACIA_LEAVES), conditionsFromItem(Items.ACACIA_LEAVES))
                .offerTo(consumer, new Identifier(DownTheRabbitHole.MOD_ID, "acacia_tea"));

        TeapotRecipeJsonBuilder.create(ModItems.FILLED_TEA_CUP)
                .addIngredient(Items.RED_MUSHROOM)
                .fluid(Items.WATER_BUCKET)
                .cupCount(1)
                .addEffect(new Identifier("minecraft", "night_vision"), 200, 0)
                .criterion(hasItem(Items.RED_MUSHROOM), conditionsFromItem(Items.RED_MUSHROOM))
                .offerTo(consumer, new Identifier(DownTheRabbitHole.MOD_ID, "mushroom_tea"));

        HatRitualRecipeJsonBuilder.create(new Identifier(DownTheRabbitHole.MOD_ID, "storybook"), true)
                .structureId(new Identifier(DownTheRabbitHole.MOD_ID, "portal/mirror_room_complete")) // 🌟 Set structure ID
                .addIngredient(ModItems.SNOW_WHITE_STORYBOOK)
                .offerTo(consumer, new Identifier(DownTheRabbitHole.MOD_ID, "snow_white_hat_ritual"));

        HatRitualRecipeJsonBuilder.create(new Identifier(DownTheRabbitHole.MOD_ID, "wonderland"), false)
                .addIngredient(ModItems.POCKETWATCH)
                .offerTo(consumer, new Identifier(DownTheRabbitHole.MOD_ID, "wonderland_hat_ritual"));

        HatRitualRecipeJsonBuilder.create(new Identifier(DownTheRabbitHole.MOD_ID, "overworld"), false)
                .addIngredient(Items.DIAMOND)
                .offerTo(consumer, new Identifier(DownTheRabbitHole.MOD_ID, "overworld_hat_ritual"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.POCKETWATCH, 1)
                .pattern("NNN")
                .pattern("N N")
                .pattern(" C ")
                .input('N', Items.GOLD_NUGGET)
                .input('C', Items.CLOCK)
                .criterion(hasItem(Items.CLOCK), conditionsFromItem(Items.CLOCK))
                .offerTo(consumer, new Identifier(getRecipeName(ModItems.POCKETWATCH)));

        offerWoodRecipe(consumer, ModBlocks.TH_WOOD, ModBlocks.TH_LOG);
        offerWoodRecipe(consumer, ModBlocks.STRIPPED_TH_WOOD, ModBlocks.STRIPPED_TH_LOG);
        offerPlanksRecipe(consumer, ModBlocks.TH_PLANKS, ModTags.Items.TH_LOGS, 4);
        offerSlabRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.TH_SLABS, ModBlocks.TH_PLANKS);
        offerStairsRecipe(consumer, ModBlocks.TH_STAIRS, ModBlocks.TH_PLANKS);
        offerButtonRecipe(consumer, ModBlocks.TH_BUTTON, ModBlocks.TH_PLANKS);
        offerPressurePlateRecipe(consumer, ModBlocks.TH_PRESSURE_PLATE, ModBlocks.TH_PLANKS);
        offerFenceRecipe(consumer, ModBlocks.TH_FENCE, ModBlocks.TH_PLANKS);
        offerFenceGateRecipe(consumer, ModBlocks.TH_FENCE_GATE, ModBlocks.TH_PLANKS);
        offerWallRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.TH_WALL, ModBlocks.TH_PLANKS);
        offerDoorRecipe(consumer, ModBlocks.TH_DOOR, ModBlocks.TH_PLANKS);
        offerTrapdoorRecipe(consumer, ModBlocks.TH_TRAPDOOR, ModBlocks.TH_PLANKS);

        offerWoodRecipe(consumer, ModBlocks.WW_WOOD, ModBlocks.WW_LOG);
        offerWoodRecipe(consumer, ModBlocks.STRIPPED_WW_WOOD, ModBlocks.STRIPPED_WW_LOG);
        offerPlanksRecipe(consumer, ModBlocks.WW_PLANKS, ModTags.Items.WW_LOGS, 4);
        offerSlabRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.WW_SLABS, ModBlocks.WW_PLANKS);
        offerStairsRecipe(consumer, ModBlocks.WW_STAIRS, ModBlocks.WW_PLANKS);
        offerButtonRecipe(consumer, ModBlocks.WW_BUTTON, ModBlocks.WW_PLANKS);
        offerPressurePlateRecipe(consumer, ModBlocks.WW_PRESSURE_PLATE, ModBlocks.WW_PLANKS);
        offerFenceRecipe(consumer, ModBlocks.WW_FENCE, ModBlocks.WW_PLANKS);
        offerFenceGateRecipe(consumer, ModBlocks.WW_FENCE_GATE, ModBlocks.WW_PLANKS);
        offerWallRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.WW_WALL, ModBlocks.WW_PLANKS);
        offerDoorRecipe(consumer, ModBlocks.WW_DOOR, ModBlocks.WW_PLANKS);
        offerTrapdoorRecipe(consumer, ModBlocks.WW_TRAPDOOR, ModBlocks.WW_PLANKS);

        offerWoodRecipe(consumer, ModBlocks.BB_WOOD, ModBlocks.BB_LOG);
        offerWoodRecipe(consumer, ModBlocks.STRIPPED_BB_WOOD, ModBlocks.STRIPPED_BB_LOG);
        offerPlanksRecipe(consumer, ModBlocks.BB_PLANKS, ModTags.Items.BB_LOGS, 4);
        offerSlabRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.BB_SLABS, ModBlocks.BB_PLANKS);
        offerStairsRecipe(consumer, ModBlocks.BB_STAIRS, ModBlocks.BB_PLANKS);
        offerButtonRecipe(consumer, ModBlocks.BB_BUTTON, ModBlocks.BB_PLANKS);
        offerPressurePlateRecipe(consumer, ModBlocks.BB_PRESSURE_PLATE, ModBlocks.BB_PLANKS);
        offerFenceRecipe(consumer, ModBlocks.BB_FENCE, ModBlocks.BB_PLANKS);
        offerFenceGateRecipe(consumer, ModBlocks.BB_FENCE_GATE, ModBlocks.BB_PLANKS);
        offerWallRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, ModBlocks.BB_WALL, ModBlocks.BB_PLANKS);
        offerDoorRecipe(consumer, ModBlocks.BB_DOOR, ModBlocks.BB_PLANKS);
        offerTrapdoorRecipe(consumer, ModBlocks.BB_TRAPDOOR, ModBlocks.BB_PLANKS);





    }

    public static void offerStairsRecipe(Consumer<RecipeJsonProvider> consumer, ItemConvertible output, ItemConvertible input) {
        createStairsRecipe(output, Ingredient.ofItems(new ItemConvertible[]{input}))
                .criterion(hasItem(input), conditionsFromItem(input))
                .offerTo(consumer);
    }

    public static void offerButtonRecipe(Consumer<RecipeJsonProvider> consumer, ItemConvertible output, ItemConvertible input) {
            ShapelessRecipeJsonBuilder.create(RecipeCategory.REDSTONE, output, 1)
                    .input(input)
                    .criterion(hasItem(input), conditionsFromItem(input))
                    .offerTo(consumer);
    }

    public static void offerFenceRecipe(Consumer<RecipeJsonProvider> consumer, ItemConvertible output, ItemConvertible input) {
        createFenceRecipe(output, Ingredient.ofItems(new ItemConvertible[]{input}))
                .criterion(hasItem(input), conditionsFromItem(input))
                .offerTo(consumer);
    }

    public static void offerFenceGateRecipe(Consumer<RecipeJsonProvider> consumer, ItemConvertible output, ItemConvertible input) {
        createFenceGateRecipe(output, Ingredient.ofItems(new ItemConvertible[]{input}))
                .criterion(hasItem(input), conditionsFromItem(input))
                .offerTo(consumer);
    }

    public static void offerDoorRecipe(Consumer<RecipeJsonProvider> consumer, ItemConvertible output, ItemConvertible input) {
        createDoorRecipe(output, Ingredient.ofItems(new ItemConvertible[]{input}))
                .criterion(hasItem(input), conditionsFromItem(input))
                .offerTo(consumer);
    }

    public static void offerTrapdoorRecipe(Consumer<RecipeJsonProvider> consumer, ItemConvertible output, ItemConvertible input) {
        createTrapdoorRecipe(output, Ingredient.ofItems(new ItemConvertible[]{input}))
                .criterion(hasItem(input), conditionsFromItem(input))
                .offerTo(consumer);
    }

    public static void offerWoodRecipe(Consumer<RecipeJsonProvider> consumer, ItemConvertible output, ItemConvertible input) {
        createWoodRecipe(output, Ingredient.ofItems(new ItemConvertible[]{input}))
                .criterion(hasItem(input), conditionsFromItem(input))
                .offerTo(consumer);
    }

    public static CraftingRecipeJsonBuilder createWoodRecipe(ItemConvertible output, Ingredient input) {
        return
                ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, output, 3)
                        .pattern("LL")
                        .pattern("LL")
                        .input('L', input);

    }

}
