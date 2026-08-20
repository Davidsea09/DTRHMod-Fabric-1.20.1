package net.emanueljdf09.dtrhmod.block.custom;

import net.emanueljdf09.dtrhmod.block.ModBlockEntities;
import net.emanueljdf09.dtrhmod.block.entity.MadHatterHatBlockEntity;
import net.emanueljdf09.dtrhmod.util.TeleportUtil;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MadHatterHatBlock extends BlockWithEntity implements BlockEntityProvider {
    public static final EnumProperty<HatState> STATE = EnumProperty.of("state", HatState.class);

    private static final VoxelShape IDLE_SHAPE = Block.createCuboidShape(3.0, 0.0, 3.0, 13.0, 10.0, 13.0);

    public MadHatterHatBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(STATE, HatState.IDLE));
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        if (!world.isClient && placer instanceof PlayerEntity player) {
            if (world.getBlockEntity(pos) instanceof MadHatterHatBlockEntity hatEntity) {
                hatEntity.setPortalOwnerUuid(player.getUuid());
            }
        }
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            if (!world.isClient) {
                BlockEntity be = world.getBlockEntity(pos);
                if (be instanceof MadHatterHatBlockEntity hatEntity) {
                    UUID ownerUuid = hatEntity.getPortalOwnerUuid();
                    ItemStack hatToRefund = new ItemStack(this);

                    if (ownerUuid != null && world.getServer() != null) {
                        PlayerEntity owner = world.getServer().getPlayerManager().getPlayer(ownerUuid);
                        if (owner != null) {
                            if (!owner.getInventory().insertStack(hatToRefund)) {
                                dropStack(world, pos, hatToRefund);
                            }
                        } else {
                            dropStack(world, pos, hatToRefund);
                        }
                    } else {
                        dropStack(world, pos, hatToRefund);
                    }
                }
            }
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBreak(world, pos, state, player);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(STATE);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return IDLE_SHAPE;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!world.isClient && state.get(STATE) == HatState.PORTAL && entity instanceof ServerPlayerEntity serverPlayer) {
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof MadHatterHatBlockEntity hatBe) {
                TeleportUtil.startHatTrance(
                        serverPlayer,
                        pos,
                        hatBe.getTargetDimension(),
                        hatBe.isInstanceDimension(),
                        hatBe.getStructureId(),
                        hatBe.getOriginDimension(),
                        hatBe.getPortalOwnerUuid()
                );
            }
        }
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new MadHatterHatBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, ModBlockEntities.MAD_HATTER_HAT, MadHatterHatBlockEntity::tick);
    }

    public enum HatState implements StringIdentifiable {
        IDLE("idle"),
        PORTAL("portal");

        private final String name;
        HatState(String name) { this.name = name; }
        @Override public String asString() { return this.name; }
    }
}