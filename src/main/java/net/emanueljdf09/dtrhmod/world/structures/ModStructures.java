package net.emanueljdf09.dtrhmod.world.structures;


import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.emanueljdf09.dtrhmod.world.structures.structure.MirrorRoomStructure;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.structure.StructureType;

public class ModStructures {
    public static StructureType<MirrorRoomStructure> MIRROR_ROOM_STRUCTURE_TYPE;

    public static void registerStructures() {
        MIRROR_ROOM_STRUCTURE_TYPE = () -> MirrorRoomStructure.CODEC;

        Registry.register(Registries.STRUCTURE_TYPE,
                new Identifier(DownTheRabbitHole.MOD_ID,"mirror_room"),
                MIRROR_ROOM_STRUCTURE_TYPE);
    }
}
