package net.emanueljdf09.dtrhmod.block.custom;

import net.emanueljdf09.dtrhmod.block.ModBlockEntities;
import net.emanueljdf09.dtrhmod.block.entity.TeapotBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class TeapotBlock extends BlockWithEntity implements BlockEntityProvider {
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    private static final Map<Direction, VoxelShape> SHAPES = createDirectionalShapes();


    public TeapotBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPES.getOrDefault(state.get(FACING), SHAPES.get(Direction.NORTH));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPES.getOrDefault(state.get(FACING), SHAPES.get(Direction.NORTH));
    }

    private static Map<Direction, VoxelShape> createDirectionalShapes() {
        Map<Direction, VoxelShape> map = new EnumMap<>(Direction.class);

        // --- NORTH Shape ---
        VoxelShape northShape = VoxelShapes.empty();
        northShape = VoxelShapes.combine(northShape, VoxelShapes.cuboid(0.1875, 0.0625, 0.3125, 0.8125, 0.4375, 0.6875), BooleanBiFunction.OR);
        northShape = VoxelShapes.combine(northShape, VoxelShapes.cuboid(0.25, 0.4375, 0.375, 0.75, 0.5, 0.625), BooleanBiFunction.OR);
        northShape = VoxelShapes.combine(northShape, VoxelShapes.cuboid(0.25, 0, 0.375, 0.75, 0.0625, 0.625), BooleanBiFunction.OR);
        map.put(Direction.NORTH, northShape.simplify());
        map.put(Direction.SOUTH, northShape.simplify());


        VoxelShape eastShape = VoxelShapes.empty();
        eastShape = VoxelShapes.combine(eastShape, VoxelShapes.cuboid(0.3125, 0.0625, 0.1875, 0.6875, 0.4375, 0.8125), BooleanBiFunction.OR);
        eastShape = VoxelShapes.combine(eastShape, VoxelShapes.cuboid(0.375, 0.4375, 0.25, 0.625, 0.5, 0.75), BooleanBiFunction.OR);
        eastShape = VoxelShapes.combine(eastShape, VoxelShapes.cuboid(0.375, 0, 0.25, 0.625, 0.0625, 0.75), BooleanBiFunction.OR);

        map.put(Direction.EAST, eastShape.simplify());
        map.put(Direction.WEST, eastShape.simplify());

        return map;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TeapotBlockEntity(pos, state);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof TeapotBlockEntity) {
                ItemScatterer.spawn(world, pos, (TeapotBlockEntity)blockEntity);
                world.updateComparators(pos,this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            NamedScreenHandlerFactory screenHandlerFactory = ((TeapotBlockEntity) world.getBlockEntity(pos));

            if (screenHandlerFactory != null) {
                player.openHandledScreen(screenHandlerFactory);
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        tooltip.add(Text.translatable("block.dtrhmod.teapot.tooltip").formatted(Formatting.BOLD, Formatting.GRAY));
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, ModBlockEntities.TEAPOT_BLOCK_ENTITY,
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1));
    }
}