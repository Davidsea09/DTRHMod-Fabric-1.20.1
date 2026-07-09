package net.emanueljdf09.dtrhmod.block.custom;

import net.emanueljdf09.dtrhmod.block.entity.ExteriorDoorEntity;
import net.emanueljdf09.dtrhmod.util.ModComponents;
import net.emanueljdf09.dtrhmod.util.components.ProgressionComponent;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ExteriorDoor extends BlockWithEntity {

    protected static final VoxelShape CLOSED_SHAPE = makeClosedShape();
    protected static final VoxelShape OPEN_SHAPE = makeOpenShape();


    public ExteriorDoor(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ExteriorDoorEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof ExteriorDoorEntity doorEntity) {
            return doorEntity.onUse(player, hand);
        }
        return ActionResult.PASS;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {

        if (context instanceof EntityShapeContext entityContext && entityContext.getEntity() instanceof PlayerEntity player) {
            if (ModComponents.PROGRESSION_COMPONENT.get(player).hasOpenedExtDoor()) {
                return OPEN_SHAPE;
            }
        }
        return CLOSED_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (context instanceof EntityShapeContext entityContext && entityContext.getEntity() instanceof PlayerEntity player) {
            ProgressionComponent component = ModComponents.PROGRESSION_COMPONENT.get(player);
            if (component.hasOpenedExtDoor()) {
                return OPEN_SHAPE;
            }
        }
        return CLOSED_SHAPE;
    }

    public static VoxelShape makeClosedShape(){
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(-0.0625, 1.0625, 0.8125, 1, 1.25, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(-0.0625, 1, 0.8125, 0.125, 1.0625, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.8125, 1, 0.8125, 1, 1.0625, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.8125, 0.8125, 0.8125, 1.1875, 1, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(-0.25, 0.8125, 0.8125, 0.125, 1, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(1, 0, 0.8125, 1.1875, 0.8125, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(-0.25, 0, 0.8125, -0.0625, 0.8125, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.125, 0.8125, 0.875, 0.8125, 1.0625, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(-0.0625, 0, 0.875, 1, 0.8125, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.03125, 0.125, 0.8125, 0.34375, 0.8125, 0.875), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.03125, 0.125, 0.8125, 0.34375, 0.8125, 0.875), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.09375, 0.375, 0.5625, 0.28125, 0.5625, 0.75), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.15625, 0.4375, 0.75, 0.21875, 0.5, 0.8125), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.09375, 0.125, 0.75, 0.28125, 0.3125, 0.8125), BooleanBiFunction.OR);

        return shape;
    }

    public static VoxelShape makeOpenShape(){
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(-0.0625, 1.0625, 0.8125, 1, 1.25, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(-0.0625, 1, 0.8125, 0.125, 1.0625, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.8125, 1, 0.8125, 1, 1.0625, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.8125, 0.8125, 0.8125, 1.1875, 1, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(-0.25, 0.8125, 0.8125, 0.125, 1, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(1, 0, 0.8125, 1.1875, 0.8125, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(-0.25, 0, 0.8125, -0.0625, 0.8125, 1), BooleanBiFunction.OR);

        return shape;
    }


}
