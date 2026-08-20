package net.emanueljdf09.dtrhmod.world.structures.maze;

import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.emanueljdf09.dtrhmod.world.structures.pieces.MazeStructurePiece;
import net.minecraft.structure.StructurePiecesCollector;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.ChunkRandom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MazeLayoutGenerator {
    public static final int MODULE_SIZE = 7;

    public enum MazeSize {
        SMALL(7),
        MEDIUM(15),
        LARGE(21);

        private final int dimension;
        MazeSize(int dimension) {
            this.dimension = dimension;
        }
        public int getDimension() { return dimension; }
    }

    public static void generateAndPlaceMaze(StructurePiecesCollector collector, StructureTemplateManager templateManager, BlockPos originPos, ChunkRandom random, MazeSize chosenSize) {
        DownTheRabbitHole.LOGGER.info("========== GENERATING MAZE ==========", chosenSize.name());
        int width = chosenSize.getDimension();
        int height = chosenSize.getDimension();

        DownTheRabbitHole.LOGGER.info("Chosen Maze Size: {} ({}x{} grid)", chosenSize.name(), width, height);

        MazeCell[][] grid = new MazeCell[width][height];
        for (int x = 0; x < width; x++) {
            for (int z = 0; z < height; z++) {
                grid[x][z] = new MazeCell(x, z);
            }
        }

        int centerX = width / 2;
        int centerZ = height / 2;

        if (chosenSize == MazeSize.LARGE) {
            for (int cx = centerX - 1; cx <= centerX + 1; cx++) {
                for (int cz = centerZ - 1; cz <= centerZ + 1; cz++) {
                    grid[cx][cz].setType(MazeCell.Type.CENTER);
                }
            }
        } else {
            grid[centerX][centerZ].setType(MazeCell.Type.CENTER);
        }

        int entranceX = centerX;
        int entranceZ = 0;
        grid[entranceX][entranceZ].setType(MazeCell.Type.ENTRANCE);

        if (chosenSize == MazeSize.LARGE) {
            MazeCell startCell = grid[centerX - 2][centerZ];
            connectCells(grid[centerX - 1][centerZ], startCell, -1, 0);
        } else {
            MazeCell startCell = grid[centerX][centerZ - 1];
            connectCells(grid[centerX][centerZ], startCell, 0, -1);
        }

        connectCells(grid[entranceX][entranceZ], grid[entranceX][entranceZ + 1], 0, 1);

        int carveStartX = (chosenSize == MazeSize.LARGE) ? centerX - 2 : centerX;
        int carveStartZ = centerZ;
        carvePassages(grid, carveStartX, carveStartZ, random);
        DownTheRabbitHole.LOGGER.info("========== MAZE CARVING FINISHED ==========");

        for (int x = 0; x < width; x++) {
            for (int z = 0; z < height; z++) {
                MazeCell cell = grid[x][z];
                if (cell.getType() != MazeCell.Type.CENTER && cell.getType() != MazeCell.Type.ENTRANCE) {
                    cell.determinePieceTypeAndRotation();
                }
            }
        }

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

        if (chosenSize == MazeSize.LARGE) {
            placeCenterPiece(collector, templateManager, originPos, centerX - 1, centerZ - 1, "maze/center/center_nw_corner");
            placeCenterPiece(collector, templateManager, originPos, centerX, centerZ - 1, "maze/center/center_north");
            placeCenterPiece(collector, templateManager, originPos, centerX + 1, centerZ - 1, "maze/center/center_ne_corner");

            placeCenterPiece(collector, templateManager, originPos, centerX - 1, centerZ, "maze/center/center_west");
            placeCenterPiece(collector, templateManager, originPos, centerX, centerZ, "maze/center/center_middle");
            placeCenterPiece(collector, templateManager, originPos, centerX + 1, centerZ, "maze/center/center_east");

            placeCenterPiece(collector, templateManager, originPos, centerX - 1, centerZ + 1, "maze/center/center_sw_corner");
            placeCenterPiece(collector, templateManager, originPos, centerX, centerZ + 1, "maze/center/center_south");
            placeCenterPiece(collector, templateManager, originPos, centerX + 1, centerZ + 1, "maze/center/center_se_corner");
        } else {
            placeCenterPiece(collector, templateManager, originPos, centerX, centerZ, "maze/center/center_middle");
        }

        BlockPos entranceWorldPos = originPos.add(
                entranceX * MODULE_SIZE,
                0,
                entranceZ * MODULE_SIZE
        );
        collector.addPiece(new MazeStructurePiece(templateManager, new Identifier(DownTheRabbitHole.MOD_ID, "maze/entrance_nwe"), entranceWorldPos, BlockRotation.NONE));

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

    private static void carvePassages(MazeCell[][] grid, int startX, int startZ, ChunkRandom random) {
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

    private static void dfsCarve(MazeCell[][] grid, int x, int z, boolean[][] visited, ChunkRandom random) {
        visited[x][z] = true;

        int[][] directions = { {0, -1}, {1, 0}, {0, 1}, {-1, 0} };
        List<int[]> dirList = new ArrayList<>();
        for (int[] d : directions) {
            dirList.add(d);
        }
        Collections.shuffle(dirList, new Random(random.nextLong()));

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
