package net.emanueljdf09.dtrhmod.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class HatRitualRecipe implements Recipe<Inventory> {
    private final Identifier id;
    private final DefaultedList<Ingredient> ingredients;
    private final Identifier targetDimension;
    private final boolean isInstanceDimension;
    private final Identifier structureId;

    public HatRitualRecipe(Identifier id, DefaultedList<Ingredient> ingredients, Identifier targetDimension, boolean isInstanceDimension, Identifier structureId) {
        this.id = id;
        this.ingredients = ingredients;
        this.targetDimension = targetDimension;
        this.isInstanceDimension = isInstanceDimension;
        this.structureId = structureId;
    }

    public Identifier getStructureId() {
        return structureId;
    }

    public Identifier getTargetDimension() {
        return targetDimension;
    }

    public boolean isInstanceDimension() {
        return isInstanceDimension;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public boolean matches(Inventory inventory, World world) {
        if (world.isClient) return false;

        java.util.List<ItemStack> inputs = new java.util.ArrayList<>();
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (!stack.isEmpty()) inputs.add(stack);
        }

        if (inputs.size() != ingredients.size()) return false;

        RecipeMatcher matcher = new RecipeMatcher();
        return matcher.match(inputs, ingredients);
    }

    @Override
    public ItemStack craft(Inventory inventory, DynamicRegistryManager registryManager) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return ItemStack.EMPTY;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<HatRitualRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "hat_ritual";
    }

    private static class RecipeMatcher {
        public boolean match(java.util.List<ItemStack> inputs, DefaultedList<Ingredient> ingredients) {
            boolean[] used = new boolean[inputs.size()];
            for (Ingredient ingredient : ingredients) {
                boolean matched = false;
                for (int i = 0; i < inputs.size(); i++) {
                    if (!used[i] && ingredient.test(inputs.get(i))) {
                        used[i] = true;
                        matched = true;
                        break;
                    }
                }
                if (!matched) return false;
            }
            return true;
        }
    }

    public static class Serializer implements RecipeSerializer<HatRitualRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "hat_ritual";

        @Override
        public HatRitualRecipe read(Identifier id, JsonObject json) {
            JsonArray ingredientsJson = JsonHelper.getArray(json, "ingredients");
            DefaultedList<Ingredient> ingredients = DefaultedList.of();

            for (int i = 0; i < ingredientsJson.size(); i++) {
                ingredients.add(Ingredient.fromJson(ingredientsJson.get(i)));
            }

            Identifier structureId = json.has("structure_id")
                    ? new Identifier(JsonHelper.getString(json, "structure_id"))
                    : null;

            Identifier targetDimension = new Identifier(JsonHelper.getString(json, "target_dimension"));
            boolean isInstance = JsonHelper.getBoolean(json, "is_instance_dimension");

            return new HatRitualRecipe(id, ingredients, targetDimension, isInstance, structureId);
        }

        @Override
        public HatRitualRecipe read(Identifier id, PacketByteBuf buf) {
            int size = buf.readVarInt();
            DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(size, Ingredient.EMPTY);

            for (int i = 0; i < size; i++) {
                ingredients.set(i, Ingredient.fromPacket(buf));
            }

            Identifier structureId = buf.readBoolean() ? buf.readIdentifier() : null;
            Identifier targetDimension = buf.readIdentifier();
            boolean isInstance = buf.readBoolean();

            return new HatRitualRecipe(id, ingredients, targetDimension, isInstance, structureId);
        }

        @Override
        public void write(PacketByteBuf buf, HatRitualRecipe recipe) {
            buf.writeVarInt(recipe.ingredients.size());
            for (Ingredient ingredient : recipe.ingredients) {
                ingredient.write(buf);
            }
            buf.writeIdentifier(recipe.targetDimension);
            buf.writeBoolean(recipe.isInstanceDimension);
            buf.writeBoolean(recipe.structureId != null);
            if (recipe.structureId != null) buf.writeIdentifier(recipe.structureId);
        }
    }
}
