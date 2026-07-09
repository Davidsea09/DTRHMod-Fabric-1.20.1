package net.emanueljdf09.dtrhmod.util.components.Mirror;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MirrorComponentImpl implements MirrorComponent {

    private final Map<BlockPos, BlockPos> links = new HashMap<>();

    @Override
    public Optional<BlockPos> getDestination(BlockPos sourcePos) {
        return Optional.ofNullable(links.get(sourcePos));
    }

    @Override
    public void linkMirrors(BlockPos posA, BlockPos posB) {
        links.put(posA, posB);
        links.put(posB, posA);
    }

    @Override
    public void removeLink(BlockPos pos) {
        BlockPos destination = links.remove(pos);
        if (destination != null) {
            links.remove(destination);
        }
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        links.clear();
        NbtList list = nbt.getList("Links", NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < list.size(); i++) {
            NbtCompound pair = list.getCompound(i);
            BlockPos posA = NbtHelper.toBlockPos(pair.getCompound("PosA"));
            BlockPos posB = NbtHelper.toBlockPos(pair.getCompound("PosB"));
            links.put(posA, posB);
        }
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        NbtList list = new NbtList();

        Map<BlockPos, BlockPos> tracked = new HashMap<>(links);

        for (Map.Entry<BlockPos, BlockPos> entry : tracked.entrySet()) {
            NbtCompound pair = new NbtCompound();
            pair.put("PosA", NbtHelper.fromBlockPos(entry.getKey()));
            pair.put("PosB", NbtHelper.fromBlockPos(entry.getValue()));
            list.add(pair);
        }
        nbt.put("Links", list);
    }
}
