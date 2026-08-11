package net.emanueljdf09.dtrhmod.compat.jei;

import net.emanueljdf09.dtrhmod.recipe.TeapotRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.DynamicRegistryManager;

public class TeapotRecipeVariant {
    public enum LiquidType {
        WATER(new ItemStack(Items.WATER_BUCKET), "minecraft:water_bucket"),
        LAVA(new ItemStack(Items.LAVA_BUCKET), "minecraft:lava_bucket"),
        MILK(new ItemStack(Items.MILK_BUCKET), "minecraft:milk_bucket");

        private final ItemStack stack;
        private final String fluidId;

        LiquidType(ItemStack stack, String fluidId) {
            this.stack = stack;
            this.fluidId = fluidId;
        }

        public ItemStack getStack() { return stack; }
        public String getFluidId() { return fluidId; }
    }

    private final TeapotRecipe recipe;
    private final LiquidType liquidType;

    public TeapotRecipeVariant(TeapotRecipe recipe, LiquidType liquidType) {
        this.recipe = recipe;
        this.liquidType = liquidType;
    }

    public TeapotRecipe getRecipe() {
        return recipe;
    }

    public LiquidType getLiquidType() {
        return liquidType;
    }

    public ItemStack getOutput(DynamicRegistryManager rm) {
        ItemStack output = recipe.getOutput(rm).copy();
        NbtCompound nbt = output.getOrCreateNbt();
        nbt.putString("FluidUsed", liquidType.getFluidId());

        if (liquidType == LiquidType.LAVA) {
            if (nbt.contains("Effects", 9)) {
                NbtList list = nbt.getList("Effects", 10);
                for (int i = 0; i < list.size(); i++) {
                    NbtCompound effectNbt = list.getCompound(i);
                    int currentAmp = effectNbt.getInt("Amplifier");
                    effectNbt.putInt("Amplifier", Math.max(currentAmp, 1));
                }
            }
        } else if (liquidType == LiquidType.MILK) {
            if (nbt.contains("Effects", 9)) {
                NbtList list = nbt.getList("Effects", 10);
                for (int i = 0; i < list.size(); i++) {
                    NbtCompound effectNbt = list.getCompound(i);
                    effectNbt.putInt("Duration", 600);
                }
            }
        }
        return output;
    }
}
