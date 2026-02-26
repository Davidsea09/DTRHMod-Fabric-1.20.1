package net.emanueljdf09.dtrhmod.util;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.emanueljdf09.dtrhmod.util.components.ExteriorComponent;
import net.emanueljdf09.dtrhmod.util.components.ExteriorComponentImpl;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

public class ModComponents implements EntityComponentInitializer {

    public static final ComponentKey<ExteriorComponent> EXTERIOR_COMPONENT =
            ComponentRegistry.getOrCreate(
                    new Identifier(DownTheRabbitHole.MOD_ID, "exterior_component"),
                    ExteriorComponent.class
            );


    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {

        registry.registerForPlayers(
                EXTERIOR_COMPONENT,
                ExteriorComponentImpl::new,
                RespawnCopyStrategy.ALWAYS_COPY
        );
    }
}
