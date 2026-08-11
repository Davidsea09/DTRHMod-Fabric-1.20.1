package net.emanueljdf09.dtrhmod.util;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.emanueljdf09.dtrhmod.util.components.ProgressionComponent;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class ModCommands {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("dtrh")
                    .requires(source -> source.hasPermissionLevel(2))

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
                                                                "§aSuccessfully set " + player.getName().getString() + "'s story stage to: §e" + targetStage
                                                        ), true);

                                                        player.sendMessage(Text.literal("§d§oYour reality shifts... An admin altered your progression stage to: §e" + targetStage), false);
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
                                                        "§b" + player.getName().getString() + " §7is currently at Stage: §e" + currentStage + " §7(Mirror Unlocked: §a" + mirrorOk + "§7)"
                                                ), false);
                                                return 1;
                                            })
                                    )
                            )
                    )
            );
        });
    }
}

