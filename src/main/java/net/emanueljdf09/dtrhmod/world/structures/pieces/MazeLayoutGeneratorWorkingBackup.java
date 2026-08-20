package net.emanueljdf09.dtrhmod.world.structures.pieces;

import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.emanueljdf09.dtrhmod.world.structures.maze.MazeCell;
import net.minecraft.structure.StructurePiecesCollector;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MazeLayoutGeneratorWorkingBackup {
    private static final int MODULE_SIZE = 7;

    public static void generateAndPlaceMaze(StructurePiecesCollector collector, StructureTemplateManager templateManager, BlockPos originPos, Random random) {
        DownTheRabbitHole.LOGGER.info("========== GENERATING MAZE ==========");
        int width = 21;
        int height = 21;

        MazeCell[][] grid = new MazeCell[width][height];
        for (int x = 0; x < width; x++) {
            for (int z = 0; z < height; z++) {
                grid[x][z] = new MazeCell(x, z);
            }
        }

        int centerX = width / 2;
        int centerZ = height / 2;

        // 1. Mark Center Room (3x3 grid cells)
        for (int cx = centerX - 1; cx <= centerX + 1; cx++) {
            for (int cz = centerZ - 1; cz <= centerZ + 1; cz++) {
                grid[cx][cz].setType(MazeCell.Type.CENTER);
            }
        }

        // 2. Mark Entrance
        int entranceX = centerX;
        int entranceZ = 0;
        grid[entranceX][entranceZ].setType(MazeCell.Type.ENTRANCE);

        // 3. Connect Center to Maze (West side opening)
        MazeCell startCell = grid[centerX - 2][centerZ];
        connectCells(grid[centerX - 1][centerZ], startCell, -1, 0);

        // 4. Connect Entrance to Maze (South side opening)
        connectCells(grid[entranceX][entranceZ], grid[entranceX][entranceZ + 1], 0, 1);

        // 5. Carve Passages across the grid
        carvePassages(grid, centerX - 2, centerZ, random);
        DownTheRabbitHole.LOGGER.info("========== MAZE CARVING FINISHED ==========");

        // 6. Determine piece types and rotations for all grid cells
        for (int x = 0; x < width; x++) {
            for (int z = 0; z < height; z++) {
                MazeCell cell = grid[x][z];
                if (cell.getType() != MazeCell.Type.CENTER && cell.getType() != MazeCell.Type.ENTRANCE) {
                    cell.determinePieceTypeAndRotation();
                }
            }
        }

        // 7. Count path cells
        int generatedCells = 0;
        for (int x = 0; x < width; x++) {
            for (int z = 0; z < height; z++) {
                MazeCell cell = grid[x][z];
                if (cell.getType() != MazeCell.Type.EMPTY &&
                        cell.getType() != MazeCell.Type.CENTER &&
                        cell.getType() != MazeCell.Type.ENTRANCE) {
                    generatedCells++;
                }
            }
        }

        DownTheRabbitHole.LOGGER.info(
                "Maze generated {} path cells out of {}",
                generatedCells,
                width * height
        );

        // 8. Place Special Pieces (Center & Entrance) using absolute grid offsets from originPos
        placeCenterPiece(collector, templateManager, originPos, centerX - 1, centerZ - 1, "maze/center/center_nw_corner");
        placeCenterPiece(collector, templateManager, originPos, centerX, centerZ - 1, "maze/center/center_north");
        placeCenterPiece(collector, templateManager, originPos, centerX + 1, centerZ - 1, "maze/center/center_ne_corner");

        placeCenterPiece(collector, templateManager, originPos, centerX - 1, centerZ, "maze/center/center_west");
        placeCenterPiece(collector, templateManager, originPos, centerX, centerZ, "maze/center/center_middle");
        placeCenterPiece(collector, templateManager, originPos, centerX + 1, centerZ, "maze/center/center_east");

        placeCenterPiece(collector, templateManager, originPos, centerX - 1, centerZ + 1, "maze/center/center_sw_corner");
        placeCenterPiece(collector, templateManager, originPos, centerX, centerZ + 1, "maze/center/center_south");
        placeCenterPiece(collector, templateManager, originPos, centerX + 1, centerZ + 1, "maze/center/center_se_corner");

        // 9. Place Entrance Piece correctly aligned to the grid
        BlockPos entranceWorldPos = originPos.add(
                entranceX * MODULE_SIZE,
                0,
                entranceZ * MODULE_SIZE
        );
        collector.addPiece(new MazeStructurePiece(templateManager, new Identifier(DownTheRabbitHole.MOD_ID, "maze/entrance_nwe"), entranceWorldPos, BlockRotation.NONE));

        // 10. Place Standard Path Pieces
        for (int x = 0; x < width; x++) {
            for (int z = 0; z < height; z++) {
                MazeCell cell = grid[x][z];
                if (cell.getType() == MazeCell.Type.CENTER || cell.getType() == MazeCell.Type.ENTRANCE || cell.getType() == MazeCell.Type.EMPTY) {
                    continue;
                }

                String pathName = getPathTemplateName(cell);
                if (pathName == null) continue;

                BlockPos cellWorldPos = originPos.add(
                        x * MODULE_SIZE,
                        0,
                        z * MODULE_SIZE
                );

                Identifier templateId = new Identifier(DownTheRabbitHole.MOD_ID, "maze/paths/" + pathName);
                collector.addPiece(new MazeStructurePiece(templateManager, templateId, cellWorldPos, BlockRotation.NONE));
            }
        }
    }

    private static void placeCenterPiece(StructurePiecesCollector collector, StructureTemplateManager templateManager, BlockPos originPos, int absX, int absZ, String fullTemplatePath) {
        BlockPos pos = originPos.add(
                absX * MODULE_SIZE,
                0,
                absZ * MODULE_SIZE
        );
        collector.addPiece(new MazeStructurePiece(templateManager, new Identifier(DownTheRabbitHole.MOD_ID, fullTemplatePath), pos, BlockRotation.NONE));
    }

    private static String getPathTemplateName(MazeCell cell) {
        switch (cell.getType()) {
            case STRAIGHT:
                if (cell.isNorthOpen() && cell.isSouthOpen()) return "straight_ns";
                return "straight_ew";

            case CORNER:
                if (cell.isNorthOpen() && cell.isEastOpen()) return "corner_ne";
                if (cell.isEastOpen() && cell.isSouthOpen()) return "corner_se";
                if (cell.isSouthOpen() && cell.isWestOpen()) return "corner_sw";
                return "corner_nw";

            case T_JUNCTION:
                if (!cell.isEastOpen()) return "t_nsw";
                if (!cell.isNorthOpen()) return "t_sew";
                if (!cell.isSouthOpen()) return "t_new";
                return "t_nse";

            case CROSSROAD:
                return "cross_nsew";

            case DEAD_END:
                if (cell.isNorthOpen()) return "deadend_n";
                if (cell.isSouthOpen()) return "deadend_s";
                if (cell.isEastOpen()) return "deadend_e";
                return "deadend_w";

            default:
                return null;
        }
    }

    private static void carvePassages(MazeCell[][] grid, int startX, int startZ, Random random) {
        boolean[][] visited = new boolean[grid.length][grid[0].length];
 for (int x = 0; x < grid.length; x++) {
            for (int z = 0; z < grid[0].length; z++) {
                if (grid[x][z].getType() == MazeCell.Type.CENTER || grid[x][z].getType() == MazeCell.Type.ENTRANCE) {
                    visited[x][z] = true;
                }
            }
        }

        dfsCarve(grid, startX, startZ, visited, random);
    }

    private static void dfsCarve(MazeCell[][] grid, int x, int z, boolean[][] visited, Random random) {
        visited[x][z] = true;

        int[][] directions = { {0, -1}, {1, 0}, {0, 1}, {-1, 0} };
        List<int[]> dirList = new ArrayList<>();
        for (int[] d : directions) {
            dirList.add(d);
        }
        Collections.shuffle(dirList, new java.util.Random(random.nextLong()));

        for (int[] d : dirList) {
            int nx = x + d[0];
            int nz = z + d[1];

            if (nx >= 0 && nx < grid.length && nz >= 0 && nz < grid[0].length && !visited[nx][nz]) {
                if (grid[nx][nz].getType() == MazeCell.Type.CENTER) continue;

                connectCells(grid[x][z], grid[nx][nz], d[0], d[1]);
                dfsCarve(grid, nx, nz, visited, random);
            }
        }
    }

    private static void connectCells(MazeCell c1, MazeCell c2, int dx, int dz) {
        if (dx == 0 && dz == -1) {
            c1.north = true;
            c2.south = true;
        } else if (dx == 1 && dz == 0) {
            c1.east = true;
            c2.west = true;
        } else if (dx == 0 && dz == 1) {
            c1.south = true;
            c2.north = true;
        } else if (dx == -1 && dz == 0) {
            c1.west = true;
            c2.east = true;
        }
    }
}
