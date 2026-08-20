package net.emanueljdf09.dtrhmod.item;

import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.emanueljdf09.dtrhmod.block.ModBlocks;
import net.emanueljdf09.dtrhmod.compat.jei.TeapotRecipeVariant;
import net.emanueljdf09.dtrhmod.recipe.TeapotRecipe;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class ModItemGroups {

    public static final ItemGroup DTRH_GROUP = Registry.register(Registries.ITEM_GROUP,
            new Identifier(DownTheRabbitHole.MOD_ID, "dtrh"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.dtrh"))
                    .icon(() -> new ItemStack(ModItems.POCKETWATCH)).entries((displayContext, entries) -> {

                        entries.add(ModItems.POCKETWATCH);
                        entries.add(ModItems.EXTERIOR_KEY);
                        entries.add(ModItems.EMPTY_CUP);
                        entries.add(ModBlocks.TEAPOT_BLOCK.asItem());
                        entries.add(ModBlocks.BLUE_MUSHROOM.asItem());
                        entries.add(ModBlocks.BLUE_MUSHROOM_BLOCK.asItem());
                        entries.add(ModBlocks.YELLOW_MUSHROOM_BLOCK.asItem());
                        entries.add(ModBlocks.YELLOW_MUSHROOM.asItem());
                        entries.add(ModBlocks.MAGENTA_MUSHROOM_BLOCK.asItem());
                        entries.add(ModBlocks.MAGENTA_MUSHROOM.asItem());

                        entries.add(ModItems.DRINK_ME);
                        entries.add(ModItems.EAT_ME);
                        entries.add(ModItems.WHITE_RABBIT_SPAWN_EGG);
                        entries.add(ModItems.WEEPING_PLAYERS_SPAWN_EGG);

                        entries.add(ModItems.MAD_HAT_ITEM);
                        entries.add(ModItems.RED_RIDING_HOOD_STORYBOOK.getDefaultStack());
                        entries.add(ModItems.CINDERELLA_STORYBOOK.getDefaultStack());
                        entries.add(ModItems.SNOW_WHITE_STORYBOOK.getDefaultStack());
                        entries.add(ModItems.JACK_AND_THE_BEANSTALK_STORYBOOK.getDefaultStack());
                        entries.add(ModItems.RAPUNZEL_STORYBOOK.getDefaultStack());
                        entries.add(ModItems.AURORA_STORYBOOK.getDefaultStack());
                        entries.add(ModItems.THE_LITTLE_MERMAID_STORYBOOK.getDefaultStack());
                        entries.add(ModItems.THREE_LITTLE_PIGS_STORYBOOK.getDefaultStack());



                        entries.add(ModBlocks.TH_SAPLING.asItem());
                        entries.add(ModBlocks.TH_LEAVES.asItem());
                        entries.add(ModBlocks.TH_LOG.asItem());
                        entries.add(ModBlocks.TH_WOOD.asItem());
                        entries.add(ModBlocks.STRIPPED_TH_LOG.asItem());
                        entries.add(ModBlocks.STRIPPED_TH_WOOD.asItem());
                        entries.add(ModBlocks.TH_PLANKS.asItem());
                        entries.add(ModBlocks.TH_STAIRS.asItem());
                        entries.add(ModBlocks.TH_SLABS.asItem());
                        entries.add(ModBlocks.TH_FENCE.asItem());
                        entries.add(ModBlocks.TH_FENCE_GATE.asItem());
                        entries.add(ModBlocks.TH_DOOR.asItem());
                        entries.add(ModBlocks.TH_TRAPDOOR.asItem());
                        entries.add(ModBlocks.TH_BUTTON.asItem());
                        entries.add(ModBlocks.TH_PRESSURE_PLATE.asItem());
                        entries.add(ModItems.TH_SIGN);
                        entries.add(ModItems.HANGING_TH_SIGN);
                        entries.add(ModItems.TH_BOAT);

                        entries.add(ModBlocks.WW_SAPLING.asItem());
                        entries.add(ModBlocks.WW_LEAVES.asItem());
                        entries.add(ModItems.WW_HANGING_LEAVES);
                        entries.add(ModBlocks.WW_LOG.asItem());
                        entries.add(ModBlocks.WW_WOOD.asItem());
                        entries.add(ModBlocks.STRIPPED_WW_LOG.asItem());
                        entries.add(ModBlocks.STRIPPED_WW_WOOD.asItem());
                        entries.add(ModBlocks.WW_PLANKS.asItem());
                        entries.add(ModBlocks.WW_STAIRS.asItem());
                        entries.add(ModBlocks.WW_SLABS.asItem());
                        entries.add(ModBlocks.WW_FENCE.asItem());
                        entries.add(ModBlocks.WW_FENCE_GATE.asItem());
                        entries.add(ModBlocks.WW_DOOR.asItem());
                        entries.add(ModBlocks.WW_TRAPDOOR.asItem());
                        entries.add(ModBlocks.WW_BUTTON.asItem());
                        entries.add(ModBlocks.WW_PRESSURE_PLATE.asItem());
                        entries.add(ModItems.WW_SIGN);
                        entries.add(ModItems.HANGING_WW_SIGN);
                        entries.add(ModItems.WW_BOAT);

                        entries.add(ModBlocks.BB_SAPLING.asItem());
                        entries.add(ModBlocks.BB_LEAVES.asItem());
                        entries.add(ModBlocks.BB_LOG.asItem());
                        entries.add(ModBlocks.BB_WOOD.asItem());
                        entries.add(ModBlocks.STRIPPED_BB_LOG.asItem());
                        entries.add(ModBlocks.STRIPPED_BB_WOOD.asItem());
                        entries.add(ModBlocks.BB_PLANKS.asItem());
                        entries.add(ModBlocks.BB_STAIRS.asItem());
                        entries.add(ModBlocks.BB_SLABS.asItem());
                        entries.add(ModBlocks.BB_FENCE.asItem());
                        entries.add(ModBlocks.BB_FENCE_GATE.asItem());
                        entries.add(ModBlocks.BB_DOOR.asItem());
                        entries.add(ModBlocks.BB_TRAPDOOR.asItem());
                        entries.add(ModBlocks.BB_BUTTON.asItem());
                        entries.add(ModBlocks.BB_PRESSURE_PLATE.asItem());
                        entries.add(ModItems.BB_SIGN);
                        entries.add(ModItems.HANGING_BB_SIGN);
                        entries.add(ModItems.BB_BOAT);

                        entries.add(ModBlocks.HH_SAPLING.asItem());
                        entries.add(ModBlocks.HH_LEAVES.asItem());
                        entries.add(ModBlocks.HH_LOG.asItem());
                        entries.add(ModBlocks.HH_WOOD.asItem());
                        entries.add(ModBlocks.STRIPPED_HH_LOG.asItem());
                        entries.add(ModBlocks.STRIPPED_HH_WOOD.asItem());
                        entries.add(ModBlocks.HH_PLANKS.asItem());
                        entries.add(ModBlocks.HH_STAIRS.asItem());
                        entries.add(ModBlocks.HH_SLABS.asItem());
                        entries.add(ModBlocks.HH_FENCE.asItem());
                        entries.add(ModBlocks.HH_FENCE_GATE.asItem());
                        entries.add(ModBlocks.HH_DOOR.asItem());
                        entries.add(ModBlocks.HH_TRAPDOOR.asItem());
                        entries.add(ModBlocks.HH_BUTTON.asItem());
                        entries.add(ModBlocks.HH_PRESSURE_PLATE.asItem());
                        entries.add(ModItems.HH_SIGN);
                        entries.add(ModItems.HANGING_HH_SIGN);
                        entries.add(ModItems.HH_BOAT);
                        entries.add(ModItems.HH_CHEST_BOAT);



                        entries.add(ModBlocks.LAWN_DAISY_PATCH.asItem());
                        entries.add(ModBlocks.WONDER_DIRT.asItem());
                        entries.add(ModBlocks.WONDER_GRASS.asItem());
                        entries.add(ModBlocks.RABBIT_HOLE.asItem());
                        entries.add(ModItems.MIRROR);

                        MinecraftClient client = MinecraftClient.getInstance();
                        if (client.world != null) {
                            for (ItemStack stack : getTeaVariants(client.getServer())) {
                                entries.add(stack);
                            }
                        }
                    }).build());

    public static List<ItemStack> getTeaVariants(MinecraftServer server) {
        List<ItemStack> teaVariants = new ArrayList<>();

        RecipeManager recipeManager = null;
        if (server != null && server.getOverworld() != null) {
            recipeManager = server.getOverworld().getRecipeManager();
        } else {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.world != null) {
                recipeManager = client.world.getRecipeManager();
            }
        }

        if (recipeManager == null) return teaVariants;

        List<TeapotRecipe> recipes = recipeManager.listAllOfType(TeapotRecipe.Type.INSTANCE);
        var registryManager = MinecraftClient.getInstance().world.getRegistryManager();

        for (TeapotRecipe recipe : recipes) {
            for (TeapotRecipeVariant.LiquidType liquidType : TeapotRecipeVariant.LiquidType.values()) {
                TeapotRecipeVariant variant = new TeapotRecipeVariant(recipe, liquidType);
                ItemStack variantStack = variant.getOutput(registryManager);

                if (!variantStack.isEmpty()) {
                    teaVariants.add(variantStack);
                }
            }
        }
        return teaVariants;
    }

    public static void registerItemGroups() {
        DownTheRabbitHole.LOGGER.info("Registering Item Groups for " + DownTheRabbitHole.MOD_ID);
    }
}
