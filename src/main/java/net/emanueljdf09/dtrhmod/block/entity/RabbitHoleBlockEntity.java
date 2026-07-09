package net.emanueljdf09.dtrhmod.block.entity;

import net.emanueljdf09.dtrhmod.block.ModBlockEntities;
import net.emanueljdf09.dtrhmod.entity.ModEntities;
import net.emanueljdf09.dtrhmod.entity.custom.WhiteRabbitEntity;
import net.emanueljdf09.dtrhmod.util.ModComponents;
import net.emanueljdf09.dtrhmod.util.TeleportUtil;
import net.emanueljdf09.dtrhmod.util.components.ProgressionComponent;
import net.emanueljdf09.dtrhmod.world.biome.ModBiomes;
import net.emanueljdf09.dtrhmod.world.dimension.ModDimensions;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class RabbitHoleBlockEntity extends BlockEntity {
    private int scanCooldown = 0;

    public RabbitHoleBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.RABBIT_HOLE_BLOCK_ENTITY, pos, state);
    }

    public void handlePlayerCollision(ServerPlayerEntity player) {
        if (!player.isInSwimmingPose()) return;

        ProgressionComponent component = ModComponents.PROGRESSION_COMPONENT.get(player);
        RegistryKey<World> currentDim = player.getWorld().getRegistryKey();

        if (currentDim == ModDimensions.WONDERLAND_LEVEL_KEY) {
            handleReturnToSpawn(player);
            return;
        }

        if (currentDim == World.OVERWORLD) {
            if (!component.hasDoneExterior()) {
                TeleportUtil.teleport(player, ModDimensions.EXTERIOR_LEVEL_KEY, 3, 70, 2);
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 200, 0));
                component.setExteriorDone(true);
            } else {
                TeleportUtil.teleportToWonderland(player);
            }
            return; // Stops execution immediately so it doesn't leak into the next if-statement
        }

        if (currentDim == ModDimensions.EXTERIOR_LEVEL_KEY) {
            BlockPos found = TeleportUtil.teleportToBiome(player,
                    ModDimensions.WONDERLAND_LEVEL_KEY,
                    ModBiomes.TULGEY_FOREST,
                    ModBiomes.TEAR_LAKE_VALLEY
            );
            component.setWonderlandSpawn(found);
        }
    }

    private void handleReturnToSpawn(ServerPlayerEntity player) {
        BlockPos spawnPos = player.getSpawnPointPosition();
        RegistryKey<World> spawnDim = player.getSpawnPointDimension();
        boolean isSpawnForced = player.isSpawnForced();
        float spawnAngle = player.getSpawnAngle();

        MinecraftServer server = player.getServer();
        if (server == null) return;
        ServerWorld targetWorld = server.getWorld(spawnDim);

        if (spawnPos != null && targetWorld != null) {
            var respawnPos = PlayerEntity.findRespawnPosition(targetWorld, spawnPos, spawnAngle, isSpawnForced, false);
            if (respawnPos.isPresent()) {
                Vec3d pos = respawnPos.get();
                player.teleport(targetWorld, pos.getX(), pos.getY(), pos.getZ(), spawnAngle, 0.0F);
                return;
            } else {
                player.sendMessage(Text.translatable("block.minecraft.spawn_obstructed"), true);
            }
        }

        if (targetWorld != null) {
            BlockPos worldSpawn = targetWorld.getSpawnPos();
            player.teleport(targetWorld, worldSpawn.getX() + 0.5, worldSpawn.getY(), worldSpawn.getZ() + 0.5, spawnAngle, 0.0F);
        }
    }

    // --- BACKGROUND GUIDING RABBIT RADAR ---
    public static void tick(World world, BlockPos pos, BlockState state, RabbitHoleBlockEntity blockEntity) {
        blockEntity.scanCooldown++;
        if (blockEntity.scanCooldown < 20) return;
        blockEntity.scanCooldown = 0;

        ServerWorld serverWorld = (ServerWorld) world;

        // Don't spawn guide rabbits if the hole block is located inside Wonderland or the Exterior drop world!
        if (world.getRegistryKey() != World.OVERWORLD) return;

        // Check for active rabbits
        Box checkRabbitBox = new Box(pos).expand(40);
        List<WhiteRabbitEntity> nearbyRabbits = serverWorld.getEntitiesByClass(
                WhiteRabbitEntity.class,
                checkRabbitBox,
                rabbit -> !rabbit.isReturning()
        );
        if (!nearbyRabbits.isEmpty()) return;

        // Check for eligible players
        Box checkPlayerBox = new Box(pos).expand(40);
        List<ServerPlayerEntity> nearbyPlayers = serverWorld.getEntitiesByClass(
                ServerPlayerEntity.class,
                checkPlayerBox,
                player -> true
        );

        for (ServerPlayerEntity player : nearbyPlayers) {
            ProgressionComponent component = ModComponents.PROGRESSION_COMPONENT.get(player);

            if (!component.hasMetWhiteRabbit()) {
                WhiteRabbitEntity rabbit = ModEntities.WHITE_RABBIT.create(serverWorld);
                if (rabbit != null) {
                    BlockPos spawnPos = pos.up();
                    rabbit.refreshPositionAndAngles(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5, world.random.nextFloat() * 360F, 0);
                    rabbit.setRabbitHolePos(pos);
                    serverWorld.spawnEntityAndPassengers(rabbit);
                    break;
                }
            }
        }
    }
}