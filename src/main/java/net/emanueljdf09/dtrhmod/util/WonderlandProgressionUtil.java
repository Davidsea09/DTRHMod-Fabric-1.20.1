package net.emanueljdf09.dtrhmod.util;

import net.emanueljdf09.dtrhmod.util.components.ProgressionComponent;
import net.emanueljdf09.dtrhmod.world.dimension.ModDimensions;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class WonderlandProgressionUtil {
    // Cache to remember every player's last known safe coordinates from the previous tick
    private static final Map<UUID, Vec3d> LAST_SAFE_POSITIONS = new HashMap<>();

    public static void register() {
        ServerTickEvents.START_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                if (player.getWorld().getRegistryKey() == ModDimensions.WONDERLAND_LEVEL_KEY) {
                    evaluateBiomeProgression(player);
                } else {
                    // Clean up cache if they leave the dimension
                    LAST_SAFE_POSITIONS.remove(player.getUuid());
                }
            }
        });

        // Clean up disconnects to prevent memory leaks
        net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            LAST_SAFE_POSITIONS.remove(handler.getPlayer().getUuid());
        });
    }

    public static void triggerMilestone(ServerPlayerEntity player, int nextStage, String bookTriggerKey) {
        ProgressionComponent story = ModComponents.PROGRESSION_COMPONENT.get(player);

        // Prevent lowering a player's stage accidentally
        if (nextStage > story.getCompletedStages()) {
            story.setCompletedStages(nextStage);

            // Logic Gate: Automatically unlock mirrors at Stage 1+
            if (nextStage >= 1) {
                story.setWonderlandMirrorUnlocked(true);
            }

            // 1. Sync the component data over the Cardinal Components network pipeline
            ModComponents.PROGRESSION_COMPONENT.sync(player);

            // 2. Play a rewarding ding sound effect
            player.getServerWorld().playSound(
                    null, player.getBlockPos(),
                    SoundEvents.UI_TOAST_CHALLENGE_COMPLETE,
                    SoundCategory.PLAYERS, 1.0f, 1.0f
            );

            // 3. Send a chat notification
            player.sendMessage(Text.literal("§d§kXX§r §l§5STORY MILESTONE UNLOCKED §r§d§kXX"), false);

            // 4. Force open the custom book screen automatically if a key was provided!
            if (bookTriggerKey != null) {
                ModPackets.sendOpenBookPacket(player, bookTriggerKey);
            }
        }
    }

    private static void evaluateBiomeProgression(ServerPlayerEntity player) {
        BlockPos pos = player.getBlockPos();
        Optional<RegistryKey<Biome>> currentBiomeKey = player.getWorld().getBiome(pos).getKey();

        if (currentBiomeKey.isEmpty()) return;
        Identifier biomeId = currentBiomeKey.get().getValue();

        ProgressionComponent story = ModComponents.PROGRESSION_COMPONENT.get(player);
        int stage = story.getCompletedStages();

        // Check if the current biome is locked for this player
        boolean isLocked = false;
        String warningMessage = "";

        if (biomeId.getPath().equals("tear_lake_valley") && stage < 1) {
            isLocked = true;
            warningMessage = "The Mad Hatter's Woods are sealed by a spatial distortion!";
        } else if (biomeId.getPath().equals("chessboard_fields") && stage < 2) {
            isLocked = true;
            warningMessage = "A wall of royal thorns blocks your passage!";
        }

        if (isLocked) {
            // Player is inside a locked zone! Teleport them back to safety instantly
            handleSolidWallCollision(player, warningMessage);
        } else {
            LAST_SAFE_POSITIONS.put(player.getUuid(), player.getPos());
        }
    }

    private static void handleSolidWallCollision(ServerPlayerEntity player, String message) {
        ServerWorld world = player.getServerWorld();
        UUID uuid = player.getUuid();

        Vec3d safePos = LAST_SAFE_POSITIONS.get(uuid);

        if (safePos != null) {
            player.setVelocity(0, 0, 0);

           player.teleport(world, safePos.x, safePos.y, safePos.z, player.getYaw(), player.getPitch());
        }

       player.addStatusEffect(new StatusEffectInstance(
                StatusEffects.DARKNESS, 40, 0, true, false, false
        ));

        double px = player.getX();
        double py = player.getEyeY();
        double pz = player.getZ();

        world.spawnParticles(ParticleTypes.WITCH, px, py, pz, 6, 0.3, 0.4, 0.3, 0.01);
        world.spawnParticles(ParticleTypes.SMOKE, px, py, pz, 4, 0.2, 0.2, 0.2, 0.02);

        // 4. FEEDBACK CHIME
        if (player.age % 10 == 0) {
            world.playSound(null, player.getBlockPos(), net.minecraft.sound.SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE, net.minecraft.sound.SoundCategory.PLAYERS, 0.6f, 0.5f);
            player.sendMessage(Text.literal("§c" + message), true); // ActionBar notification
        }
    }
}