package net.emanueljdf09.dtrhmod.block.models.renderers;

import net.emanueljdf09.dtrhmod.block.entity.ExteriorChestEntity;
import net.emanueljdf09.dtrhmod.block.models.ExteriorChestModel;
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

public class ExteriorChestRenderer extends GeoBlockRenderer<ExteriorChestEntity> {

    public ExteriorChestRenderer(BlockEntityRendererFactory.Context context) {
        super(new ExteriorChestModel());
    }

}