package net.emanueljdf09.dtrhmod.datagen;

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

        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.WONDER_DIRT);

        blockStateModelGenerator.registerTintableCross(ModBlocks.TH_SAPLING, BlockStateModelGenerator.TintType.NOT_TINTED);
        blockStateModelGenerator.registerTintableCross(ModBlocks.WW_SAPLING, BlockStateModelGenerator.TintType.NOT_TINTED);
        blockStateModelGenerator.registerTintableCross(ModBlocks.BB_SAPLING, BlockStateModelGenerator.TintType.NOT_TINTED);


        blockStateModelGenerator.registerLog(ModBlocks.TH_LOG).log(ModBlocks.TH_LOG).wood(ModBlocks.TH_WOOD);
        blockStateModelGenerator.registerLog(ModBlocks.STRIPPED_TH_LOG).log(ModBlocks.STRIPPED_TH_LOG).wood(ModBlocks.STRIPPED_TH_WOOD);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.TH_LEAVES);

        BlockStateModelGenerator.BlockTexturePool tuggleWoodPool
                =  blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.TH_PLANKS);

        tuggleWoodPool.button(ModBlocks.TH_BUTTON);
        tuggleWoodPool.stairs(ModBlocks.TH_STAIRS);
        tuggleWoodPool.slab(ModBlocks.TH_SLABS);
        tuggleWoodPool.pressurePlate(ModBlocks.TH_PRESSURE_PLATE);
        tuggleWoodPool.fence(ModBlocks.TH_FENCE);
        tuggleWoodPool.fenceGate(ModBlocks.TH_FENCE_GATE);
        tuggleWoodPool.wall(ModBlocks.TH_WALL);

        blockStateModelGenerator.registerDoor(ModBlocks.TH_DOOR);
        blockStateModelGenerator.registerTrapdoor(ModBlocks.TH_TRAPDOOR);

        blockStateModelGenerator.registerLog(ModBlocks.WW_LOG).log(ModBlocks.WW_LOG).wood(ModBlocks.WW_WOOD);
        blockStateModelGenerator.registerLog(ModBlocks.STRIPPED_WW_LOG).log(ModBlocks.STRIPPED_WW_LOG).wood(ModBlocks.STRIPPED_WW_WOOD);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.WW_LEAVES);
        blockStateModelGenerator.registerPlantPart(ModBlocks.WW_HANGING_LEAVES_PLANT, ModBlocks.WW_HANGING_LEAVES, BlockStateModelGenerator.TintType.NOT_TINTED);

        BlockStateModelGenerator.BlockTexturePool weepingWoodPool
                =  blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.WW_PLANKS);

        weepingWoodPool.button(ModBlocks.WW_BUTTON);
        weepingWoodPool.stairs(ModBlocks.WW_STAIRS);
        weepingWoodPool.slab(ModBlocks.WW_SLABS);
        weepingWoodPool.pressurePlate(ModBlocks.WW_PRESSURE_PLATE);
        weepingWoodPool.fence(ModBlocks.WW_FENCE);
        weepingWoodPool.fenceGate(ModBlocks.WW_FENCE_GATE);
        weepingWoodPool.wall(ModBlocks.WW_WALL);

        blockStateModelGenerator.registerDoor(ModBlocks.WW_DOOR);
        blockStateModelGenerator.registerTrapdoor(ModBlocks.WW_TRAPDOOR);

        blockStateModelGenerator.registerLog(ModBlocks.BB_LOG).log(ModBlocks.BB_LOG).wood(ModBlocks.BB_WOOD);
        blockStateModelGenerator.registerLog(ModBlocks.STRIPPED_BB_LOG).log(ModBlocks.STRIPPED_BB_LOG).wood(ModBlocks.STRIPPED_BB_WOOD);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.BB_LEAVES);

        BlockStateModelGenerator.BlockTexturePool bandersnatchWoodPool
                =  blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.BB_PLANKS);

        bandersnatchWoodPool.button(ModBlocks.BB_BUTTON);
        bandersnatchWoodPool.stairs(ModBlocks.BB_STAIRS);
        bandersnatchWoodPool.slab(ModBlocks.BB_SLABS);
        bandersnatchWoodPool.pressurePlate(ModBlocks.BB_PRESSURE_PLATE);
        bandersnatchWoodPool.fence(ModBlocks.BB_FENCE);
        bandersnatchWoodPool.fenceGate(ModBlocks.BB_FENCE_GATE);
        bandersnatchWoodPool.wall(ModBlocks.BB_WALL);

        blockStateModelGenerator.registerDoor(ModBlocks.BB_DOOR);
        blockStateModelGenerator.registerTrapdoor(ModBlocks.BB_TRAPDOOR);

        blockStateModelGenerator.registerParentedItemModel(ModItems.WHITE_RABBIT_SPAWN_EGG, ModelIds.getMinecraftNamespacedItem("template_spawn_egg"));
        blockStateModelGenerator.registerParentedItemModel(ModItems.WEEPING_PLAYERS_SPAWN_EGG, ModelIds.getMinecraftNamespacedItem("template_spawn_egg"));





    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {


        for (Item item : ModItems.STORYBOOK_ITEMS) {
            itemModelGenerator.register(item, Models.GENERATED);
        }

        itemModelGenerator.register(ModItems.POCKETWATCH, Models.GENERATED);
        itemModelGenerator.register(ModItems.EMPTY_CUP, Models.GENERATED);
        itemModelGenerator.register(ModItems.DRINK_ME, Models.GENERATED);
        itemModelGenerator.register(ModItems.EAT_ME, Models.GENERATED);
        itemModelGenerator.register(ModItems.EXTERIOR_KEY, Models.GENERATED);
    }


}
