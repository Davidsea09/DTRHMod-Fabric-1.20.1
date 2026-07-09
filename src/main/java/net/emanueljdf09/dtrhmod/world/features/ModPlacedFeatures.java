package net.emanueljdf09.dtrhmod.world.features;

import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.emanueljdf09.dtrhmod.block.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.Direction;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.CherryFoliagePlacer;
import net.minecraft.world.gen.placementmodifier.*;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.trunk.CherryTrunkPlacer;

import java.util.List;

public class ModPlacedFeatures {

    public static final RegistryKey<PlacedFeature> DISK_CLAY_PLACED = registerKey("disk_clay_placed");
    public static final RegistryKey<PlacedFeature> DISK_SAND_PLACED = registerKey("disk_sand_placed");
    public static final RegistryKey<PlacedFeature> DISK_GRAVEL_PLACED = registerKey("disk_gravel_placed");

    public static final RegistryKey<PlacedFeature> TH_TREE_PLACED = registerKey("th_tree_placed");
    public static final RegistryKey<PlacedFeature> BB_TREE_PLACED = registerKey("bb_tree_placed");
    public static final RegistryKey<PlacedFeature> WW_TREE_PLACED = registerKey("ww_tree_placed");
    public static final RegistryKey<PlacedFeature> TULGEY_TREE_PLACED = registerKey("tulgey_tree_placed");

    public static void bootstrap(Registerable<PlacedFeature> context) {
        var configuredFeatureRegistryEntryLookup = context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);

        register(context, DISK_CLAY_PLACED, configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.DISK_CLAY), SquarePlacementModifier.of(), PlacedFeatures.OCEAN_FLOOR_WG_HEIGHTMAP, BlockFilterPlacementModifier.of(BlockPredicate.matchingFluids(new Fluid[]{Fluids.WATER})), BiomePlacementModifier.of());
        register(context, DISK_GRAVEL_PLACED, configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.DISK_GRAVEL), SquarePlacementModifier.of(), PlacedFeatures.OCEAN_FLOOR_WG_HEIGHTMAP, BlockFilterPlacementModifier.of(BlockPredicate.matchingFluids(new Fluid[]{Fluids.WATER})), BiomePlacementModifier.of());
        register(context, DISK_SAND_PLACED, configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.DISK_SAND), CountPlacementModifier.of(3), SquarePlacementModifier.of(), PlacedFeatures.OCEAN_FLOOR_WG_HEIGHTMAP, BlockFilterPlacementModifier.of(BlockPredicate.matchingFluids(new Fluid[]{Fluids.WATER})), BiomePlacementModifier.of());


        register(context, TH_TREE_PLACED, configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.TH_TREE),
                VegetationPlacedFeatures.treeModifiersWithWouldSurvive(PlacedFeatures.createCountExtraModifier(3, 0.1f, 2),
                        ModBlocks.TH_SAPLING));

        register(context, BB_TREE_PLACED, configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.BB_TREE),
                VegetationPlacedFeatures.treeModifiersWithWouldSurvive(PlacedFeatures.createCountExtraModifier(3, 0.1f, 2),
                        ModBlocks.BB_SAPLING));

        register(context, TULGEY_TREE_PLACED, configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.TULGEY_TREE_SELECTOR),
                VegetationPlacedFeatures.treeModifiersWithWouldSurvive(
                        PlacedFeatures.createCountExtraModifier(10, 0.1f, 3),
                        ModBlocks.TH_SAPLING));

        register(context, WW_TREE_PLACED, configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.WW_TREE),
                VegetationPlacedFeatures.treeModifiersWithWouldSurvive(
                        PlacedFeatures.createCountExtraModifier(3, 0.1f, 2),
                        ModBlocks.WW_SAPLING));

    }

    public static RegistryKey<PlacedFeature> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier(DownTheRabbitHole.MOD_ID, name));
    }

    private static void register(Registerable<PlacedFeature> context, RegistryKey<PlacedFeature> key, RegistryEntry<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {

        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }

    public static void register(Registerable<PlacedFeature> context, RegistryKey<PlacedFeature> key, RegistryEntry<ConfiguredFeature<?, ?>> configuration, PlacementModifier... modifiers) {
        register(context, key, configuration, List.of(modifiers));
    }
}
