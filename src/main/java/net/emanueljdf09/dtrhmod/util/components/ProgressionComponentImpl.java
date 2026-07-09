package net.emanueljdf09.dtrhmod.util.components;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.emanueljdf09.dtrhmod.util.ModComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

public class ProgressionComponentImpl implements ProgressionComponent, AutoSyncedComponent {

    private final PlayerEntity player;
    private boolean exteriorDone = false;
    private BlockPos wonderlandSpawn = null;
    private boolean openedExtChest = false;
    private boolean openedExtGrownChest = false;
    private boolean triggeredChestGrowth = false;
    private boolean doorOpened = false;
    private boolean wonderlandMirrorUnlocked = false;
    private int completedStages = 0;
    private boolean metWhiteRabbit = false;
    private boolean metInOverworld = false;


    public ProgressionComponentImpl(PlayerEntity player) {
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
            ModComponents.PROGRESSION_COMPONENT.sync(serverPlayer);
        }
    }

    @Override
    public boolean hasOpenedExtChest() {
        return openedExtChest;
    }

    @Override
    public void setOpenedExtChest(boolean openedExtChest) {
        this.openedExtChest = openedExtChest;

        if (player instanceof ServerPlayerEntity serverPlayer) {
            ModComponents.PROGRESSION_COMPONENT.sync(serverPlayer);
        }
    }

    @Override
    public boolean hasOpenedExtGrownChest() {
        return openedExtGrownChest;
    }

    @Override
    public void setOpenedExtGrownChest(boolean openedExtGrownChest) {
        this.openedExtGrownChest = openedExtGrownChest;

        if (player instanceof ServerPlayerEntity serverPlayer) {
            ModComponents.PROGRESSION_COMPONENT.sync(serverPlayer);
        }

    }

    @Override
    public boolean hasOpenedExtDoor() {
        return doorOpened;
    }

    @Override
    public void setOpenedExtDoor(boolean openedExtDoor) {
        this.doorOpened = openedExtDoor;

        if (player instanceof ServerPlayerEntity serverPlayer) {
            ModComponents.PROGRESSION_COMPONENT.sync(serverPlayer);
        }
    }

    @Override
    public boolean isWonderlandMirrorUnlocked() {
        return this.wonderlandMirrorUnlocked;
    }

    @Override
    public void setWonderlandMirrorUnlocked(boolean unlocked) {
        this.wonderlandMirrorUnlocked = unlocked;

    }

    @Override
    public int getCompletedStages() {
        return this.completedStages;
    }

    @Override
    public void setCompletedStages(int stages) {

        this.completedStages = stages;
    }

    @Override
    public boolean hasMetWhiteRabbit() {
        return this.metWhiteRabbit;
    }

    @Override
    public void setMetWhiteRabbit(boolean met) {
        this.metWhiteRabbit = met;
    }

    @Override
    public boolean hasMetInOverworld() { return this.metInOverworld; }
    @Override
    public void setMetInOverworld(boolean met) { this.metInOverworld = met; }

    @Override
    public void setWonderlandSpawn(BlockPos pos) {
        this.wonderlandSpawn = pos;

        if (player instanceof ServerPlayerEntity serverPlayer) {
            ModComponents.PROGRESSION_COMPONENT.sync(serverPlayer);
        }
    }

    @Override
    public BlockPos getWonderlandSpawn() {
        return this.wonderlandSpawn;
    }


    @Override
    public void readFromNbt(NbtCompound nbtCompound) {
        exteriorDone = nbtCompound.getBoolean("ExteriorDone");
        openedExtChest = nbtCompound.getBoolean("OpenedExtChest");
        doorOpened = nbtCompound.getBoolean("DoorOpened");
        wonderlandMirrorUnlocked = nbtCompound.getBoolean("WonderlandMirrorUnlocked");
        completedStages = nbtCompound.getInt("CompletedStages");
        this.metWhiteRabbit = nbtCompound.getBoolean("MetWhiteRabbit");
        this.metInOverworld = nbtCompound.getBoolean("MetInOverworld");

        if (nbtCompound.contains("wonderlandSpawn", 10)) { // 10 is the ID for NbtCompound
            this.wonderlandSpawn = NbtHelper.toBlockPos(nbtCompound.getCompound("wonderlandSpawn"));
        } else {
            this.wonderlandSpawn = null;
        }
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound) {
        nbtCompound.putBoolean("ExteriorDone", exteriorDone);
        nbtCompound.putBoolean("DoorOpened", doorOpened);
        nbtCompound.putBoolean("OpenedExtChest", openedExtChest);
        nbtCompound.putBoolean("WonderlandMirrorUnlocked", this.wonderlandMirrorUnlocked);
        nbtCompound.putInt("CompletedStages", this.completedStages);
        nbtCompound.putBoolean("MetWhiteRabbit", this.metWhiteRabbit);
        nbtCompound.putBoolean("MetInOverworld", this.metInOverworld);

        if (this.wonderlandSpawn != null) {
            nbtCompound.put("wonderlandSpawn", NbtHelper.fromBlockPos(this.wonderlandSpawn));
        }

    }


}
