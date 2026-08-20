package net.emanueljdf09.dtrhmod.world.features;

import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.emanueljdf09.dtrhmod.block.ModBlocks;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.*;

import java.util.List;

public class ModPlacedFeatures {

    public static final RegistryKey<PlacedFeature> FOREST_ROCKS_PLACED = registerKey("forest_rocks_placed");
    public static final RegistryKey<PlacedFeature> FOREST_FLOWERBEDS_PLACED = registerKey("forest_flowerbeds_placed");
    public static final RegistryKey<PlacedFeature> FOREST_GROUND_MUSHROOMS_PLACED = registerKey("forest_ground_mushrooms_placed");

    public static final RegistryKey<PlacedFeature> DISK_CLAY_PLACED = registerKey("disk_clay_placed");
    public static final RegistryKey<PlacedFeature> DISK_SAND_PLACED = registerKey("disk_sand_placed");
    public static final RegistryKey<PlacedFeature> DISK_GRAVEL_PLACED = registerKey("disk_gravel_placed");

    public static final RegistryKey<PlacedFeature> TH_TREE_PLACED = registerKey("th_tree_placed");
    public static final RegistryKey<PlacedFeature> BB_TREE_PLACED = registerKey("bb_tree_placed");
    public static final RegistryKey<PlacedFeature> WW_TREE_PLACED = registerKey("ww_tree_placed");
    public static final RegistryKey<PlacedFeature> HH_TREE_PLACED = registerKey("hh_tree_placed");
    public static final RegistryKey<PlacedFeature> TULGEY_TREE_PLACED = registerKey("tulgey_tree_placed");

    public static final RegistryKey<PlacedFeature> BIG_BLUE_MUSHROOM_PLACED = registerKey("big_blue_mushroom_placed");
    public static final RegistryKey<PlacedFeature> BIG_YELLOW_MUSHROOM_PLACED = registerKey("big_yellow_mushroom_placed");
    public static final RegistryKey<PlacedFeature> BIG_MAGENTA_MUSHROOM_PLACED = registerKey("big_magenta_mushroom_placed");

    public static final RegistryKey<PlacedFeature> VALE_OF_TEARS_SELECTOR_PLACED = registerKey("vale_of_tears_vegetation_placed");

    public static void bootstrap(Registerable<PlacedFeature> context) {
        var configuredFeatureRegistryEntryLookup = context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);

        register(context, FOREST_ROCKS_PLACED, configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.FOREST_ROCKS),
                List.of(
                        RarityFilterPlacementModifier.of(6),
                        SquarePlacementModifier.of(),
                        PlacedFeatures.OCEAN_FLOOR_WG_HEIGHTMAP, // Finds the actual floor instead of water surface
                        SurfaceWaterDepthFilterPlacementModifier.of(0), // Rejects placement if water is present above
                        BiomePlacementModifier.of()
                )
        );

        register(context, FOREST_FLOWERBEDS_PLACED, configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.FOREST_FLOWERBEDS),
                List.of(
                        RarityFilterPlacementModifier.of(8),
                        SquarePlacementModifier.of(),
                        PlacedFeatures.OCEAN_FLOOR_WG_HEIGHTMAP,
                        SurfaceWaterDepthFilterPlacementModifier.of(0),
                        BiomePlacementModifier.of()
                )
        );

        register(context, FOREST_GROUND_MUSHROOMS_PLACED, configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.FOREST_GROUND_MUSHROOMS),
                List.of(
                        RarityFilterPlacementModifier.of(4),
                        SquarePlacementModifier.of(),
                        PlacedFeatures.OCEAN_FLOOR_WG_HEIGHTMAP,
                        SurfaceWaterDepthFilterPlacementModifier.of(0),
                        BiomePlacementModifier.of()
                )
        );


        register(context, DISK_CLAY_PLACED, configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.DISK_CLAY), SquarePlacementModifier.of(), PlacedFeatures.OCEAN_FLOOR_WG_HEIGHTMAP, BlockFilterPlacementModifier.of(BlockPredicate.matchingFluids(new Fluid[]{Fluids.WATER})), BiomePlacementModifier.of());
        register(context, DISK_GRAVEL_PLACED, configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.DISK_GRAVEL), SquarePlacementModifier.of(), PlacedFeatures.OCEAN_FLOOR_WG_HEIGHTMAP, BlockFilterPlacementModifier.of(BlockPredicate.matchingFluids(new Fluid[]{Fluids.WATER})), BiomePlacementModifier.of());
        register(context, DISK_SAND_PLACED, configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.DISK_SAND), CountPlacementModifier.of(3), SquarePlacementModifier.of(), PlacedFeatures.OCEAN_FLOOR_WG_HEIGHTMAP, BlockFilterPlacementModifier.of(BlockPredicate.matchingFluids(new Fluid[]{Fluids.WATER})), BiomePlacementModifier.of());


        register(context, TH_TREE_PLACED, configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.TH_TREE_SELECTOR),
                VegetationPlacedFeatures.treeModifiersWithWouldSurvive(PlacedFeatures.createCountExtraModifier(6, 0.2f, 1),
                        ModBlocks.TH_SAPLING));

        register(context, BB_TREE_PLACED, configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.BB_TREE_SELECTOR),
                VegetationPlacedFeatures.treeModifiersWithWouldSurvive(PlacedFeatures.createCountExtraModifier(6, 0.2f, 1),
                        ModBlocks.BB_SAPLING));

        register(context, TULGEY_TREE_PLACED, configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.TULGEY_TREE_SELECTOR),
                VegetationPlacedFeatures.treeModifiersWithWouldSurvive(
                        PlacedFeatures.createCountExtraModifier(6, 0.2f, 2),
                        ModBlocks.TH_SAPLING));

        register(context, WW_TREE_PLACED, configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.WW_TREE_SELECTOR),
                VegetationPlacedFeatures.treeModifiersWithWouldSurvive(
                        PlacedFeatures.createCountExtraModifier(4, 0.2f, 1),
                        ModBlocks.WW_SAPLING));

        register(context, HH_TREE_PLACED, configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.WW_TREE_SELECTOR),
                VegetationPlacedFeatures.treeModifiersWithWouldSurvive(
                        PlacedFeatures.createCountExtraModifier(2, 0.2f, 1),
                        ModBlocks.HH_SAPLING));

        register(context, BIG_BLUE_MUSHROOM_PLACED, configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.BIG_BLUE_MUSHROOM_SELECTOR),
                RarityFilterPlacementModifier.of(1),
                SquarePlacementModifier.of(),
                PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
                BiomePlacementModifier.of());
        register(context, BIG_YELLOW_MUSHROOM_PLACED, configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.BIG_YELLOW_MUSHROOM_SELECTOR),
                RarityFilterPlacementModifier.of(2),
                SquarePlacementModifier.of(),
                PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
                BiomePlacementModifier.of());
        register(context, BIG_MAGENTA_MUSHROOM_PLACED, configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.BIG_MAGENTA_MUSHROOM_SELECTOR),
                RarityFilterPlacementModifier.of(1),
                SquarePlacementModifier.of(),
                PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
                BiomePlacementModifier.of());

        register(context, VALE_OF_TEARS_SELECTOR_PLACED, configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.VALE_OF_TEARS_SELECTOR),
                VegetationPlacedFeatures.treeModifiersWithWouldSurvive(
                        PlacedFeatures.createCountExtraModifier(2, 0.2f, 1),
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
