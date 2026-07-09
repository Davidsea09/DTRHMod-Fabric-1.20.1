package net.emanueljdf09.dtrhmod.entity.custom;

import net.emanueljdf09.dtrhmod.entity.ai.goals.StalkerGoal;
import net.emanueljdf09.dtrhmod.entity.client.models.WeepingPlayerModel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class WeepingPlayerEntity extends PathAwareEntity implements GeoEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public WeepingPlayerEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    private static final TrackedData<Integer> POSE_ID =
            DataTracker.registerData(WeepingPlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);

    private static final TrackedData<Boolean> IS_SLIM_MODEL =
            DataTracker.registerData(WeepingPlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    private static final TrackedData<Integer> SKIN_POOL_INDEX =
            DataTracker.registerData(WeepingPlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(POSE_ID, this.random.nextInt(6));
        this.dataTracker.startTracking(SKIN_POOL_INDEX, this.random.nextInt(4) - 1);
        this.dataTracker.startTracking(IS_SLIM_MODEL, false);
    }

    public int getSkinPoolIndex() {
        return this.dataTracker.get(SKIN_POOL_INDEX);
    }

    public int getPoseId() {
        return this.dataTracker.get(POSE_ID);
    }

    public boolean isSlimModel() {
        return this.dataTracker.get(IS_SLIM_MODEL);
    }

    private boolean isAiFrozen() {
        if (this.goalSelector == null) {
            return false;
        }

        return this.goalSelector.getRunningGoals()
                .anyMatch(goal -> goal.getGoal() instanceof StalkerGoal stalker && stalker.isFrozen());
    }

    @Override
    public void setYaw(float yaw) {
        if (this.isAiFrozen()) return;

        float snappedYaw = Math.round(yaw / 45.0f) * 45.0f;
        super.setYaw(snappedYaw);
        this.setBodyYaw(snappedYaw);
    }

    @Override
    public void setHeadYaw(float headYaw) {
        if (this.isAiFrozen()) return;

        float snappedHeadYaw = Math.round(headYaw / 45.0f) * 45.0f;
        super.setHeadYaw(snappedHeadYaw);
    }

    @Override
    public void setPitch(float pitch) {
        if (this.isAiFrozen()) return;
        super.setPitch(pitch);
    }

    @Override
    protected void initGoals() {
        StalkerGoal stalkerGoal = new StalkerGoal(this);

        this.goalSelector.add(0, stalkerGoal);
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return PathAwareEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.22f)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0f)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 40.0f)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.7f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6.0f);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public void pushAwayFrom(Entity entity) {

    }

    @Override
    public void tick() {
        super.tick();

        if (!this.getWorld().isClient()) {
            int poolIndex = this.getSkinPoolIndex();

            if (poolIndex == -1) {
            } else {
                boolean shouldBeSlim = WeepingPlayerModel.isPoolSkinSlim(poolIndex);
                if (this.isSlimModel() != shouldBeSlim) {
                    this.dataTracker.set(IS_SLIM_MODEL, shouldBeSlim);
                }
            }
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "poseController", 0, event -> {
            WeepingPlayerEntity entity = event.getAnimatable();
            int currentPose = entity.getPoseId();
            boolean isSlim = entity.isSlimModel();

            if (entity.getWorld().isClient() && entity.getSkinPoolIndex() == -1) {
                PlayerEntity localPlayer = MinecraftClient.getInstance().world.getClosestPlayer(entity.getX(), entity.getY(), entity.getZ(), 24.0, false);
                if (localPlayer instanceof AbstractClientPlayerEntity clientTarget) {
                    isSlim = "slim".equals(clientTarget.getModel());
                }
            }

            if (isSlim) {
                switch (currentPose) {
                    case 0 -> event.getController().setAnimation(RawAnimation.begin().thenLoop("alex_covering_eyes"));
                    case 1 -> event.getController().setAnimation(RawAnimation.begin().thenLoop("alex_attack"));
                    case 2 -> event.getController().setAnimation(RawAnimation.begin().thenLoop("alex_pointing"));
                    default -> event.getController().setAnimation(RawAnimation.begin().thenLoop("alex_covering_eyes"));
                }
            } else {
                switch (currentPose) {
                    case 0 -> event.getController().setAnimation(RawAnimation.begin().thenLoop("covering_eyes"));
                    case 1 -> event.getController().setAnimation(RawAnimation.begin().thenLoop("attack"));
                    case 2 -> event.getController().setAnimation(RawAnimation.begin().thenLoop("pointing"));
                    default -> event.getController().setAnimation(RawAnimation.begin().thenLoop("covering_eyes"));
                }
            }

            return PlayState.CONTINUE;
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
