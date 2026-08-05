package net.emanueljdf09.dtrhmod.block.models.renderers;

import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.emanueljdf09.dtrhmod.block.entity.MadHatterHatBlockEntity;
import net.emanueljdf09.dtrhmod.block.models.MadHatterHatModel;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class MadHatterHatBlockEntityRenderer extends GeoBlockRenderer<MadHatterHatBlockEntity> {
    public MadHatterHatBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        super(new MadHatterHatModel());
    }
}
