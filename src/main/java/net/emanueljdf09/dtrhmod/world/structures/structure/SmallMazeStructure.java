package net.emanueljdf09.dtrhmod.world.structures.structure;

import com.mojang.serialization.Codec;
import net.emanueljdf09.dtrhmod.world.structures.ModStructures;
import net.emanueljdf09.dtrhmod.world.structures.maze.MazeLayoutGenerator;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.noise.NoiseConfig;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

import java.util.Optional;

import static net.emanueljdf09.dtrhmod.world.structures.maze.MazeLayoutGenerator.MODULE_SIZE;

public class SmallMazeStructure extends Structure {
    public static final Codec<SmallMazeStructure> CODEC = createCodec(SmallMazeStructure::new);

    public SmallMazeStructure(Structure.Config config) { super(config); }

    @Override
    public Optional<StructurePosition> getStructurePosition(Context context) {
        return Optional.of(new StructurePosition(context.chunkPos().getStartPos(), collector -> {
            StructureTemplateManager templateManager = context.structureTemplateManager();
            BlockPos centerPos = context.chunkPos().getCenterAtY(0);
            NoiseConfig noiseConfig = context.noiseConfig();

            int surfaceY = context.chunkGenerator().getHeight(centerPos.getX(), centerPos.getZ(), Heightmap.Type.WORLD_SURFACE_WG, context.world(), noiseConfig);

            int dimension = MazeLayoutGenerator.MazeSize.LARGE.getDimension();
            int totalWidthBlocks = dimension * MODULE_SIZE;

            BlockPos originPos = new BlockPos(
                    centerPos.getX() - (totalWidthBlocks / 2),
                    surfaceY - 1,
                    centerPos.getZ() - (totalWidthBlocks / 2));

            MazeLayoutGenerator.generateAndPlaceMaze(collector, templateManager, originPos, context.random(), MazeLayoutGenerator.MazeSize.SMALL);
        }));
    }

    @Override
    public StructureType<?> getType() { return ModStructures.SMALL_MAZE_STRUCTURE_TYPE; }
}
