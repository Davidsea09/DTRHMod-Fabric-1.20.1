package net.emanueljdf09.dtrhmod.util;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.emanueljdf09.dtrhmod.util.components.ProgressionComponent;
import net.emanueljdf09.dtrhmod.util.config.ModConfigData;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class ModCommands {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("dtrh")
                    .requires(source -> source.hasPermissionLevel(0))

                    .then(CommandManager.literal("togglelocks")
                            .executes(context -> {
                                boolean newState = ModConfigData.toggleBiomeLocking();
                                String status = newState ? "§aENABLED" : "§cDISABLED";
                                context.getSource().sendFeedback(
                                        () -> Text.literal("§e[DTRH] Wonderland Biome Locking is now " + status),
                                        true
                                );
                                return 1;
                            })
                    )

                    .then(CommandManager.literal("stage")
                            .then(CommandManager.literal("set")
                                    .then(CommandManager.argument("target", EntityArgumentType.player())
                                            .then(CommandManager.argument("stage_number", IntegerArgumentType.integer(0, 10))
                                                    .executes(context -> {
                                                        ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "target");
                                                        int targetStage = IntegerArgumentType.getInteger(context, "stage_number");

                                                        ProgressionComponent story = ModComponents.PROGRESSION_COMPONENT.get(player);
                                                        story.setCompletedStages(targetStage);

                                                        if (targetStage >= 1) {
                                                            story.setWonderlandMirrorUnlocked(true);
                                                        } else {
                                                            story.setWonderlandMirrorUnlocked(false);
                                                        }

                                                        ModComponents.PROGRESSION_COMPONENT.sync(player);

                                                        context.getSource().sendFeedback(() -> Text.literal(
                                                                "Successfully set " + player.getName().getString() + "'s  stage to: " + targetStage
                                                        ), true);

                                                        player.sendMessage(Text.literal("Progression stage set to: " + targetStage), false);
                                                        return 1;
                                                    })
                                            )
                                    )
                            )
                            .then(CommandManager.literal("query")
                                    .then(CommandManager.argument("target", EntityArgumentType.player())
                                            .executes(context -> {
                                                ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "target");
                                                ProgressionComponent story = ModComponents.PROGRESSION_COMPONENT.get(player);
                                                int currentStage = story.getCompletedStages();
                                                boolean mirrorOk = story.isWonderlandMirrorUnlocked();

                                                context.getSource().sendFeedback(() -> Text.literal(
                                                        player.getName().getString() + " is currently at Stage: " + currentStage + "(Mirror Unlocked: " + mirrorOk + ")"
                                                ), false);
                                                return 1;
                                            })
                                    )
                            )

                            .then(CommandManager.literal("reset")
                                    .then(CommandManager.argument("target", EntityArgumentType.player())
                                            .executes(context -> {
                                                ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "target");
                                                ProgressionComponent story = ModComponents.PROGRESSION_COMPONENT.get(player);

                                                story.reset();

                                                context.getSource().sendFeedback(() -> Text.literal(
                                                        "Successfully reset progression for " + player.getName().getString()
                                                ), true);

                                                player.sendMessage(Text.literal("Your progression has been reset."), false);
                                                return 1;
                                            })
                                    )
                            )
                    )
            );
        });
    }
}
