package net.emanueljdf09.dtrhmod.world.features;

import dev.corgitaco.ohthetreesyoullgrow.world.level.levelgen.feature.TYGFeatures;
import dev.corgitaco.ohthetreesyoullgrow.world.level.levelgen.feature.configurations.TreeFromStructureNBTConfig;
import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.emanueljdf09.dtrhmod.block.ModBlocks;
import net.emanueljdf09.dtrhmod.world.features.tree.deco.HangingLeavesTreeDeco;
import net.emanueljdf09.dtrhmod.world.features.tree.deco.HangFromTreeDeco;
import net.minecraft.block.BigDripleafBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerbedBlock;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.intprovider.BiasedToBottomIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.PredicatedStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;

import java.util.List;
import java.util.Set;

import static net.minecraft.block.LanternBlock.HANGING;

public class ModConfiguredFeatures {

    public static final RegistryKey<ConfiguredFeature<?, ?>> FOREST_ROCKS = registerKey("forest_rocks");
    public static final RegistryKey<ConfiguredFeature<?, ?>> FOREST_FLOWERBEDS = registerKey("forest_flowerbeds");
    public static final RegistryKey<ConfiguredFeature<?, ?>> FOREST_GROUND_MUSHROOMS = registerKey("forest_ground_mushrooms");

    public static final RegistryKey<ConfiguredFeature<?, ?>> DISK_CLAY = registerKey("disk_clay");
    public static final RegistryKey<ConfiguredFeature<?, ?>> DISK_SAND = registerKey("disk_sand");
    public static final RegistryKey<ConfiguredFeature<?, ?>> DISK_GRAVEL = registerKey("disk_gravel");

    public static final RegistryKey<ConfiguredFeature<?, ?>> TH_TREE1 = registerKey("th_tree1");
    public static final RegistryKey<ConfiguredFeature<?, ?>> TH_TREE2 = registerKey("th_tree2");
    public static final RegistryKey<ConfiguredFeature<?, ?>> TH_TREE3 = registerKey("th_tree3");


    public static final RegistryKey<ConfiguredFeature<?, ?>> BB_TREE1 = registerKey("bb_tree1");
    public static final RegistryKey<ConfiguredFeature<?, ?>> BB_TREE2 = registerKey("bb_tree2");
    public static final RegistryKey<ConfiguredFeature<?, ?>> BB_TREE3 = registerKey("bb_tree3");
    public static final RegistryKey<ConfiguredFeature<?, ?>> BB_STUMP = registerKey("bb_stump");

    public static final RegistryKey<ConfiguredFeature<?, ?>> WW_TREE1 = registerKey("ww_tree1");
    public static final RegistryKey<ConfiguredFeature<?, ?>> WW_TREE2 = registerKey("ww_tree2");
    public static final RegistryKey<ConfiguredFeature<?, ?>> WW_TREE3 = registerKey("ww_tree3");
    public static final RegistryKey<ConfiguredFeature<?, ?>> WW_TREE4 = registerKey("ww_tree4");
    public static final RegistryKey<ConfiguredFeature<?, ?>> DEAD_WW_TREE = registerKey("dead_ww_tree");

    public static final RegistryKey<ConfiguredFeature<?, ?>> HH_TREE1 = registerKey("hh_tree1");
    public static final RegistryKey<ConfiguredFeature<?, ?>> HH_TREE2 = registerKey("hh_tree2");
    public static final RegistryKey<ConfiguredFeature<?, ?>> HH_TREE3 = registerKey("hh_tree3");
    public static final RegistryKey<ConfiguredFeature<?, ?>> HH_TREE4 = registerKey("hh_tree4");
    public static final RegistryKey<ConfiguredFeature<?, ?>> HH_TREE5 = registerKey("hh_tree5");

    public static final RegistryKey<ConfiguredFeature<?, ?>> WW_TREE_SELECTOR = registerKey("ww_tree");
    public static final RegistryKey<ConfiguredFeature<?, ?>> TH_TREE_SELECTOR = registerKey("th_tree");
    public static final RegistryKey<ConfiguredFeature<?, ?>> BB_TREE_SELECTOR = registerKey("bb_tree");
    public static final RegistryKey<ConfiguredFeature<?, ?>> HH_TREE_SELECTOR = registerKey("hh_tree");
    public static final RegistryKey<ConfiguredFeature<?, ?>> TULGEY_TREE_SELECTOR = registerKey("tulgey_tree");


    public static final RegistryKey<ConfiguredFeature<?, ?>> BIG_BLUE_MUSHROOM1 = registerKey("big_blue_mushroom1");
    public static final RegistryKey<ConfiguredFeature<?, ?>> BIG_BLUE_MUSHROOM2 = registerKey("big_blue_mushroom2");
    public static final RegistryKey<ConfiguredFeature<?, ?>> BIG_BLUE_MUSHROOM3 = registerKey("big_blue_mushroom3");
    public static final RegistryKey<ConfiguredFeature<?, ?>> BIG_BLUE_MUSHROOM4 = registerKey("big_blue_mushroom4");
    public static final RegistryKey<ConfiguredFeature<?, ?>> BIG_BLUE_MUSHROOM_SELECTOR = registerKey("big_blue_mushroom");

    public static final RegistryKey<ConfiguredFeature<?, ?>> BIG_MAGENTA_MUSHROOM1 = registerKey("big_magenta_mushroom1");
    public static final RegistryKey<ConfiguredFeature<?, ?>> BIG_MAGENTA_MUSHROOM2 = registerKey("big_magenta_mushroom2");
    public static final RegistryKey<ConfiguredFeature<?, ?>> BIG_MAGENTA_MUSHROOM3 = registerKey("big_magenta_mushroom3");
    public static final RegistryKey<ConfiguredFeature<?, ?>> BIG_MAGENTA_MUSHROOM4 = registerKey("big_magenta_mushroom4");
    public static final RegistryKey<ConfiguredFeature<?, ?>> BIG_MAGENTA_MUSHROOM_SELECTOR = registerKey("big_magenta_mushroom");

