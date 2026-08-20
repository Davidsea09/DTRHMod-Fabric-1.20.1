package net.emanueljdf09.dtrhmod.world.structures.maze;

import net.minecraft.block.Block;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public class MazeCell {
    public enum Type {
        EMPTY, STRAIGHT, CORNER, T_JUNCTION, CROSSROAD, DEAD_END, ENTRANCE, CENTER
    }

    private final int gridX;
    private final int gridZ;
    private Type type = Type.EMPTY;
    private BlockRotation rotation = BlockRotation.NONE;
    private boolean visited = false;

    public boolean north = false;
    public boolean east = false;
    public boolean south = false;
    public boolean west = false;

    public MazeCell(int gridX, int gridZ) {
        this.gridX = gridX;
        this.gridZ = gridZ;
    }

    public int getGridX() { return gridX; }
    public int getGridZ() { return gridZ; }
    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }
    public BlockRotation getRotation() { return rotation; }
    public boolean isVisited() { return visited; }
    public void setVisited(boolean visited) { this.visited = visited; }

    public boolean isNorthOpen() { return north; }
    public boolean isEastOpen() { return east; }
    public boolean isSouthOpen() { return south; }
    public boolean isWestOpen() { return west; }

    public void determinePieceTypeAndRotation() {
        int count = (north ? 1 : 0) + (east ? 1 : 0) + (south ? 1 : 0) + (west ? 1 : 0);

        if (count == 0) {
            this.type = Type.EMPTY;
            return;
        }

        if (count == 4) {
            this.type = Type.CROSSROAD;
            this.rotation = BlockRotation.NONE;
        } else if (count == 3) {
            this.type = Type.T_JUNCTION;
            if (!east) {
                this.rotation = BlockRotation.NONE;
            } else if (!north) {
                this.rotation = BlockRotation.CLOCKWISE_90;
            } else if (!west) {
                this.rotation = BlockRotation.CLOCKWISE_180;
            } else {
                this.rotation = BlockRotation.COUNTERCLOCKWISE_90;
            }
        } else if (count == 2) {
            if ((north && south) || (east && west)) {
                this.type = Type.STRAIGHT;
                this.rotation = (east && west) ? BlockRotation.CLOCKWISE_90 : BlockRotation.NONE;
            } else {
                this.type = Type.CORNER;
                if (north && east) this.rotation = BlockRotation.NONE;
                else if (east && south) this.rotation = BlockRotation.CLOCKWISE_90;
                else if (south && west) this.rotation = BlockRotation.CLOCKWISE_180;
                else this.rotation = BlockRotation.COUNTERCLOCKWISE_90;
            }
        } else if (count == 1) {
            this.type = Type.DEAD_END;
            if (north) this.rotation = BlockRotation.CLOCKWISE_180;
            else if (east) this.rotation = BlockRotation.COUNTERCLOCKWISE_90;
            else if (south) this.rotation = BlockRotation.NONE;
            else this.rotation = BlockRotation.CLOCKWISE_90;
        }
    }
}
