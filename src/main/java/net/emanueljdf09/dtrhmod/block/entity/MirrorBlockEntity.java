package net.emanueljdf09.dtrhmod.block.entity;

import net.emanueljdf09.dtrhmod.block.ModBlockEntities;
import net.emanueljdf09.dtrhmod.block.custom.mirror.MirrorBlock;
import net.emanueljdf09.dtrhmod.block.custom.mirror.MirrorType;
import net.emanueljdf09.dtrhmod.util.TeleportUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;


public class MirrorBlockEntity extends BlockEntity {

    public MirrorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MIRROR_BLOCK_ENTITY, pos, state);
    }

    public boolean isPlayerInTrance(UUID playerUuid) {
        return TeleportUtil.isPlayerInTranceAt(playerUuid, this.pos);
    }

    public void activateWonderlandPortal(World world, BlockState lowerState, BlockPos lowerPos) {
        BlockPos upperPos = lowerPos.up();
        BlockState upperState = world.getBlockState(upperPos);

        world.setBlockState(lowerPos, lowerState.with(MirrorBlock.TYPE, MirrorType.wonderland), Block.NOTIFY_ALL);
        if (upperState.isOf(lowerState.getBlock())) {
            world.setBlockState(upperPos, upperState.with(MirrorBlock.TYPE, MirrorType.wonderland), Block.NOTIFY_ALL);
        }
        world.playSound(null, lowerPos, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    public void startTeleportSequence(ServerPlayerEntity serverPlayer, ServerWorld serverWorld) {
        TeleportUtil.startMirrorTrance(serverPlayer, this.pos, serverWorld);
    }


}
