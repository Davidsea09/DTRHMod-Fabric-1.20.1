package net.emanueljdf09.dtrhmod.block.entity;

import net.emanueljdf09.dtrhmod.block.ModBlockEntities;
import net.emanueljdf09.dtrhmod.item.ModItems;
import net.emanueljdf09.dtrhmod.util.ClientAnimationHelper;
import net.emanueljdf09.dtrhmod.util.ModComponents;
import net.emanueljdf09.dtrhmod.util.ModEffects;
import net.emanueljdf09.dtrhmod.util.components.ProgressionComponent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class ExteriorDoorEntity extends BlockEntity implements GeoBlockEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.doorhandle.idle");
    private static final RawAnimation OPENING = RawAnimation.begin().thenPlay("animation.doorhandle.opening").thenLoop("animation.doorhandle.open_idle");
    private static final RawAnimation FAIL = RawAnimation.begin().thenPlay("animation.doorhandle.fail").thenLoop("animation.doorhandle.idle");
    private static final RawAnimation OPEN_IDLE = RawAnimation.begin().thenLoop("animation.doorhandle.open_idle");

    public ExteriorDoorEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.EXTERIOR_DOOR_ENTITY, pos, state);
    }

    public ActionResult onUse(PlayerEntity player, Hand hand) {
        assert world != null;

        if (hand != Hand.MAIN_HAND) {
            return ActionResult.PASS;
        }

        ProgressionComponent component = ModComponents.PROGRESSION_COMPONENT.get(player);
        ItemStack stack = player.getStackInHand(hand);

        if (component.hasOpenedExtDoor()) {
            return ActionResult.PASS;
        }

        if (stack.isOf(ModItems.EXTERIOR_KEY) && player.hasStatusEffect(ModEffects.SHRINK)) {
            if (!world.isClient) {
                component.setOpenedExtDoor(true);
                world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
                player.sendMessage(Text.translatable("exterior.player.opendoor"), true);
            } else {
                triggerAnim("door_controller", "opening");
            }
            return ActionResult.SUCCESS;
        }

        else {
            if (!world.isClient) {
                if (!component.hasOpenedExtGrownChest()) {
                    player.sendMessage(Text.translatable("exterior.player.faileddoorgrowth"), true);
                } else {
                    player.sendMessage(Text.translatable("exterior.player.faileddoor"), true);
                }
            } else {
                triggerAnim("door_controller", "fail");
            }
            return ActionResult.CONSUME;
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "door_controller", 0, state -> {
            if (this.getWorld() != null && this.getWorld().isClient()) {
                return ClientAnimationHelper.handleDoorAnimation(state, IDLE, OPEN_IDLE);
            }
            return state.setAndContinue(IDLE);
        })
                .triggerableAnim("opening", OPENING)
                .triggerableAnim("fail", FAIL));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}


