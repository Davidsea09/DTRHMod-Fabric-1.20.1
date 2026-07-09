package net.emanueljdf09.dtrhmod.block.custom;

import net.emanueljdf09.dtrhmod.block.ModBlockEntities;
import net.emanueljdf09.dtrhmod.block.entity.RabbitHoleBlockEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class RabbitHoleBlock extends BlockWithEntity {

    public RabbitHoleBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL; // Forces the block to render its regular block JSON model
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (world.isClient) return;

        // Route the collision logic straight down into the Block Entity
        if (entity instanceof ServerPlayerEntity player && world.getBlockEntity(pos) instanceof RabbitHoleBlockEntity rabbitHole) {
            rabbitHole.handlePlayerCollision(player);
        }
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new RabbitHoleBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {

        return world.isClient() ? null : checkType(type, ModBlockEntities.RABBIT_HOLE_BLOCK_ENTITY, RabbitHoleBlockEntity::tick);
    }
}