package net.emanueljdf09.dtrhmod.util;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.emanueljdf09.dtrhmod.util.components.ExteriorComponent;
import net.emanueljdf09.dtrhmod.world.dimension.ModDimensions;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class RabbitholeLogic {

    public static void handlePortal(ServerPlayerEntity player) {

        ExteriorComponent component = ModComponents.EXTERIOR_COMPONENT.get(player);

        if (!component.hasDoneExterior()) {
            TeleportUtil.teleport(
                    player,
                    ModDimensions.EXTERIOR_LEVEL_KEY,
                    0.5, 68, 0.5
            );

            component.setExteriorDone(true);

        } else {

            TeleportUtil.teleport(
                    player,
                    ModDimensions.WONDERLAND_LEVEL_KEY,
                    0.5, 68, 0.5
            );
        }
    }
}
