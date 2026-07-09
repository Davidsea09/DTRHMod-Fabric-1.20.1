package net.emanueljdf09.dtrhmod.block.models.renderers;

import net.emanueljdf09.dtrhmod.block.entity.ExteriorDoorEntity;
import net.emanueljdf09.dtrhmod.block.models.ExteriorDoorModel;
import net.emanueljdf09.dtrhmod.util.ModComponents;
import net.emanueljdf09.dtrhmod.util.components.ProgressionComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;


public class ExteriorDoorRenderer extends GeoBlockRenderer<ExteriorDoorEntity> {

    public ExteriorDoorRenderer(BlockEntityRendererFactory.Context context) {
        super(new ExteriorDoorModel());
    }

    @Override
    public void actuallyRender(MatrixStack poseStack, ExteriorDoorEntity animatable, BakedGeoModel model, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        var player = MinecraftClient.getInstance().player;
        if (player != null) {
            ProgressionComponent component = ModComponents.PROGRESSION_COMPONENT.get(player);

            if (!component.hasOpenedExtDoor()) {

                var controller = animatable.getAnimatableInstanceCache()
                        .getManagerForId(0)
                        .getAnimationControllers().get("door_controller");

                if (controller != null) {
                    controller.forceAnimationReset();
                }
            }
        }

        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender,
                partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
