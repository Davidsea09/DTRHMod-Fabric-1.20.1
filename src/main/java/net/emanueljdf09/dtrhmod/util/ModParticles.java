package net.emanueljdf09.dtrhmod.util;

import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModParticles {

    public static final DefaultParticleType BB_LEAVE_PARTICLE = registerParticle("bb");
    public static final DefaultParticleType TH_LEAVE_PARTICLE  = registerParticle("th");
    public static final DefaultParticleType WW_LEAVE_PARTICLE  = registerParticle("ww");
    public static final DefaultParticleType WONDERLAND_LOCK_PARTICLE  = registerParticle("wonderland_lock");

    private static DefaultParticleType registerParticle(String name) {
        return Registry.register(
                Registries.PARTICLE_TYPE,
                new Identifier(DownTheRabbitHole.MOD_ID, name),
                FabricParticleTypes.simple()
        );
    }

    public static void registerParticles() {
        DownTheRabbitHole.LOGGER.info("Registering Particles for " + DownTheRabbitHole.MOD_ID);
    }
}
