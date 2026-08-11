package net.emanueljdf09.dtrhmod.mixin;

import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {

    private static float currentFogStart = -8.0F;
    private static float currentFogEnd = 15.0F;

    @Inject(method = "applyFog", at = @At("TAIL"))
    private static void onApplyFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientWorld world = client.world;

        float targetStart = -8.0F;
        float targetEnd = viewDistance;

        if (world != null && camera.getFocusedEntity() != null) {
            BlockPos pos = camera.getBlockPos();
            Optional<RegistryKey<Biome>> biomeKey = world.getBiome(pos).getKey();

            if (biomeKey.isPresent()) {
                Identifier biomeId = biomeKey.get().getValue();

                if (biomeId.getNamespace().equals(DownTheRabbitHole.MOD_ID)) {
                    String path = biomeId.getPath();

                    if (path.equals("tulgey_wood")) {
                        targetStart = 4.0F;
                        targetEnd = 40.0F;
                    } else if (path.equals("vale_of_tears")) {
                        targetStart = 4.0F;
                        targetEnd = 32.0F;
                    } else if (path.equals("chessboard_fields")) {
                        targetStart = 10.0F;
                        targetEnd = 160.0F;
                    }
                }
            }
        }

        float transitionSpeed = 0.05F;
        currentFogStart += (targetStart - currentFogStart) * transitionSpeed;
        currentFogEnd += (targetEnd - currentFogEnd) * transitionSpeed;

        com.mojang.blaze3d.systems.RenderSystem.setShaderFogStart(currentFogStart);
        com.mojang.blaze3d.systems.RenderSystem.setShaderFogEnd(currentFogEnd);
    }
}