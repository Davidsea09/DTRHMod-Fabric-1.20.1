package net.emanueljdf09.dtrhmod;

import net.emanueljdf09.dtrhmod.block.ModBlockEntities;
import net.emanueljdf09.dtrhmod.block.ModBlocks;
import net.emanueljdf09.dtrhmod.block.models.renderers.ExteriorChestRenderer;
import net.emanueljdf09.dtrhmod.block.models.renderers.ExteriorDoorRenderer;
import net.emanueljdf09.dtrhmod.block.models.renderers.MadHatterHatBlockEntityRenderer;
import net.emanueljdf09.dtrhmod.block.models.renderers.MirrorBlockEntityRenderer;
import net.emanueljdf09.dtrhmod.entity.ModEntities;
import net.emanueljdf09.dtrhmod.entity.client.renderers.WeepingPlayerRenderer;
import net.emanueljdf09.dtrhmod.entity.client.renderers.WhiteRabbitRenderer;
import net.emanueljdf09.dtrhmod.item.ModItems;
import net.emanueljdf09.dtrhmod.item.custom.DynamicTeaCupItem;
import net.emanueljdf09.dtrhmod.menu.ModScreenHandlers;
import net.emanueljdf09.dtrhmod.menu.screen.BookCoverScreen;
import net.emanueljdf09.dtrhmod.menu.screen.TeapotScreen;
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
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.potion.PotionUtil;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;

import javax.xml.stream.events.EntityReference;

public class DownTheRabbitHoleClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        registerClientNetworkPackets();


        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> GrassColors.getColor(0.5, 1.0), ModBlocks.WONDER_GRASS);

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            if (tintIndex == 1) {
                int baseColor = net.minecraft.potion.PotionUtil.getColor(DynamicTeaCupItem.getEffectsFromStack(stack));

                NbtCompound nbt = stack.getNbt();
                if (nbt != null && nbt.contains("FluidUsed")) {
                    String fluidId = nbt.getString("FluidUsed");

                    int r = (baseColor >> 16) & 0xFF;
                    int g = (baseColor >> 8) & 0xFF;
                    int b = baseColor & 0xFF;

                    if (fluidId.contains("lava_bucket")) {
                        r = (int) (r * 0.6 + 0xFF * 0.4);
                        g = (int) (g * 0.6 + 0x45 * 0.4);
                        b = (int) (b * 0.6 + 0x00 * 0.4);
                        return (r << 16) | (g << 8) | b;
                    }
                    else if (fluidId.contains("milk_bucket")) {
                        r = (int) (r * 0.5 + 255 * 0.5);
                        g = (int) (g * 0.5 + 255 * 0.5);
                        b = (int) (b * 0.5 + 255 * 0.5);
                        return (r << 16) | (g << 8) | b;
                    }
                }
                return baseColor;
            }
            return -1;
        }, ModItems.FILLED_TEA_CUP);

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
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.YELLOW_MUSHROOM, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BLUE_MUSHROOM, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MAGENTA_MUSHROOM, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.TEAPOT_BLOCK, RenderLayer.getCutout());

        BlockEntityRendererRegistry.register(ModBlockEntities.MIRROR_BLOCK_ENTITY, MirrorBlockEntityRenderer::new);

        BlockEntityRendererRegistry.register(ModBlockEntities.EXTERIOR_DOOR_ENTITY, ExteriorDoorRenderer::new);
        BlockEntityRendererRegistry.register(ModBlockEntities.EXTERIOR_CHEST_ENTITY, ExteriorChestRenderer::new);

        //BlockEntityRendererRegistry.register(ModBlockEntities.MAD_HATTER_HAT, MadHatterHatBlockEntityRenderer::new);

        EntityRendererRegistry.register(ModEntities.WHITE_RABBIT, WhiteRabbitRenderer::new);
        EntityRendererRegistry.register(ModEntities.WEEPING_PLAYER, WeepingPlayerRenderer::new);

        HandledScreens.register(ModScreenHandlers.TEAPOT_SCREEN_HANDLER, TeapotScreen::new);
    }

    public static void registerClientNetworkPackets() {
        ClientPlayNetworking.registerGlobalReceiver(ModPackets.OPEN_BOOK_PACKET_ID, (client, handler, buf, responseSender) -> {

            String triggerKey = buf.readString();

           client.execute(() -> {

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
