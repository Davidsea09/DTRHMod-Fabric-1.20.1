package net.emanueljdf09.dtrhmod.world.features;

import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.emanueljdf09.dtrhmod.block.ModBlocks;
import net.emanueljdf09.dtrhmod.world.features.tree.deco.WwTreeDecorator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.foliage.CherryFoliagePlacer;
import net.minecraft.world.gen.foliage.DarkOakFoliagePlacer;
import net.minecraft.world.gen.foliage.SpruceFoliagePlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.PredicatedStateProvider;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.trunk.CherryTrunkPlacer;
import net.minecraft.world.gen.trunk.DarkOakTrunkPlacer;
import net.minecraft.world.gen.trunk.GiantTrunkPlacer;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

import java.util.List;

public class ModConfiguredFeatures {

    public static final RegistryKey<ConfiguredFeature<?, ?>> DISK_CLAY = registerKey("disk_clay");
    public static final RegistryKey<ConfiguredFeature<?, ?>> DISK_SAND = registerKey("disk_sand");
    public static final RegistryKey<ConfiguredFeature<?, ?>> DISK_GRAVEL = registerKey("disk_gravel");

    public static final RegistryKey<ConfiguredFeature<?, ?>> TH_TREE = registerKey("th_tree");
    public static final RegistryKey<ConfiguredFeature<?, ?>> BB_TREE = registerKey("bb_tree");
    public static final RegistryKey<ConfiguredFeature<?, ?>> WW_TREE = registerKey("ww_tree");

    public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> context) {
        register(context, DISK_CLAY, Feature.DISK, new DiskFeatureConfig(PredicatedStateProvider.of(Blocks.CLAY), BlockPredicate.matchingBlocks(List.of(ModBlocks.WONDER_DIRT, Blocks.CLAY)), UniformIntProvider.create(2, 3), 1));
        register(context, DISK_GRAVEL, Feature.DISK, new DiskFeatureConfig(PredicatedStateProvider.of(Blocks.GRAVEL), BlockPredicate.matchingBlocks(List.of(ModBlocks.WONDER_DIRT, Blocks.GRASS_BLOCK)), UniformIntProvider.create(2, 5), 2));
        register(context, DISK_SAND, Feature.DISK, new DiskFeatureConfig(new PredicatedStateProvider(BlockStateProvider.of(Blocks.SAND), List.of(new PredicatedStateProvider.Rule(BlockPredicate.matchingBlocks(Direction.DOWN.getVector(), Blocks.AIR), BlockStateProvider.of(Blocks.SANDSTONE)))), BlockPredicate.matchingBlocks(List.of(ModBlocks.WONDER_DIRT, ModBlocks.WONDER_GRASS)), UniformIntProvider.create(2, 6), 2));

        register(context, TH_TREE, Feature.TREE, new TreeFeatureConfig.Builder(
                BlockStateProvider.of(ModBlocks.TH_LOG),
                new GiantTrunkPlacer(7, 2, 8),
                BlockStateProvider.of(ModBlocks.TH_LEAVES),
                new SpruceFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(4), ConstantIntProvider.create(12)),
                new TwoLayersFeatureSize(1, 0, 2)).ignoreVines().build());

    register(context, BB_TREE, Feature.TREE, new TreeFeatureConfig.Builder(
                BlockStateProvider.of(ModBlocks.BB_LOG),
                new DarkOakTrunkPlacer(5, 4, 4),
                BlockStateProvider.of(ModBlocks.BB_LEAVES),
                new DarkOakFoliagePlacer(ConstantIntProvider.create(1), ConstantIntProvider.create(1)),
                new TwoLayersFeatureSize(1, 0, 3)).build());

    register(context, WW_TREE, Feature.TREE, new TreeFeatureConfig.Builder(
                BlockStateProvider.of(ModBlocks.WW_LOG),
                new CherryTrunkPlacer(5, 1, 0,
                        UniformIntProvider.create(1, 3),
                        UniformIntProvider.create(2, 4),
                        UniformIntProvider.create(-1, 0),
                        UniformIntProvider.create(-4, -3)),
                BlockStateProvider.of(ModBlocks.WW_LEAVES),
                new CherryFoliagePlacer(
                        ConstantIntProvider.create(4),
                        ConstantIntProvider.create(1),
                        ConstantIntProvider.create(4),
                        0.25f, 3.0f, 1.0f, 0.25f),
                new TwoLayersFeatureSize(4, 0, 5))
            .decorators(List.of(new WwTreeDecorator()))
            .ignoreVines()
            .build());


    }


    public static RegistryKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, new Identifier(DownTheRabbitHole.MOD_ID, name));
    }

    public static <FC extends FeatureConfig, F extends Feature<FC>> void register(Registerable<ConfiguredFeature<?, ?>> context,
                                                                                  RegistryKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {

        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
