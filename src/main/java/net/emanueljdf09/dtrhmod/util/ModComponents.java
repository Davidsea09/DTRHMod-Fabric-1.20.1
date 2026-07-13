package net.emanueljdf09.dtrhmod.util;

import dev.onyxstudios.cca.api.v3.block.BlockComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.block.BlockComponentInitializer;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import dev.onyxstudios.cca.api.v3.world.WorldComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.world.WorldComponentInitializer;
import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.emanueljdf09.dtrhmod.block.entity.MirrorBlockEntity;
import net.emanueljdf09.dtrhmod.util.components.ProgressionComponent;
import net.emanueljdf09.dtrhmod.util.components.ProgressionComponentImpl;
import net.emanueljdf09.dtrhmod.util.components.Mirror.MirrorComponent;
import net.emanueljdf09.dtrhmod.util.components.Mirror.MirrorComponentImpl;
import net.minecraft.util.Identifier;

public class ModComponents implements EntityComponentInitializer, WorldComponentInitializer, BlockComponentInitializer {


    public static final ComponentKey<ProgressionComponent> PROGRESSION_COMPONENT =
            ComponentRegistry.getOrCreate(
                    new Identifier(DownTheRabbitHole.MOD_ID, "progression_component"),
                    ProgressionComponent.class
            );

    public static final ComponentKey<MirrorComponent> MIRROR_COMPONENT =
                ComponentRegistry.getOrCreate(
                        new Identifier(DownTheRabbitHole.MOD_ID, "mirror_component"),
                        MirrorComponent.class
                );



    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {

        registry.registerForPlayers(
                PROGRESSION_COMPONENT,
                ProgressionComponentImpl::new,
                RespawnCopyStrategy.ALWAYS_COPY
        );

    }


    @Override
    public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
        registry.register(MIRROR_COMPONENT, world -> new MirrorComponentImpl());
    }

    @Override
    public void registerBlockComponentFactories(BlockComponentFactoryRegistry registry) {
    }
}
