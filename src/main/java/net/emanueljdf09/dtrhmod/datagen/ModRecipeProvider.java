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
import net.minecraft.world.World;

import java.util.function.Consumer;

import static net.minecraft.data.server.recipe.RecipeProvider.*;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> consumer) {

        TeapotRecipeJsonBuilder.create(ModItems.FILLED_TEA_CUP)
                .addIngredient(Items.OAK_LEAVES)
                .fluid(Items.WATER_BUCKET)
                .cupCount(1)
                .addEffect(new Identifier("minecraft", "weakness"), 200, 0)
                .criterion(hasItem(Items.OAK_LEAVES), conditionsFromItem(Items.OAK_LEAVES))
                .offerTo(consumer, new Identifier(DownTheRabbitHole.MOD_ID, "oak_tea"));

        TeapotRecipeJsonBuilder.create(ModItems.FILLED_TEA_CUP)
                .addIngredient(Items.SWEET_BERRIES)
                .fluid(Items.WATER_BUCKET)
                .cupCount(1)
                .addEffect(new Identifier("minecraft", "slowness"), 200, 0)
                .criterion(hasItem(Items.SWEET_BERRIES), conditionsFromItem(Items.SWEET_BERRIES))
                .offerTo(consumer, new Identifier(DownTheRabbitHole.MOD_ID, "berry_tea"));

        TeapotRecipeJsonBuilder.create(ModItems.FILLED_TEA_CUP)
                .addIngredient(Items.BIRCH_LEAVES)
                .fluid(Items.WATER_BUCKET)
                .cupCount(1)
                .addEffect(new Identifier("minecraft", "speed"), 200, 0)
                .criterion(hasItem(Items.BIRCH_LEAVES), conditionsFromItem(Items.BIRCH_LEAVES))
                .offerTo(consumer, new Identifier(DownTheRabbitHole.MOD_ID, "birch_tea"));

        TeapotRecipeJsonBuilder.create(ModItems.FILLED_TEA_CUP)
                .addIngredient(Items.JUNGLE_LEAVES)
                .fluid(Items.WATER_BUCKET)
                .cupCount(1)
                .addEffect(new Identifier("minecraft", "jump_boost"), 200, 0)
                .criterion(hasItem(Items.JUNGLE_LEAVES), conditionsFromItem(Items.JUNGLE_LEAVES))
                .offerTo(consumer, new Identifier(DownTheRabbitHole.MOD_ID, "jungle_tea"));

        TeapotRecipeJsonBuilder.create(ModItems.FILLED_TEA_CUP)
                .addIngredient(Items.MANGROVE_LEAVES)
                .fluid(Items.WATER_BUCKET)
                .cupCount(1)
                .addEffect(new Identifier("minecraft", "water_breathing"), 200, 0)
                .criterion(hasItem(Items.MANGROVE_LEAVES), conditionsFromItem(Items.MANGROVE_LEAVES))
                .offerTo(consumer, new Identifier(DownTheRabbitHole.MOD_ID, "mangrove_tea"));

        TeapotRecipeJsonBuilder.create(ModItems.FILLED_TEA_CUP)
                .addIngredient(Items.DARK_OAK_LEAVES)
                .fluid(Items.WATER_BUCKET)
                .cupCount(1)
                .addEffect(new Identifier("minecraft", "instant_health"), 200, 0)
                .criterion(hasItem(Items.DARK_OAK_LEAVES), conditionsFromItem(Items.DARK_OAK_LEAVES))
                .offerTo(consumer, new Identifier(DownTheRabbitHole.MOD_ID, "dark_oak_tea"));

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
                .addEffect(new Identifier("minecraft", "instant_damage"), 200, 0)
                .criterion(hasItem(Items.RED_MUSHROOM), conditionsFromItem(Items.RED_MUSHROOM))
                .offerTo(consumer, new Identifier(DownTheRabbitHole.MOD_ID, "mushroom_tea"));

        TeapotRecipeJsonBuilder.create(ModItems.FILLED_TEA_CUP)
                .addIngredient(Items.CHERRY_LEAVES)
                .fluid(Items.WATER_BUCKET)
                .cupCount(1)
                .addEffect(new Identifier("minecraft", "regeneration"), 200, 0)
                .criterion(hasItem(Items.CHERRY_LEAVES), conditionsFromItem(Items.CHERRY_LEAVES))
                .offerTo(consumer, new Identifier(DownTheRabbitHole.MOD_ID, "cherry_tea"));

        TeapotRecipeJsonBuilder.create(ModItems.FILLED_TEA_CUP)
                .addIngredient(Items.GLOW_BERRIES)
                .fluid(Items.WATER_BUCKET)
                .cupCount(1)
                .addEffect(new Identifier("minecraft", "night_vision"), 200, 0)
                .criterion(hasItem(Items.GLOW_BERRIES), conditionsFromItem(Items.GLOW_BERRIES))
                .offerTo(consumer, new Identifier(DownTheRabbitHole.MOD_ID, "glow_berry_tea"));

        TeapotRecipeJsonBuilder.create(ModItems.FILLED_TEA_CUP)
                .addIngredient(Items.CRIMSON_FUNGUS)
                .fluid(Items.WATER_BUCKET)
                .cupCount(1)
                .addEffect(new Identifier("minecraft", "fire_resistance"), 200, 0)
                .criterion(hasItem(Items.CRIMSON_FUNGUS), conditionsFromItem(Items.CRIMSON_FUNGUS))
                .offerTo(consumer, new Identifier(DownTheRabbitHole.MOD_ID, "crimson_tea"));

        TeapotRecipeJsonBuilder.create(ModItems.FILLED_TEA_CUP)
                .addIngredient(Items.BROWN_MUSHROOM)
                .fluid(Items.WATER_BUCKET)
                .cupCount(1)
                .addEffect(new Identifier("minecraft", "poison"), 200, 0)
                .criterion(hasItem(Items.BROWN_MUSHROOM), conditionsFromItem(Items.BROWN_MUSHROOM))
                .offerTo(consumer, new Identifier(DownTheRabbitHole.MOD_ID, "brown_mushroom_tea"));

        TeapotRecipeJsonBuilder.create(ModItems.FILLED_TEA_CUP)
                .addIngredient(Items.WARPED_FUNGUS)
                .fluid(Items.WATER_BUCKET)
                .cupCount(1)
                .addEffect(new Identifier("minecraft", "invisibility"), 200, 0)
                .criterion(hasItem(Items.WARPED_FUNGUS), conditionsFromItem(Items.WARPED_FUNGUS))
                .offerTo(consumer, new Identifier(DownTheRabbitHole.MOD_ID, "warped_tea"));

        TeapotRecipeJsonBuilder.create(ModItems.FILLED_TEA_CUP)
                .addIngredient(ModBlocks.BLUE_MUSHROOM.asItem())
                .fluid(Items.WATER_BUCKET)
                .cupCount(1)
                .addEffect(new Identifier(DownTheRabbitHole.MOD_ID, "shrink"), 200, 0)
                .criterion(hasItem(ModBlocks.BLUE_MUSHROOM.asItem()), conditionsFromItem(ModBlocks.BLUE_MUSHROOM.asItem()))
                .offerTo(consumer, new Identifier(DownTheRabbitHole.MOD_ID, "blue_mushroom_tea"));

        TeapotRecipeJsonBuilder.create(ModItems.FILLED_TEA_CUP)
                .addIngredient(ModBlocks.MAGENTA_MUSHROOM.asItem())
                .fluid(Items.WATER_BUCKET)
                .cupCount(1)
                .addEffect(new Identifier(DownTheRabbitHole.MOD_ID, "grow"), 200, 0)
                .criterion(hasItem(ModBlocks.MAGENTA_MUSHROOM.asItem()), conditionsFromItem(ModBlocks.MAGENTA_MUSHROOM.asItem()))
                .offerTo(consumer, new Identifier(DownTheRabbitHole.MOD_ID, "magenta_mushroom_tea"));

        TeapotRecipeJsonBuilder.create(ModItems.FILLED_TEA_CUP)
                .addIngredient(ModBlocks.YELLOW_MUSHROOM.asItem())
                .fluid(Items.WATER_BUCKET)
                .cupCount(1)
                .addEffect(new Identifier("minecraft", "luck"), 200, 0)
                .criterion(hasItem(ModBlocks.YELLOW_MUSHROOM.asItem()), conditionsFromItem(ModBlocks.YELLOW_MUSHROOM.asItem()))
                .offerTo(consumer, new Identifier(DownTheRabbitHole.MOD_ID, "yellow_mushroom_tea"));


        HatRitualRecipeJsonBuilder.create(new Identifier(DownTheRabbitHole.MOD_ID, "storybook"), true)
                .structureId(new Identifier(DownTheRabbitHole.MOD_ID, "portal/mirror_room_complete"))
                .addIngredient(ModItems.SNOW_WHITE_STORYBOOK)
                .offerTo(consumer, new Identifier(DownTheRabbitHole.MOD_ID, "snow_white_hat_ritual"));

        HatRitualRecipeJsonBuilder.create(new Identifier(DownTheRabbitHole.MOD_ID, "wonderland"), false)
                .addIngredient(ModItems.POCKETWATCH)
                .offerTo(consumer, new Identifier(DownTheRabbitHole.MOD_ID, "wonderland_hat_ritual"));

        HatRitualRecipeJsonBuilder.create(World.OVERWORLD.getValue(), false)
                .addIngredient(Items.COBBLESTONE)
                .addIngredient(Items.GRASS_BLOCK)
                .addIngredient(Items.APPLE)
                .offerTo(consumer, new Identifier(DownTheRabbitHole.MOD_ID, "overworld_hat_ritual"));

        HatRitualRecipeJsonBuilder.create(World.NETHER.getValue(), false)
                .addIngredient(Items.OBSIDIAN)
                .addIngredient(Items.NETHERRACK)
                .addIngredient(Items.CRIMSON_FUNGUS)
                .offerTo(consumer, new Identifier(DownTheRabbitHole.MOD_ID, "nether_hat_ritual"));

        HatRitualRecipeJsonBuilder.create(World.END.getValue(), false)
                .addIngredient(Items.END_STONE)
                .addIngredient(Items.GHAST_TEAR)
                .addIngredient(Items.CHORUS_FRUIT)
                .offerTo(consumer, new Identifier(DownTheRabbitHole.MOD_ID, "end_hat_ritual"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.TEAPOT_BLOCK.asItem(), 1)
                .pattern("NIN")
                .pattern("IBI")
                .pattern("IPI")
                .input('N', Items.IRON_NUGGET)
                .input('I', Items.IRON_INGOT)
                .input('B', Items.BUCKET)
                .input('P', Items.IRON_BLOCK)
                .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                .offerTo(consumer, new Identifier(getRecipeName(ModBlocks.TEAPOT_BLOCK)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.POCKETWATCH, 1)
                .pattern("NNN")
                .pattern("N N")
                .pattern(" C ")
                .input('N', Items.GOLD_NUGGET)
                .input('C', Items.CLOCK)
                .criterion(hasItem(Items.CLOCK), conditionsFromItem(Items.CLOCK))
                .offerTo(consumer, new Identifier(getRecipeName(ModItems.POCKETWATCH)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.EMPTY_CUP, 5)
                .pattern("   ")
                .pattern("G G")
                .pattern("GGG")
                .input('G', Items.GLASS)
                .criterion(hasItem(Items.GLASS), conditionsFromItem(Items.GLASS))
                .offerTo(consumer, new Identifier(getRecipeName(ModItems.EMPTY_CUP)));

        offerWoodRecipe(consumer, ModBlocks.TH_WOOD, ModBlocks.TH_LOG);
        offerWoodRecipe(consumer, ModBlocks.STRIPPED_TH_WOOD, ModBlocks.STRIPPED_TH_LOG);
        offerPlanksRecipe(consumer, ModBlocks.TH_PLANKS, ModTags.Items.TH_LOGS, 4);
        generateFamily(consumer, ModBlocks.TH_FAMILY);
        offerHangingSignRecipe(consumer, ModItems.HANGING_TH_SIGN, ModBlocks.STRIPPED_TH_LOG);
        offerBoatRecipe(consumer, ModItems.TH_BOAT, ModBlocks.TH_PLANKS);
        offerChestBoatRecipe(consumer, ModItems.TH_CHEST_BOAT, ModBlocks.TH_PLANKS);

        offerWoodRecipe(consumer, ModBlocks.BB_WOOD, ModBlocks.BB_LOG);
        offerWoodRecipe(consumer, ModBlocks.STRIPPED_BB_WOOD, ModBlocks.STRIPPED_BB_LOG);
        offerPlanksRecipe(consumer, ModBlocks.BB_PLANKS, ModTags.Items.BB_LOGS, 4);
        generateFamily(consumer, ModBlocks.BB_FAMILY);
        offerHangingSignRecipe(consumer, ModItems.HANGING_BB_SIGN, ModBlocks.STRIPPED_BB_LOG);
        offerBoatRecipe(consumer, ModItems.BB_BOAT, ModBlocks.BB_PLANKS);
        offerChestBoatRecipe(consumer, ModItems.BB_CHEST_BOAT, ModBlocks.BB_PLANKS);

        offerWoodRecipe(consumer, ModBlocks.WW_WOOD, ModBlocks.WW_LOG);
        offerWoodRecipe(consumer, ModBlocks.STRIPPED_WW_WOOD, ModBlocks.STRIPPED_WW_LOG);
        offerPlanksRecipe(consumer, ModBlocks.WW_PLANKS, ModTags.Items.WW_LOGS, 4);
        generateFamily(consumer, ModBlocks.WW_FAMILY);
        offerHangingSignRecipe(consumer, ModItems.HANGING_WW_SIGN, ModBlocks.STRIPPED_WW_LOG);
        offerBoatRecipe(consumer, ModItems.WW_BOAT, ModBlocks.WW_PLANKS);
        offerChestBoatRecipe(consumer, ModItems.WW_CHEST_BOAT, ModBlocks.WW_PLANKS);

        offerWoodRecipe(consumer, ModBlocks.HH_WOOD, ModBlocks.HH_LOG);
        offerWoodRecipe(consumer, ModBlocks.STRIPPED_HH_WOOD, ModBlocks.STRIPPED_HH_LOG);
        offerPlanksRecipe(consumer, ModBlocks.HH_PLANKS, ModTags.Items.HH_LOGS, 4);
        generateFamily(consumer, ModBlocks.HH_FAMILY);
        offerHangingSignRecipe(consumer, ModItems.HANGING_HH_SIGN, ModBlocks.STRIPPED_HH_LOG);
        offerBoatRecipe(consumer, ModItems.HH_BOAT, ModBlocks.HH_PLANKS);
        offerChestBoatRecipe(consumer, ModItems.HH_CHEST_BOAT, ModBlocks.HH_PLANKS);

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
