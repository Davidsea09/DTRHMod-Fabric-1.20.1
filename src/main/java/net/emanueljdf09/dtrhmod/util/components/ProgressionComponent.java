package net.emanueljdf09.dtrhmod.util.components;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.util.math.BlockPos;

public interface ProgressionComponent extends Component {

    boolean hasDoneExterior();
    void setExteriorDone(boolean done);

    void setWonderlandSpawn(BlockPos pos);
    BlockPos getWonderlandSpawn();

    boolean hasOpenedExtChest();
    void setOpenedExtChest(boolean done);
    boolean hasOpenedExtGrownChest();
    void setOpenedExtGrownChest(boolean done);

    boolean hasOpenedExtDoor();
    void setOpenedExtDoor(boolean done);

    boolean isWonderlandMirrorUnlocked();
    void setWonderlandMirrorUnlocked(boolean unlocked);

    int getCompletedStages();
    void setCompletedStages(int stages);

    boolean hasMetWhiteRabbit();
    void setMetWhiteRabbit(boolean met);

    boolean hasMetInOverworld();
    void setMetInOverworld(boolean met);
}
