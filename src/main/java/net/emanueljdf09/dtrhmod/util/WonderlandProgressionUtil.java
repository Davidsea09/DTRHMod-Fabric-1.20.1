package net.emanueljdf09.dtrhmod.util;

import net.emanueljdf09.dtrhmod.util.components.ProgressionComponent;
import net.emanueljdf09.dtrhmod.util.config.ModConfigData;
import net.emanueljdf09.dtrhmod.world.dimension.ModDimensions;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
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

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class WonderlandProgressionUtil {
    private static final Map<UUID, Long> TOAST_COOLDOWNS = new HashMap<>();

    public static void register() {
        ServerTickEvents.START_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                if (player.getWorld().getRegistryKey() == ModDimensions.WONDERLAND_LEVEL_KEY) {
                    evaluateBiomeProgression(player);
                } else {
                    TOAST_COOLDOWNS.remove(player.getUuid());
                }
            }
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            TOAST_COOLDOWNS.remove(handler.getPlayer().getUuid());
        });
    }

    public static void triggerMilestone(ServerPlayerEntity player, int nextStage, String bookTriggerKey) {
        ProgressionComponent story = ModComponents.PROGRESSION_COMPONENT.get(player);

        if (nextStage > story.getCompletedStages()) {
            story.setCompletedStages(nextStage);

            if (nextStage >= 1) {
                story.setWonderlandMirrorUnlocked(true);
            }

            ModComponents.PROGRESSION_COMPONENT.sync(player);

            player.getServerWorld().playSound(
                    null, player.getBlockPos(),
                    SoundEvents.UI_TOAST_CHALLENGE_COMPLETE,
                    SoundCategory.PLAYERS, 1.0f, 1.0f
            );

            player.sendMessage(Text.literal("§d§kXX§r §l§5STORY MILESTONE UNLOCKED §r§d§kXX"), false);

            if (bookTriggerKey != null) {
                ModPackets.sendOpenBookPacket(player, bookTriggerKey);
            }
        }
    }

    private static void evaluateBiomeProgression(ServerPlayerEntity player) {
        if (!ModConfigData.isBiomeLockingEnabled()) {
            return;
        }

        if (player.isCreative() || player.isSpectator()) {
            return;
        }

        BlockPos pos = player.getBlockPos();
        Optional<RegistryKey<Biome>> currentBiomeKey = player.getWorld().getBiome(pos).getKey();

        if (currentBiomeKey.isEmpty()) return;
        Identifier biomeId = currentBiomeKey.get().getValue();

        ProgressionComponent story = ModComponents.PROGRESSION_COMPONENT.get(player);
        int stage = story.getCompletedStages();

        boolean isLocked = false;
        String warningMessage = "";

        if (biomeId.getPath().equals("vale_of_tears") && stage < 1) {
            isLocked = true;
            warningMessage = "You're not supposed to be here yet";
        } else if (biomeId.getPath().equals("chessboard_fields") && stage < 2) {
            isLocked = true;
            warningMessage = "You're not supposed to be here yet";
        }

        if (isLocked) {
            UUID uuid = player.getUuid();
            long currentTime = player.getWorld().getTime();

            player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 100, 0, true, false, false));

            if (player.age % 100 == 0) {
                player.damage(player.getDamageSources().generic(), 1.0f);
                player.getServerWorld().playSound(
                        null, player.getBlockPos(),
                        SoundEvents.ENTITY_PLAYER_HURT,
                        SoundCategory.PLAYERS, 0.5f, 1.0f
                );
                player.sendMessage(Text.literal("§c" + warningMessage), true);
            }

            if (!TOAST_COOLDOWNS.containsKey(uuid) || currentTime - TOAST_COOLDOWNS.get(uuid) > 100) {
                TOAST_COOLDOWNS.put(uuid, currentTime);

                ModPackets.sendLockedBiomeToastPacket(player);
            }

        }
    }

    public record LockedBiomeToast(ItemStack item) implements Toast {

        private static final Identifier TEXTURE = new Identifier("textures/gui/toasts.png");

        private static final Text TITLE = Text.translatable("misc.dtrhmod.biome_locked.title");
        private static final Text DESCRIPTION = Text.translatable("misc.dtrhmod.biome_locked.description");

        @Override
        public Visibility draw(DrawContext context, ToastManager manager, long startTime) {
            context.drawTexture(TEXTURE, 0, 0, 0, 0, this.getWidth(), this.getHeight());
            context.drawItem(this.item(), 6, 8);
            context.drawText(manager.getClient().textRenderer, TITLE, 25,7, -256, false);
            context.drawText(manager.getClient().textRenderer, DESCRIPTION, 25, 18, 16777215, false);


            return startTime >= 10000L ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
        }
    }

}