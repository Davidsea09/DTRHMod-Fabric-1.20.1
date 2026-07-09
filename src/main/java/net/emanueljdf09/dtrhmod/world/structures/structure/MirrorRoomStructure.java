package net.emanueljdf09.dtrhmod.world.structures.structure;

import com.mojang.serialization.Codec;
import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.emanueljdf09.dtrhmod.world.structures.ModStructures;
import net.emanueljdf09.dtrhmod.world.structures.pieces.MirrorRoomStructurePiece;
import net.minecraft.structure.StructurePiecesCollector;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.gen.noise.NoiseConfig;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

import java.util.Optional;

public class MirrorRoomStructure extends Structure {
    public static final Codec<MirrorRoomStructure> CODEC = createCodec(MirrorRoomStructure::new);

    public MirrorRoomStructure(Structure.Config config) {
        super(config);
    }

    @Override
    public Optional<Structure.StructurePosition> getStructurePosition(Structure.Context context) {
        // Hand off spacing entirely to structure_set JSON for optimized native execution
        return Optional.of(new Structure.StructurePosition(context.chunkPos().getStartPos(), collector -> {
            addPieces(collector, context);
        }));
    }

    private static void addPieces(StructurePiecesCollector collector, Structure.Context context) {
        StructureTemplateManager templateManager = context.structureTemplateManager();
        BlockPos centerPos = context.chunkPos().getCenterAtY(0);
        NoiseConfig noiseConfig = context.noiseConfig();

        // Sample ground levels at the center of the target chunk
        int solidFloorY = context.chunkGenerator().getHeight(centerPos.getX(), centerPos.getZ(),
                Heightmap.Type.WORLD_SURFACE_WG, context.world(), noiseConfig);

        int waterSurfaceY = context.chunkGenerator().getHeight(centerPos.getX(), centerPos.getZ(),
                Heightmap.Type.OCEAN_FLOOR_WG, context.world(), noiseConfig);

        // Safety Filter: Skip generation only if the chunk is deep under an ocean or deep lake
        if ((solidFloorY - waterSurfaceY) > 4) {
            return;
        }

        // Detect if the target generation biome belongs to Overworld or Wonderland
        var biomeRegistryEntry = context.biomeSource().getBiomes().stream().findFirst();
        boolean isOverworld = true;
        if (biomeRegistryEntry.isPresent()) {
            String namespace = biomeRegistryEntry.get().getKey().map(key -> key.getValue().getNamespace()).orElse("minecraft");
            isOverworld = !namespace.equalsIgnoreCase(DownTheRabbitHole.MOD_ID);
        }

        Identifier templateId = isOverworld
                ? new Identifier(DownTheRabbitHole.MOD_ID, "portal/mirror_room_ruined")
                : new Identifier(DownTheRabbitHole.MOD_ID, "portal/mirror_room_complete");

        // Generate at the exact surface level detected
        BlockPos placementPos = new BlockPos(centerPos.getX(), solidFloorY, centerPos.getZ());
        BlockRotation rotation = BlockRotation.random(context.random());

        collector.addPiece(new MirrorRoomStructurePiece(templateManager, templateId, placementPos, rotation));
    }

    @Override
    public StructureType<?> getType() {
        return ModStructures.MIRROR_ROOM_STRUCTURE_TYPE;
    }
}