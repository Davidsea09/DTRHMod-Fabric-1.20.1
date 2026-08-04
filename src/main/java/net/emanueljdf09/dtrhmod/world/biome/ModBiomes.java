package net.emanueljdf09.dtrhmod.world.biome;

import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.emanueljdf09.dtrhmod.world.features.WonderBiomeFeatures;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;

public class ModBiomes {
    public static final RegistryKey<Biome> TULGEY_FOREST =
            register("tulgey_forest");

    public static final RegistryKey<Biome> CHESSBOARD_FIELDS =
            register("chessboard_fields");

    public static final RegistryKey<Biome> TEAR_LAKE_VALLEY =
            register("tear_lake_valley");

    public static final RegistryKey<Biome> THE_EXTERIOR =
            register("the_exterior");

    public static final RegistryKey<Biome> ENCHANTED_FOREST =
            register("enchanted_forest");

    public static final RegistryKey<Biome> LAND_OF_FLOWERS =
            register("land_of_flowers");


    private static RegistryKey<Biome> register(String name) {
        return RegistryKey.of(
                RegistryKeys.BIOME,
                new Identifier(DownTheRabbitHole.MOD_ID, name)
        );
    }

    public static void bootstrap(Registerable<Biome> context) {
        context.register(TULGEY_FOREST, tulgeyForest(context));
        context.register(THE_EXTERIOR, theExterior(context));
        context.register(ENCHANTED_FOREST, enchantedForest(context));
        context.register(TEAR_LAKE_VALLEY, tearLakeValley(context));
        context.register(CHESSBOARD_FIELDS, chessBoardFields(context));
    }

    public static void globalWonderlandGeneration(GenerationSettings.LookupBackedBuilder builder) {
    }

    public  static Biome tulgeyForest(Registerable<Biome> context) {
        SpawnSettings.Builder spawnBuilder = new SpawnSettings.Builder();

        GenerationSettings.LookupBackedBuilder biomeBuilder =
                new GenerationSettings.LookupBackedBuilder(context.getRegistryLookup(RegistryKeys.PLACED_FEATURE),
                        context.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER));

        WonderBiomeFeatures.addDefaultDisks(biomeBuilder);
        WonderBiomeFeatures.addTulgeyWoodsVegetation(biomeBuilder);
        return new Biome.Builder()
                .precipitation(true)
                .temperature(0.7f)
                .downfall(0.8f)
                .effects(new BiomeEffects.Builder()
                        .waterColor(4159204)
                        .waterFogColor(329011)
                        .fogColor(12632256)
                        .skyColor(7907327)
                        .grassColor(7907327)
                        .build())
                .spawnSettings(spawnBuilder.build())
                .generationSettings(biomeBuilder.build())
                .build();
    }

    public  static Biome tearLakeValley(Registerable<Biome> context) {
        SpawnSettings.Builder spawnBuilder = new SpawnSettings.Builder();

        GenerationSettings.LookupBackedBuilder biomeBuilder =
                new GenerationSettings.LookupBackedBuilder(context.getRegistryLookup(RegistryKeys.PLACED_FEATURE),
                        context.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER));

        WonderBiomeFeatures.addDefaultDisks(biomeBuilder);
        WonderBiomeFeatures.addValeOfTearsVegetation(biomeBuilder);

        return new Biome.Builder()
                .precipitation(true)
                .temperature(0.7f)
                .downfall(0.8f)
                .effects(new BiomeEffects.Builder()
                        .waterColor(4159204)
                        .waterFogColor(329011)
                        .fogColor(12632256)
                        .skyColor(7907986)
                        .grassColor(7907986)
                        .build())
                .spawnSettings(spawnBuilder.build())
                .generationSettings(biomeBuilder.build())
                .build();
    }

    public  static Biome chessBoardFields(Registerable<Biome> context) {
        SpawnSettings.Builder spawnBuilder = new SpawnSettings.Builder();

        GenerationSettings.LookupBackedBuilder biomeBuilder =
                new GenerationSettings.LookupBackedBuilder(context.getRegistryLookup(RegistryKeys.PLACED_FEATURE),
                        context.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER));

        WonderBiomeFeatures.addDefaultDisks(biomeBuilder);
        return new Biome.Builder()
                .precipitation(true)
                .temperature(0.7f)
                .downfall(0.8f)
                .effects(new BiomeEffects.Builder()
                        .waterColor(4159204)
                        .waterFogColor(329011)
                        .fogColor(12632256)
                        .skyColor(7907853)
                        .grassColor(7907853)
                        .build())
                .spawnSettings(spawnBuilder.build())
                .generationSettings(biomeBuilder.build())
                .build();
    }

    public  static Biome theExterior(Registerable<Biome> context) {
        SpawnSettings.Builder spawnBuilder = new SpawnSettings.Builder();

        GenerationSettings.LookupBackedBuilder biomeBuilder =
                new GenerationSettings.LookupBackedBuilder(context.getRegistryLookup(RegistryKeys.PLACED_FEATURE),
                        context.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER));

        return new Biome.Builder()
                .precipitation(false)
                .temperature(0.5f)
                .downfall(0.5f)
                .effects(new BiomeEffects.Builder()
                        .waterColor(4159318)
                        .waterFogColor(329011)
                        .fogColor(12632256)
                        .skyColor(7907303)
                        .grassColor(7907303)
                        .build())
                .spawnSettings(spawnBuilder.build())
                .generationSettings(biomeBuilder.build())
                .build();
    }

    public  static Biome enchantedForest(Registerable<Biome> context) {
        SpawnSettings.Builder spawnBuilder = new SpawnSettings.Builder();

        GenerationSettings.LookupBackedBuilder biomeBuilder =
                new GenerationSettings.LookupBackedBuilder(context.getRegistryLookup(RegistryKeys.PLACED_FEATURE),
                        context.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER));

        return new Biome.Builder()
                .precipitation(false)
                .temperature(0.5f)
                .downfall(0.5f)
                .effects(new BiomeEffects.Builder()
                        .waterColor(4159318)
                        .waterFogColor(329011)
                        .fogColor(12632256)
                        .skyColor(7907303)
                        .grassColor(7907303)
                        .build())
                .spawnSettings(spawnBuilder.build())
                .generationSettings(biomeBuilder.build())
                .build();
    }
}
