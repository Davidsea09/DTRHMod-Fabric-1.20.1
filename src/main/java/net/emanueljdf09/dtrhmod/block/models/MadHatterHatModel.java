package net.emanueljdf09.dtrhmod.block.models;

import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.emanueljdf09.dtrhmod.block.entity.MadHatterHatBlockEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class MadHatterHatModel extends GeoModel<MadHatterHatBlockEntity> {

    @Override
    public Identifier getModelResource(MadHatterHatBlockEntity animatable) {
        return new Identifier(DownTheRabbitHole.MOD_ID, "geo/block/mad_hat.geo.json");
    }

    @Override
    public Identifier getTextureResource(MadHatterHatBlockEntity animatable) {
        return new Identifier(DownTheRabbitHole.MOD_ID, "textures/block/mad_hat.png");
    }

    public Identifier getAnimationResource(MadHatterHatBlockEntity animatable) {
        return new Identifier(DownTheRabbitHole.MOD_ID, "animations/block/hat.animation.json");
    }
}
