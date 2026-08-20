package net.emanueljdf09.dtrhmod.block.custom;

import net.emanueljdf09.dtrhmod.block.ModBlocks;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.WorldView;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.treedecorator.TreeDecorator;

public class WHangingLeavesBodyBlock extends AbstractPlantBlock {
    public static final VoxelShape SHAPE = Block.createCuboidShape(4.0, 9.0, 4.0, 12.0, 16.0, 12.0);

    public WHangingLeavesBodyBlock(Settings settings) {
        super(settings, Direction.DOWN, SHAPE, false);
    }

    @Override
    protected AbstractPlantStemBlock getStem() {
        return (AbstractPlantStemBlock) ModBlocks.WW_HANGING_LEAVES;
    }

    @Override
    protected boolean canAttachTo(BlockState state) {
        return super.canAttachTo(state) || state.isOf(ModBlocks.WW_LOG) || state.isOf(ModBlocks.WW_LEAVES);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockState aboveState = world.getBlockState(pos.up());
        return aboveState.isOf(this) || aboveState.isOf(ModBlocks.WW_HANGING_LEAVES) || aboveState.isOf(ModBlocks.WW_LEAVES);
    }
}
