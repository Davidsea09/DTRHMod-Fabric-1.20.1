package net.emanueljdf09.dtrhmod.block.models.renderers;

import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.emanueljdf09.dtrhmod.block.ModBlocks;
import net.emanueljdf09.dtrhmod.block.custom.mirror.MirrorBlock;
import net.emanueljdf09.dtrhmod.block.custom.mirror.MirrorType;
import net.emanueljdf09.dtrhmod.block.entity.MirrorBlockEntity;
import net.emanueljdf09.dtrhmod.util.ModComponents;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;

import java.util.List;

public class MirrorBlockEntityRenderer implements BlockEntityRenderer<MirrorBlockEntity> {
    private static final Identifier WONDERLAND_PORTAL = new Identifier(DownTheRabbitHole.MOD_ID, "textures/block/mirror/mirror_portal_animated.png");
    private static final Identifier MAGIC_MIRROR_FACE = new Identifier(DownTheRabbitHole.MOD_ID, "textures/block/mirror/magic_mirror_face.png");

    private final BlockRenderManager renderManager;

    public MirrorBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.renderManager = ctx.getRenderManager();
    }

    @Override
    public void render(MirrorBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        BlockState state = entity.getCachedState();
        if (!state.isOf(net.emanueljdf09.dtrhmod.block.ModBlocks.MIRROR_BLOCK)) return;

        MirrorType type = state.get(MirrorBlock.TYPE);
        if (type != MirrorType.wonderland && type != MirrorType.structure && type != MirrorType.magic_mirror) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        float overlayAlpha = 0.0f;
        Identifier textureToUse = WONDERLAND_PORTAL;

        // --- Distance Logic ---
        double pX = client.player.getX();
        double pY = client.player.getEyeY();
        double pZ = client.player.getZ();
        double mX = entity.getPos().getX() + 0.5;
        double mY = entity.getPos().getY() + 1.0;
        double mZ = entity.getPos().getZ() + 0.5;
        double distance = Math.sqrt(Math.pow(pX - mX, 2) + Math.pow(pY - mY, 2) + Math.pow(pZ - mZ, 2));

        if (type == MirrorType.magic_mirror) {
            textureToUse = MAGIC_MIRROR_FACE;

            // Direct, zero-cost component access on the client!
            java.util.UUID owner = ModComponents.MIRROR_GOSSIP.get(entity).getOwnerUuid();

            if (owner != null && client.player.getUuid().equals(owner) && distance <= 4.0) {
                overlayAlpha = 1.0f;
            } else {
                overlayAlpha = 0.0f;
            }
        } else {
            // --- RESTORED WONDERLAND / STRUCTURE DISTANCE LOGIC ---

            if (entity.isPlayerInTrance(client.player.getUuid())) {
                overlayAlpha = 1.0f;
            } else {
                double maxDist = 6.0;
                double minDist = 2.0;
                if (distance <= minDist) {
                    overlayAlpha = 1.0f;
                } else if (distance < maxDist) {
                    overlayAlpha = (float) ((maxDist - distance) / (maxDist - minDist));
                } else {
                    overlayAlpha = 0.0f;
                }
            }
        }

        if (overlayAlpha > 0.0f) {
            Direction facing = state.get(MirrorBlock.FACING);

            matrices.push();

            // 1. Pivot rotation around the center point natively
            matrices.translate(0.5, 0.5, 0.5);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-facing.asRotation()));

            // 2. THE FLOOD-FIX FLIP: Invert the local Z-axis using a negative scale factor!
            // This safely mirrors the rendering grid to the opposite side without breaking backface culling.
            matrices.scale(1.0f, 1.0f, -1.0f);

            matrices.translate(-0.5, -0.5, -0.5);

            // 3. Shift to your exact Z glass plane (15.25 / 16.0), pulling it forward a tiny hair
            float glassDepth = (15.25f / 16.0f) - 0.003f;
            matrices.translate(0.0f, 0.0f, glassDepth);

            VertexConsumer buffer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(textureToUse));
            MatrixStack.Entry entry = matrices.peek();

            // 4. Boundaries based on your 64x64 Blockbench elements
            float minX = 2.25f / 16.0f;
            float maxX = 13.75f / 16.0f;
            float minY = 2.75f / 16.0f;
            float maxY = 2.0f - minX;

            // 5. Draw vertices manual style
            buffer.vertex(entry.getPositionMatrix(), minX, minY, 0.0f).color(1f, 1f, 1f, overlayAlpha).texture(1f, 1f).overlay(overlay).light(0xF000F0).normal(entry.getNormalMatrix(), 0f, 0f, -1f).next();
            buffer.vertex(entry.getPositionMatrix(), maxX, minY, 0.0f).color(1f, 1f, 1f, overlayAlpha).texture(0f, 1f).overlay(overlay).light(0xF000F0).normal(entry.getNormalMatrix(), 0f, 0f, -1f).next();
            buffer.vertex(entry.getPositionMatrix(), maxX, maxY, 0.0f).color(1f, 1f, 1f, overlayAlpha).texture(0f, 0f).overlay(overlay).light(0xF000F0).normal(entry.getNormalMatrix(), 0f, 0f, -1f).next();
            buffer.vertex(entry.getPositionMatrix(), minX, maxY, 0.0f).color(1f, 1f, 1f, overlayAlpha).texture(1f, 0f).overlay(overlay).light(0xF000F0).normal(entry.getNormalMatrix(), 0f, 0f, -1f).next();

            matrices.pop();
        }
    }
}