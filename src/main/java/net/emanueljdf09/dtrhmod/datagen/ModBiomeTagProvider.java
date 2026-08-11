package net.emanueljdf09.dtrhmod.datagen;

import net.emanueljdf09.dtrhmod.util.ModTags;
import net.emanueljdf09.dtrhmod.world.biome.ModBiomes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

import java.util.concurrent.CompletableFuture;

public class ModBiomeTagProvider extends FabricTagProvider<Biome> {

    public ModBiomeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.BIOME, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {

        getOrCreateTagBuilder(ModTags.Biomes.WONDERLAND_BIOMES)
                .add(ModBiomes.CHESSBOARD_FIELDS)
                .add(ModBiomes.VALE_OF_TEARS)
                .add(ModBiomes.TULGEY_WOOD);

        getOrCreateTagBuilder(ModTags.Biomes.IS_CHESSBOARD)
                .add(ModBiomes.CHESSBOARD_FIELDS);

        getOrCreateTagBuilder(ModTags.Biomes.IS_EXTERIOR)
                .add(ModBiomes.THE_EXTERIOR);

        getOrCreateTagBuilder(ModTags.Biomes.HAS_MIRROR)
                .addOptionalTag(BiomeTags.IS_OVERWORLD)
                .addTag(ModTags.Biomes.WONDERLAND_BIOMES);

        getOrCreateTagBuilder(ModTags.Biomes.HAS_RABBITHOLE)
                .add(BiomeKeys.WINDSWEPT_FOREST)
                .add(BiomeKeys.SUNFLOWER_PLAINS)
                .add(BiomeKeys.PLAINS)
                .add(BiomeKeys.MEADOW)
                .add(BiomeKeys.FLOWER_FOREST)
                .add(BiomeKeys.FOREST)
                .add(BiomeKeys.CHERRY_GROVE);
    }
}
