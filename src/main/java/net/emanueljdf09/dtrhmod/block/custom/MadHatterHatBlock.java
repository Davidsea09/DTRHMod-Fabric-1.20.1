package net.emanueljdf09.dtrhmod.block.custom;

import net.emanueljdf09.dtrhmod.block.ModBlockEntities;
import net.emanueljdf09.dtrhmod.block.entity.MadHatterHatBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class MadHatterHatBlock extends BlockWithEntity implements BlockEntityProvider {
    public static final EnumProperty<HatState> STATE = EnumProperty.of("state", HatState.class);

    private static final VoxelShape IDLE_SHAPE = Block.createCuboidShape(3.0, 0.0, 3.0, 13.0, 10.0, 13.0);
    private static final VoxelShape PORTAL_SHAPE = VoxelShapes.fullCube();

    public MadHatterHatBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(STATE, HatState.IDLE));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(STATE);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(STATE) == HatState.PORTAL ? PORTAL_SHAPE : IDLE_SHAPE;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
       // return BlockRenderType.ENTITYBLOCK_ANIMATED;
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!world.isClient && state.get(STATE) == HatState.PORTAL && entity instanceof PlayerEntity player) {
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof MadHatterHatBlockEntity hatBe) {
                hatBe.triggerTeleport(player);
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
        PORTAL("portal"); // Make sure this is added so blockstates match up!

        private final String name;
        HatState(String name) { this.name = name; }
        @Override public String asString() { return this.name; }
    }
}