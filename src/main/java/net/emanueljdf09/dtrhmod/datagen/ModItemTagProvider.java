package net.emanueljdf09.dtrhmod.datagen;

import net.emanueljdf09.dtrhmod.block.ModBlocks;
import net.emanueljdf09.dtrhmod.util.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {

        getOrCreateTagBuilder(ItemTags.DIRT)
                .add(ModBlocks.WONDER_DIRT.asItem())
                        .add(ModBlocks.WONDER_GRASS.asItem());

        getOrCreateTagBuilder(ItemTags.LOGS_THAT_BURN)
                .add(ModBlocks.TH_LOG.asItem())
                .add(ModBlocks.STRIPPED_TH_LOG.asItem())
                .add(ModBlocks.TH_WOOD.asItem())
                .add(ModBlocks.STRIPPED_TH_WOOD.asItem())
                .add(ModBlocks.WW_LOG.asItem())
                .add(ModBlocks.STRIPPED_WW_LOG.asItem())
                .add(ModBlocks.WW_WOOD.asItem())
                .add(ModBlocks.STRIPPED_WW_WOOD.asItem())
                .add(ModBlocks.BB_LOG.asItem())
                .add(ModBlocks.STRIPPED_BB_LOG.asItem())
                .add(ModBlocks.BB_WOOD.asItem())
                .add(ModBlocks.STRIPPED_BB_WOOD.asItem())
                .add(ModBlocks.HH_LOG.asItem())
                .add(ModBlocks.STRIPPED_HH_LOG.asItem())
                .add(ModBlocks.HH_WOOD.asItem())
                .add(ModBlocks.STRIPPED_HH_WOOD.asItem());

        getOrCreateTagBuilder(ModTags.Items.TH_LOGS)
                .add(ModBlocks.TH_LOG.asItem())
                .add(ModBlocks.STRIPPED_TH_LOG.asItem())
                .add(ModBlocks.TH_WOOD.asItem())
                .add(ModBlocks.STRIPPED_TH_WOOD.asItem());

    getOrCreateTagBuilder(ModTags.Items.WW_LOGS)
                .add(ModBlocks.WW_LOG.asItem())
                .add(ModBlocks.STRIPPED_WW_LOG.asItem())
                .add(ModBlocks.WW_WOOD.asItem())
                .add(ModBlocks.STRIPPED_WW_WOOD.asItem());

    getOrCreateTagBuilder(ModTags.Items.BB_LOGS)
                .add(ModBlocks.BB_LOG.asItem())
                .add(ModBlocks.STRIPPED_BB_LOG.asItem())
                .add(ModBlocks.BB_WOOD.asItem())
                .add(ModBlocks.STRIPPED_BB_WOOD.asItem());

    getOrCreateTagBuilder(ModTags.Items.HH_LOGS)
                .add(ModBlocks.HH_LOG.asItem())
                .add(ModBlocks.STRIPPED_HH_LOG.asItem())
                .add(ModBlocks.HH_WOOD.asItem())
                .add(ModBlocks.STRIPPED_HH_WOOD.asItem());


        getOrCreateTagBuilder(ItemTags.PLANKS)
                .add(ModBlocks.TH_PLANKS.asItem())
                .add(ModBlocks.WW_PLANKS.asItem())
                .add(ModBlocks.HH_PLANKS.asItem())
                .add(ModBlocks.BB_PLANKS.asItem());

        getOrCreateTagBuilder(ItemTags.FENCES)
                .add(ModBlocks.TH_FENCE.asItem())
                .add(ModBlocks.WW_FENCE.asItem())
                .add(ModBlocks.BB_FENCE.asItem())
                .add(ModBlocks.HH_FENCE.asItem());

        getOrCreateTagBuilder(ItemTags.FENCE_GATES)
                .add(ModBlocks.TH_FENCE_GATE.asItem())
                .add(ModBlocks.WW_FENCE_GATE.asItem())
                .add(ModBlocks.HH_FENCE_GATE.asItem())
                .add(ModBlocks.BB_FENCE_GATE.asItem());


        getOrCreateTagBuilder(ItemTags.DOORS)
                .add(ModBlocks.TH_DOOR.asItem())
                .add(ModBlocks.WW_DOOR.asItem())
                .add(ModBlocks.BB_DOOR.asItem())
                .add(ModBlocks.HH_DOOR.asItem());

        getOrCreateTagBuilder(ItemTags.TRAPDOORS)
                .add(ModBlocks.TH_TRAPDOOR.asItem())
                .add(ModBlocks.WW_TRAPDOOR.asItem())
                .add(ModBlocks.BB_TRAPDOOR.asItem())
                .add(ModBlocks.HH_TRAPDOOR.asItem());

        getOrCreateTagBuilder(ItemTags.BUTTONS)
                .add(ModBlocks.TH_BUTTON.asItem())
                .add(ModBlocks.WW_BUTTON.asItem())
                .add(ModBlocks.BB_BUTTON.asItem())
                .add(ModBlocks.HH_BUTTON.asItem());

        getOrCreateTagBuilder(ItemTags.SLABS)
                .add(ModBlocks.TH_SLABS.asItem())
                .add(ModBlocks.WW_SLABS.asItem())
                .add(ModBlocks.BB_SLABS.asItem())
                .add(ModBlocks.HH_SLABS.asItem());

        getOrCreateTagBuilder(ItemTags.STAIRS)
                .add(ModBlocks.TH_STAIRS.asItem())
                .add(ModBlocks.WW_STAIRS.asItem())
                .add(ModBlocks.BB_STAIRS.asItem())
                .add(ModBlocks.HH_STAIRS.asItem());

        getOrCreateTagBuilder(ItemTags.WOODEN_PRESSURE_PLATES)
                .add(ModBlocks.TH_PRESSURE_PLATE.asItem())
                .add(ModBlocks.WW_PRESSURE_PLATE.asItem())
                .add(ModBlocks.BB_PRESSURE_PLATE.asItem())
                .add(ModBlocks.HH_PRESSURE_PLATE.asItem());

        getOrCreateTagBuilder(ItemTags.LEAVES)
                .add(ModBlocks.TH_LEAVES.asItem())
                .add(ModBlocks.WW_LEAVES.asItem())
                .add(ModBlocks.BB_LEAVES.asItem())
                .add(ModBlocks.HH_LEAVES.asItem());

        getOrCreateTagBuilder(ItemTags.SAPLINGS)
                .add(ModBlocks.BB_SAPLING.asItem())
                .add(ModBlocks.TH_SAPLING.asItem())
                .add(ModBlocks.WW_SAPLING.asItem())
                .add(ModBlocks.HH_SAPLING.asItem());




    }
}
