package net.emanueljdf09.dtrhmod.entity.custom;

import net.emanueljdf09.dtrhmod.block.ModBlocks;
import net.emanueljdf09.dtrhmod.entity.ai.goals.RunToRabbitHoleGoal;
import net.emanueljdf09.dtrhmod.entity.ai.goals.WanderAroundHoleGoal;
import net.emanueljdf09.dtrhmod.util.ModComponents;
import net.emanueljdf09.dtrhmod.util.components.ProgressionComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;


public class WhiteRabbitEntity extends PathAwareEntity implements GeoEntity {
    protected static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.white_rabbit.idle");
    protected static final RawAnimation IDLEBREAK = RawAnimation.begin().thenLoop("animation.white_rabbit.idle2");
     protected static final RawAnimation RUN = RawAnimation.begin().thenLoop("animation.white_rabbit.run");
    protected static final RawAnimation GREET = RawAnimation.begin().thenLoop("animation.white_rabbit.greet");


    private static final TrackedData<Optional<BlockPos>> RABBIT_HOLE_POS = DataTracker.registerData(WhiteRabbitEntity.class, TrackedDataHandlerRegistry.OPTIONAL_BLOCK_POS);
    private static final TrackedData<Boolean> IS_RETURNING = DataTracker.registerData(WhiteRabbitEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> IS_GREETING = DataTracker.registerData(WhiteRabbitEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private int greetTicks = 0;

    public WhiteRabbitEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return PathAwareEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3f)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0f)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 40.0f)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.7f);

    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(RABBIT_HOLE_POS, Optional.empty());
        this.dataTracker.startTracking(IS_RETURNING, false);
        this.dataTracker.startTracking(IS_GREETING, false);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new RunToRabbitHoleGoal(this));

        this.goalSelector.add(2, new LookAtEntityGoal(this, PlayerEntity.class, 2.0f, 1.0f)); // 1.0f means 100% chance to activate when in range

        this.goalSelector.add(3, new WanderAroundHoleGoal(this, 30));

        this.goalSelector.add(4, new LookAtEntityGoal(this, PlayerEntity.class, 6.0f));
        this.goalSelector.add(5, new LookAroundGoal(this));
    }

    @Override
    public void tickMovement() {
        super.tickMovement();

        if (!this.getWorld().isClient) {
            RegistryKey<World> currentWorldKey = this.getWorld().getRegistryKey();

            if (currentWorldKey == World.OVERWORLD) {

                if (getRabbitHolePos().isEmpty()) {
                    BlockPos foundHole = findNearbyRabbitHole();
                    if (foundHole != null) {
                        setRabbitHolePos(foundHole);
                    } else {
                        setRabbitHolePos(this.getBlockPos());
                    }
                }

                // GREETING TRACKING & LOOKING LOGIC
                if (isGreeting()) {
                    greetTicks++;

                    // Force the rabbit to face the closest player while it greets them
                    PlayerEntity closestPlayer = this.getWorld().getClosestPlayer(this, 5.0D);
                    if (closestPlayer != null) {
                        this.getLookControl().lookAt(closestPlayer, 30.0F, 30.0F);
                    }

                    if (greetTicks >= 40) { // 2 seconds
                        setGreeting(false);
                        greetTicks = 0;
                        setReturning(true);
                    }
                }

                if (isReturning() && getRabbitHolePos().isPresent()) {
                    if (this.getBlockPos().isWithinDistance(getRabbitHolePos().get(), 1.5)) {

                        if (this.getWorld() instanceof net.minecraft.server.world.ServerWorld serverWorld) {
                            serverWorld.spawnParticles(
                                    ParticleTypes.CAMPFIRE_COSY_SMOKE, // A nice thick, cartoony cloud
                                    this.getX(), this.getY() + 0.5D, this.getZ(),
                                    15, // Number of particles to spawn
                                    0.2D, 0.2D, 0.2D, // Random spread/radius around the rabbit
                                    0.05D // Speed of the smoke floating away
                            );

                            serverWorld.playSound(
                                    null, this.getX(), this.getY(), this.getZ(),
                                    net.minecraft.sound.SoundEvents.ENTITY_CHICKEN_EGG, // High-pitched pop sound
                                    net.minecraft.sound.SoundCategory.NEUTRAL,
                                    1.0F, 1.5F
                            );
                        }

                        this.discard(); // Safely delete the rabbit after spawning the cloud
                    }
                }
            }
        }
    }

    private BlockPos findNearbyRabbitHole() {
        BlockPos entityPos = this.getBlockPos();
        int radius = 15;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -4; y <= 4; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos checkPos = entityPos.add(x, y, z);
                    if (this.getWorld().getBlockState(checkPos).isOf(ModBlocks.RABBIT_HOLE)) {
                        return checkPos;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getWorld().isClient()) {

            if (this.isReturning()) {

                this.getWorld().addParticle(
                        ParticleTypes.POOF,
                        this.getX(), this.getY(), this.getZ(),
                        0.0D, 0.0D, 0.0D
                );

                if (this.random.nextFloat() < 0.4f) {
                    this.getWorld().addParticle(
                            ParticleTypes.SPLASH,
                            this.getX() + (this.random.nextDouble() - 0.5D) * this.getWidth(),
                            this.getEyeY() + 0.2D,
                            this.getZ() + (this.random.nextDouble() - 0.5D) * this.getWidth(),
                            0.0D, 0.1D, 0.0D
                    );
                }
            }
        }
    }

    @Override
    public ActionResult interactAt(PlayerEntity player, Vec3d hitPos, Hand hand) {
        if (hand != Hand.MAIN_HAND || this.getWorld().isClient) {
            return ActionResult.PASS;
        }

        RegistryKey<World> currentWorldKey = this.getWorld().getRegistryKey();

        if (player instanceof ServerPlayerEntity serverPlayer) {
            ProgressionComponent component = ModComponents.PROGRESSION_COMPONENT.get(serverPlayer);

            // --- OVERWORLD LOGIC (The Guide Sequence) ---
            if (currentWorldKey == World.OVERWORLD) {
                if (isReturning() || isGreeting()) return ActionResult.PASS;

                if (!component.hasMetWhiteRabbit()) {
                    player.sendMessage(Text.translatable("entity.dtrhmod.white_rabbit.greet_msg"), true);

                    component.setMetWhiteRabbit(true);
                    component.setMetInOverworld(true); // Player officially followed the intended intro!
                    ModComponents.PROGRESSION_COMPONENT.sync(serverPlayer);

                    setGreeting(true);
                    this.getNavigation().stop();
                    return ActionResult.SUCCESS;
                } else {
                    player.sendMessage(Text.translatable("entity.dtrhmod.white_rabbit.busy_msg"), true);
                    return ActionResult.SUCCESS;
                }
            }

            // --- WONDERLAND / STORY LOGIC ---
            else {
                // SCENARIO A: Player met the rabbit in the Overworld first
                if (component.hasMetInOverworld()) {
                    player.sendMessage(Text.translatable("entity.dtrhmod.white_rabbit.wonderland_remember_msg"), false);
                }
                // SCENARIO B: Player skipped the rabbit and fell in the hole blindly
                else {
                    player.sendMessage(Text.translatable("entity.dtrhmod.white_rabbit.wonderland_stranger_msg"), false);

                    // Mark them as met now so future interactions change
                    component.setMetWhiteRabbit(true);
                    ModComponents.PROGRESSION_COMPONENT.sync(serverPlayer);
                }
                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 4, this::predicate));
    }

    protected <E extends WhiteRabbitEntity> PlayState predicate(final AnimationState<E> event) {
        if (this.isGreeting()) {
            event.getController().setAnimationSpeed(1.0D);
            return event.setAndContinue(GREET);
        }

        if (this.isReturning()) {
            event.getController().setAnimationSpeed(1.5D);
            return event.setAndContinue(RUN);
        }

        if (event.isMoving() || this.getNavigation().isFollowingPath() || this.getVelocity().horizontalLengthSquared() > 0.002) {
            event.getController().setAnimationSpeed(1.0D);
            return event.setAndContinue(RUN);
        }
        event.getController().setAnimationSpeed(1.0D);

        return event.setAndContinue(this.random.nextFloat() < 0.3f ? IDLEBREAK : IDLE);
    }

    public Optional<BlockPos> getRabbitHolePos() { return this.dataTracker.get(RABBIT_HOLE_POS); }
    public void setRabbitHolePos(BlockPos pos) { this.dataTracker.set(RABBIT_HOLE_POS, Optional.ofNullable(pos)); }
    public boolean isReturning() { return this.dataTracker.get(IS_RETURNING); }
    public void setReturning(boolean returning) { this.dataTracker.set(IS_RETURNING, returning); }
    public boolean isGreeting() { return this.dataTracker.get(IS_GREETING); }
    public void setGreeting(boolean greeting) { this.dataTracker.set(IS_GREETING, greeting); }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        getRabbitHolePos().ifPresent(blockPos -> nbt.put("RabbitHolePos", NbtHelper.fromBlockPos(blockPos)));
        nbt.putBoolean("IsReturning", isReturning());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("RabbitHolePos")) {
            setRabbitHolePos(NbtHelper.toBlockPos(nbt.getCompound("RabbitHolePos")));
        }
        setReturning(nbt.getBoolean("IsReturning"));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() { return this.cache; }
}
