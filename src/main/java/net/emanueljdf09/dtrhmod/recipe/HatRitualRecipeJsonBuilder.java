package net.emanueljdf09.dtrhmod.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.item.Item;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class HatRitualRecipeJsonBuilder {
    private final List<Ingredient> ingredients = new ArrayList<>();
    private final Identifier targetDimension;
    private final boolean isInstanceDimension;
    @Nullable
    private Identifier structureId;

    private final Advancement.Builder advancementBuilder = Advancement.Builder.createUntelemetered();

    public HatRitualRecipeJsonBuilder(Identifier targetDimension, boolean isInstanceDimension) {
        this.targetDimension = targetDimension;
        this.isInstanceDimension = isInstanceDimension;
    }

    public static HatRitualRecipeJsonBuilder create(Identifier targetDimension, boolean isInstanceDimension) {
        return new HatRitualRecipeJsonBuilder(targetDimension, isInstanceDimension);
    }

    public HatRitualRecipeJsonBuilder structureId(Identifier structureId) {
        this.structureId = structureId;
        return this;
    }

    public HatRitualRecipeJsonBuilder addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
        return this;
    }

    public HatRitualRecipeJsonBuilder addIngredient(Item item) {
        this.ingredients.add(Ingredient.ofItems(item));
        return this;
    }

    public void offerTo(Consumer<RecipeJsonProvider> exporter, Identifier id) {
        Advancement.Builder recipeAdvancement = Advancement.Builder.createUntelemetered()
                .parent(CraftingRecipeJsonBuilder.ROOT)
                .criterion("has_the_recipe", RecipeUnlockedCriterion.create(id))
                .rewards(net.minecraft.advancement.AdvancementRewards.Builder.recipe(id));

        this.advancementBuilder.getCriteria().forEach(recipeAdvancement::criterion);

        Identifier advancementId = id.withPrefixedPath("recipes/hat_ritual/");

        exporter.accept(new Provider(
                id,
                this.ingredients,
                this.targetDimension,
                this.isInstanceDimension,
                this.structureId,
                recipeAdvancement,
                advancementId
        ));
    }

    private static class Provider implements RecipeJsonProvider {
        private final Identifier id;
        private final List<Ingredient> ingredients;
        private final Identifier targetDimension;
        private final boolean isInstanceDimension;
        @Nullable
        private final Identifier structureId;
        private final Advancement.Builder advancementBuilder;
        private final Identifier advancementId;

        public Provider(Identifier id, List<Ingredient> ingredients, Identifier targetDimension, boolean isInstanceDimension,
                        @Nullable Identifier structureId, Advancement.Builder advancementBuilder, Identifier advancementId) {
            this.id = id;
            this.ingredients = ingredients;
            this.targetDimension = targetDimension;
            this.isInstanceDimension = isInstanceDimension;
            this.structureId = structureId;
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

            json.addProperty("target_dimension", this.targetDimension.toString());
            json.addProperty("is_instance_dimension", this.isInstanceDimension);

            if (this.structureId != null) {
                json.addProperty("structure_id", this.structureId.toString());
            }
        }

        @Override
        public Identifier getRecipeId() {
            return this.id;
        }

        @Override
        public RecipeSerializer<?> getSerializer() {
            return HatRitualRecipe.Serializer.INSTANCE;
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
