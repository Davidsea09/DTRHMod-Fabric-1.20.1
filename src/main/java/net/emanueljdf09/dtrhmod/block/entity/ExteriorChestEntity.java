package net.emanueljdf09.dtrhmod.block.entity;

import net.emanueljdf09.dtrhmod.block.ModBlockEntities;
import net.emanueljdf09.dtrhmod.item.ModItems;
import net.emanueljdf09.dtrhmod.util.ClientAnimationHelper;
import net.emanueljdf09.dtrhmod.util.ModComponents;
import net.emanueljdf09.dtrhmod.util.ModEffects;
import net.emanueljdf09.dtrhmod.util.components.ProgressionComponent;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
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

public class ExteriorChestEntity extends BlockEntity implements GeoBlockEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public static final RawAnimation CLOSED = RawAnimation.begin().thenPlay("closed");
    public static final RawAnimation OPEN_NORMAL = RawAnimation.begin().thenPlay("opening_normal").thenLoop("open_empty");
    public static final RawAnimation OPEN_AND_GROW_SEQUENCE = RawAnimation.begin()
            .thenPlay("growing")
            .thenLoop("open_grown");
    public static final RawAnimation TAKE_GROWN_ITEMS = RawAnimation.begin().thenPlay("take_grown_items").thenLoop("invisible");

    public ExteriorChestEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.EXTERIOR_CHEST_ENTITY, pos, state);
    }

    public ActionResult onUse(PlayerEntity player, Hand hand) {
        assert world != null;
        if (hand != Hand.MAIN_HAND) {
            return ActionResult.PASS;
        }

        ProgressionComponent component = ModComponents.PROGRESSION_COMPONENT.get(player);

        if (component.hasOpenedExtGrownChest()) {
            return ActionResult.PASS;
        }

        if (!component.hasOpenedExtChest()) {
            if (!world.isClient) {
                player.sendMessage(Text.translatable("exterior.player.openchest"), true);
                player.giveItemStack(ModItems.EAT_ME.getDefaultStack());
                component.setOpenedExtChest(true);

                world.updateListeners(pos, getCachedState(), getCachedState(), 3);
                ModComponents.PROGRESSION_COMPONENT.sync((ServerPlayerEntity) player);
                markDirty();
            }
            return ActionResult.SUCCESS;
        }

        if (!component.hasOpenedExtGrownChest() && player.hasStatusEffect(ModEffects.GROW)) {
            if (!world.isClient) {
                player.sendMessage(Text.translatable("exterior.player.opengrownchest"), true);
                player.giveItemStack(ModItems.DRINK_ME.getDefaultStack());
                player.giveItemStack(ModItems.EXTERIOR_KEY.getDefaultStack());
                component.setOpenedExtGrownChest(true);

                world.updateListeners(pos, getCachedState(), getCachedState(), 3);
                ModComponents.PROGRESSION_COMPONENT.sync((ServerPlayerEntity) player);
                markDirty();
            } else {
                triggerAnim("chest_controller", "take_grown_items");
            }
            return ActionResult.SUCCESS;
        }

        if (!component.hasOpenedExtGrownChest() && !player.hasStatusEffect(ModEffects.GROW)) {
            if (!world.isClient) {
                player.sendMessage(Text.translatable("exterior.player.failchestGrow"), true);
            }
            return ActionResult.FAIL;
        }

        if (!world.isClient) {
            player.sendMessage(Text.translatable("exterior.player.failchest"), true);
        }
        return ActionResult.CONSUME;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "chest_controller", 0, state -> {
            if (this.getWorld() != null && this.getWorld().isClient()) {
                return ClientAnimationHelper.handleChestAnimation(state, CLOSED, OPEN_NORMAL, TAKE_GROWN_ITEMS, OPEN_AND_GROW_SEQUENCE);
            }
            return state.setAndContinue(CLOSED);
        })
                .triggerableAnim("open_and_grow", OPEN_AND_GROW_SEQUENCE)
                .triggerableAnim("take_grown_items", TAKE_GROWN_ITEMS));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}