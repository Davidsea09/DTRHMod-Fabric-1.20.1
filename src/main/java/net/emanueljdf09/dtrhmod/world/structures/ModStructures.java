package net.emanueljdf09.dtrhmod.world.structures;


import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.emanueljdf09.dtrhmod.world.structures.structure.LargeMazeStructure;
import net.emanueljdf09.dtrhmod.world.structures.structure.MediumMazeStructure;
import net.emanueljdf09.dtrhmod.world.structures.structure.MirrorRoomStructure;
import net.emanueljdf09.dtrhmod.world.structures.structure.SmallMazeStructure;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.structure.StructureType;

public class ModStructures {
    public static StructureType<MirrorRoomStructure> MIRROR_ROOM_STRUCTURE_TYPE;
    public static StructureType<LargeMazeStructure> LARGE_MAZE_STRUCTURE_TYPE;
    public static StructureType<MediumMazeStructure> MEDIUM_MAZE_STRUCTURE_TYPE;
    public static StructureType<SmallMazeStructure> SMALL_MAZE_STRUCTURE_TYPE;

    public static void registerStructures() {
        MIRROR_ROOM_STRUCTURE_TYPE = () -> MirrorRoomStructure.CODEC;

        Registry.register(Registries.STRUCTURE_TYPE,
                new Identifier(DownTheRabbitHole.MOD_ID,"mirror_room"),
                MIRROR_ROOM_STRUCTURE_TYPE);


        LARGE_MAZE_STRUCTURE_TYPE = () -> LargeMazeStructure.CODEC;

        Registry.register(Registries.STRUCTURE_TYPE,
                new Identifier(DownTheRabbitHole.MOD_ID, "large_maze"),
                LARGE_MAZE_STRUCTURE_TYPE);

        MEDIUM_MAZE_STRUCTURE_TYPE = () -> MediumMazeStructure.CODEC;

        Registry.register(Registries.STRUCTURE_TYPE,
                new Identifier(DownTheRabbitHole.MOD_ID, "medium_maze"),
                MEDIUM_MAZE_STRUCTURE_TYPE);

        SMALL_MAZE_STRUCTURE_TYPE = () -> SmallMazeStructure.CODEC;

        Registry.register(Registries.STRUCTURE_TYPE,
                new Identifier(DownTheRabbitHole.MOD_ID, "small_maze"),
                SMALL_MAZE_STRUCTURE_TYPE);
    }
}
