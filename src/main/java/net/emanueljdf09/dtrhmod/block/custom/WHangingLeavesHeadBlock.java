package net.emanueljdf09.dtrhmod.block.custom;

import net.emanueljdf09.dtrhmod.block.ModBlocks;
import net.minecraft.block.*;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class WHangingLeavesHeadBlock extends AbstractPlantStemBlock {
    public static final VoxelShape SHAPE = Block.createCuboidShape(4.0, 9.0, 4.0, 12.0, 16.0, 12.0);

    public WHangingLeavesHeadBlock(Settings settings) {
        super(settings, Direction.DOWN, SHAPE, false, 0.14D);
        this.setDefaultState(this.stateManager.getDefaultState().with(AGE, 0)); }

    @Override
    protected int getGrowthLength(Random random) {
        return random.nextInt(4) + 2;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
    }

    @Override
    protected boolean chooseStemState(BlockState state) {
        return state.isAir();
    }

    @Override
    protected Block getPlant() {
        return ModBlocks.WW_HANGING_LEAVES_PLANT;
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockPos blockPos = pos.offset(this.growthDirection.getOpposite());
        BlockState blockState = world.getBlockState(blockPos);

        return blockState.isOf(ModBlocks.WW_LEAVES) ||
                blockState.isOf(ModBlocks.WW_HANGING_LEAVES_PLANT) ||
                blockState.isSideSolidFullSquare(world, blockPos, Direction.DOWN);
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return world.getBlockState(pos.down()).isAir();
    }
}
