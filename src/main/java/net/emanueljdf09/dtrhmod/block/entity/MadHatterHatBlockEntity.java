package net.emanueljdf09.dtrhmod.block.entity;

import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.emanueljdf09.dtrhmod.block.ModBlockEntities;
import net.emanueljdf09.dtrhmod.block.ModBlocks;
import net.emanueljdf09.dtrhmod.block.custom.MadHatterHatBlock;
import net.emanueljdf09.dtrhmod.recipe.HatRitualRecipe;
import net.emanueljdf09.dtrhmod.util.TeleportUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MadHatterHatBlockEntity extends BlockEntity implements GeoBlockEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation ACTIVE = RawAnimation.begin().thenPlay("active");
    private static final RawAnimation FAIL = RawAnimation.begin().thenPlay("fail");
    private static final RawAnimation PORTAL = RawAnimation.begin().thenPlay("success").thenLoop("spinning");
    private static final RawAnimation STOP = RawAnimation.begin().thenPlay("stopping");

    private boolean playFailAnimation = false;
    private boolean playStopAnimation = false;
    private int animationTimer = 0;

    private UUID portalOwnerUuid;
    private int ritualTicks = 0;
    private int portalCooldownTicks = 300;
    private boolean isSpinning = false;
    private boolean isRecipeValid = false;

    private RegistryKey<World> originDimension;
    private BlockPos originPos;
    private RegistryKey<World> targetDimension;
    private boolean isInstanceDimension;
    private Identifier structureId;

    private ItemStack storedRecipeItem = ItemStack.EMPTY;

    public enum AnimState {
        NONE,
        FAIL,
        STOP
    }

    private AnimState currentAnimState = AnimState.NONE;
    private int animTicks = 0;

    public MadHatterHatBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MAD_HATTER_HAT, pos, state);
    }

    public void setPortalOwnerUuid(UUID ownerUuid) {
        this.portalOwnerUuid = ownerUuid;
        markDirty();
    }

    public UUID getPortalOwnerUuid() {
        return this.portalOwnerUuid;
    }

    public RegistryKey<World> getTargetDimension() {
        return this.targetDimension;
    }

    public boolean isInstanceDimension() {
        return this.isInstanceDimension;
    }

    public Identifier getStructureId() {
        return this.structureId;
    }

    public RegistryKey<World> getOriginDimension() {
        return this.originDimension;
    }

    public static void tick(World world, BlockPos pos, BlockState state, MadHatterHatBlockEntity entity) {
        if (world.isClient) return;

        if (entity.currentAnimState == AnimState.STOP) {
            entity.animTicks++;
            if (entity.animTicks > 100) {

                world.breakBlock(pos, false);
                return;
            }
        }

        if (entity.currentAnimState == AnimState.FAIL) {
            entity.animTicks++;
            if (entity.animTicks == 40) {
                entity.spawnBurpedItems(world, pos);
            }
            if (entity.animTicks > 80) {
                entity.currentAnimState = AnimState.NONE;
                entity.animTicks = 0;

                var manager = entity.getAnimatableInstanceCache().getManagerForId(entity.hashCode());
                if (manager != null) {
                    var controller = manager.getAnimationControllers().get("controller");
                    if (controller != null) {
                        controller.stop();
                        controller.forceAnimationReset();
                    }
                }

                entity.markDirty();
            }
        }

        MadHatterHatBlock.HatState currentState = state.get(MadHatterHatBlock.STATE);

        if (currentState == MadHatterHatBlock.HatState.PORTAL) {
            if (entity.isInstanceDimension) {
                if (entity.targetDimension != null) {
                    var targetWorld = world.getServer().getWorld(entity.targetDimension);
                    int playerCount = (targetWorld != null) ? targetWorld.getPlayers().size() : 0;

                    if (playerCount > 0) {
                        entity.portalCooldownTicks = 300;
                    } else {
                        entity.portalCooldownTicks--;
                    }
                }
            } else {
                entity.portalCooldownTicks--;
            }

            if (entity.portalCooldownTicks <= 0) {
                entity.expirePersistentPortalAndRefund(world, pos);
                return;
            }
        }

        if (!entity.isSpinning && currentState == MadHatterHatBlock.HatState.IDLE) {
            Box box = new Box(pos).expand(0.3, 0.5, 0.3);
            List<ItemEntity> items = world.getEntitiesByClass(ItemEntity.class, box, EntityPredicates.VALID_ENTITY);

            if (!items.isEmpty()) {
                boolean hasRedstone = items.stream().anyMatch(item -> item.getStack().isOf(Items.REDSTONE));

                if (hasRedstone) {
                    for (ItemEntity item : items) {
                        if (item.getStack().isOf(Items.REDSTONE)) {
                            item.getStack().decrement(1);
                            if (item.getStack().isEmpty()) {
                                item.discard();
                            }
                            break;
                        }
                    }

                    entity.evaluateRecipeAndStructure(world, pos, items);

                    if (entity.isRecipeValid) {
                        var manager = entity.getAnimatableInstanceCache().getManagerForId(entity.hashCode());
                        if (manager != null) {
                            var controller = manager.getAnimationControllers().get("controller");
                            if (controller != null) {
                                controller.stop();
                                controller.forceAnimationReset();
                            }
                        }

                        entity.currentAnimState = AnimState.NONE;
                        entity.portalCooldownTicks = 300;
                        world.setBlockState(pos, state.with(MadHatterHatBlock.STATE, MadHatterHatBlock.HatState.PORTAL));
                        world.playSound(null, pos, SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.BLOCKS, 1.0f, 1.0f);
                    } else {
                        world.playSound(null, pos, SoundEvents.ENTITY_DONKEY_DEATH, SoundCategory.BLOCKS, 1.0f, 0.5f);
                        entity.startFailSequence(world, pos);
                    }
                }
            }
        }
    }

    private void evaluateRecipeAndStructure(World world, BlockPos pos, List<ItemEntity> items) {
        this.originDimension = world.getRegistryKey();
        this.originPos = pos;
        this.isRecipeValid = false;

        if (!checkStructure(world, pos)) {
            return;
        }

        SimpleInventory inventory = new SimpleInventory(items.size());
        for (int i = 0; i < items.size(); i++) {
            inventory.setStack(i, items.get(i).getStack());
        }

        Optional<HatRitualRecipe> match = world.getRecipeManager()
                .getFirstMatch(HatRitualRecipe.Type.INSTANCE, inventory, world);

        if (match.isPresent()) {
            HatRitualRecipe recipe = match.get();
            this.isRecipeValid = true;
            this.targetDimension = RegistryKey.of(RegistryKeys.WORLD, recipe.getTargetDimension());
            this.isInstanceDimension = recipe.isInstanceDimension();
            this.structureId = recipe.getStructureId();

            this.storedRecipeItem = items.get(0).getStack().copy();

            markDirty();
            consumeItems(items);
        } else {
            this.isRecipeValid = false;
        }
    }

    private void expirePersistentPortalAndRefund(World world, BlockPos pos) {
        if (this.currentAnimState == AnimState.STOP) return;

        this.currentAnimState = AnimState.STOP;
        this.animTicks = 0;
        markDirty();

        triggerAnim("controller", "stop_anim");
        world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1.0f, 0.5f);
    }

    private boolean checkStructure(World world, BlockPos pos) {

        if (!world.getBlockState(pos.down()).isOf(ModBlocks.BB_LOG)) {
            return false;
        }


        BlockPos[] surround = { pos.north(), pos.south(), pos.east(), pos.west() };
        for (BlockPos p : surround) {
            if (!world.getBlockState(p).isIn(BlockTags.CANDLES)) {
                return false;
            }
        }

        return true;
    }


    private void consumeItems(List<ItemEntity> items) {
        for (ItemEntity item : items) {
            item.discard();
        }
    }

    private void startFailSequence(World world, BlockPos pos) {
        Box box = new Box(pos).expand(0.3, 0.5, 0.3);
        List<ItemEntity> items = world.getEntitiesByClass(ItemEntity.class, box, EntityPredicates.VALID_ENTITY);

        for (ItemEntity item : items) {
            item.discard();
        }

        this.currentAnimState = AnimState.FAIL;
        this.animTicks = 0;

        var manager = this.getAnimatableInstanceCache().getManagerForId(this.hashCode());
        if (manager != null) {
            var controller = manager.getAnimationControllers().get("controller");
            if (controller != null) {
                controller.stop();
                controller.forceAnimationReset();
            }
        }

        triggerAnim("controller", "fail_anim");
        markDirty();
    }

    private void spawnBurpedItems(World world, BlockPos pos) {
        ItemStack penaltyStack = new ItemStack(Items.ROTTEN_FLESH);

        ItemEntity burpedItem = new ItemEntity(
                world,
                pos.getX() + 0.5,
                pos.getY() + 1.0,
                pos.getZ() + 0.5,
                penaltyStack
        );

        burpedItem.setVelocity(
                (world.random.nextDouble() - 0.5) * 0.3,
                0.4,
                (world.random.nextDouble() - 0.5) * 0.3
        );

        world.spawnEntity(burpedItem);
        world.playSound(null, pos, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.BLOCKS, 1.0f, 1.0f);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if (originDimension != null) nbt.putString("OriginDim", originDimension.getValue().toString());
        if (originPos != null) nbt.put("OriginPos", NbtHelper.fromBlockPos(originPos));
        if (portalOwnerUuid != null) nbt.putUuid("PortalOwner", portalOwnerUuid);
        if (targetDimension != null) nbt.putString("TargetDim", targetDimension.getValue().toString());
        if (structureId != null) nbt.putString("StructureId", structureId.toString());
        if (!storedRecipeItem.isEmpty()) nbt.put("StoredRecipeItem", storedRecipeItem.writeNbt(new NbtCompound()));
        nbt.putBoolean("IsInstance", isInstanceDimension);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (nbt.contains("OriginDim")) {
            originDimension = RegistryKey.of(RegistryKeys.WORLD, new Identifier(nbt.getString("OriginDim")));
        }
        if (nbt.contains("OriginPos")) {
            originPos = NbtHelper.toBlockPos(nbt.getCompound("OriginPos"));
        }
        if (nbt.containsUuid("PortalOwner")) {
            portalOwnerUuid = nbt.getUuid("PortalOwner");
        }
        if (nbt.contains("TargetDim")) {
            targetDimension = RegistryKey.of(RegistryKeys.WORLD, new Identifier(nbt.getString("TargetDim")));
        }
        if (nbt.contains("StructureId")) {
            structureId = new Identifier(nbt.getString("StructureId"));
        }
        if (nbt.contains("StoredRecipeItem")) {
            storedRecipeItem = ItemStack.fromNbt(nbt.getCompound("StoredRecipeItem"));
        }
        isInstanceDimension = nbt.getBoolean("IsInstance");
    }

    public void setTargetDimension(RegistryKey<World> targetDimension) {
        this.targetDimension = targetDimension;
    }

    public void setTargetPosition(BlockPos targetPos) {
        this.originPos = targetPos;
    }

    public void setInstanceDimension(boolean isInstanceDimension) {
        this.isInstanceDimension = isInstanceDimension;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, state -> {
            MadHatterHatBlock.HatState hatState = getCachedState().get(MadHatterHatBlock.STATE);

            return switch (hatState) {
                case PORTAL -> state.setAndContinue(PORTAL);
                case IDLE -> {
                    boolean inStructure = checkStructure(getWorld(), getPos());
                    yield inStructure ? state.setAndContinue(ACTIVE) : state.setAndContinue(IDLE);
                }
                default -> state.setAndContinue(IDLE);
            };
        })
                .triggerableAnim("fail_anim", FAIL)
                .triggerableAnim("stop_anim", STOP));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}