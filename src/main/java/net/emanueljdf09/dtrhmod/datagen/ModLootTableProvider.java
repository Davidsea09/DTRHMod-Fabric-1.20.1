package net.emanueljdf09.dtrhmod.datagen;

import net.emanueljdf09.dtrhmod.block.ModBlocks;
import net.emanueljdf09.dtrhmod.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
    public ModLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {

        flowerbedDrops(ModBlocks.LAWN_DAISY_PATCH);
        addDropWithSilkTouch(ModBlocks.WONDER_GRASS, ModBlocks.WONDER_DIRT);
        addDrop(ModBlocks.WONDER_DIRT);
        addDrop(ModBlocks.TEAPOT_BLOCK);

        addDrop(ModBlocks.MIRROR_BLOCK);
        addDrop(ModBlocks.BLUE_MUSHROOM);
        mushroomBlockDrops(ModBlocks.BLUE_MUSHROOM_BLOCK, ModBlocks.BLUE_MUSHROOM);
        addDrop(ModBlocks.MAGENTA_MUSHROOM);
        mushroomBlockDrops(ModBlocks.MAGENTA_MUSHROOM_BLOCK, ModBlocks.MAGENTA_MUSHROOM);
        addDrop(ModBlocks.YELLOW_MUSHROOM);
        mushroomBlockDrops(ModBlocks.YELLOW_MUSHROOM_BLOCK, ModBlocks.YELLOW_MUSHROOM);

        addDrop(ModBlocks.TH_SAPLING);
        addDrop(ModBlocks.TH_LOG);
        addDrop(ModBlocks.STRIPPED_TH_LOG);
        addDrop(ModBlocks.STRIPPED_TH_WOOD);
        addDrop(ModBlocks.TH_WOOD);
        addDrop(ModBlocks.TH_PLANKS);
        addDrop(ModBlocks.TH_SLABS, slabDrops(ModBlocks.TH_SLABS));
        addDrop(ModBlocks.TH_STAIRS);
        addDrop(ModBlocks.TH_PRESSURE_PLATE);
        addDrop(ModBlocks.TH_DOOR, doorDrops(ModBlocks.TH_DOOR));
        addDrop(ModBlocks.TH_TRAPDOOR);
        addDrop(ModBlocks.TH_BUTTON);
        addDrop(ModBlocks.TH_FENCE);
        addDrop(ModBlocks.TH_FENCE_GATE);
        addDrop(ModBlocks.TH_SIGN, ModItems.TH_SIGN);
        addDrop(ModBlocks.TH_HANGING_SIGN, ModItems.HANGING_TH_SIGN);

        addDrop(ModBlocks.TH_LEAVES, leavesDrops(ModBlocks.TH_LEAVES, ModBlocks.TH_SAPLING, 0.0025f));

        addDrop(ModBlocks.WW_SAPLING);
        addDrop(ModBlocks.WW_LOG);
        addDrop(ModBlocks.STRIPPED_WW_LOG);
        addDrop(ModBlocks.STRIPPED_WW_WOOD);
        addDrop(ModBlocks.WW_WOOD);
        addDrop(ModBlocks.WW_PLANKS);
        addDrop(ModBlocks.WW_SLABS, slabDrops(ModBlocks.WW_SLABS));
        addDrop(ModBlocks.WW_STAIRS);
        addDrop(ModBlocks.WW_PRESSURE_PLATE);
        addDrop(ModBlocks.WW_DOOR, doorDrops(ModBlocks.WW_DOOR));
        addDrop(ModBlocks.WW_TRAPDOOR);
        addDrop(ModBlocks.WW_BUTTON);
        addDrop(ModBlocks.WW_FENCE);
        addDrop(ModBlocks.WW_FENCE_GATE);
        addDrop(ModBlocks.WW_SIGN, ModItems.WW_SIGN);
        addDrop(ModBlocks.WW_HANGING_SIGN, ModItems.HANGING_WW_SIGN);

        addDrop(ModBlocks.WW_LEAVES, leavesDrops(ModBlocks.WW_LEAVES, ModBlocks.WW_SAPLING, 0.0025f));
        addDrop(ModBlocks.WW_HANGING_LEAVES, dropsWithShears(ModBlocks.WW_HANGING_LEAVES));
        addDrop(ModBlocks.WW_HANGING_LEAVES_PLANT, dropsWithShears(ModBlocks.WW_HANGING_LEAVES));

        addDrop(ModBlocks.BB_SAPLING);
        addDrop(ModBlocks.BB_LOG);
        addDrop(ModBlocks.STRIPPED_BB_LOG);
        addDrop(ModBlocks.STRIPPED_BB_WOOD);
        addDrop(ModBlocks.BB_WOOD);
        addDrop(ModBlocks.BB_PLANKS);
        addDrop(ModBlocks.BB_SLABS, slabDrops(ModBlocks.BB_SLABS));
        addDrop(ModBlocks.BB_STAIRS);
        addDrop(ModBlocks.BB_PRESSURE_PLATE);
        addDrop(ModBlocks.BB_DOOR, doorDrops(ModBlocks.BB_DOOR));
        addDrop(ModBlocks.BB_TRAPDOOR);
        addDrop(ModBlocks.BB_BUTTON);
        addDrop(ModBlocks.BB_FENCE);
        addDrop(ModBlocks.BB_FENCE_GATE);
        addDrop(ModBlocks.BB_SIGN, ModItems.BB_SIGN);
        addDrop(ModBlocks.BB_HANGING_SIGN, ModItems.HANGING_BB_SIGN);

        addDrop(ModBlocks.BB_LEAVES, leavesDrops(ModBlocks.BB_LEAVES, ModBlocks.BB_SAPLING, 0.0025f));

        addDrop(ModBlocks.HH_SAPLING);
        addDrop(ModBlocks.HH_LOG);
        addDrop(ModBlocks.STRIPPED_HH_LOG);
        addDrop(ModBlocks.STRIPPED_HH_WOOD);
        addDrop(ModBlocks.HH_WOOD);
        addDrop(ModBlocks.HH_PLANKS);
        addDrop(ModBlocks.HH_SLABS, slabDrops(ModBlocks.HH_SLABS));
        addDrop(ModBlocks.HH_STAIRS);
        addDrop(ModBlocks.HH_PRESSURE_PLATE);
        addDrop(ModBlocks.HH_DOOR, doorDrops(ModBlocks.HH_DOOR));
        addDrop(ModBlocks.HH_TRAPDOOR);
        addDrop(ModBlocks.HH_BUTTON);
        addDrop(ModBlocks.HH_FENCE);
        addDrop(ModBlocks.HH_FENCE_GATE);
        addDrop(ModBlocks.HH_SIGN, ModItems.HH_SIGN);
        addDrop(ModBlocks.HH_HANGING_SIGN, ModItems.HANGING_HH_SIGN);

        addDrop(ModBlocks.HH_LEAVES, leavesDrops(ModBlocks.HH_LEAVES, ModBlocks.HH_SAPLING, 0.0025f));
    }
}
