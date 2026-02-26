package net.emanueljdf09.dtrhmod.block.custom;

import net.emanueljdf09.dtrhmod.util.RabbitholeLogic;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RabbitHoleBlock extends Block {

    public RabbitHoleBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (world.isClient) return;

        if (!(entity instanceof ServerPlayerEntity player)) return;

        if (!player.isInSwimmingPose()) return;

        RabbitholeLogic.handlePortal(player);
    }
}
