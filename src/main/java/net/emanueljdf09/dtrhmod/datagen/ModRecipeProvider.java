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
                .structureId(new Identifier(DownTheRabbitHole.MOD_ID, "portal/mirror_room_complete"))
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

        generateFamily(consumer, ModBlocks.TH_FAMILY);
        offerHangingSignRecipe(consumer, ModItems.HANGING_TH_SIGN, ModBlocks.STRIPPED_TH_LOG);

        generateFamily(consumer, ModBlocks.BB_FAMILY);
        offerHangingSignRecipe(consumer, ModItems.HANGING_BB_SIGN, ModBlocks.STRIPPED_BB_LOG);

        generateFamily(consumer, ModBlocks.WW_FAMILY);
        offerHangingSignRecipe(consumer, ModItems.HANGING_WW_SIGN, ModBlocks.STRIPPED_WW_LOG);

    }

}
