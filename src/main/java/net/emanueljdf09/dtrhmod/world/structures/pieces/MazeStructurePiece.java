package net.emanueljdf09.dtrhmod.world.structures.pieces;

import net.emanueljdf09.dtrhmod.world.structures.ModStructurePieces;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.structure.SimpleStructurePiece;
import net.minecraft.structure.StructureContext;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;

public class MazeStructurePiece extends SimpleStructurePiece {

    public MazeStructurePiece(StructureTemplateManager manager, Identifier templateId, BlockPos pos, BlockRotation rotation) {
        super(ModStructurePieces.MAZE_STRUCTURE_PIECE_TYPE, 0, manager, templateId, templateId.toString(), createPlacementData(rotation), pos);
    }

    public MazeStructurePiece(StructureContext context, NbtCompound nbt) {
        super(
                ModStructurePieces.MAZE_STRUCTURE_PIECE_TYPE,
                nbt,
                context.structureTemplateManager(),
                id -> createPlacementData(
                        nbt.contains("Rot")
                                ? BlockRotation.valueOf(nbt.getString("Rot"))
                                : BlockRotation.NONE
                )
        );
    }

    private static StructurePlacementData createPlacementData(BlockRotation rotation) {
        return new StructurePlacementData()
                .setRotation(rotation)
                .setMirror(BlockMirror.NONE);
    }

    @Override
    protected void writeNbt(StructureContext context, NbtCompound nbt) {
        super.writeNbt(context, nbt);
        nbt.putString("Rot", this.placementData.getRotation().name());
    }

    @Override
    public void generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
        super.generate(world, structureAccessor, chunkGenerator, random, chunkBox, chunkPos, pivot);
    }

    @Override
    protected void handleMetadata(String metadata, BlockPos pos, ServerWorldAccess world, Random random, BlockBox boundingBox) {

    }
}
