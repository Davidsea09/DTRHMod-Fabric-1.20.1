package net.emanueljdf09.dtrhmod.world.structures;

import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.emanueljdf09.dtrhmod.world.structures.pieces.MazeStructurePiece;
import net.emanueljdf09.dtrhmod.world.structures.pieces.MirrorRoomStructurePiece;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.Identifier;

public class ModStructurePieces {
    public static final StructurePieceType MIRROR_ROOM_STRUCTURE_PIECE_TYPE = MirrorRoomStructurePiece::new;
    public static final StructurePieceType MAZE_STRUCTURE_PIECE_TYPE = MazeStructurePiece::new;

    public static void register() {
        Registry.register(Registries.STRUCTURE_PIECE, new Identifier(DownTheRabbitHole.MOD_ID, "mirror_room_piece"), MIRROR_ROOM_STRUCTURE_PIECE_TYPE);
        Registry.register(Registries.STRUCTURE_PIECE, new Identifier(DownTheRabbitHole.MOD_ID, "maze_piece"), MAZE_STRUCTURE_PIECE_TYPE);
    }
}
