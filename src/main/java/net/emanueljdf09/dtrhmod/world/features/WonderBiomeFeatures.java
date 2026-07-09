package net.emanueljdf09.dtrhmod.world.features;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.feature.MiscPlacedFeatures;

public class WonderBiomeFeatures {

    public static void addDefaultDisks(GenerationSettings.LookupBackedBuilder builder) {
        builder.feature(GenerationStep.Feature.UNDERGROUND_ORES, ModPlacedFeatures.DISK_SAND_PLACED);
        builder.feature(GenerationStep.Feature.UNDERGROUND_ORES, ModPlacedFeatures.DISK_CLAY_PLACED);
        builder.feature(GenerationStep.Feature.UNDERGROUND_ORES, ModPlacedFeatures.DISK_GRAVEL_PLACED);
    }

    public static void addTulgeyWoodsVegetation(GenerationSettings.LookupBackedBuilder builder) {
        DefaultBiomeFeatures.addForestGrass(builder);
        builder.feature(GenerationStep.Feature.VEGETAL_DECORATION, ModPlacedFeatures.TULGEY_TREE_PLACED);

    }

    public static void addValeOfTearsVegetation(GenerationSettings.LookupBackedBuilder builder) {
        DefaultBiomeFeatures.addForestGrass(builder);
        builder.feature(GenerationStep.Feature.VEGETAL_DECORATION, ModPlacedFeatures.WW_TREE_PLACED);

    }

}
