package net.emanueljdf09.dtrhmod.datagen;

import net.emanueljdf09.dtrhmod.block.ModBlocks;
import net.emanueljdf09.dtrhmod.util.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {

        getOrCreateTagBuilder(BlockTags.MUSHROOM_GROW_BLOCK)
                .add(ModBlocks.WONDER_DIRT)
                        .add(ModBlocks.WONDER_GRASS);

        getOrCreateTagBuilder(BlockTags.LOGS_THAT_BURN)
                .add(ModBlocks.TH_LOG)
                .add(ModBlocks.STRIPPED_TH_LOG)
                .add(ModBlocks.TH_WOOD)
                .add(ModBlocks.STRIPPED_TH_WOOD)
                .add(ModBlocks.WW_LOG)
                .add(ModBlocks.STRIPPED_WW_LOG)
                .add(ModBlocks.WW_WOOD)
                .add(ModBlocks.STRIPPED_WW_WOOD)
                .add(ModBlocks.BB_LOG)
                .add(ModBlocks.STRIPPED_BB_LOG)
                .add(ModBlocks.BB_WOOD)
                .add(ModBlocks.STRIPPED_BB_WOOD);

        getOrCreateTagBuilder(ModTags.Blocks.WONDERLAND_LOGS)
                .addTag(ModTags.Blocks.TH_LOGS)
                .addTag(ModTags.Blocks.WW_LOGS)
                .addTag(ModTags.Blocks.BB_LOGS);

        getOrCreateTagBuilder(ModTags.Blocks.TH_LOGS)
                .add(ModBlocks.TH_LOG)
                .add(ModBlocks.STRIPPED_TH_LOG)
                .add(ModBlocks.TH_WOOD)
                .add(ModBlocks.STRIPPED_TH_WOOD);

        getOrCreateTagBuilder(ModTags.Blocks.WW_LOGS)
                .add(ModBlocks.WW_LOG)
                .add(ModBlocks.STRIPPED_WW_LOG)
                .add(ModBlocks.WW_WOOD)
                .add(ModBlocks.STRIPPED_WW_WOOD);

        getOrCreateTagBuilder(ModTags.Blocks.BB_LOGS)
                .add(ModBlocks.BB_LOG)
                .add(ModBlocks.STRIPPED_BB_LOG)
                .add(ModBlocks.BB_WOOD)
                .add(ModBlocks.STRIPPED_BB_WOOD);


        getOrCreateTagBuilder(BlockTags.PLANKS)
                .add(ModBlocks.TH_PLANKS)
                .add(ModBlocks.WW_PLANKS)
                .add(ModBlocks.BB_PLANKS);

        getOrCreateTagBuilder(BlockTags.WOODEN_FENCES)
                .add(ModBlocks.TH_FENCE)
                .add(ModBlocks.WW_FENCE)
                .add(ModBlocks.BB_FENCE);

        getOrCreateTagBuilder(BlockTags.FENCE_GATES)
                .add(ModBlocks.TH_FENCE_GATE)
                .add(ModBlocks.WW_FENCE_GATE)
                .add(ModBlocks.BB_FENCE_GATE);

        getOrCreateTagBuilder(BlockTags.WALLS)
                .add(ModBlocks.TH_WALL)
                .add(ModBlocks.WW_WALL)
                .add(ModBlocks.BB_WALL);

        getOrCreateTagBuilder(BlockTags.WOODEN_DOORS)
                .add(ModBlocks.TH_DOOR)
                .add(ModBlocks.WW_DOOR)
                .add(ModBlocks.BB_DOOR);

        getOrCreateTagBuilder(BlockTags.WOODEN_TRAPDOORS)
                .add(ModBlocks.TH_TRAPDOOR)
                .add(ModBlocks.WW_TRAPDOOR)
                .add(ModBlocks.BB_TRAPDOOR);

        getOrCreateTagBuilder(BlockTags.WOODEN_BUTTONS)
                .add(ModBlocks.TH_BUTTON)
                .add(ModBlocks.WW_BUTTON)
                .add(ModBlocks.BB_BUTTON);

        getOrCreateTagBuilder(BlockTags.WOODEN_SLABS)
                .add(ModBlocks.TH_SLABS)
                .add(ModBlocks.WW_SLABS)
                .add(ModBlocks.BB_SLABS);

        getOrCreateTagBuilder(BlockTags.WOODEN_STAIRS)
                .add(ModBlocks.TH_STAIRS)
                .add(ModBlocks.WW_STAIRS)
                .add(ModBlocks.BB_STAIRS);

        getOrCreateTagBuilder(BlockTags.WOODEN_PRESSURE_PLATES)
                .add(ModBlocks.TH_PRESSURE_PLATE)
                .add(ModBlocks.WW_PRESSURE_PLATE)
                .add(ModBlocks.BB_PRESSURE_PLATE);

        getOrCreateTagBuilder(BlockTags.LEAVES)
                .add(ModBlocks.TH_LEAVES)
                .add(ModBlocks.WW_LEAVES)
                .add(ModBlocks.BB_LEAVES);

        getOrCreateTagBuilder(BlockTags.DIRT)
                .add(ModBlocks.WONDER_DIRT)
                .add(ModBlocks.WONDER_GRASS);


    }
}
