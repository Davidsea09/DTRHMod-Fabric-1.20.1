package net.emanueljdf09.dtrhmod.datagen;

import net.emanueljdf09.dtrhmod.block.ModBlockCollections;
import net.emanueljdf09.dtrhmod.block.ModBlocks;
import net.emanueljdf09.dtrhmod.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.ModelIds;
import net.minecraft.data.client.Models;
import net.minecraft.item.Item;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

        blockStateModelGenerator.registerFlowerbed(ModBlocks.LAWN_DAISY_PATCH);

        blockStateModelGenerator.registerMushroomBlock(ModBlocks.BLUE_MUSHROOM_BLOCK);
        blockStateModelGenerator.registerTintableCross(ModBlocks.BLUE_MUSHROOM, BlockStateModelGenerator.TintType.NOT_TINTED);
        blockStateModelGenerator.registerMushroomBlock(ModBlocks.YELLOW_MUSHROOM_BLOCK);
        blockStateModelGenerator.registerTintableCross(ModBlocks.YELLOW_MUSHROOM, BlockStateModelGenerator.TintType.NOT_TINTED);
        blockStateModelGenerator.registerMushroomBlock(ModBlocks.MAGENTA_MUSHROOM_BLOCK);
        blockStateModelGenerator.registerTintableCross(ModBlocks.MAGENTA_MUSHROOM, BlockStateModelGenerator.TintType.NOT_TINTED);

        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.RABBIT_HOLE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.EXTERIOR_PORTAL);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.EXTERIOR_DOOR);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.MAD_HATTER_HAT);

        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.WONDER_DIRT);

        blockStateModelGenerator.registerTintableCross(ModBlocks.TH_SAPLING, BlockStateModelGenerator.TintType.NOT_TINTED);
        blockStateModelGenerator.registerTintableCross(ModBlocks.WW_SAPLING, BlockStateModelGenerator.TintType.NOT_TINTED);
        blockStateModelGenerator.registerTintableCross(ModBlocks.BB_SAPLING, BlockStateModelGenerator.TintType.NOT_TINTED);


        blockStateModelGenerator.registerLog(ModBlocks.TH_LOG).log(ModBlocks.TH_LOG).wood(ModBlocks.TH_WOOD);
        blockStateModelGenerator.registerLog(ModBlocks.STRIPPED_TH_LOG).log(ModBlocks.STRIPPED_TH_LOG).wood(ModBlocks.STRIPPED_TH_WOOD);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.TH_LEAVES);

        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.TH_PLANKS).family(ModBlocks.TH_FAMILY);

        blockStateModelGenerator.registerLog(ModBlocks.WW_LOG).log(ModBlocks.WW_LOG).wood(ModBlocks.WW_WOOD);
        blockStateModelGenerator.registerLog(ModBlocks.STRIPPED_WW_LOG).log(ModBlocks.STRIPPED_WW_LOG).wood(ModBlocks.STRIPPED_WW_WOOD);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.WW_LEAVES);
        blockStateModelGenerator.registerPlantPart(ModBlocks.WW_HANGING_LEAVES_PLANT, ModBlocks.WW_HANGING_LEAVES, BlockStateModelGenerator.TintType.NOT_TINTED);

        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.WW_PLANKS).family(ModBlocks.WW_FAMILY);

        blockStateModelGenerator.registerLog(ModBlocks.BB_LOG).log(ModBlocks.BB_LOG).wood(ModBlocks.BB_WOOD);
        blockStateModelGenerator.registerLog(ModBlocks.STRIPPED_BB_LOG).log(ModBlocks.STRIPPED_BB_LOG).wood(ModBlocks.STRIPPED_BB_WOOD);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.BB_LEAVES);


        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.BB_PLANKS).family(ModBlocks.BB_FAMILY);

        blockStateModelGenerator.registerParentedItemModel(ModItems.WHITE_RABBIT_SPAWN_EGG, ModelIds.getMinecraftNamespacedItem("template_spawn_egg"));
        blockStateModelGenerator.registerParentedItemModel(ModItems.WEEPING_PLAYERS_SPAWN_EGG, ModelIds.getMinecraftNamespacedItem("template_spawn_egg"));





    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {


        for (Item item : ModItems.STORYBOOK_ITEMS) {
            itemModelGenerator.register(item, Models.GENERATED);
        }

        itemModelGenerator.register(ModItems.HANGING_WW_SIGN, Models.GENERATED);
        itemModelGenerator.register(ModItems.HANGING_BB_SIGN, Models.GENERATED);
        itemModelGenerator.register(ModItems.HANGING_TH_SIGN, Models.GENERATED);
        itemModelGenerator.register(ModItems.POCKETWATCH, Models.GENERATED);
        itemModelGenerator.register(ModItems.EMPTY_CUP, Models.GENERATED);
        itemModelGenerator.register(ModItems.DRINK_ME, Models.GENERATED);
        itemModelGenerator.register(ModItems.EAT_ME, Models.GENERATED);
        itemModelGenerator.register(ModItems.EXTERIOR_KEY, Models.GENERATED);
    }


}
