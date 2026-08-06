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

    public static PlayState handleChestAnimation(AnimationState<?> state, RawAnimation closed, RawAnimation openNormal, RawAnimation takeGrownItems, RawAnimation openGrown) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player != null) {
            ProgressionComponent component = ModComponents.PROGRESSION_COMPONENT.get(client.player);

            if (component.hasOpenedExtGrownChest()) {
                return state.setAndContinue(takeGrownItems);
            }

            if (component.hasOpenedExtChest() && client.player.hasStatusEffect(ModEffects.GROW)) {
                return state.setAndContinue(openGrown);
            }
            if (component.hasOpenedExtChest()) {
                return state.setAndContinue(openNormal);
            }
        }

        return state.setAndContinue(closed);
    }
}
