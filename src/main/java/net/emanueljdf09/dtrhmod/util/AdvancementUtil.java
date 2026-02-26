package net.emanueljdf09.dtrhmod.util;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class AdvancementUtil {

    public  static  void grant(ServerPlayerEntity player, Identifier id) {

        Advancement advancement = player.getServer()
                .getAdvancementLoader()
                .get(id);

        if (advancement == null) return;

        AdvancementProgress progress =
                player.getAdvancementTracker().getProgress(advancement);

        for (String criterion : progress.getUnobtainedCriteria()) {
            player.getAdvancementTracker().grantCriterion(advancement, criterion);

        }
    }
}