    public static final RegistryKey<ConfiguredFeature<?, ?>> BIG_YELLOW_MUSHROOM1 = registerKey("big_yellow_mushroom1");
    public static final RegistryKey<ConfiguredFeature<?, ?>> BIG_YELLOW_MUSHROOM2 = registerKey("big_yellow_mushroom2");
    public static final RegistryKey<ConfiguredFeature<?, ?>> BIG_YELLOW_MUSHROOM3 = registerKey("big_yellow_mushroom3");
    public static final RegistryKey<ConfiguredFeature<?, ?>> BIG_YELLOW_MUSHROOM_SELECTOR = registerKey("big_yellow_mushroom");

    public static final RegistryKey<ConfiguredFeature<?, ?>> VALE_OF_TEARS_SELECTOR = registerKey("vale_of_tears_vegetation");

    public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> context) {
        var placedLookup = context.getRegistryLookup(RegistryKeys.PLACED_FEATURE);
        var configuredLookup = context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);

        register(context, FOREST_ROCKS,Feature.SIMPLE_BLOCK,
                new SimpleBlockFeatureConfig(BlockStateProvider.of(Blocks.STONE_SLAB))
        );

        DataPool.Builder<BlockState> builder_flowerbeds = new DataPool.Builder<>();

        for (int i = 1; i <= 4; ++i) {
            for (Direction direction : Direction.values()) {
                if (direction.getAxis().isHorizontal()) {
                    builder_flowerbeds.add(
                            ModBlocks.LAWN_DAISY_PATCH.getDefaultState()
                                    .with(FlowerbedBlock.FLOWER_AMOUNT, i)
                                    .with(FlowerbedBlock.FACING, direction),
                            1
                    );
                }
            }
        }

        register(context, FOREST_FLOWERBEDS, Feature.FLOWER,
                new RandomPatchFeatureConfig(
                        16,
                        6,
                        2,
                        PlacedFeatures.createEntry(
                                Feature.SIMPLE_BLOCK,
                                new SimpleBlockFeatureConfig(
                                        new WeightedBlockStateProvider(builder_flowerbeds)
                                )
                        )
                )
        );

        register(context, FOREST_GROUND_MUSHROOMS, Feature.FLOWER,
                new RandomPatchFeatureConfig(
                        16,
                        6,
                        2,
                        PlacedFeatures.createEntry(
                                Feature.SIMPLE_BLOCK,
                                new SimpleBlockFeatureConfig(
                                        new WeightedBlockStateProvider(
                                                DataPool.<BlockState>builder()
                                                        .add(ModBlocks.YELLOW_MUSHROOM.getDefaultState(), 3) // Common
                                                        .add(ModBlocks.BLUE_MUSHROOM.getDefaultState(), 2) // Uncommon
                                                        .add(ModBlocks.MAGENTA_MUSHROOM.getDefaultState(), 1) // Rare
                                                        .build()
                                        )
                                )
                        )
                )
        );


        register(context, DISK_CLAY, Feature.DISK, new DiskFeatureConfig(PredicatedStateProvider.of(Blocks.CLAY), BlockPredicate.matchingBlocks(List.of(ModBlocks.WONDER_DIRT, Blocks.CLAY)), UniformIntProvider.create(2, 3), 1));
        register(context, DISK_GRAVEL, Feature.DISK, new DiskFeatureConfig(PredicatedStateProvider.of(Blocks.GRAVEL), BlockPredicate.matchingBlocks(List.of(ModBlocks.WONDER_DIRT, Blocks.GRASS_BLOCK)), UniformIntProvider.create(2, 5), 2));
        register(context, DISK_SAND, Feature.DISK, new DiskFeatureConfig(new PredicatedStateProvider(BlockStateProvider.of(Blocks.SAND), List.of(new PredicatedStateProvider.Rule(BlockPredicate.matchingBlocks(Direction.DOWN.getVector(), Blocks.AIR), BlockStateProvider.of(Blocks.SANDSTONE)))), BlockPredicate.matchingBlocks(List.of(ModBlocks.WONDER_DIRT, ModBlocks.WONDER_GRASS)), UniformIntProvider.create(2, 6), 2));

        register(context, BB_TREE1, TYGFeatures.TREE_FROM_NBT_V1.get(),
                new TreeFromStructureNBTConfig.Builder()
                        .baseLocation(new Identifier(DownTheRabbitHole.MOD_ID, "trees/bb/forgotten_tree_trunk1"))
                        .canopyLocation(new Identifier(DownTheRabbitHole.MOD_ID, "trees/bb/forgotten_tree_canopy1"))
                        .height(BiasedToBottomIntProvider.create(5, 15))
                        .logProvider(BlockStateProvider.of(ModBlocks.BB_LOG))
                        .leavesProvider(BlockStateProvider.of(ModBlocks.BB_LEAVES))
                        .logTarget(Set.of(Blocks.OAK_LOG))
                        .leavesTarget(Set.of(Blocks.OAK_LEAVES))
                        .growableOn(BlockPredicate.matchingBlockTag(BlockTags.DIRT))
                        .maxLogDepth(5)
                        .build()


        );

        register(context, BB_TREE2, TYGFeatures.TREE_FROM_NBT_V1.get(),
                new TreeFromStructureNBTConfig.Builder()
                        .baseLocation(new Identifier(DownTheRabbitHole.MOD_ID, "trees/bb/enchanted_trunk_2"))
                        .canopyLocation(new Identifier(DownTheRabbitHole.MOD_ID, "trees/bb/enchanted_canopy_2"))
                        .height(BiasedToBottomIntProvider.create(1, 5))
                        .logProvider(BlockStateProvider.of(ModBlocks.BB_LOG))
                        .leavesProvider(BlockStateProvider.of(ModBlocks.BB_LEAVES))
                        .logTarget(Set.of(Blocks.OAK_LOG))
                        .leavesTarget(Set.of(Blocks.OAK_LEAVES))
                        .growableOn(BlockPredicate.matchingBlockTag(BlockTags.DIRT))
                        .maxLogDepth(5)
                        .build()


        );

        register(context, BB_TREE3, TYGFeatures.TREE_FROM_NBT_V1.get(),
                new TreeFromStructureNBTConfig.Builder()
                        .baseLocation(new Identifier(DownTheRabbitHole.MOD_ID, "trees/bb/enchanted_trunk_3"))
                        .canopyLocation(new Identifier(DownTheRabbitHole.MOD_ID, "trees/bb/enchanted_canopy_3"))
                        .height(BiasedToBottomIntProvider.create(1, 5))
                        .logProvider(BlockStateProvider.of(ModBlocks.BB_LOG))
                        .leavesProvider(BlockStateProvider.of(ModBlocks.BB_LEAVES))
                        .logTarget(Set.of(Blocks.OAK_LOG))
                        .leavesTarget(Set.of(Blocks.OAK_LEAVES))
                        .growableOn(BlockPredicate.matchingBlockTag(BlockTags.DIRT))
                        .maxLogDepth(5)
//                      .treeDecorators(List.of(
//                                new HangFromTreeDeco(
//                                        1,
//                                        1,
//                                        2,
//                                        4,
//                                        2,
//                                        BlockStateProvider.of(Blocks.CHAIN),
//                                        BlockStateProvider.of(Blocks.LANTERN.getDefaultState().with(HANGING, true))))
//                        )
                        .build()


        );

        register(context, BB_STUMP, TYGFeatures.TREE_FROM_NBT_V1.get(),
                new TreeFromStructureNBTConfig.Builder()
                        .baseLocation(new Identifier(DownTheRabbitHole.MOD_ID, "trees/bb/woodlands_stump_trunk1"))
                        .canopyLocation(new Identifier(DownTheRabbitHole.MOD_ID, "trees/bb/woodlands_stump_canopy1"))
                        .height(BiasedToBottomIntProvider.create(2, 5))
                        .logProvider(BlockStateProvider.of(ModBlocks.BB_LOG))
                        .leavesProvider(BlockStateProvider.of(ModBlocks.BB_LEAVES))
                        .logTarget(Set.of(Blocks.OAK_LOG))
                        .leavesTarget(Set.of(Blocks.OAK_LEAVES))
                        .growableOn(BlockPredicate.matchingBlockTag(BlockTags.DIRT))
                        .maxLogDepth(5)
                        .build()


        );

        register(context, BB_TREE_SELECTOR, Feature.RANDOM_SELECTOR, new RandomFeatureConfig(
                List.of(
                        new RandomFeatureEntry(
                                RegistryEntry.of(new PlacedFeature(configuredLookup.getOrThrow(ModConfiguredFeatures.BB_TREE1),
                                        List.of())),
                                0.35f
                        ),
                        new RandomFeatureEntry(
                                RegistryEntry.of(new PlacedFeature(configuredLookup.getOrThrow(ModConfiguredFeatures.BB_TREE2),
                                        List.of())),
                                0.35f
                        )
                ),
                RegistryEntry.of(new PlacedFeature(configuredLookup.getOrThrow(ModConfiguredFeatures.BB_TREE3), List.of()))
        ));


        register(context, TH_TREE1, TYGFeatures.TREE_FROM_NBT_V1.get(),
                new TreeFromStructureNBTConfig.Builder()
                        .baseLocation(new Identifier(DownTheRabbitHole.MOD_ID, "trees/th/ancient_tree_trunk1"))
                        .canopyLocation(new Identifier(DownTheRabbitHole.MOD_ID, "trees/th/ancient_tree_canopy1"))
                        .height(BiasedToBottomIntProvider.create(5, 15))
                        .logProvider(BlockStateProvider.of(ModBlocks.TH_LOG))
                        .leavesProvider(BlockStateProvider.of(ModBlocks.TH_LEAVES))
                        .logTarget(Set.of(Blocks.DARK_OAK_LOG))
                        .leavesTarget(Set.of(Blocks.DARK_OAK_LEAVES))
                        .growableOn(BlockPredicate.matchingBlockTag(BlockTags.DIRT))
                        .maxLogDepth(5)
                        .build()


        );

        register(context, TH_TREE2, TYGFeatures.TREE_FROM_NBT_V1.get(),
                new TreeFromStructureNBTConfig.Builder()
                        .baseLocation(new Identifier(DownTheRabbitHole.MOD_ID, "trees/th/ancient_tree_trunk2"))
                        .canopyLocation(new Identifier(DownTheRabbitHole.MOD_ID, "trees/th/ancient_tree_canopy2"))
                        .height(BiasedToBottomIntProvider.create(5, 15))
                        .logProvider(BlockStateProvider.of(ModBlocks.TH_LOG))
                        .leavesProvider(BlockStateProvider.of(ModBlocks.TH_LEAVES))
                        .logTarget(Set.of(Blocks.OAK_LOG))
                        .leavesTarget(Set.of(Blocks.OAK_LEAVES))
                        .growableOn(BlockPredicate.matchingBlockTag(BlockTags.DIRT))
                        .maxLogDepth(5)
                        .build()


        );

        register(context, TH_TREE3, TYGFeatures.TREE_FROM_NBT_V1.get(),
                new TreeFromStructureNBTConfig.Builder()
                        .baseLocation(new Identifier(DownTheRabbitHole.MOD_ID, "trees/th/ancient_tree_trunk3"))
                        .canopyLocation(new Identifier(DownTheRabbitHole.MOD_ID, "trees/th/ancient_tree_canopy3"))
                        .height(BiasedToBottomIntProvider.create(5, 15))
                        .logProvider(BlockStateProvider.of(ModBlocks.TH_LOG))
                        .leavesProvider(BlockStateProvider.of(ModBlocks.TH_LEAVES))
                        .logTarget(Set.of(Blocks.OAK_LOG))
                        .leavesTarget(Set.of(Blocks.OAK_LEAVES))
                        .growableOn(BlockPredicate.matchingBlockTag(BlockTags.DIRT))
                        .maxLogDepth(5)
                        .build()


        );

        register(context, TH_TREE_SELECTOR, Feature.RANDOM_SELECTOR, new RandomFeatureConfig(
                List.of(
                        new RandomFeatureEntry(
                                RegistryEntry.of(new PlacedFeature(configuredLookup.getOrThrow(ModConfiguredFeatures.TH_TREE1),
                                        List.of())),
                                0.33f
                        ),
                        new RandomFeatureEntry(
                                RegistryEntry.of(new PlacedFeature(configuredLookup.getOrThrow(ModConfiguredFeatures.TH_TREE2),
                                        List.of())),
                                0.33f
                        )
                ),
                RegistryEntry.of(new PlacedFeature(configuredLookup.getOrThrow(ModConfiguredFeatures.TH_TREE3), List.of()))
        ));

        register(context, TULGEY_TREE_SELECTOR, Feature.RANDOM_SELECTOR, new RandomFeatureConfig(
                List.of(
                        new RandomFeatureEntry(
                                RegistryEntry.of(new PlacedFeature(configuredLookup.getOrThrow(ModConfiguredFeatures.TH_TREE_SELECTOR),
                                        List.of())),
                                0.50f
                        ),
                        new RandomFeatureEntry(
                                RegistryEntry.of(new PlacedFeature(configuredLookup.getOrThrow(ModConfiguredFeatures.BB_STUMP),
                                        List.of())),
                                0.02f
                        )
                ),
                RegistryEntry.of(new PlacedFeature(configuredLookup.getOrThrow(ModConfiguredFeatures.BB_TREE_SELECTOR), List.of()))
        ));

        register(context, WW_TREE1, TYGFeatures.TREE_FROM_NBT_V1.get(),
                new TreeFromStructureNBTConfig.Builder()
                        .baseLocation(new Identifier(DownTheRabbitHole.MOD_ID, "trees/ww/willow_trunk1"))
                        .canopyLocation(new Identifier(DownTheRabbitHole.MOD_ID, "trees/ww/willow_canopy1"))
                        .height(BiasedToBottomIntProvider.create(5, 15))
                        .logProvider(BlockStateProvider.of(ModBlocks.WW_LOG))
                        .leavesProvider(BlockStateProvider.of(ModBlocks.WW_LEAVES))
                        .logTarget(Set.of(Blocks.OAK_LOG))
                        .leavesTarget(Set.of(Blocks.OAK_LEAVES))
                        .growableOn(BlockPredicate.matchingBlockTag(BlockTags.DIRT))
                        .maxLogDepth(10)
                        .treeDecorators(List.of(new HangingLeavesTreeDeco(0.35f, 2, 5)))
                        .build()


        );

        register(context, WW_TREE2, TYGFeatures.TREE_FROM_NBT_V1.get(),
                new TreeFromStructureNBTConfig.Builder()
                        .baseLocation(new Identifier(DownTheRabbitHole.MOD_ID, "trees/ww/willow_trunk1"))
                        .canopyLocation(new Identifier(DownTheRabbitHole.MOD_ID, "trees/ww/willow_canopy2"))
                        .height(BiasedToBottomIntProvider.create(5, 15))
                        .logProvider(BlockStateProvider.of(ModBlocks.WW_LOG))
                        .leavesProvider(BlockStateProvider.of(ModBlocks.WW_LEAVES))
                        .logTarget(Set.of(Blocks.OAK_LOG))
                        .leavesTarget(Set.of(Blocks.OAK_LEAVES))
                        .growableOn(BlockPredicate.matchingBlockTag(BlockTags.DIRT))
                        .maxLogDepth(10)
                        .treeDecorators(List.of(new HangingLeavesTreeDeco(0.35f, 2, 5)))
                        .build()


        );

        register(context, WW_TREE3, TYGFeatures.TREE_FROM_NBT_V1.get(),
                new TreeFromStructureNBTConfig.Builder()
                        .baseLocation(new Identifier(DownTheRabbitHole.MOD_ID, "trees/ww/willow_trunk1"))
                        .canopyLocation(new Identifier(DownTheRabbitHole.MOD_ID, "trees/ww/willow_canopy3"))
                        .height(BiasedToBottomIntProvider.create(5, 15))
                        .logProvider(BlockStateProvider.of(ModBlocks.WW_LOG))
                        .leavesProvider(BlockStateProvider.of(ModBlocks.WW_LEAVES))
                        .logTarget(Set.of(Blocks.OAK_LOG))
                        .leavesTarget(Set.of(Blocks.OAK_LEAVES))
                        .growableOn(BlockPredicate.matchingBlockTag(BlockTags.DIRT))
                        .maxLogDepth(10)
                        .treeDecorators(List.of(new HangingLeavesTreeDeco(0.35f, 2, 5)))
                        .build()


        );

        register(context, WW_TREE4, TYGFeatures.TREE_FROM_NBT_V1.get(),
                new TreeFromStructureNBTConfig.Builder()
                        .baseLocation(new Identifier(DownTheRabbitHole.MOD_ID, "trees/ww/willow_trunk2"))
                        .canopyLocation(new Identifier(DownTheRabbitHole.MOD_ID, "trees/ww/willow_canopy4"))
                        .height(BiasedToBottomIntProvider.create(5, 15))
                        .logProvider(BlockStateProvider.of(ModBlocks.WW_LOG))
                        .leavesProvider(BlockStateProvider.of(ModBlocks.WW_LEAVES))
                        .logTarget(Set.of(Blocks.OAK_LOG))
                        .leavesTarget(Set.of(Blocks.OAK_LEAVES))
                        .growableOn(BlockPredicate.matchingBlockTag(BlockTags.DIRT))
                        .maxLogDepth(10)
                        .treeDecorators(List.of(new HangingLeavesTreeDeco(0.35f, 2, 5)))
                        .build()


        );

        register(context, DEAD_WW_TREE, TYGFeatures.TREE_FROM_NBT_V1.get(),
                new TreeFromStructureNBTConfig.Builder()
                        .baseLocation(new Identifier(DownTheRabbitHole.MOD_ID, "trees/ww/dead_willow_trunk"))
                        .canopyLocation(new Identifier(DownTheRabbitHole.MOD_ID, "trees/ww/dead_willow_canopy"))
                        .height(BiasedToBottomIntProvider.create(5, 15))
                        .logProvider(BlockStateProvider.of(ModBlocks.WW_LOG))
                        .leavesProvider(BlockStateProvider.of(ModBlocks.WW_LEAVES))
                        .logTarget(Set.of(Blocks.OAK_LOG))
                        .leavesTarget(Set.of(Blocks.OAK_LEAVES))
                        .growableOn(BlockPredicate.matchingBlockTag(BlockTags.DIRT))
                        .maxLogDepth(10)
                        .build()


        );



        register(context, WW_TREE_SELECTOR, Feature.RANDOM_SELECTOR, new RandomFeatureConfig(
                List.of(
                        new RandomFeatureEntry(
                                RegistryEntry.of(new PlacedFeature(configuredLookup.getOrThrow(ModConfiguredFeatures.DEAD_WW_TREE),
                                        List.of())),
                                0.01f
                        ),
                        new RandomFeatureEntry(
                                RegistryEntry.of(new PlacedFeature(configuredLookup.getOrThrow(ModConfiguredFeatures.WW_TREE1),
                                        List.of())),
                                0.25f
                        ),
                        new RandomFeatureEntry(
                                RegistryEntry.of(new PlacedFeature(configuredLookup.getOrThrow(ModConfiguredFeatures.WW_TREE2),
                                        List.of())),
                                0.25f
                        ),
                        new RandomFeatureEntry(
                                RegistryEntry.of(new PlacedFeature(configuredLookup.getOrThrow(ModConfiguredFeatures.WW_TREE3),
                                        List.of())),
                                0.25f
                        )
        ),
                RegistryEntry.of(new PlacedFeature(configuredLookup.getOrThrow(ModConfiguredFeatures.WW_TREE4), List.of()))
        ));

        register(context, HH_TREE1, TYGFeatures.TREE_FROM_NBT_V1.get(),
                new TreeFromStructureNBTConfig.Builder()
                        .baseLocation(new Identifier(DownTheRabbitHole.MOD_ID, "trees/hh/hh_tree_trunk1"))
                        .canopyLocation(new Identifier(DownTheRabbitHole.MOD_ID, "trees/hh/hh_tree_canopy1"))
                        .height(BiasedToBottomIntProvider.create(5, 7))
                        .logProvider(BlockStateProvider.of(ModBlocks.HH_LOG))
                        .leavesProvider(BlockStateProvider.of(ModBlocks.HH_LEAVES))
                        .logTarget(Set.of(Blocks.OAK_LOG))
                        .leavesTarget(Set.of(Blocks.OAK_LEAVES))
                        .growableOn(BlockPredicate.matchingBlockTag(BlockTags.DIRT))
                        .maxLogDepth(3)
                        .build()


        );

        register(context, HH_TREE2, TYGFeatures.TREE_FROM_NBT_V1.get(),
                new TreeFromStructureNBTConfig.Builder()
                        .baseLocation(new Identifier(DownTheRabbitHole.MOD_ID, "trees/hh/hh_tree_trunk2"))
                        .canopyLocation(new Identifier(DownTheRabbitHole.MOD_ID, "trees/hh/hh_tree_canopy2"))
                        .height(BiasedToBottomIntProvider.create(2, 5))
                        .logProvider(BlockStateProvider.of(ModBlocks.HH_LOG))
                        .leavesProvider(BlockStateProvider.of(ModBlocks.HH_LEAVES))
                        .logTarget(Set.of(Blocks.OAK_LOG))
                        .leavesTarget(Set.of(Blocks.OAK_LEAVES))
                        .growableOn(BlockPredicate.matchingBlockTag(BlockTags.DIRT))
                        .maxLogDepth(5)
                        .build()


        );

        register(context, HH_TREE3, TYGFeatures.TREE_FROM_NBT_V1.get(),
                new TreeFromStructureNBTConfig.Builder()
                        .baseLocation(new Identifier(DownTheRabbitHole.MOD_ID, "trees/hh/hh_tree_trunk3"))
                        .canopyLocation(new Identifier(DownTheRabbitHole.MOD_ID, "trees/hh/hh_tree_canopy3"))
                        .height(BiasedToBottomIntProvider.create(5, 8))
                        .logProvider(BlockStateProvider.of(ModBlocks.HH_LOG))
                        .leavesProvider(BlockStateProvider.of(ModBlocks.HH_LEAVES))
                        .logTarget(Set.of(Blocks.OAK_LOG))
                        .leavesTarget(Set.of(Blocks.OAK_LEAVES))
                        .growableOn(BlockPredicate.matchingBlockTag(BlockTags.DIRT))
                        .maxLogDepth(3)
                        .build()


        );

        register(context, HH_TREE4, TYGFeatures.TREE_FROM_NBT_V1.get(),
                new TreeFromStructureNBTConfig.Builder()
                        .baseLocation(new Identifier(DownTheRabbitHole.MOD_ID, "trees/hh/hh_tree_trunk1"))
                        .canopyLocation(new Identifier(DownTheRabbitHole.MOD_ID, "trees/hh/hh_tree_canopy3"))
                        .height(BiasedToBottomIntProvider.create(5, 8))
                        .logProvider(BlockStateProvider.of(ModBlocks.HH_LOG))
                        .leavesProvider(BlockStateProvider.of(ModBlocks.HH_LEAVES))
                        .logTarget(Set.of(Blocks.OAK_LOG))
                        .leavesTarget(Set.of(Blocks.OAK_LEAVES))
                        .growableOn(BlockPredicate.matchingBlockTag(BlockTags.DIRT))
                        .maxLogDepth(3)
                        .build()


        );

        register(context, HH_TREE5, TYGFeatures.TREE_FROM_NBT_V1.get(),
                new TreeFromStructureNBTConfig.Builder()
                        .baseLocation(new Identifier(DownTheRabbitHole.MOD_ID, "trees/hh/hh_tree_trunk3"))
                        .canopyLocation(new Identifier(DownTheRabbitHole.MOD_ID, "trees/hh/hh_tree_canopy1"))
                        .height(BiasedToBottomIntProvider.create(5, 8))
                        .logProvider(BlockStateProvider.of(ModBlocks.HH_LOG))
                        .leavesProvider(BlockStateProvider.of(ModBlocks.HH_LEAVES))
                        .logTarget(Set.of(Blocks.OAK_LOG))
                        .leavesTarget(Set.of(Blocks.OAK_LEAVES))
                        .growableOn(BlockPredicate.matchingBlockTag(BlockTags.DIRT))
                        .maxLogDepth(3)
                        .build()


        );


        register(context, HH_TREE_SELECTOR, Feature.RANDOM_SELECTOR, new RandomFeatureConfig(
                List.of(
                        new RandomFeatureEntry(
                                RegistryEntry.of(new PlacedFeature(configuredLookup.getOrThrow(ModConfiguredFeatures.HH_TREE1),
                                        List.of())),
                                0.20f
                        ),
                        new RandomFeatureEntry(
                                RegistryEntry.of(new PlacedFeature(configuredLookup.getOrThrow(ModConfiguredFeatures.HH_TREE2),
                                        List.of())),
                                0.20f
                        ),
                        new RandomFeatureEntry(
                                RegistryEntry.of(new PlacedFeature(configuredLookup.getOrThrow(ModConfiguredFeatures.HH_TREE3),
                                        List.of())),
                                0.20f
                        ),
                        new RandomFeatureEntry(
                                RegistryEntry.of(new PlacedFeature(configuredLookup.getOrThrow(ModConfiguredFeatures.HH_TREE4),
                                        List.of())),
                                0.20f
                        )
                ),
                RegistryEntry.of(new PlacedFeature(configuredLookup.getOrThrow(ModConfiguredFeatures.HH_TREE5), List.of()))
        ));


        register(context, BIG_BLUE_MUSHROOM1, TYGFeatures.TREE_FROM_NBT_V1.get(),
                new TreeFromStructureNBTConfig.Builder()
                        .baseLocation(new Identifier(DownTheRabbitHole.MOD_ID, "mushrooms/trunk1"))
                        .canopyLocation(new Identifier(DownTheRabbitHole.MOD_ID, "mushrooms/canopy1"))
                        .height(BiasedToBottomIntProvider.create(2, 4))
                        .logProvider(BlockStateProvider.of(Blocks.MUSHROOM_STEM))
                        .leavesProvider(BlockStateProvider.of(ModBlocks.BLUE_MUSHROOM_BLOCK))
                        .logTarget(Set.of(Blocks.MUSHROOM_STEM))
                        .leavesTarget(Set.of(Blocks.RED_MUSHROOM_BLOCK))
                        .growableOn(BlockPredicate.matchingBlockTag(BlockTags.MUSHROOM_GROW_BLOCK))
                        .maxLogDepth(3)
                        .build()


        );

        register(context, BIG_BLUE_MUSHROOM2, TYGFeatures.TREE_FROM_NBT_V1.get(),
                new TreeFromStructureNBTConfig.Builder()
                        .baseLocation(new Identifier(DownTheRabbitHole.MOD_ID, "mushrooms/trunk1"))
                        .canopyLocation(new Identifier(DownTheRabbitHole.MOD_ID, "mushrooms/canopy2"))
                        .height(BiasedToBottomIntProvider.create(2, 4))
                        .logProvider(BlockStateProvider.of(Blocks.MUSHROOM_STEM))
                        .leavesProvider(BlockStateProvider.of(ModBlocks.BLUE_MUSHROOM_BLOCK))
                        .logTarget(Set.of(Blocks.MUSHROOM_STEM))
                        .leavesTarget(Set.of(Blocks.RED_MUSHROOM_BLOCK))
                        .growableOn(BlockPredicate.matchingBlockTag(BlockTags.MUSHROOM_GROW_BLOCK))
                        .maxLogDepth(3)
                        .build()


        );

        register(context, BIG_BLUE_MUSHROOM3, TYGFeatures.TREE_FROM_NBT_V1.get(),
                new TreeFromStructureNBTConfig.Builder()
                        .baseLocation(new Identifier(DownTheRabbitHole.MOD_ID, "mushrooms/trunk2"))
                        .canopyLocation(new Identifier(DownTheRabbitHole.MOD_ID, "mushrooms/canopy5"))
                        .height(BiasedToBottomIntProvider.create(3, 4))
                        .logProvider(BlockStateProvider.of(Blocks.MUSHROOM_STEM))
                        .leavesProvider(BlockStateProvider.of(ModBlocks.BLUE_MUSHROOM_BLOCK))
                        .logTarget(Set.of(Blocks.MUSHROOM_STEM))
                        .leavesTarget(Set.of(Blocks.RED_MUSHROOM_BLOCK))
                        .growableOn(BlockPredicate.matchingBlockTag(BlockTags.MUSHROOM_GROW_BLOCK))
                        .maxLogDepth(3)
                        .build()


        );

        register(context, BIG_BLUE_MUSHROOM4, TYGFeatures.TREE_FROM_NBT_V1.get(),
                new TreeFromStructureNBTConfig.Builder()
                        .baseLocation(new Identifier(DownTheRabbitHole.MOD_ID, "mushrooms/trunk2"))
                        .canopyLocation(new Identifier(DownTheRabbitHole.MOD_ID, "mushrooms/canopy6"))
                        .height(BiasedToBottomIntProvider.create(2, 4))
                        .logProvider(BlockStateProvider.of(Blocks.MUSHROOM_STEM))
                        .leavesProvider(BlockStateProvider.of(ModBlocks.BLUE_MUSHROOM_BLOCK))
                        .logTarget(Set.of(Blocks.MUSHROOM_STEM))
                        .leavesTarget(Set.of(Blocks.RED_MUSHROOM_BLOCK))
                        .growableOn(BlockPredicate.matchingBlockTag(BlockTags.MUSHROOM_GROW_BLOCK))
                        .maxLogDepth(3)
                        .build()


        );

        register(context, BIG_BLUE_MUSHROOM_SELECTOR, Feature.RANDOM_SELECTOR, new RandomFeatureConfig(
                List.of(
                        new RandomFeatureEntry(
                                RegistryEntry.of(new PlacedFeature(configuredLookup.getOrThrow(ModConfiguredFeatures.BIG_BLUE_MUSHROOM1),
                                        List.of())),
                                0.25f
                        ),
                        new RandomFeatureEntry(
                                RegistryEntry.of(new PlacedFeature(configuredLookup.getOrThrow(ModConfiguredFeatures.BIG_BLUE_MUSHROOM2),
                                        List.of())),
                                0.25f
                        ),
                        new RandomFeatureEntry(
                                RegistryEntry.of(new PlacedFeature(configuredLookup.getOrThrow(ModConfiguredFeatures.BIG_BLUE_MUSHROOM3),
                                        List.of())),
                                0.25f
                                )
                ),
                RegistryEntry.of(new PlacedFeature(configuredLookup.getOrThrow(ModConfiguredFeatures.BIG_BLUE_MUSHROOM4), List.of()))
        ));


        register(context, BIG_MAGENTA_MUSHROOM1, TYGFeatures.TREE_FROM_NBT_V1.get(),
                new TreeFromStructureNBTConfig.Builder()
                        .baseLocation(new Identifier(DownTheRabbitHole.MOD_ID, "mushrooms/trunk1"))
                        .canopyLocation(new Identifier(DownTheRabbitHole.MOD_ID, "mushrooms/canopy2"))
                        .height(BiasedToBottomIntProvider.create(3, 5))
                        .logProvider(BlockStateProvider.of(Blocks.MUSHROOM_STEM))
                        .leavesProvider(BlockStateProvider.of(ModBlocks.MAGENTA_MUSHROOM_BLOCK))
                        .logTarget(Set.of(Blocks.MUSHROOM_STEM))
                        .leavesTarget(Set.of(Blocks.RED_MUSHROOM_BLOCK))
                        .growableOn(BlockPredicate.matchingBlockTag(BlockTags.MUSHROOM_GROW_BLOCK))
                        .maxLogDepth(3)
                        .build()


        );

        register(context, BIG_MAGENTA_MUSHROOM2, TYGFeatures.TREE_FROM_NBT_V1.get(),
                new TreeFromStructureNBTConfig.Builder()
                        .baseLocation(new Identifier(DownTheRabbitHole.MOD_ID, "mushrooms/trunk1"))
                        .canopyLocation(new Identifier(DownTheRabbitHole.MOD_ID, "mushrooms/canopy3"))
                        .height(BiasedToBottomIntProvider.create(3, 5))
                        .logProvider(BlockStateProvider.of(Blocks.MUSHROOM_STEM))
                        .leavesProvider(BlockStateProvider.of(ModBlocks.MAGENTA_MUSHROOM_BLOCK))
                        .logTarget(Set.of(Blocks.MUSHROOM_STEM))
                        .leavesTarget(Set.of(Blocks.RED_MUSHROOM_BLOCK))
                        .growableOn(BlockPredicate.matchingBlockTag(BlockTags.MUSHROOM_GROW_BLOCK))
                        .maxLogDepth(3)
                        .build()


        );

        register(context, BIG_MAGENTA_MUSHROOM3, TYGFeatures.TREE_FROM_NBT_V1.get(),
                new TreeFromStructureNBTConfig.Builder()
                        .baseLocation(new Identifier(DownTheRabbitHole.MOD_ID, "mushrooms/trunk2"))
                        .canopyLocation(new Identifier(DownTheRabbitHole.MOD_ID, "mushrooms/canopy6"))
                        .height(BiasedToBottomIntProvider.create(3, 5))
                        .logProvider(BlockStateProvider.of(Blocks.MUSHROOM_STEM))
                        .leavesProvider(BlockStateProvider.of(ModBlocks.MAGENTA_MUSHROOM_BLOCK))
                        .logTarget(Set.of(Blocks.MUSHROOM_STEM))
                        .leavesTarget(Set.of(Blocks.RED_MUSHROOM_BLOCK))
                        .growableOn(BlockPredicate.matchingBlockTag(BlockTags.MUSHROOM_GROW_BLOCK))
                        .maxLogDepth(3)
                        .build()


        );

        register(context, BIG_MAGENTA_MUSHROOM4, TYGFeatures.TREE_FROM_NBT_V1.get(),
                new TreeFromStructureNBTConfig.Builder()
                        .baseLocation(new Identifier(DownTheRabbitHole.MOD_ID, "mushrooms/trunk2"))
                        .canopyLocation(new Identifier(DownTheRabbitHole.MOD_ID, "mushrooms/canopy3"))
                        .height(BiasedToBottomIntProvider.create(3, 4))
                        .logProvider(BlockStateProvider.of(Blocks.MUSHROOM_STEM))
                        .leavesProvider(BlockStateProvider.of(ModBlocks.MAGENTA_MUSHROOM_BLOCK))
                        .logTarget(Set.of(Blocks.MUSHROOM_STEM))
                        .leavesTarget(Set.of(Blocks.RED_MUSHROOM_BLOCK))
                        .growableOn(BlockPredicate.matchingBlockTag(BlockTags.MUSHROOM_GROW_BLOCK))
                        .maxLogDepth(3)
                        .build()


        );

        register(context, BIG_MAGENTA_MUSHROOM_SELECTOR, Feature.RANDOM_SELECTOR, new RandomFeatureConfig(
                List.of(
                        new RandomFeatureEntry(
                                RegistryEntry.of(new PlacedFeature(configuredLookup.getOrThrow(ModConfiguredFeatures.BIG_MAGENTA_MUSHROOM1),
                                        List.of())),
                                0.25f
                        ),
                        new RandomFeatureEntry(
                                RegistryEntry.of(new PlacedFeature(configuredLookup.getOrThrow(ModConfiguredFeatures.BIG_MAGENTA_MUSHROOM2),
                                        List.of())),
                                0.25f
                        ),
                        new RandomFeatureEntry(
                                RegistryEntry.of(new PlacedFeature(configuredLookup.getOrThrow(ModConfiguredFeatures.BIG_MAGENTA_MUSHROOM3),
                                        List.of())),
                                0.25f
                        )
                ),
                RegistryEntry.of(new PlacedFeature(configuredLookup.getOrThrow(ModConfiguredFeatures.BIG_MAGENTA_MUSHROOM4), List.of()))
        ));

        register(context, BIG_YELLOW_MUSHROOM1, TYGFeatures.TREE_FROM_NBT_V1.get(),
                new TreeFromStructureNBTConfig.Builder()
                        .baseLocation(new Identifier(DownTheRabbitHole.MOD_ID, "mushrooms/trunk1"))
                        .canopyLocation(new Identifier(DownTheRabbitHole.MOD_ID, "mushrooms/canopy3"))
                        .height(BiasedToBottomIntProvider.create(3, 5))
                        .logProvider(BlockStateProvider.of(Blocks.MUSHROOM_STEM))
                        .leavesProvider(BlockStateProvider.of(ModBlocks.YELLOW_MUSHROOM_BLOCK))
                        .logTarget(Set.of(Blocks.MUSHROOM_STEM))
                        .leavesTarget(Set.of(Blocks.RED_MUSHROOM_BLOCK))
                        .growableOn(BlockPredicate.matchingBlockTag(BlockTags.MUSHROOM_GROW_BLOCK))
                        .maxLogDepth(3)
                        .build()


        );

        register(context, BIG_YELLOW_MUSHROOM2, TYGFeatures.TREE_FROM_NBT_V1.get(),
                new TreeFromStructureNBTConfig.Builder()
                        .baseLocation(new Identifier(DownTheRabbitHole.MOD_ID, "mushrooms/trunk1"))
                        .canopyLocation(new Identifier(DownTheRabbitHole.MOD_ID, "mushrooms/canopy4"))
                        .height(BiasedToBottomIntProvider.create(3, 5))
                        .logProvider(BlockStateProvider.of(Blocks.MUSHROOM_STEM))
                        .leavesProvider(BlockStateProvider.of(ModBlocks.YELLOW_MUSHROOM_BLOCK))
                        .logTarget(Set.of(Blocks.MUSHROOM_STEM))
                        .leavesTarget(Set.of(Blocks.RED_MUSHROOM_BLOCK))
                        .growableOn(BlockPredicate.matchingBlockTag(BlockTags.MUSHROOM_GROW_BLOCK))
                        .maxLogDepth(3)
                        .build()


        );

        register(context, BIG_YELLOW_MUSHROOM3, TYGFeatures.TREE_FROM_NBT_V1.get(),
                new TreeFromStructureNBTConfig.Builder()
                        .baseLocation(new Identifier(DownTheRabbitHole.MOD_ID, "mushrooms/trunk2"))
                        .canopyLocation(new Identifier(DownTheRabbitHole.MOD_ID, "mushrooms/canopy3"))
                        .height(BiasedToBottomIntProvider.create(3, 5))
                        .logProvider(BlockStateProvider.of(Blocks.MUSHROOM_STEM))
                        .leavesProvider(BlockStateProvider.of(ModBlocks.YELLOW_MUSHROOM_BLOCK))
                        .logTarget(Set.of(Blocks.MUSHROOM_STEM))
                        .leavesTarget(Set.of(Blocks.RED_MUSHROOM_BLOCK))
                        .growableOn(BlockPredicate.matchingBlockTag(BlockTags.MUSHROOM_GROW_BLOCK))
                        .maxLogDepth(3)
                        .build()


        );

        register(context, BIG_YELLOW_MUSHROOM_SELECTOR, Feature.RANDOM_SELECTOR, new RandomFeatureConfig(
                List.of(
                        new RandomFeatureEntry(
                                RegistryEntry.of(new PlacedFeature(configuredLookup.getOrThrow(ModConfiguredFeatures.BIG_YELLOW_MUSHROOM2),
                                        List.of())),
                                0.25f
                        ),
                        new RandomFeatureEntry(
                                RegistryEntry.of(new PlacedFeature(configuredLookup.getOrThrow(ModConfiguredFeatures.BIG_YELLOW_MUSHROOM3),
                                        List.of())),
                                0.25f
                        )
                ),
                RegistryEntry.of(new PlacedFeature(configuredLookup.getOrThrow(ModConfiguredFeatures.BIG_YELLOW_MUSHROOM1), List.of()))
        ));

        register(context, VALE_OF_TEARS_SELECTOR, Feature.RANDOM_SELECTOR, new RandomFeatureConfig(
                List.of(
                        new RandomFeatureEntry(
                                RegistryEntry.of(new PlacedFeature(configuredLookup.getOrThrow(ModConfiguredFeatures.BIG_YELLOW_MUSHROOM_SELECTOR),
                                        List.of())),
                                0.25f
                        ),
                        new RandomFeatureEntry(
                                RegistryEntry.of(new PlacedFeature(configuredLookup.getOrThrow(ModConfiguredFeatures.BIG_MAGENTA_MUSHROOM_SELECTOR),
                                        List.of())),
                                0.25f
                        ),
                        new RandomFeatureEntry(
                                RegistryEntry.of(new PlacedFeature(configuredLookup.getOrThrow(ModConfiguredFeatures.BIG_BLUE_MUSHROOM_SELECTOR),
                                        List.of())),
                                0.25f
                        )
                ),
                RegistryEntry.of(new PlacedFeature(configuredLookup.getOrThrow(ModConfiguredFeatures.WW_TREE_SELECTOR), List.of()))
        ));


    }



    public static RegistryKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, new Identifier(DownTheRabbitHole.MOD_ID, name));
    }

    public static <FC extends FeatureConfig, F extends Feature<FC>> void register(Registerable<ConfiguredFeature<?, ?>> context,
                                                                                  RegistryKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {

        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
