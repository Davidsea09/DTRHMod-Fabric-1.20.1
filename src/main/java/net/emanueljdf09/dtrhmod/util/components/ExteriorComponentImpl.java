package net.emanueljdf09.dtrhmod.util.components;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.emanueljdf09.dtrhmod.util.ModComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;

public class ExteriorComponentImpl implements ExteriorComponent, AutoSyncedComponent {

    private final PlayerEntity player;
    private boolean exteriorDone = false;

    public ExteriorComponentImpl(PlayerEntity player) {
        this.player = player;
    }

    @Override
    public boolean hasDoneExterior() {
        return exteriorDone;
    }

    @Override
    public void setExteriorDone(boolean done) {
        this.exteriorDone = done;

        if (player instanceof ServerPlayerEntity serverPlayer) {
            ModComponents.EXTERIOR_COMPONENT.sync(serverPlayer);
        }
    }


    @Override
    public void readFromNbt(NbtCompound nbtCompound) {
        exteriorDone = nbtCompound.getBoolean("ExteriorDone");
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound) {
        nbtCompound.putBoolean("ExteriorDone", exteriorDone);

    }


}
