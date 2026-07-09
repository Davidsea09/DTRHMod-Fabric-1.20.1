package net.emanueljdf09.dtrhmod.util.components.Mirror;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public interface MirrorComponent extends Component {

    Optional<BlockPos> getDestination(BlockPos sourcePos);

    void linkMirrors(BlockPos posA, BlockPos posB);

    void removeLink(BlockPos pos);

}
