package net.emanueljdf09.dtrhmod.datagen;

import net.emanueljdf09.dtrhmod.util.ModTags;
import net.emanueljdf09.dtrhmod.world.biome.ModBiomes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.world.biome.Biome;

import java.util.concurrent.CompletableFuture;

public class ModBiomeTagProvider extends FabricTagProvider<Biome> {

    public ModBiomeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.BIOME, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {

        getOrCreateTagBuilder(ModTags.Biomes.WONDERLAND_BIOMES)
                .add(ModBiomes.CHESSBOARD_FIELDS)
                .add(ModBiomes.TEAR_LAKE_VALLEY)
                .add(ModBiomes.TULGEY_FOREST);

        getOrCreateTagBuilder(ModTags.Biomes.IS_CHESSBOARD)
                .add(ModBiomes.CHESSBOARD_FIELDS);

        getOrCreateTagBuilder(ModTags.Biomes.IS_EXTERIOR)
                .add(ModBiomes.THE_EXTERIOR);

        getOrCreateTagBuilder(ModTags.Biomes.HAS_MIRROR)
                .addOptionalTag(BiomeTags.IS_OVERWORLD)
                .addTag(ModTags.Biomes.WONDERLAND_BIOMES);
    }
}
