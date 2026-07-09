package net.emanueljdf09.dtrhmod;

import net.emanueljdf09.dtrhmod.block.ModBlockEntities;
import net.emanueljdf09.dtrhmod.block.ModBlocks;
import net.emanueljdf09.dtrhmod.block.models.renderers.ExteriorChestRenderer;
import net.emanueljdf09.dtrhmod.block.models.renderers.ExteriorDoorRenderer;
import net.emanueljdf09.dtrhmod.block.models.renderers.MirrorBlockEntityRenderer;
import net.emanueljdf09.dtrhmod.entity.ModEntities;
import net.emanueljdf09.dtrhmod.entity.client.renderers.WeepingPlayerRenderer;
import net.emanueljdf09.dtrhmod.entity.client.renderers.WhiteRabbitRenderer;
import net.emanueljdf09.dtrhmod.menu.screen.BookCoverScreen;
import net.emanueljdf09.dtrhmod.util.ModPackets;
import net.emanueljdf09.dtrhmod.util.ModTags;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.GrassColors;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;

import javax.xml.stream.events.EntityReference;

public class DownTheRabbitHoleClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        registerClientNetworkPackets();


        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> GrassColors.getColor(0.5, 1.0), ModBlocks.WONDER_GRASS);

        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {

            if (tintIndex != 0) return 0xFFFFFF;

            if (world == null || pos == null)
                return 0xFFFFFF;

            int baseColor = BiomeColors.getGrassColor(world, pos);

            var biome = world.getBiomeFabric(pos);

            if (biome == null || !biome.hasKeyAndValue()) {
                return baseColor;
            }

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

        BlockEntityRendererRegistry.register(ModBlockEntities.MIRROR_BLOCK_ENTITY, MirrorBlockEntityRenderer::new);

        BlockEntityRendererRegistry.register(ModBlockEntities.EXTERIOR_DOOR_ENTITY, ExteriorDoorRenderer::new);
        BlockEntityRendererRegistry.register(ModBlockEntities.EXTERIOR_CHEST_ENTITY, ExteriorChestRenderer::new);

        EntityRendererRegistry.register(ModEntities.WHITE_RABBIT, WhiteRabbitRenderer::new);
        EntityRendererRegistry.register(ModEntities.WEEPING_PLAYER, WeepingPlayerRenderer::new);
    }

    public static void registerClientNetworkPackets() {
        // Register a receiver bound to our unique packet ID channel
        ClientPlayNetworking.registerGlobalReceiver(ModPackets.OPEN_BOOK_PACKET_ID, (client, handler, buf, responseSender) -> {

            // 1. Read the exact same data out in the EXACT same order it was written server-side
            String triggerKey = buf.readString();

            // 2. Switch from the network thread to the main game engine thread safely
            client.execute(() -> {
                // 3. Force-open your highly customizable book screen using your new string constructor!
                MinecraftClient.getInstance().setScreen(new BookCoverScreen(triggerKey));
            });
        });
    }

    private static int darken(int color, float factor) {

        int r = (int)(((color >> 16) & 255) * factor);
        int g = (int)(((color >> 8) & 255) * factor);
        int b = (int)((color & 255) * factor);

        return (r << 16) | (g << 8) | b;
    }
}
