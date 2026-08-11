package net.emanueljdf09.dtrhmod.world.biome;

import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.emanueljdf09.dtrhmod.world.features.WonderBiomeFeatures;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.*;

public class ModBiomes {
    public static final RegistryKey<Biome> TULGEY_WOOD =
            register("tulgey_wood");

    public static final RegistryKey<Biome> CHESSBOARD_FIELDS =
            register("chessboard_fields");

    public static final RegistryKey<Biome> VALE_OF_TEARS =
            register("vale_of_tears");

    public static final RegistryKey<Biome> THE_EXTERIOR =
            register("the_exterior");

    public static final RegistryKey<Biome> ENCHANTED_FOREST =
            register("enchanted_forest");

    public static final RegistryKey<Biome> WONDERLAND_OCEAN =
            register("wonderland_ocean");

    public static final RegistryKey<Biome> LAND_OF_FLOWERS =
            register("land_of_flowers");


    private static RegistryKey<Biome> register(String name) {
        return RegistryKey.of(
                RegistryKeys.BIOME,
                new Identifier(DownTheRabbitHole.MOD_ID, name)
        );
    }

    public static void bootstrap(Registerable<Biome> context) {
        context.register(TULGEY_WOOD, tulgeyForest(context));
        context.register(THE_EXTERIOR, theExterior(context));
        context.register(ENCHANTED_FOREST, enchantedForest(context));
        context.register(VALE_OF_TEARS, tearLakeValley(context));
        context.register(CHESSBOARD_FIELDS, chessBoardFields(context));
    }

    public  static Biome tulgeyForest(Registerable<Biome> context) {
        SpawnSettings.Builder spawnBuilder = new SpawnSettings.Builder();

        GenerationSettings.LookupBackedBuilder biomeBuilder =
                new GenerationSettings.LookupBackedBuilder(context.getRegistryLookup(RegistryKeys.PLACED_FEATURE),
                        context.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER));

        WonderBiomeFeatures.addDefaultDisks(biomeBuilder);
        WonderBiomeFeatures.addTulgeyWoodsVegetation(biomeBuilder);
        return new Biome.Builder()
                .precipitation(false)
                .temperature(0.7f)
                .downfall(0.8f)
                .effects(new BiomeEffects.Builder()
                        .waterColor(2977143)
                        .waterFogColor(2977143)
                        .fogColor(4416619)
                        .skyColor(4416619)
                        .grassColor(4416619)
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
                .precipitation(false)
                .temperature(0.7f)
                .downfall(0.8f)
                .effects(new BiomeEffects.Builder()
                        .waterColor(2919324)
                        .waterFogColor(2919324)
                        .fogColor(4418396)
                        .skyColor(4747926)
                        .grassColor(4418396)
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
                .precipitation(false)
                .temperature(0.7f)
                .downfall(0.8f)
                .effects(new BiomeEffects.Builder()
                        .waterColor(2461864)
                        .waterFogColor(2461864)
                        .fogColor(6000314)
                        .skyColor(6000314)
                        .grassColor(3576124)
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
