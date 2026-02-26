package net.emanueljdf09.dtrhmod;

import net.emanueljdf09.dtrhmod.block.ModBlocks;
import net.emanueljdf09.dtrhmod.util.ModTags;
import net.emanueljdf09.dtrhmod.world.biome.ModBiomes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.mixin.client.rendering.DimensionEffectsAccessor;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.GrassColors;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class DownTheRabbitHoleClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> GrassColors.getColor(0.5, 1.0), ModBlocks.CHESS_GRASS);
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> GrassColors.getColor(0.5, 1.0), ModBlocks.WONDER_GRASS);

        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {

            if (tintIndex != 0) return 0xFFFFFF;

            if (world == null || pos == null)
                return 0xFFFFFF;

            int baseColor = BiomeColors.getGrassColor(world, pos);

            int size = 4;

            boolean darkSquare =
                    (Math.floorDiv(pos.getX(), size)
                            + Math.floorDiv(pos.getZ(), size)) % 2 == 0;

            if (!darkSquare) return baseColor;

            return darken(baseColor, 0.7f);

        }, ModBlocks.CHESS_GRASS);

        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {

            if (tintIndex != 0) return 0xFFFFFF;

            if (world == null || pos == null)
                return 0xFFFFFF;

            int baseColor = BiomeColors.getGrassColor(world, pos);

            var biome = world.getBiomeFabric(pos);

            boolean isChessBiome =
                    biome.isIn(ModTags.Biomes.IS_CHESSBOARD);

            if (!isChessBiome)
                return baseColor;

            int size = 4;

            boolean darkSquare =
                    (Math.floorDiv(pos.getX(), size)
                            + Math.floorDiv(pos.getZ(), size)) % 2 == 0;

            return darkSquare ? darken(baseColor, 0.7f) : baseColor;

        }, ModBlocks.WONDER_GRASS);

        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {

            if (tintIndex != 0) {
                return world != null && pos != null ? BiomeColors.getGrassColor(world, pos) : GrassColors.getDefaultColor();
            } else {
                return -1;
            }
        }, ModBlocks.LAWN_DAISY_PATCH);

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CHESS_GRASS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WONDER_GRASS, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LAWN_DAISY_PATCH, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.TH_LEAVES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.TH_DOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.TH_TRAPDOOR, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WW_LEAVES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WW_DOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WW_TRAPDOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WW_HANGING_LEAVES_PLANT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WW_HANGING_LEAVES, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BB_LEAVES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BB_DOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BB_TRAPDOOR, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.TH_SAPLING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WW_SAPLING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BB_SAPLING, RenderLayer.getCutout());



    }

    private static int darken(int color, float factor) {

        int r = (int)(((color >> 16) & 255) * factor);
        int g = (int)(((color >> 8) & 255) * factor);
        int b = (int)((color & 255) * factor);

        return (r << 16) | (g << 8) | b;
    }
}
