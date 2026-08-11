package net.emanueljdf09.dtrhmod.entity.client.models;

import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.emanueljdf09.dtrhmod.entity.custom.WhiteRabbitEntity;
import net.minecraft.client.render.entity.ZombieBaseEntityRenderer;
import net.minecraft.client.render.entity.ZombieEntityRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class WhiteRabbitModel extends GeoModel<WhiteRabbitEntity> {


    @Override
    public Identifier getModelResource(WhiteRabbitEntity animatable) {
        return new Identifier(DownTheRabbitHole.MOD_ID, "geo/entity/white_rabbit.geo.json");
    }

    @Override
    public Identifier getTextureResource(WhiteRabbitEntity animatable) {
        return new Identifier(DownTheRabbitHole.MOD_ID, "textures/entity/rabbit.png");
    }

    @Override
    public Identifier getAnimationResource(WhiteRabbitEntity animatable) {
        return new Identifier(DownTheRabbitHole.MOD_ID, "animations/entity/white_rabbit.animation.json");
    }

    @Override
    public void setCustomAnimations(WhiteRabbitEntity animatable, long instanceId, AnimationState<WhiteRabbitEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            head.setRotX(entityData.headPitch() * MathHelper.RADIANS_PER_DEGREE);
            head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
        }
    }

}
