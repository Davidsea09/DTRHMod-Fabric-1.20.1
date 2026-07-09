package net.emanueljdf09.dtrhmod.entity;

import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.emanueljdf09.dtrhmod.entity.custom.WeepingPlayerEntity;
import net.emanueljdf09.dtrhmod.entity.custom.WhiteRabbitEntity;
import net.emanueljdf09.dtrhmod.world.dimension.ModDimensions;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {

    public static EntityType<WhiteRabbitEntity> WHITE_RABBIT = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(DownTheRabbitHole.MOD_ID, "white_rabbit"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, WhiteRabbitEntity::new)
                    .dimensions(EntityDimensions.fixed(1f, 1.25f)).build()
    );

    public static EntityType<WeepingPlayerEntity> WEEPING_PLAYER = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(DownTheRabbitHole.MOD_ID, "weeping_player"),
                    FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, WeepingPlayerEntity::new)
                            .dimensions(EntityDimensions.fixed(0.8f, 2.25f)).build()
    );





    public static void registerModEntities() {
        DownTheRabbitHole.LOGGER.info("Registering ModEntities for " + DownTheRabbitHole.MOD_ID);
        FabricDefaultAttributeRegistry.register(ModEntities.WHITE_RABBIT, WhiteRabbitEntity.setAttributes());
        FabricDefaultAttributeRegistry.register(ModEntities.WEEPING_PLAYER, WeepingPlayerEntity.setAttributes());
    }

}
