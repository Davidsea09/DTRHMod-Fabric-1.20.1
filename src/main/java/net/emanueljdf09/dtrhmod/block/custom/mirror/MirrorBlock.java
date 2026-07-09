package net.emanueljdf09.dtrhmod.block.custom.mirror;

import net.emanueljdf09.dtrhmod.block.entity.MirrorBlockEntity;
import net.emanueljdf09.dtrhmod.item.ModItems;
import net.emanueljdf09.dtrhmod.util.ModComponents;
import net.emanueljdf09.dtrhmod.util.components.ProgressionComponent;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class MirrorBlock extends BlockWithEntity {
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final EnumProperty<DoubleBlockHalf> HALF = Properties.DOUBLE_BLOCK_HALF;
    public static final EnumProperty<MirrorType> TYPE = EnumProperty.of("type", MirrorType.class);

    public MirrorBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(HALF, DoubleBlockHalf.LOWER)
                .with(TYPE, MirrorType.normal));
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos blockPos = ctx.getBlockPos();
        World world = ctx.getWorld();

        if (blockPos.getY() < world.getTopY() - 1 && world.getBlockState(blockPos.up()).canReplace(ctx)) {
            return this.getDefaultState()
                    .with(FACING, ctx.getHorizontalPlayerFacing().getOpposite())
                    .with(HALF, DoubleBlockHalf.LOWER)
                    .with(TYPE, MirrorType.normal);
        }
        return null;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        world.setBlockState(pos.up(), state.with(HALF, DoubleBlockHalf.UPPER), Block.NOTIFY_ALL);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        if (state.get(HALF) == DoubleBlockHalf.UPPER) {
            BlockState blockState = world.getBlockState(pos.down());
            return blockState.isOf(this) && blockState.get(HALF) == DoubleBlockHalf.LOWER;
        }
        return super.canPlaceAt(state, world, pos);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        DoubleBlockHalf doubleBlockHalf = state.get(HALF);

        if (direction.getAxis() == Direction.Axis.Y && (doubleBlockHalf == DoubleBlockHalf.LOWER) == (direction == Direction.UP)) {
            if (neighborState.isOf(this) && neighborState.get(HALF) != doubleBlockHalf) {
                return state.with(FACING, neighborState.get(FACING)).with(TYPE, neighborState.get(TYPE));
            }
            return Blocks.AIR.getDefaultState();
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient && world.getServer() != null) {
            BlockPos lowerPos = (state.get(HALF) == DoubleBlockHalf.LOWER) ? pos : pos.down();

            ServerWorld overworld = world.getServer().getWorld(World.OVERWORLD);
            ServerWorld wonderland = world.getServer().getWorld(net.emanueljdf09.dtrhmod.world.dimension.ModDimensions.WONDERLAND_LEVEL_KEY);

            if (overworld != null) ModComponents.MIRROR_COMPONENT.get(overworld).removeLink(lowerPos);
            if (wonderland != null) ModComponents.MIRROR_COMPONENT.get(wonderland).removeLink(lowerPos);
        }
        super.onBreak(world, pos, state, player);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);

        BlockPos lowerPos = (state.get(HALF) == DoubleBlockHalf.LOWER) ? pos : pos.down();
        BlockState lowerState = world.getBlockState(lowerPos);

        // Safety check: ensure we are actually working with our mirror block state
        if (!lowerState.isOf(this)) return ActionResult.PASS;
        MirrorType currentType = lowerState.get(TYPE);

        if (world.getBlockEntity(lowerPos) instanceof MirrorBlockEntity mirrorEntity) {

            if (currentType == MirrorType.normal) {
                if (stack.isOf(ModItems.POCKETWATCH)) {
                    if (!world.isClient) {
                        mirrorEntity.activateWonderlandPortal(world, lowerState, lowerPos);
                    }
                    return ActionResult.SUCCESS;
                }

                if (stack.isOf(Items.GOLD_INGOT)) {
                    if (!world.isClient) {
                        mirrorEntity.activateMagicMirror(world, lowerState, lowerPos);
                    }
                    return ActionResult.SUCCESS;
                }
                return ActionResult.PASS;
            }

            if (currentType == MirrorType.magic_mirror) {
                if (!world.isClient) {
                    mirrorEntity.handleMagicMirrorInteraction(player, hand, lowerPos);
                }
                return ActionResult.SUCCESS;
            }

            if (!world.isClient() && player instanceof ServerPlayerEntity serverPlayer) {

                ProgressionComponent story = ModComponents.PROGRESSION_COMPONENT.get(serverPlayer);
                if (!story.isWonderlandMirrorUnlocked()) {
                    serverPlayer.sendMessage(Text.literal("§cThe glass refuses to reflect a path. You have not unlocked this secret yet..."), true);

                    world.playSound(null, pos, net.minecraft.sound.SoundEvents.BLOCK_GLASS_BREAK, net.minecraft.sound.SoundCategory.BLOCKS, 0.75f, 0.5f);
                    return ActionResult.SUCCESS;
                }

                mirrorEntity.startTeleportSequence(serverPlayer, (ServerWorld) world);
                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.PASS;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF, TYPE);
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
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        // We only want the gossip scanner running on the server side (performance friendly!)
        if (world.isClient) return null;

        // This registers our static tick method to run every single game frame
        return checkType(type, net.emanueljdf09.dtrhmod.block.ModBlockEntities.MIRROR_BLOCK_ENTITY, MirrorBlockEntity::tick);
    }

    private static final VoxelShape UPPER_NORTH = makeShapeNorthUp();
    private static final VoxelShape UPPER_SOUTH = makeShapeSouthUp();
    private static final VoxelShape UPPER_EAST = makeShapeEastUp();
    private static final VoxelShape UPPER_WEST = makeShapeWestUp();

    private static final VoxelShape LOWER_NORTH = makeShapeNorthBt();
    private static final VoxelShape LOWER_SOUTH = makeShapeSouthBt();
    private static final VoxelShape LOWER_EAST = makeShapeEastBt();
    private static final VoxelShape LOWER_WEST = makeShapeWestBt();

    public static VoxelShape makeShapeNorthBt(){
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.046875, 0.421875, 0.90625, 0.15625, 1, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.828125, 0.328125, 0.90625, 0.90625, 0.421875, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.15625, 0.234375, 0.90625, 0.234375, 0.328125, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.28125, 0.078125, 0.90625, 0.390625, 0.171875, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.203125, 0.140625, 0.90625, 0.28125, 0.234375, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.09375, 0.328125, 0.90625, 0.171875, 0.421875, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.84375, 0.421875, 0.90625, 0.953125, 1, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.390625, 0.03125, 0.90625, 0.609375, 0.1875, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.234375, 0.234375, 0.90625, 0.265625, 0.3125, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.0625, 0.359375, 0.90625, 0.09375, 0.421875, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.90625, 0.359375, 0.90625, 0.9375, 0.421875, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.171875, 0.328125, 0.90625, 0.21875, 0.375, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.84375, 0.265625, 0.90625, 0.890625, 0.328125, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.796875, 0.171875, 0.90625, 0.828125, 0.234375, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.71875, 0.09375, 0.90625, 0.765625, 0.140625, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.109375, 0.265625, 0.90625, 0.15625, 0.328125, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.609375, 0.046875, 0.90625, 0.671875, 0.078125, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.171875, 0.171875, 0.90625, 0.203125, 0.234375, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.328125, 0.046875, 0.90625, 0.390625, 0.078125, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.734375, 0.234375, 0.90625, 0.765625, 0.3125, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.671875, 0.171875, 0.90625, 0.71875, 0.21875, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.234375, 0.09375, 0.90625, 0.28125, 0.140625, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.78125, 0.328125, 0.90625, 0.828125, 0.375, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.28125, 0.171875, 0.90625, 0.328125, 0.21875, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.453125, 0.1875, 0.90625, 0.546875, 0.21875, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.4375, 0, 0.90625, 0.5625, 0.03125, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.765625, 0.234375, 0.90625, 0.84375, 0.328125, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.609375, 0.078125, 0.90625, 0.71875, 0.171875, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.71875, 0.140625, 0.90625, 0.796875, 0.234375, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.234375, 0.171875, 0.953125, 0.765625, 1, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.765625, 0.265625, 0.953125, 0.859375, 1, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.140625, 0.265625, 0.953125, 0.234375, 1, 1), BooleanBiFunction.OR);

        return shape;
    }

    public static VoxelShape makeShapeWestBt(){
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.90625, 0.421875, 0.84375, 1, 1, 0.953125), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.90625, 0.328125, 0.09375, 1, 0.421875, 0.171875), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.90625, 0.234375, 0.765625, 1, 0.328125, 0.84375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.90625, 0.078125, 0.609375, 1, 0.171875, 0.71875), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.90625, 0.140625, 0.71875, 1, 0.234375, 0.796875), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.90625, 0.328125, 0.828125, 1, 0.421875, 0.90625), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.90625, 0.421875, 0.046875, 1, 1, 0.15625), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.90625, 0.03125, 0.390625, 1, 0.1875, 0.609375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.90625, 0.234375, 0.734375, 1, 0.3125, 0.765625), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.90625, 0.359375, 0.90625, 1, 0.421875, 0.9375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.90625, 0.359375, 0.0625, 1, 0.421875, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.90625, 0.328125, 0.78125, 1, 0.375, 0.828125), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.90625, 0.265625, 0.109375, 1, 0.328125, 0.15625), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.90625, 0.171875, 0.171875, 1, 0.234375, 0.203125), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.90625, 0.09375, 0.234375, 1, 0.140625, 0.28125), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.90625, 0.265625, 0.84375, 1, 0.328125, 0.890625), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.90625, 0.046875, 0.328125, 1, 0.078125, 0.390625), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.90625, 0.171875, 0.796875, 1, 0.234375, 0.828125), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.90625, 0.046875, 0.609375, 1, 0.078125, 0.671875), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.90625, 0.234375, 0.234375, 1, 0.3125, 0.265625), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.90625, 0.171875, 0.28125, 1, 0.21875, 0.328125), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.90625, 0.09375, 0.71875, 1, 0.140625, 0.765625), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.90625, 0.328125, 0.171875, 1, 0.375, 0.21875), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.90625, 0.171875, 0.671875, 1, 0.21875, 0.71875), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.90625, 0.1875, 0.453125, 1, 0.21875, 0.546875), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.90625, 0, 0.4375, 1, 0.03125, 0.5625), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.90625, 0.234375, 0.15625, 1, 0.328125, 0.234375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.90625, 0.078125, 0.28125, 1, 0.171875, 0.390625), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.90625, 0.140625, 0.203125, 1, 0.234375, 0.28125), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.953125, 0.171875, 0.234375, 1, 1, 0.765625), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.953125, 0.265625, 0.140625, 1, 1, 0.234375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.953125, 0.265625, 0.765625, 1, 1, 0.859375), BooleanBiFunction.OR);

        return shape;
    }

    public static VoxelShape makeShapeEastBt(){
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0, 0.421875, 0.046875, 0.09375, 1, 0.15625), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0, 0.328125, 0.828125, 0.09375, 0.421875, 0.90625), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0, 0.234375, 0.15625, 0.09375, 0.328125, 0.234375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0, 0.078125, 0.28125, 0.09375, 0.171875, 0.390625), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0, 0.140625, 0.203125, 0.09375, 0.234375, 0.28125), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0, 0.328125, 0.09375, 0.09375, 0.421875, 0.171875), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0, 0.421875, 0.84375, 0.09375, 1, 0.953125), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0, 0.03125, 0.390625, 0.09375, 0.1875, 0.609375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0, 0.234375, 0.234375, 0.09375, 0.3125, 0.265625), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0, 0.359375, 0.0625, 0.09375, 0.421875, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0, 0.359375, 0.90625, 0.09375, 0.421875, 0.9375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0, 0.328125, 0.171875, 0.09375, 0.375, 0.21875), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0, 0.265625, 0.84375, 0.09375, 0.328125, 0.890625), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0, 0.171875, 0.796875, 0.09375, 0.234375, 0.828125), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0, 0.09375, 0.71875, 0.09375, 0.140625, 0.765625), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0, 0.265625, 0.109375, 0.09375, 0.328125, 0.15625), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0, 0.046875, 0.609375, 0.09375, 0.078125, 0.671875), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0, 0.171875, 0.171875, 0.09375, 0.234375, 0.203125), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0, 0.046875, 0.328125, 0.09375, 0.078125, 0.390625), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0, 0.234375, 0.734375, 0.09375, 0.3125, 0.765625), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0, 0.171875, 0.671875, 0.09375, 0.21875, 0.71875), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0, 0.09375, 0.234375, 0.09375, 0.140625, 0.28125), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0, 0.328125, 0.78125, 0.09375, 0.375, 0.828125), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0, 0.171875, 0.28125, 0.09375, 0.21875, 0.328125), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0, 0.1875, 0.453125, 0.09375, 0.21875, 0.546875), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0, 0, 0.4375, 0.09375, 0.03125, 0.5625), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0, 0.234375, 0.765625, 0.09375, 0.328125, 0.84375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0, 0.078125, 0.609375, 0.09375, 0.171875, 0.71875), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0, 0.140625, 0.71875, 0.09375, 0.234375, 0.796875), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0, 0.171875, 0.234375, 0.046875, 1, 0.765625), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0, 0.265625, 0.765625, 0.046875, 1, 0.859375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0, 0.265625, 0.140625, 0.046875, 1, 0.234375), BooleanBiFunction.OR);

        return shape;
    }

    public static VoxelShape makeShapeSouthBt(){
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.84375, 0.421875, 0, 0.953125, 1, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.09375, 0.328125, 0, 0.171875, 0.421875, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.765625, 0.234375, 0, 0.84375, 0.328125, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.609375, 0.078125, 0, 0.71875, 0.171875, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.71875, 0.140625, 0, 0.796875, 0.234375, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.828125, 0.328125, 0, 0.90625, 0.421875, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.046875, 0.421875, 0, 0.15625, 1, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.390625, 0.03125, 0, 0.609375, 0.1875, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.734375, 0.234375, 0, 0.765625, 0.3125, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.90625, 0.359375, 0, 0.9375, 0.421875, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.0625, 0.359375, 0, 0.09375, 0.421875, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.78125, 0.328125, 0, 0.828125, 0.375, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.109375, 0.265625, 0, 0.15625, 0.328125, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.171875, 0.171875, 0, 0.203125, 0.234375, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.234375, 0.09375, 0, 0.28125, 0.140625, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.84375, 0.265625, 0, 0.890625, 0.328125, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.328125, 0.046875, 0, 0.390625, 0.078125, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.796875, 0.171875, 0, 0.828125, 0.234375, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.609375, 0.046875, 0, 0.671875, 0.078125, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.234375, 0.234375, 0, 0.265625, 0.3125, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.28125, 0.171875, 0, 0.328125, 0.21875, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.71875, 0.09375, 0, 0.765625, 0.140625, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.171875, 0.328125, 0, 0.21875, 0.375, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.671875, 0.171875, 0, 0.71875, 0.21875, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.453125, 0.1875, 0, 0.546875, 0.21875, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.4375, 0, 0, 0.5625, 0.03125, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.15625, 0.234375, 0, 0.234375, 0.328125, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.28125, 0.078125, 0, 0.390625, 0.171875, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.203125, 0.140625, 0, 0.28125, 0.234375, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.234375, 0.171875, 0, 0.765625, 1, 0.046875), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.140625, 0.265625, 0, 0.234375, 1, 0.046875), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.765625, 0.265625, 0, 0.859375, 1, 0.046875), BooleanBiFunction.OR);

        return shape;
    }

    public static VoxelShape makeShapeNorthUp(){
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.84375, 0, 0.90625, 0.953125, 0.578125, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.09375, 0.578125, 0.90625, 0.171875, 0.671875, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.765625, 0.671875, 0.90625, 0.84375, 0.765625, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.609375, 0.828125, 0.90625, 0.71875, 0.921875, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.71875, 0.765625, 0.90625, 0.796875, 0.859375, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.828125, 0.578125, 0.90625, 0.90625, 0.671875, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.046875, 0, 0.90625, 0.15625, 0.578125, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.390625, 0.8125, 0.90625, 0.609375, 0.96875, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.734375, 0.6875, 0.90625, 0.765625, 0.765625, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.90625, 0.578125, 0.90625, 0.9375, 0.640625, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.0625, 0.578125, 0.90625, 0.09375, 0.640625, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.78125, 0.625, 0.90625, 0.828125, 0.671875, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.109375, 0.671875, 0.90625, 0.15625, 0.734375, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.171875, 0.765625, 0.90625, 0.203125, 0.828125, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.234375, 0.859375, 0.90625, 0.28125, 0.90625, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.84375, 0.671875, 0.90625, 0.890625, 0.734375, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.328125, 0.921875, 0.90625, 0.390625, 0.953125, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.796875, 0.765625, 0.90625, 0.828125, 0.828125, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.609375, 0.921875, 0.90625, 0.671875, 0.953125, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.234375, 0.6875, 0.90625, 0.265625, 0.765625, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.28125, 0.78125, 0.90625, 0.328125, 0.828125, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.71875, 0.859375, 0.90625, 0.765625, 0.90625, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.171875, 0.625, 0.90625, 0.21875, 0.671875, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.671875, 0.78125, 0.90625, 0.71875, 0.828125, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.453125, 0.78125, 0.90625, 0.546875, 0.8125, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.4375, 0.96875, 0.90625, 0.5625, 1, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.15625, 0.671875, 0.90625, 0.234375, 0.765625, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.28125, 0.828125, 0.90625, 0.390625, 0.921875, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.203125, 0.765625, 0.90625, 0.28125, 0.859375, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.234375, 0, 0.953125, 0.765625, 0.828125, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.140625, 0, 0.953125, 0.234375, 0.734375, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.765625, 0, 0.953125, 0.859375, 0.734375, 1), BooleanBiFunction.OR);

        return shape;
    }

    public static VoxelShape makeShapeSouthUp(){
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.046875, 0, 0, 0.15625, 0.578125, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.828125, 0.578125, 0, 0.90625, 0.671875, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.15625, 0.671875, 0, 0.234375, 0.765625, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.28125, 0.828125, 0, 0.390625, 0.921875, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.203125, 0.765625, 0, 0.28125, 0.859375, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.09375, 0.578125, 0, 0.171875, 0.671875, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.84375, 0, 0, 0.953125, 0.578125, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.390625, 0.8125, 0, 0.609375, 0.96875, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.234375, 0.6875, 0, 0.265625, 0.765625, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.0625, 0.578125, 0, 0.09375, 0.640625, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.90625, 0.578125, 0, 0.9375, 0.640625, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.171875, 0.625, 0, 0.21875, 0.671875, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.84375, 0.671875, 0, 0.890625, 0.734375, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.796875, 0.765625, 0, 0.828125, 0.828125, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.71875, 0.859375, 0, 0.765625, 0.90625, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.109375, 0.671875, 0, 0.15625, 0.734375, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.609375, 0.921875, 0, 0.671875, 0.953125, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.171875, 0.765625, 0, 0.203125, 0.828125, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.328125, 0.921875, 0, 0.390625, 0.953125, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.734375, 0.6875, 0, 0.765625, 0.765625, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.671875, 0.78125, 0, 0.71875, 0.828125, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.234375, 0.859375, 0, 0.28125, 0.90625, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.78125, 0.625, 0, 0.828125, 0.671875, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.28125, 0.78125, 0, 0.328125, 0.828125, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.453125, 0.78125, 0, 0.546875, 0.8125, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.4375, 0.96875, 0, 0.5625, 1, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.765625, 0.671875, 0, 0.84375, 0.765625, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.609375, 0.828125, 0, 0.71875, 0.921875, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.71875, 0.765625, 0, 0.796875, 0.859375, 0.09375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.234375, 0, 0, 0.765625, 0.828125, 0.046875), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.765625, 0, 0, 0.859375, 0.734375, 0.046875), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.140625, 0, 0, 0.234375, 0.734375, 0.046875), BooleanBiFunction.OR);

        return shape;
    }

    public static VoxelShape makeShapeWestUp(){
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.909375, 0, 0.8468749999999999, 1.003125, 0.578125, 0.9562499999999999), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.909375, 0.578125, 0.09687499999999996, 1.003125, 0.671875, 0.17499999999999996), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.909375, 0.671875, 0.7687499999999999, 1.003125, 0.765625, 0.8468749999999999), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.909375, 0.828125, 0.6124999999999999, 1.003125, 0.921875, 0.7218749999999999), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.909375, 0.765625, 0.7218749999999999, 1.003125, 0.859375, 0.7999999999999999), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.909375, 0.578125, 0.8312499999999999, 1.003125, 0.671875, 0.9093749999999999), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.909375, 0, 0.049999999999999954, 1.003125, 0.578125, 0.15937499999999996), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.909375, 0.8125, 0.39374999999999993, 1.003125, 0.96875, 0.6124999999999999), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.909375, 0.6875, 0.7374999999999999, 1.003125, 0.765625, 0.7687499999999999), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.909375, 0.578125, 0.9093749999999999, 1.003125, 0.640625, 0.9406249999999999), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.909375, 0.578125, 0.06562499999999996, 1.003125, 0.640625, 0.09687499999999996), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.909375, 0.625, 0.7843749999999999, 1.003125, 0.671875, 0.8312499999999999), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.909375, 0.671875, 0.11249999999999996, 1.003125, 0.734375, 0.15937499999999996), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.909375, 0.765625, 0.17499999999999996, 1.003125, 0.828125, 0.20624999999999996), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.909375, 0.859375, 0.23749999999999996, 1.003125, 0.90625, 0.28437499999999993), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.909375, 0.671875, 0.8468749999999999, 1.003125, 0.734375, 0.8937499999999999), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.909375, 0.921875, 0.33124999999999993, 1.003125, 0.953125, 0.39374999999999993), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.909375, 0.765625, 0.7999999999999999, 1.003125, 0.828125, 0.8312499999999999), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.909375, 0.921875, 0.6124999999999999, 1.003125, 0.953125, 0.6749999999999999), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.909375, 0.6875, 0.23749999999999996, 1.003125, 0.765625, 0.26874999999999993), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.909375, 0.78125, 0.28437499999999993, 1.003125, 0.828125, 0.33124999999999993), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.909375, 0.859375, 0.7218749999999999, 1.003125, 0.90625, 0.7687499999999999), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.909375, 0.625, 0.17499999999999996, 1.003125, 0.671875, 0.22187499999999996), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.909375, 0.78125, 0.6749999999999999, 1.003125, 0.828125, 0.7218749999999999), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.909375, 0.78125, 0.45624999999999993, 1.003125, 0.8125, 0.5499999999999999), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.909375, 0.96875, 0.44062499999999993, 1.003125, 1, 0.5656249999999999), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.909375, 0.671875, 0.15937499999999996, 1.003125, 0.765625, 0.23749999999999996), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.909375, 0.828125, 0.28437499999999993, 1.003125, 0.921875, 0.39374999999999993), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.909375, 0.765625, 0.20624999999999996, 1.003125, 0.859375, 0.28437499999999993), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.95625, 0, 0.23749999999999996, 1.003125, 0.828125, 0.7687499999999999), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.95625, 0, 0.14374999999999996, 1.003125, 0.734375, 0.23749999999999996), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.95625, 0, 0.7687499999999999, 1.003125, 0.734375, 0.8624999999999999), BooleanBiFunction.OR);

        return shape;
    }

    public static VoxelShape makeShapeEastUp(){
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(-0.0031249999999999993, 0, 0.8468749999999999, 0.09062499999999998, 0.578125, 0.9562499999999999), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(-0.0031249999999999993, 0.578125, 0.09687499999999996, 0.09062499999999998, 0.671875, 0.17499999999999996), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(-0.0031249999999999993, 0.671875, 0.7687499999999999, 0.09062499999999998, 0.765625, 0.8468749999999999), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(-0.0031249999999999993, 0.828125, 0.6124999999999999, 0.09062499999999998, 0.921875, 0.7218749999999999), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(-0.0031249999999999993, 0.765625, 0.7218749999999999, 0.09062499999999998, 0.859375, 0.7999999999999999), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(-0.0031249999999999993, 0.578125, 0.8312499999999999, 0.09062499999999998, 0.671875, 0.9093749999999999), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(-0.0031249999999999993, 0, 0.049999999999999954, 0.09062499999999998, 0.578125, 0.15937499999999996), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(-0.0031249999999999993, 0.8125, 0.39374999999999993, 0.09062499999999998, 0.96875, 0.6124999999999999), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(-0.0031249999999999993, 0.6875, 0.7374999999999999, 0.09062499999999998, 0.765625, 0.7687499999999999), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(-0.0031249999999999993, 0.578125, 0.9093749999999999, 0.09062499999999998, 0.640625, 0.9406249999999999), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(-0.0031249999999999993, 0.578125, 0.06562499999999996, 0.09062499999999998, 0.640625, 0.09687499999999996), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(-0.0031249999999999993, 0.625, 0.7843749999999999, 0.09062499999999998, 0.671875, 0.8312499999999999), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(-0.0031249999999999993, 0.671875, 0.11249999999999996, 0.09062499999999998, 0.734375, 0.15937499999999996), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(-0.0031249999999999993, 0.765625, 0.17499999999999996, 0.09062499999999998, 0.828125, 0.20624999999999996), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(-0.0031249999999999993, 0.859375, 0.23749999999999996, 0.09062499999999998, 0.90625, 0.28437499999999993), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(-0.0031249999999999993, 0.671875, 0.8468749999999999, 0.09062499999999998, 0.734375, 0.8937499999999999), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(-0.0031249999999999993, 0.921875, 0.33124999999999993, 0.09062499999999998, 0.953125, 0.39374999999999993), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(-0.0031249999999999993, 0.765625, 0.7999999999999999, 0.09062499999999998, 0.828125, 0.8312499999999999), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(-0.0031249999999999993, 0.921875, 0.6124999999999999, 0.09062499999999998, 0.953125, 0.6749999999999999), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(-0.0031249999999999993, 0.6875, 0.23749999999999996, 0.09062499999999998, 0.765625, 0.26874999999999993), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(-0.0031249999999999993, 0.78125, 0.28437499999999993, 0.09062499999999998, 0.828125, 0.33124999999999993), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(-0.0031249999999999993, 0.859375, 0.7218749999999999, 0.09062499999999998, 0.90625, 0.7687499999999999), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(-0.0031249999999999993, 0.625, 0.17499999999999996, 0.09062499999999998, 0.671875, 0.22187499999999996), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(-0.0031249999999999993, 0.78125, 0.6749999999999999, 0.09062499999999998, 0.828125, 0.7218749999999999), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(-0.0031249999999999993, 0.78125, 0.45624999999999993, 0.09062499999999998, 0.8125, 0.5499999999999999), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(-0.0031249999999999993, 0.96875, 0.44062499999999993, 0.09062499999999998, 1, 0.5656249999999999), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(-0.0031249999999999993, 0.671875, 0.15937499999999996, 0.09062499999999998, 0.765625, 0.23749999999999996), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(-0.0031249999999999993, 0.828125, 0.28437499999999993, 0.09062499999999998, 0.921875, 0.39374999999999993), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(-0.0031249999999999993, 0.765625, 0.20624999999999996, 0.09062499999999998, 0.859375, 0.28437499999999993), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(-0.0031249999999999993, 0, 0.23749999999999996, 0.04375, 0.828125, 0.7687499999999999), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(-0.0031249999999999993, 0, 0.14374999999999996, 0.04375, 0.734375, 0.23749999999999996), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(-0.0031249999999999993, 0, 0.7687499999999999, 0.04375, 0.734375, 0.8624999999999999), BooleanBiFunction.OR);

        return shape;
    }


    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction facing = state.get(FACING);
        DoubleBlockHalf half = state.get(HALF);

        if (half == DoubleBlockHalf.LOWER) {
            switch (facing) {
                case SOUTH -> { return LOWER_SOUTH; }
                case WEST  -> { return LOWER_WEST; }
                case EAST  -> { return LOWER_EAST; }
                default    -> { return LOWER_NORTH; }
            }
        } else {
            // UPPER half bounding boxes
            switch (facing) {
                case SOUTH -> { return UPPER_SOUTH; }
                case WEST  -> { return UPPER_WEST; }
                case EAST  -> { return UPPER_EAST; }
                default    -> { return UPPER_NORTH; }
            }
        }
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction facing = state.get(FACING);
        DoubleBlockHalf half = state.get(HALF);

        if (half == DoubleBlockHalf.LOWER) {
            switch (facing) {
                case SOUTH -> { return LOWER_SOUTH; }
                case WEST  -> { return LOWER_WEST; }
                case EAST  -> { return LOWER_EAST; }
                default    -> { return LOWER_NORTH; }
            }
        } else {
            // UPPER half bounding boxes
            switch (facing) {
                case SOUTH -> { return UPPER_SOUTH; }
                case WEST  -> { return UPPER_WEST; }
                case EAST  -> { return UPPER_EAST; }
                default    -> { return UPPER_NORTH; }
            }
        }
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        if (state.get(HALF) == DoubleBlockHalf.LOWER) {
            return new MirrorBlockEntity(pos, state);
        }
        return null;
    }
}
