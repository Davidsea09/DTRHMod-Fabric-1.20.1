package net.emanueljdf09.dtrhmod.entity.client.models;

import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.emanueljdf09.dtrhmod.entity.custom.WeepingPlayerEntity;
import net.emanueljdf09.dtrhmod.util.TextureProcessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeepingPlayerModel extends GeoModel<WeepingPlayerEntity> {
    private static final Map<String, Identifier> STONE_SKIN_CACHE = new HashMap<>();
    private static final Identifier FALLBACK_STONE_TEXTURE = new Identifier(DownTheRabbitHole.MOD_ID, "textures/entity/pool/alice_slim.png");

    private static final List<Identifier> LOCAL_SKIN_POOL = List.of(
            new Identifier(DownTheRabbitHole.MOD_ID, "textures/entity/pool/alice_slim.png"),
            new Identifier(DownTheRabbitHole.MOD_ID, "textures/entity/pool/ceshire_slim.png"),
            new Identifier(DownTheRabbitHole.MOD_ID, "textures/entity/pool/hatter.png")
    );

    @Override
    public Identifier getModelResource(WeepingPlayerEntity animatable) {
        return new Identifier(DownTheRabbitHole.MOD_ID, "geo/entity/weeping_statue.geo.json");
    }

    @Override
    public Identifier getAnimationResource(WeepingPlayerEntity animatable) {
        return new Identifier(DownTheRabbitHole.MOD_ID, "animations/entity/weeping_players.animation.json");
    }

    @Override
    public Identifier getTextureResource(WeepingPlayerEntity animatable) {
        int poolIndex = animatable.getSkinPoolIndex();
        MinecraftClient client = MinecraftClient.getInstance();

        if (poolIndex == -1 && client.world != null) {
            PlayerEntity closestPlayer = client.world.getClosestPlayer(animatable.getX(), animatable.getY(), animatable.getZ(), 24.0, false);

            if (closestPlayer instanceof AbstractClientPlayerEntity player) {
                String cacheKey = "player_" + player.getUuid().toString();

                if (STONE_SKIN_CACHE.containsKey(cacheKey)) {
                    return STONE_SKIN_CACHE.get(cacheKey);
                }

                Identifier originalSkin = player.getSkinTexture();

                try {

                    var resourceOpt = client.getResourceManager().getResource(originalSkin);
                    if (resourceOpt.isPresent()) {
                        NativeImage originalNative = NativeImage.read(resourceOpt.get().getInputStream());
                        return registerAndCacheStoneTexture(client, originalNative, cacheKey, player.getUuid().toString());
                    }

                    AbstractTexture texture = client.getTextureManager().getTexture(originalSkin);
                    if (texture instanceof NativeImageBackedTexture nativeTex) {
                        NativeImage originalNative = nativeTex.getImage();
                        if (originalNative != null) {
                            NativeImage stoneNative = TextureProcessor.convertToStone(originalNative);
                            NativeImageBackedTexture dynamicTex = new NativeImageBackedTexture(stoneNative);
                            Identifier stoneIdentifier = new Identifier(DownTheRabbitHole.MOD_ID, "dynamic_stone_" + player.getUuid());

                            client.getTextureManager().registerTexture(stoneIdentifier, dynamicTex);
                            STONE_SKIN_CACHE.put(cacheKey, stoneIdentifier);
                            return stoneIdentifier;
                        }
                    }

                    return originalSkin;

                } catch (Exception e) {
                    return FALLBACK_STONE_TEXTURE;
                }
            }
            return FALLBACK_STONE_TEXTURE;
        }

        if (poolIndex >= LOCAL_SKIN_POOL.size() || poolIndex < 0) poolIndex = 0;
        Identifier localSkinPath = LOCAL_SKIN_POOL.get(poolIndex);
        String cacheKey = localSkinPath.toString();

        if (STONE_SKIN_CACHE.containsKey(cacheKey)) {
            return STONE_SKIN_CACHE.get(cacheKey);
        }

        try {
            NativeImage originalNative = NativeImage.read(client.getResourceManager().getResource(localSkinPath).get().getInputStream());
            return registerAndCacheStoneTexture(client, originalNative, cacheKey, "pool_" + poolIndex);
        } catch (Exception e) {
            return FALLBACK_STONE_TEXTURE;
        }
    }

    private Identifier registerAndCacheStoneTexture(MinecraftClient client, NativeImage originalNative, String cacheKey, String uniqueId) {
        NativeImage stoneNative = TextureProcessor.convertToStone(originalNative);
        NativeImageBackedTexture dynamicTex = new NativeImageBackedTexture(stoneNative);
        Identifier stoneIdentifier = new Identifier(DownTheRabbitHole.MOD_ID, "stone_tex_" + uniqueId);

        client.getTextureManager().registerTexture(stoneIdentifier, dynamicTex);
        STONE_SKIN_CACHE.put(cacheKey, stoneIdentifier);

        return stoneIdentifier;
    }

    public static boolean isPoolSkinSlim(int index) {
        if (index >= 0 && index < LOCAL_SKIN_POOL.size()) {
            return LOCAL_SKIN_POOL.get(index).getPath().contains("slim");
        }
        return false;
    }
}