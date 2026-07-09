package net.emanueljdf09.dtrhmod.block.models;

import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.emanueljdf09.dtrhmod.block.entity.ExteriorDoorEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class ExteriorDoorModel extends GeoModel<ExteriorDoorEntity> {
    @Override
    public Identifier getModelResource(ExteriorDoorEntity animatable) {
        return new Identifier(DownTheRabbitHole.MOD_ID, "geo/block/exterior_door.geo.json");
    }

    @Override
    public Identifier getTextureResource(ExteriorDoorEntity animatable) {
       return new Identifier(DownTheRabbitHole.MOD_ID, "textures/block/exterior_door.png");
    }

    @Override
    public Identifier getAnimationResource(ExteriorDoorEntity animatable) {
        return new Identifier(DownTheRabbitHole.MOD_ID, "animations/block/exterior_door.animation.json");
    }
}
