package net.emanueljdf09.dtrhmod.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class TeapotRecipe implements Recipe<SimpleInventory> {
    private final Identifier id;
    private final ItemStack output;
    private final List<Ingredient> recipeItems;
    private final Ingredient fluidIngredient;
    private final int cupCount;
    private final List<StatusEffectInstance> effects;

    public TeapotRecipe(Identifier id, List<Ingredient> recipeItems, Ingredient fluidIngredient, int cupCount, ItemStack output, List<StatusEffectInstance> effects) {
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;
        this.fluidIngredient = fluidIngredient;
        this.cupCount = cupCount;
        this.effects = effects;
    }

    public List<StatusEffectInstance> getEffects() {
        return this.effects;
    }


    @Override
    public boolean matches(SimpleInventory inventory, World world) {
        if (world.isClient) {
        }

        List<ItemStack> inputInputs = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ItemStack stack = inventory.getStack(i);
            if (!stack.isEmpty()) {
                inputInputs.add(stack);
            }
        }

        if (inputInputs.size() != this.recipeItems.size()) {
            return false;
        }

        for (Ingredient ingredient : this.recipeItems) {
            boolean matched = false;
            for (int i = 0; i < inputInputs.size(); i++) {
                if (ingredient.test(inputInputs.get(i))) {
                    inputInputs.remove(i);
                    matched = true;
                    break;
                }
            }

            if (!matched) {
                return false;
            }
        }

        if (inventory.size() <= 3) {
            return true;
        }

        if (!this.fluidIngredient.test(inventory.getStack(3)) && !inventory.getStack(3).isEmpty()) {
            return false;
        }

        List<Item> uniqueLeaves = new ArrayList<>();
        int totalItems = 0;

        int[] leafSlots = new int[]{2, 3, 4};
        for (int slot : leafSlots) {
            ItemStack stack = inventory.getStack(slot);
            if (!stack.isEmpty()) {
                totalItems++;
                if (!uniqueLeaves.contains(stack.getItem())) {
                    uniqueLeaves.add(stack.getItem());
                }
            }
        }

        if (uniqueLeaves.size() != totalItems) {
            return false;
        }

        return true;
    }
    @Override
    public ItemStack craft(SimpleInventory inventory, DynamicRegistryManager registryManager) {
        return output.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        ItemStack displayStack = this.output.copy();

        if (this.effects != null && !this.effects.isEmpty()) {
            NbtCompound nbt = displayStack.getOrCreateNbt();
            NbtList effectsList = new NbtList();

            for (StatusEffectInstance effect : this.effects) {
                NbtCompound effectNbt = new NbtCompound();
                effect.writeNbt(effectNbt);
                effectsList.add(effectNbt);
            }

            nbt.put("Effects", effectsList);

            if (this.fluidIngredient.test(new ItemStack(Items.LAVA_BUCKET))) {
                nbt.putString("FluidUsed", "minecraft:lava_bucket");
            } else if (this.fluidIngredient.test(new ItemStack(Items.MILK_BUCKET))) {
                nbt.putString("FluidUsed", "minecraft:milk_bucket");
            } else {
                nbt.putString("FluidUsed", "minecraft:water_bucket");
            }
        }

        return displayStack;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {

        DefaultedList<Ingredient> allIngredients = DefaultedList.ofSize(this.recipeItems.size(), Ingredient.EMPTY);
        for (int i = 0; i < this.recipeItems.size(); i++) {
            allIngredients.set(i, this.recipeItems.get(i));
        }
        return allIngredients;
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }


    public static class Type implements RecipeType<TeapotRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "teapot";
    }

    public static class Serializer implements RecipeSerializer<TeapotRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "teapot";

        @Override
        public TeapotRecipe read(Identifier id, JsonObject json) {
            JsonArray ingredientsJson = JsonHelper.getArray(json, "ingredients");
            List<Ingredient> inputs = new java.util.ArrayList<>();
            for (int i = 0; i < ingredientsJson.size(); i++) {
                inputs.add(Ingredient.fromJson(ingredientsJson.get(i)));
            }

            Ingredient fluid = json.has("fluid")
                    ? Ingredient.fromJson(json.get("fluid"))
                    : Ingredient.ofItems(Items.WATER_BUCKET);

            int cups = JsonHelper.getInt(json, "cup_count", 4);

            JsonObject resultJson = JsonHelper.getObject(json, "result");
            String itemId = JsonHelper.getString(resultJson, "item");
            int count = JsonHelper.getInt(resultJson, "count", 1);
            ItemStack output = new ItemStack(Registries.ITEM.get(new Identifier(itemId)), count);

            List<StatusEffectInstance> effects = new java.util.ArrayList<>();
            if (json.has("effects")) {
                JsonArray effectsJson = JsonHelper.getArray(json, "effects");
                for (int i = 0; i < effectsJson.size(); i++) {
                    JsonObject effectObj = effectsJson.get(i).getAsJsonObject();
                    String effectId = JsonHelper.getString(effectObj, "effect");
                    int duration = JsonHelper.getInt(effectObj, "duration", 200);
                    int amplifier = JsonHelper.getInt(effectObj, "amplifier", 0);

                    StatusEffect effect = Registries.STATUS_EFFECT.get(new Identifier(effectId));
                    if (effect != null) {
                        effects.add(new StatusEffectInstance(effect, duration, amplifier));
                    }
                }
            }

            return new TeapotRecipe(id, inputs, fluid, cups, output, effects);
        }

        @Override
        public TeapotRecipe read(Identifier id, PacketByteBuf buf) {
            int size = buf.readInt();
            List<Ingredient> inputs = new java.util.ArrayList<>();
            for (int i = 0; i < size; i++) {
                inputs.add(Ingredient.fromPacket(buf));
            }
            Ingredient fluid = Ingredient.fromPacket(buf);
            int cups = buf.readInt();

            ItemStack output = buf.readItemStack();

            int effectSize = buf.readInt();
            List<StatusEffectInstance> effects = new java.util.ArrayList<>();
            for (int i = 0; i < effectSize; i++) {
                Identifier effectId = buf.readIdentifier();
                int duration = buf.readInt();
                int amplifier = buf.readInt();

                StatusEffect effect = Registries.STATUS_EFFECT.get(effectId);
                if (effect != null) {
                    effects.add(new StatusEffectInstance(effect, duration, amplifier));
                }
            }

            return new TeapotRecipe(id, inputs, fluid, cups, output, effects);
        }

        @Override
        public void write(PacketByteBuf buf, TeapotRecipe recipe) {
            buf.writeInt(recipe.recipeItems.size());
            for (Ingredient ingredient : recipe.recipeItems) {
                ingredient.write(buf);
            }
            recipe.fluidIngredient.write(buf);
            buf.writeInt(recipe.cupCount);

            buf.writeItemStack(recipe.output);

            buf.writeInt(recipe.effects.size());
            for (StatusEffectInstance effect : recipe.effects) {
                buf.writeIdentifier(Registries.STATUS_EFFECT.getId(effect.getEffectType()));
                buf.writeInt(effect.getDuration());
                buf.writeInt(effect.getAmplifier());
            }
        }
    }
}
