package net.emanueljdf09.dtrhmod.util.components.Mirror;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.nbt.NbtCompound;

import java.util.UUID;

public class MirrorGossipComponent implements AutoSyncedComponent {
    private UUID ownerUuid = null;
    private String lastSeenPlayerName = "nobody... yet";

    public UUID getOwnerUuid() { return this.ownerUuid; }
    public void setOwnerUuid(UUID uuid) { this.ownerUuid = uuid; }

    public String getLastSeenPlayerName() { return this.lastSeenPlayerName; }
    public void setLastSeenPlayerName(String name) { this.lastSeenPlayerName = name; }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        if (nbt.containsUuid("Owner")) this.ownerUuid = nbt.getUuid("Owner");
        if (nbt.contains("LastSeen")) this.lastSeenPlayerName = nbt.getString("LastSeen");

    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        if (this.ownerUuid != null) nbt.putUuid("Owner", this.ownerUuid);
        nbt.putString("LastSeen", this.lastSeenPlayerName);
    }
}
