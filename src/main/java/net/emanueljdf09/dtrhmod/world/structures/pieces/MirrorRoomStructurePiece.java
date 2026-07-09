package net.emanueljdf09.dtrhmod.world.structures.pieces;

import net.emanueljdf09.dtrhmod.block.ModBlocks;
import net.emanueljdf09.dtrhmod.block.custom.mirror.MirrorBlock;
import net.emanueljdf09.dtrhmod.block.custom.mirror.MirrorType;
import net.emanueljdf09.dtrhmod.world.structures.ModStructurePieces;
import net.minecraft.block.BlockState;
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
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;

public class MirrorRoomStructurePiece extends SimpleStructurePiece {

    public MirrorRoomStructurePiece(StructureTemplateManager manager, Identifier templateId, BlockPos pos, BlockRotation rotation) {
        super(ModStructurePieces.MIRROR_ROOM_STRUCTURE_PIECE_TYPE, 0, manager, templateId, templateId.toString(), createPlacementData(rotation), pos);
    }

    public MirrorRoomStructurePiece(StructureContext context, NbtCompound nbt) {
        super(ModStructurePieces.MIRROR_ROOM_STRUCTURE_PIECE_TYPE, nbt, context.structureTemplateManager(), (id) -> createPlacementData(BlockRotation.valueOf(nbt.getString("Rot"))));
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
    protected void handleMetadata(String metadata, BlockPos pos, ServerWorldAccess world, Random random, BlockBox boundingBox) {
    }

    @Override
    public void generate(StructureWorldAccess world, StructureAccessor structures, ChunkGenerator generator, Random random, BlockBox box, ChunkPos chunkPos, BlockPos pivot) {

        // 2. Run the vanilla NBT template placement first
        super.generate(world, structures, generator, random, box, chunkPos, pivot);

        // 3. Scan the exact bounding box of the pasted structure for your mirror block
        BlockPos.iterate(box.getMinX(), box.getMinY(), box.getMinZ(), box.getMaxX(), box.getMaxY(), box.getMaxZ()).forEach(currentPos -> {
            BlockState state = world.getBlockState(currentPos);

            // 4. Force-apply the structure type property to both halves
            if (state.isOf(ModBlocks.MIRROR_BLOCK)) {
                if (state.get(MirrorBlock.TYPE) != MirrorType.structure) {

                    BlockState repairedState = state.with(MirrorBlock.TYPE, MirrorType.structure);

                    // Flag 2 safely modifies the chunk array without triggering early block updates
                    world.setBlockState(currentPos, repairedState, 2);
                }
            }
        });
    }

}
