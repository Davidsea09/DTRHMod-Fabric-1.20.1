package net.emanueljdf09.dtrhmod.block.models;

import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.emanueljdf09.dtrhmod.block.custom.ExteriorChest;
import net.emanueljdf09.dtrhmod.block.entity.ExteriorChestEntity;
import net.emanueljdf09.dtrhmod.block.entity.ExteriorDoorEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class ExteriorChestModel extends GeoModel<ExteriorChestEntity> {
    @Override
    public Identifier getModelResource(ExteriorChestEntity animatable) {
        return new Identifier(DownTheRabbitHole.MOD_ID, "geo/block/exterior_chest.geo.json");
    }

    @Override
    public Identifier getTextureResource(ExteriorChestEntity animatable) {
        return new Identifier(DownTheRabbitHole.MOD_ID, "textures/block/exterior_chest.png");
    }

    @Override
    public Identifier getAnimationResource(ExteriorChestEntity animatable) {
        return new Identifier(DownTheRabbitHole.MOD_ID, "animations/block/exterior_chest.animation.json");
    }
}