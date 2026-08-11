package net.emanueljdf09.dtrhmod.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TeapotRecipeJsonBuilder {
    private final Item output;
    private final int outputCount;
    private final List<Ingredient> ingredients = new ArrayList<>();
    private Ingredient fluid = Ingredient.ofItems(Items.WATER_BUCKET);
    private int cupCount = 4;
    private final List<JsonObject> effects = new ArrayList<>();

    private final Advancement.Builder advancementBuilder = Advancement.Builder.createUntelemetered();

    public TeapotRecipeJsonBuilder(Item output, int outputCount) {
        this.output = output;
        this.outputCount = outputCount;
    }

    public static TeapotRecipeJsonBuilder create(Item output) {
        return new TeapotRecipeJsonBuilder(output, 1);
    }

    public static TeapotRecipeJsonBuilder create(Item output, int count) {
        return new TeapotRecipeJsonBuilder(output, count);
    }

    public TeapotRecipeJsonBuilder addEffect(Identifier effectId, int duration, int amplifier) {
        JsonObject effectJson = new JsonObject();
        effectJson.addProperty("effect", effectId.toString());
        effectJson.addProperty("duration", duration);
        effectJson.addProperty("amplifier", amplifier);
        this.effects.add(effectJson);
        return this;
    }

    public TeapotRecipeJsonBuilder fluid(Item fluidItem) {
        this.fluid = Ingredient.ofItems(fluidItem);
        return this;
    }

    public TeapotRecipeJsonBuilder cupCount(int count) {
        this.cupCount = count;
        return this;
    }


    public TeapotRecipeJsonBuilder criterion(String name, CriterionConditions criterionConditions) {
        this.advancementBuilder.criterion(name, criterionConditions);
        return this;
    }

    public TeapotRecipeJsonBuilder addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
        return this;
    }

    public TeapotRecipeJsonBuilder addIngredient(Item item) {
        this.ingredients.add(Ingredient.ofItems(item));
        return this;
    }

    public void offerTo(Consumer<RecipeJsonProvider> exporter, Identifier id) {
        Advancement.Builder recipeAdvancement = Advancement.Builder.createUntelemetered()
                .parent(CraftingRecipeJsonBuilder.ROOT)
                .criterion("has_the_recipe", RecipeUnlockedCriterion.create(id))
                .rewards(AdvancementRewards.Builder.recipe(id));

        this.advancementBuilder.getCriteria().forEach(recipeAdvancement::criterion);

        Identifier advancementId = id.withPrefixedPath("recipes/" + Registries.ITEM.getId(this.output).getPath() + "/");

        exporter.accept(new TeapotRecipeJsonProvider(
                id,
                this.output,
                this.outputCount,
                this.ingredients,
                this.fluid,
                this.cupCount,
                this.effects,
                recipeAdvancement,
                advancementId
        ));
    }

    private static class TeapotRecipeJsonProvider implements RecipeJsonProvider {
        private final Identifier id;
        private final Item output;
        private final int outputCount;
        private final List<Ingredient> ingredients;
        private final Ingredient fluid;
        private final int cupCount;
        private final List<JsonObject> effects;
        private final Advancement.Builder advancementBuilder;
        private final Identifier advancementId;


        public TeapotRecipeJsonProvider(Identifier id, Item output, int outputCount, List<Ingredient> ingredients, Ingredient fluid, int cupCount, List<JsonObject> effects,
                                        Advancement.Builder advancementBuilder, Identifier advancementId) {
            this.id = id;
            this.output = output;
            this.outputCount = outputCount;
            this.ingredients = ingredients;
            this.fluid = fluid;
            this.cupCount = cupCount;
            this.effects = effects;
            this.advancementBuilder = advancementBuilder;
            this.advancementId = advancementId;
        }

        @Override
        public void serialize(JsonObject json) {
            JsonArray ingredientsArray = new JsonArray();
            for (Ingredient ingredient : this.ingredients) {
                ingredientsArray.add(ingredient.toJson());
            }
            json.add("ingredients", ingredientsArray);

            json.add("fluid", this.fluid.toJson());
            json.addProperty("cup_count", this.cupCount);

            if (!this.effects.isEmpty()) {
                JsonArray effectsArray = new JsonArray();
                for (JsonObject effect : this.effects) {
                    effectsArray.add(effect);
                }
                json.add("effects", effectsArray);
            }

            JsonObject resultJson = new JsonObject();
            resultJson.addProperty("item", Registries.ITEM.getId(this.output).toString());
            if (this.outputCount > 1) {
                resultJson.addProperty("count", this.outputCount);
            }
            json.add("result", resultJson);
        }

        @Override
        public Identifier getRecipeId() {
            return this.id;
        }

        @Override
        public RecipeSerializer<?> getSerializer() {
            return TeapotRecipe.Serializer.INSTANCE;
        }

        @Nullable
        @Override
        public JsonObject toAdvancementJson() {
            return this.advancementBuilder.toJson();
        }

        @Nullable
        @Override
        public Identifier getAdvancementId() {
            return this.advancementId;
        }
    }
}
