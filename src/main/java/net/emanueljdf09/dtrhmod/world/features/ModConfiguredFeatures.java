package net.emanueljdf09.dtrhmod.world.features;

import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.emanueljdf09.dtrhmod.block.ModBlocks;
import net.emanueljdf09.dtrhmod.world.features.tree.deco.WwTreeDecorator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MushroomBlock;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.*;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.PredicatedStateProvider;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.trunk.*;

import java.util.List;

public class ModConfiguredFeatures {

    public static final RegistryKey<ConfiguredFeature<?, ?>> DISK_CLAY = registerKey("disk_clay");
    public static final RegistryKey<ConfiguredFeature<?, ?>> DISK_SAND = registerKey("disk_sand");
    public static final RegistryKey<ConfiguredFeature<?, ?>> DISK_GRAVEL = registerKey("disk_gravel");

    public static final RegistryKey<ConfiguredFeature<?, ?>> TH_TREE = registerKey("th_tree");
    public static final RegistryKey<ConfiguredFeature<?, ?>> BB_TREE = registerKey("bb_tree");
    public static final RegistryKey<ConfiguredFeature<?, ?>> WW_TREE = registerKey("ww_tree");
    public static final RegistryKey<ConfiguredFeature<?, ?>> TULGEY_TREE_SELECTOR = registerKey("tulgey_tree");

    public static final RegistryKey<ConfiguredFeature<?, ?>> BIG_BLUE_MUSHROOM = registerKey("big_blue_mushroom");
    public static final RegistryKey<ConfiguredFeature<?, ?>> BIG_YELLOW_MUSHROOM = registerKey("big_yellow_mushroom");
    public static final RegistryKey<ConfiguredFeature<?, ?>> BIG_MAGENTA_MUSHROOM = registerKey("big_magenta_mushroom");

    public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> context) {
        var placedLookup = context.getRegistryLookup(RegistryKeys.PLACED_FEATURE);
        var configuredLookup = context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);

        register(context, DISK_CLAY, Feature.DISK, new DiskFeatureConfig(PredicatedStateProvider.of(Blocks.CLAY), BlockPredicate.matchingBlocks(List.of(ModBlocks.WONDER_DIRT, Blocks.CLAY)), UniformIntProvider.create(2, 3), 1));
        register(context, DISK_GRAVEL, Feature.DISK, new DiskFeatureConfig(PredicatedStateProvider.of(Blocks.GRAVEL), BlockPredicate.matchingBlocks(List.of(ModBlocks.WONDER_DIRT, Blocks.GRASS_BLOCK)), UniformIntProvider.create(2, 5), 2));
        register(context, DISK_SAND, Feature.DISK, new DiskFeatureConfig(new PredicatedStateProvider(BlockStateProvider.of(Blocks.SAND), List.of(new PredicatedStateProvider.Rule(BlockPredicate.matchingBlocks(Direction.DOWN.getVector(), Blocks.AIR), BlockStateProvider.of(Blocks.SANDSTONE)))), BlockPredicate.matchingBlocks(List.of(ModBlocks.WONDER_DIRT, ModBlocks.WONDER_GRASS)), UniformIntProvider.create(2, 6), 2));

        register(context, TH_TREE, Feature.TREE, new TreeFeatureConfig.Builder(
                BlockStateProvider.of(ModBlocks.TH_LOG),
                new DarkOakTrunkPlacer(6, 2, 2),
                BlockStateProvider.of(ModBlocks.TH_LEAVES),
                new DarkOakFoliagePlacer(ConstantIntProvider.create(0), ConstantIntProvider.create(0)),
                new TwoLayersFeatureSize(1, 0, 2))
                .dirtProvider(BlockStateProvider.of(ModBlocks.WONDER_DIRT)).ignoreVines().build());

        register(context, BB_TREE, Feature.TREE, new TreeFeatureConfig.Builder(
                BlockStateProvider.of(ModBlocks.BB_LOG),
                new DarkOakTrunkPlacer(11, 4, 4),  BlockStateProvider.of(ModBlocks.BB_LEAVES),
                new DarkOakFoliagePlacer(ConstantIntProvider.create(1), ConstantIntProvider.create(1)),
                new TwoLayersFeatureSize(1, 0, 3))
                .dirtProvider(BlockStateProvider.of(ModBlocks.WONDER_DIRT)).build());

        register(context, TULGEY_TREE_SELECTOR, Feature.RANDOM_SELECTOR, new RandomFeatureConfig(List.of(
                        new RandomFeatureEntry(
                                RegistryEntry.of(new PlacedFeature(configuredLookup.getOrThrow(ModConfiguredFeatures.BB_TREE), List.of())),
                                0.35f)),
                        RegistryEntry.of(new PlacedFeature(
                                configuredLookup.getOrThrow(ModConfiguredFeatures.TH_TREE),
                                List.of()))
        ));

       register(context, WW_TREE, Feature.TREE, new TreeFeatureConfig.Builder(
                BlockStateProvider.of(ModBlocks.WW_LOG),
                new ForkingTrunkPlacer(5, 2, 2),
                BlockStateProvider.of(ModBlocks.WW_LEAVES),
                new AcaciaFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0)),
                new TwoLayersFeatureSize(1, 0, 2))
                .decorators(List.of(new WwTreeDecorator()))
                .dirtProvider(BlockStateProvider.of(ModBlocks.WONDER_DIRT))
                .ignoreVines()
                .build()
        );

       register(context, BIG_BLUE_MUSHROOM, Feature.HUGE_RED_MUSHROOM, new HugeMushroomFeatureConfig(
               BlockStateProvider.of(ModBlocks.BLUE_MUSHROOM_BLOCK.getDefaultState()),
               BlockStateProvider.of(Blocks.MUSHROOM_STEM.getDefaultState()),
               2

       ));
       register(context, BIG_YELLOW_MUSHROOM, Feature.HUGE_RED_MUSHROOM, new HugeMushroomFeatureConfig(
               BlockStateProvider.of(ModBlocks.YELLOW_MUSHROOM_BLOCK.getDefaultState()),
               BlockStateProvider.of(Blocks.MUSHROOM_STEM.getDefaultState()),
               2

       ));
       register(context, BIG_MAGENTA_MUSHROOM, Feature.HUGE_BROWN_MUSHROOM, new HugeMushroomFeatureConfig(
               BlockStateProvider.of(ModBlocks.MAGENTA_MUSHROOM_BLOCK.getDefaultState()),
               BlockStateProvider.of(Blocks.MUSHROOM_STEM.getDefaultState()),
               3

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
