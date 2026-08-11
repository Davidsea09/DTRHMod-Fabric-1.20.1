package net.emanueljdf09.dtrhmod;

import com.terraformersmc.terraform.sign.SpriteIdentifierRegistry;
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
import net.emanueljdf09.dtrhmod.util.*;
import net.emanueljdf09.dtrhmod.util.components.ProgressionComponent;
import net.emanueljdf09.dtrhmod.util.particles.FallingBBLeafParticle;
import net.emanueljdf09.dtrhmod.util.particles.FallingTHLeafParticle;
import net.emanueljdf09.dtrhmod.util.particles.FallingWWLeafParticle;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.*;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.GrassColors;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.potion.PotionUtil;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

import java.util.Optional;

public class DownTheRabbitHoleClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        registerClientNetworkPackets();

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> GrassColors.getColor(0.5, 1.0), ModBlocks.WONDER_GRASS);

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            if (tintIndex == 1) {
                int baseColor = PotionUtil.getColor(DynamicTeaCupItem.getEffectsFromStack(stack));

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
                    } else if (fluidId.contains("milk_bucket")) {
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

        }, ModBlocks.WONDER_GRASS,
                Blocks.GRASS,
                Blocks.TALL_GRASS,
                Blocks.FERN,
                Blocks.LARGE_FERN
        );

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

        BlockEntityRendererRegistry.register(ModBlockEntities.MAD_HATTER_HAT, MadHatterHatBlockEntityRenderer::new);

        EntityRendererRegistry.register(ModEntities.WHITE_RABBIT, WhiteRabbitRenderer::new);
        EntityRendererRegistry.register(ModEntities.WEEPING_PLAYER, WeepingPlayerRenderer::new);

        HandledScreens.register(ModScreenHandlers.TEAPOT_SCREEN_HANDLER, TeapotScreen::new);

        SpriteIdentifierRegistry.INSTANCE.addIdentifier(new SpriteIdentifier(TexturedRenderLayers.SIGNS_ATLAS_TEXTURE, ModBlocks.WW_SIGN_TEXTURE));
        SpriteIdentifierRegistry.INSTANCE.addIdentifier(new SpriteIdentifier(TexturedRenderLayers.SIGNS_ATLAS_TEXTURE, ModBlocks.HANGING_WW_SIGN_TEXTURE));
        SpriteIdentifierRegistry.INSTANCE.addIdentifier(new SpriteIdentifier(TexturedRenderLayers.SIGNS_ATLAS_TEXTURE, ModBlocks.BB_SIGN_TEXTURE));
        SpriteIdentifierRegistry.INSTANCE.addIdentifier(new SpriteIdentifier(TexturedRenderLayers.SIGNS_ATLAS_TEXTURE, ModBlocks.HANGING_BB_SIGN_TEXTURE));
        SpriteIdentifierRegistry.INSTANCE.addIdentifier(new SpriteIdentifier(TexturedRenderLayers.SIGNS_ATLAS_TEXTURE, ModBlocks.TH_SIGN_TEXTURE));
        SpriteIdentifierRegistry.INSTANCE.addIdentifier(new SpriteIdentifier(TexturedRenderLayers.SIGNS_ATLAS_TEXTURE, ModBlocks.HANGING_TH_SIGN_TEXTURE));


        ParticleFactoryRegistry.getInstance().register(
                ModParticles.BB_LEAVE_PARTICLE,
                FallingBBLeafParticle.Factory::new
        );
        ParticleFactoryRegistry.getInstance().register(
                ModParticles.TH_LEAVE_PARTICLE,
                FallingTHLeafParticle.Factory::new
        );
        ParticleFactoryRegistry.getInstance().register(
                ModParticles.WW_LEAVE_PARTICLE,
                FallingWWLeafParticle.Factory::new
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.world == null || client.isPaused()) return;

            if (client.world.getRegistryKey().getValue().getNamespace().equals(DownTheRabbitHole.MOD_ID)) {
                ProgressionComponent progression = ModComponents.PROGRESSION_COMPONENT.get(client.player);
                int stage = progression.getCompletedStages();
                BlockPos playerPos = client.player.getBlockPos();

                if (client.world.getTime() % 3 == 0) {
                    Optional<RegistryKey<Biome>> currentBiomeKey = client.world.getBiome(playerPos).getKey();
                    if (currentBiomeKey.isPresent()) {
                        Identifier biomeId = currentBiomeKey.get().getValue();

                        if (biomeId.getPath().equals("tulgey_wood")) {
                            double x = client.player.getX() + (client.world.random.nextDouble() - 0.5) * 16.0;
                            double y = client.player.getY() + client.world.random.nextDouble() * 8.0;
                            double z = client.player.getZ() + (client.world.random.nextDouble() - 0.5) * 16.0;
                            client.world.addParticle(ParticleTypes.SOUL, x, y, z, 0.0, 0.05, 0.0);
                        } else if (biomeId.getPath().equals("vale_of_tears") && stage >= 1) {
                            double x = client.player.getX() + (client.world.random.nextDouble() - 0.5) * 16.0;
                            double y = client.player.getY() + client.world.random.nextDouble() * 8.0;
                            double z = client.player.getZ() + (client.world.random.nextDouble() - 0.5) * 16.0;
                            client.world.addParticle(ParticleTypes.SOUL, x, y, z, 0.0, 0.05, 0.0);
                        } else if (biomeId.getPath().equals("chessboard_fields") && stage >= 2) {
                            double x = client.player.getX() + (client.world.random.nextDouble() - 0.5) * 16.0;
                            double y = client.player.getY() + client.world.random.nextDouble() * 5.0;
                            double z = client.player.getZ() + (client.world.random.nextDouble() - 0.5) * 16.0;
                            client.world.addParticle(ParticleTypes.END_ROD, x, y, z, 0.0, -0.01, 0.0);
                        }
                    }
                }

                if (client.world.getTime() % 10 == 0) {
                    int radius = 8;
                    for (int x = -radius; x <= radius; x += 2) {
                        for (int z = -radius; z <= radius; z += 2) {
                            BlockPos checkPos = playerPos.add(x, 0, z);
                            Optional<RegistryKey<Biome>> biomeKey = client.world.getBiome(checkPos).getKey();

                            if (biomeKey.isPresent()) {
                                Identifier biomeId = biomeKey.get().getValue();
                                boolean isLocked = false;

                                if (biomeId.getPath().equals("vale_of_tears") && stage < 1) {
                                    isLocked = true;
                                } else if (biomeId.getPath().equals("chessboard_fields") && stage < 2) {
                                    isLocked = true;
                                }

                                if (isLocked) {
                                    double px = checkPos.getX() + 0.5;
                                    double pz = checkPos.getZ() + 0.5;

                                    for (double py = client.player.getY() - 1; py <= client.player.getY() + 3; py += 1.0) {
                                        client.world.addParticle(
                                                ParticleTypes.REVERSE_PORTAL,
                                                px, py, pz,
                                                0.0, 0.05, 0.0
                                        );
                                    }
                                }
                            }
                        }
                    }
                }

                if (client.world.getTime() % 5 == 0) {
                    Optional<RegistryKey<Biome>> currentBiomeKey = client.world.getBiome(playerPos).getKey();
                    if (currentBiomeKey.isPresent()) {
                        Identifier biomeId = currentBiomeKey.get().getValue();

                        if (biomeId.getPath().equals("tulgey_wood")) {

                            for (int i = 0; i < 4; i++) {

                                double x = client.player.getX() + (client.world.random.nextDouble() - 0.5) * 32.0;
                                double y = client.player.getY() + 15.0 + client.world.random.nextDouble() * 15.0;
                                double z = client.player.getZ() + (client.world.random.nextDouble() - 0.5) * 32.0;

                                client.world.addParticle(
                                        ModParticles.BB_LEAVE_PARTICLE,
                                        x, y, z,
                                        (client.world.random.nextDouble() - 0.5) * 0.02, -0.02, (client.world.random.nextDouble() - 0.5) * 0.02
                                );
                                client.world.addParticle(
                                        ModParticles.TH_LEAVE_PARTICLE,
                                        x, y, z,
                                        (client.world.random.nextDouble() - 0.5) * 0.02, -0.02, (client.world.random.nextDouble() - 0.5) * 0.02
                                );
                            }
                        }

                        else if (biomeId.getPath().equals("vale_of_tears") && stage >= 1) {
                            for (int i = 0; i < 4; i++) {
                                double x = client.player.getX() + (client.world.random.nextDouble() - 0.5) * 32.0;
                                double y = client.player.getY() + 15.0 + client.world.random.nextDouble() * 15.0;
                                double z = client.player.getZ() + (client.world.random.nextDouble() - 0.5) * 32.0;

                                client.world.addParticle(
                                        ModParticles.WW_LEAVE_PARTICLE,
                                        x, y, z,
                                        (client.world.random.nextDouble() - 0.5) * 0.02, -0.02, (client.world.random.nextDouble() - 0.5) * 0.02
                                );
                            }
                        }

                        else if (biomeId.getPath().equals("chessboard_fields") && stage >= 2) {
                            for (int i = 0; i < 3; i++) {
                                double x = client.player.getX() + (client.world.random.nextDouble() - 0.5) * 32.0;
                                double y = client.player.getY() + 15.0 + client.world.random.nextDouble() * 15.0;
                                double z = client.player.getZ() + (client.world.random.nextDouble() - 0.5) * 32.0;

                                client.world.addParticle(
                                        ParticleTypes.FALLING_NECTAR,
                                        x, y, z,
                                        0.0, -0.05, 0.0
                                );
                            }
                        }
                    }
                }
            }
        });
    }

    public static void registerClientNetworkPackets() {
        ClientPlayNetworking.registerGlobalReceiver(ModPackets.OPEN_BOOK_PACKET_ID, (client, handler, buf, responseSender) -> {

            String triggerKey = buf.readString();

           client.execute(() -> {

               MinecraftClient.getInstance().setScreen(new BookCoverScreen(triggerKey));
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(ModPackets.LOCKED_BIOME_TOAST_PACKET_ID, (client, handler, buf, responseSender) -> {
            client.execute(() -> {

                ClientProgressionCache.setNearLockedBiome(true);
                MinecraftClient.getInstance().getToastManager().add(new WonderlandProgressionUtil.LockedBiomeToast(new ItemStack(ModItems.POCKETWATCH)));
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
