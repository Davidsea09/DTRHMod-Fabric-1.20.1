package net.emanueljdf09.dtrhmod.util;

import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public class TeleportUtil {

    public static void teleport(ServerPlayerEntity player, RegistryKey<World> targetDim,
                                double x, double y, double z) {

        MinecraftServer server = player.getServer();
        if (server == null) return;

        ServerWorld world = server.getWorld(targetDim);
        if (world == null) return;

        player.teleport(world, x, y, z, player.getYaw(), player.getPitch());
    }

}
