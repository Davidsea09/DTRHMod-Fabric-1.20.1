package net.emanueljdf09.dtrhmod.block.custom;

import net.emanueljdf09.dtrhmod.block.ModBlocks;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
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
        super(settings, Direction.DOWN, SHAPE, false, 0.3);
    }

    @Override
    protected int getGrowthLength(Random random) {
        return 1;
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
    protected boolean canAttachTo(BlockState state) {
         return super.canAttachTo(state) || state.isOf(ModBlocks.WW_LOG) || state.isOf(ModBlocks.WW_LEAVES) || state.isOf(ModBlocks.WW_HANGING_LEAVES_PLANT);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockState aboveState = world.getBlockState(pos.up());
        return aboveState.isOf(this) || aboveState.isOf(ModBlocks.WW_HANGING_LEAVES) || aboveState.isOf(ModBlocks.WW_LEAVES) || aboveState.isOf(ModBlocks.WW_HANGING_LEAVES_PLANT);
    }
}