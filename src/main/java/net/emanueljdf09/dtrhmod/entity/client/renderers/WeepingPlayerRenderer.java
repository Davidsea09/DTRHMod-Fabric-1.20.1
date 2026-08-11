package net.emanueljdf09.dtrhmod.entity.client.renderers;

import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.emanueljdf09.dtrhmod.entity.client.models.WeepingPlayerModel;
import net.emanueljdf09.dtrhmod.entity.custom.WeepingPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;


public class WeepingPlayerRenderer extends GeoEntityRenderer<WeepingPlayerEntity> {

    private static final Identifier BASE_STONE_TEX = new Identifier(DownTheRabbitHole.MOD_ID, "textures/entity/weeping_statue.png");

    public WeepingPlayerRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new WeepingPlayerModel());

    }

    @Override
    public void renderRecursively(MatrixStack poseStack, WeepingPlayerEntity animatable, GeoBone bone,
                                  RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer,
                                  boolean isReRender, float partialTick, int packedLight, int packedOverlay,
                                  float red, float green, float blue, float alpha) {

        if (bone.getName().equals("statue_base")) {
            renderType = RenderLayer.getEntityCutoutNoCull(BASE_STONE_TEX);
            buffer = bufferSource.getBuffer(renderType);
        }

        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer,
                isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public Identifier getTextureLocation(WeepingPlayerEntity animatable) {
        return this.model.getTextureResource(animatable);
    }
}
