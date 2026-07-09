package net.emanueljdf09.dtrhmod.block.entity;

import net.emanueljdf09.dtrhmod.block.ModBlockEntities;
import net.emanueljdf09.dtrhmod.block.custom.mirror.MirrorBlock;
import net.emanueljdf09.dtrhmod.block.custom.mirror.MirrorType;
import net.emanueljdf09.dtrhmod.item.ModItems;
import net.emanueljdf09.dtrhmod.util.ModComponents;
import net.emanueljdf09.dtrhmod.util.TeleportUtil;
import net.emanueljdf09.dtrhmod.util.components.Mirror.MirrorGossipComponent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;


public class MirrorBlockEntity extends BlockEntity {
    private int scanCooldown = 0;

    public MirrorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MIRROR_BLOCK_ENTITY, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, MirrorBlockEntity blockEntity) {
        if (world.isClient()) return;

        blockEntity.scanCooldown++;
        if (blockEntity.scanCooldown >= 20) {
            blockEntity.scanCooldown = 0;

            MirrorGossipComponent gossip = ModComponents.MIRROR_GOSSIP.get(blockEntity);
            if (gossip.getOwnerUuid() == null) return;

            Box scanBox = new Box(pos).expand(8);
            List<ServerPlayerEntity> players = world.getEntitiesByClass(ServerPlayerEntity.class, scanBox, p -> true);

            String newestIntruder = null;
            for (ServerPlayerEntity player : players) {
                if (!player.getUuid().equals(gossip.getOwnerUuid())) {
                    newestIntruder = player.getName().getString();
                }
            }

            if (newestIntruder != null && !newestIntruder.equals(gossip.getLastSeenPlayerName())) {
                gossip.setLastSeenPlayerName(newestIntruder);
                ModComponents.MIRROR_GOSSIP.sync(blockEntity);
            }
        }
    }

    public void handleMagicMirrorInteraction(net.minecraft.entity.player.PlayerEntity player, net.minecraft.util.Hand hand, BlockPos soundPos) {
        if (this.world == null || this.world.isClient) return;

        BlockState state = this.getCachedState();
        if (!state.isOf(net.emanueljdf09.dtrhmod.block.ModBlocks.MIRROR_BLOCK)) return;

        // 1. Find the coordinates for BOTH halves
        DoubleBlockHalf half = state.get(Properties.DOUBLE_BLOCK_HALF);
        BlockPos lowerPos = (half == DoubleBlockHalf.LOWER) ? this.pos : this.pos.down();
        BlockPos upperPos = lowerPos.up();

        BlockEntity lowerBe = this.world.getBlockEntity(lowerPos);
        BlockEntity upperBe = this.world.getBlockEntity(upperPos);

        ItemStack heldItem = player.getStackInHand(hand);


        var gossipLower = ModComponents.MIRROR_GOSSIP.get(lowerBe);

        var gossipUpper = ModComponents.MIRROR_GOSSIP.get(upperBe);

        // --- ITEM BINDING LOGIC ---
        if ((heldItem.isOf(net.minecraft.item.Items.AMETHYST_SHARD))) {
            UUID currentOwner = gossipLower.getOwnerUuid();

            if (currentOwner != null && !player.getUuid().equals(currentOwner)) {
                player.sendMessage(Text.literal("§dMagic Mirror: §cThe amethyst shatters in vain! My loyalty belongs to another..."), false);
                this.world.playSound(null, soundPos, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 0.5f, 1.5f);
                return;
            }
            // 2. Apply owner to the LOWER half component
            if (lowerBe instanceof MirrorBlockEntity) {
                gossipLower.setOwnerUuid(player.getUuid());
                gossipLower.setLastSeenPlayerName("nobody... yet");
                ModComponents.MIRROR_GOSSIP.sync(lowerBe);
            }

            // 3. Apply the exact same owner to the UPPER half component
            if (upperBe instanceof MirrorBlockEntity) {
                gossipUpper.setOwnerUuid(player.getUuid());
                gossipUpper.setLastSeenPlayerName("nobody... yet");
                ModComponents.MIRROR_GOSSIP.sync(upperBe);
            }

            if (!player.isCreative()) {
                heldItem.decrement(1);
            }

            player.sendMessage(net.minecraft.text.Text.literal("§dMagic Mirror: §eThe amethyst shatters against the glass... I serve a new master now."), false);
            this.world.playSound(null, soundPos, net.minecraft.sound.SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, net.minecraft.sound.SoundCategory.BLOCKS, 1.0f, 1.0f);
            return;
        }

        // --- GOSSIP LOGIC (Always read from lower handle for consistency) ---
        if (lowerBe instanceof MirrorBlockEntity) {
            var gossip = ModComponents.MIRROR_GOSSIP.get(lowerBe);

            if (gossip.getOwnerUuid() == null) {
                player.sendMessage(net.minecraft.text.Text.literal("§dMagic Mirror: §5The glass ripples idly. It needs an amethyst shard catalyst to bind to..."), false);
                return;
            }

            if (player.getUuid().equals(gossip.getOwnerUuid())) {
                player.sendMessage(net.minecraft.text.Text.literal("§dMagic Mirror whispers: §7I last saw §b" + gossip.getLastSeenPlayerName() + "§7 snooping around here..."), false);
            } else {
                player.sendMessage(net.minecraft.text.Text.literal("§dMagic Mirror: §5The glass remains silent. You are not my master."), false);
            }
        }
    }

    public boolean isPlayerInTrance(UUID playerUuid) {
        return TeleportUtil.isPlayerInTranceAt(playerUuid, this.pos);
    }

    public void activateWonderlandPortal(World world, BlockState lowerState, BlockPos lowerPos) {
        BlockPos upperPos = lowerPos.up();
        BlockState upperState = world.getBlockState(upperPos);

        // Mutate states
        world.setBlockState(lowerPos, lowerState.with(MirrorBlock.TYPE, MirrorType.wonderland), 3);
        if (upperState.isOf(lowerState.getBlock())) {
            world.setBlockState(upperPos, upperState.with(MirrorBlock.TYPE, MirrorType.wonderland), 3);
        }

        world.playSound(null, lowerPos, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    public void activateMagicMirror(World world, BlockState lowerState, BlockPos lowerPos) {
        BlockPos upperPos = lowerPos.up();
        BlockState upperState = world.getBlockState(upperPos);

        world.setBlockState(lowerPos, lowerState.with(MirrorBlock.TYPE, MirrorType.magic_mirror), 3);
        if (upperState.isOf(lowerState.getBlock())) {
            world.setBlockState(upperPos, upperState.with(MirrorBlock.TYPE, MirrorType.magic_mirror), 3);
        }

        world.playSound(null, lowerPos, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    public void startTeleportSequence(ServerPlayerEntity serverPlayer, ServerWorld serverWorld) {
        TeleportUtil.startMirrorTrance(serverPlayer, this.pos, serverWorld);
    }


}
