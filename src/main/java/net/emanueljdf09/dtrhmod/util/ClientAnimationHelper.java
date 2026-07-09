package net.emanueljdf09.dtrhmod.util;

import net.emanueljdf09.dtrhmod.util.components.ProgressionComponent;
import net.minecraft.client.MinecraftClient;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class ClientAnimationHelper {
    public static PlayState handleDoorAnimation(AnimationState<?> state, RawAnimation idle, RawAnimation openIdle) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player != null) {
            ProgressionComponent component = ModComponents.PROGRESSION_COMPONENT.get(client.player);

            if (component.hasOpenedExtDoor()) {
                return state.setAndContinue(openIdle);
            }
        }
        return state.setAndContinue(idle);
    }

    public static PlayState handleChestAnimation(AnimationState<?> state, RawAnimation closed, RawAnimation invisible) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player != null) {
            ProgressionComponent component = ModComponents.PROGRESSION_COMPONENT.get(client.player);

            if (component.hasOpenedExtGrownChest()) {
                state.getController().setAnimation(invisible);
                return PlayState.CONTINUE;
            }
        }

        return PlayState.CONTINUE;
    }
}
