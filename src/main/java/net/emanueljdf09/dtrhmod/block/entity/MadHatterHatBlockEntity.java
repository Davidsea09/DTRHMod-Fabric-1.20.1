package net.emanueljdf09.dtrhmod.block.entity;

import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.emanueljdf09.dtrhmod.block.ModBlockEntities;
import net.emanueljdf09.dtrhmod.block.ModBlocks;
import net.emanueljdf09.dtrhmod.block.custom.MadHatterHatBlock;
import net.emanueljdf09.dtrhmod.item.ModItems;
import net.emanueljdf09.dtrhmod.recipe.HatRitualRecipe;
import net.emanueljdf09.dtrhmod.util.TeleportUtil;
import net.emanueljdf09.dtrhmod.world.dimension.ModDimensions;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
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

    private UUID portalOwnerUuid;
    private int ritualTicks = 0;
    private int portalCooldownTicks = 300; // 15 seconds (300 ticks)
    private boolean isSpinning = false;
    private boolean isRecipeValid = false;

    private RegistryKey<World> originDimension;
    private BlockPos originPos;
    private RegistryKey<World> targetDimension;
    private boolean isInstanceDimension;
    private Identifier structureId;

    private ItemStack storedRecipeItem = ItemStack.EMPTY;

    public MadHatterHatBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MAD_HATTER_HAT, pos, state);
    }
    public static void tick(World world, BlockPos pos, BlockState state, MadHatterHatBlockEntity entity) {
        if (world.isClient) return;

        MadHatterHatBlock.HatState currentState = state.get(MadHatterHatBlock.STATE);

        // ==========================================
        // 1. PORTAL ACTIVE PHASE
        // ==========================================
        if (currentState == MadHatterHatBlock.HatState.PORTAL) {

            if (entity.isInstanceDimension) {
                // INSTANCED REALM (Storybook): Freeze 15s timer while player is inside
                if (entity.targetDimension != null) {
                    var targetWorld = world.getServer().getWorld(entity.targetDimension);
                    int playerCount = (targetWorld != null) ? targetWorld.getPlayers().size() : 0;

                    if (playerCount > 0) {
                        entity.portalCooldownTicks = 300; // Keep full 15s delay ready
                    } else {
                        entity.portalCooldownTicks--;
                    }
                }
            } else {
                // PERSISTENT REALMS (Wonderland / Overworld): Always count down 15s
                entity.portalCooldownTicks--;
            }

            // 🌟 PORTAL TIMER EXPIRED: Destroy origin hat & refund recipe item directly to owner!
            if (entity.portalCooldownTicks <= 0) {
                entity.expirePersistentPortalAndRefund(world, pos);
                return;
            }
        }

        // ==========================================
        // 2. IDLE SCANNING PHASE
        // ==========================================
        if (!entity.isSpinning && currentState == MadHatterHatBlock.HatState.IDLE) {
            Box box = new Box(pos).expand(0.3, 0.5, 0.3);
            List<ItemEntity> items = world.getEntitiesByClass(ItemEntity.class, box, EntityPredicates.VALID_ENTITY);

            if (!items.isEmpty()) {
                entity.evaluateRecipeAndStructure(world, pos, items);
                entity.isSpinning = true;
                entity.ritualTicks = 0;
                world.setBlockState(pos, state.with(MadHatterHatBlock.STATE, MadHatterHatBlock.HatState.SPINNING));
            }
        }

        // ==========================================
        // 3. SPINNING SEQUENCE PHASE
        // ==========================================
        if (entity.isSpinning) {
            entity.ritualTicks++;

            if (entity.ritualTicks >= 60) { // 3 seconds spinning animation
                entity.isSpinning = false;

                if (entity.isRecipeValid) {
                    entity.portalCooldownTicks = 300; // Reset to 15 seconds
                    world.setBlockState(pos, state.with(MadHatterHatBlock.STATE, MadHatterHatBlock.HatState.PORTAL));
                    world.playSound(null, pos, SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.BLOCKS, 1.0f, 1.0f);
                } else {
                    world.setBlockState(pos, state.with(MadHatterHatBlock.STATE, MadHatterHatBlock.HatState.IDLE));
                    world.playSound(null, pos, SoundEvents.ENTITY_DONKEY_DEATH, SoundCategory.BLOCKS, 1.0f, 0.5f);
                    entity.burpItems(world, pos);
                }
            }
        }
    }

    private void evaluateRecipeAndStructure(World world, BlockPos pos, List<ItemEntity> items) {
        this.originDimension = world.getRegistryKey();
        this.originPos = pos;

        if (!checkStructure(world, pos)) {
            this.isRecipeValid = false;
            return;
        }

        net.minecraft.inventory.SimpleInventory inventory = new net.minecraft.inventory.SimpleInventory(items.size());
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

            // Store recipe item copy for returning to inventory later
            this.storedRecipeItem = items.get(0).getStack().copy();

            PlayerEntity closestPlayer = world.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 5.0, false);
            if (closestPlayer != null) {
                this.portalOwnerUuid = closestPlayer.getUuid();
            }

            markDirty();
            consumeItems(items);
        } else {
            this.isRecipeValid = false;
        }
    }

    private void expirePersistentPortalAndRefund(World world, BlockPos pos) {
       ItemStack hatToRefund = new ItemStack(ModBlocks.MAD_HATTER_HAT.asItem());

        if (this.portalOwnerUuid != null && world.getServer() != null) {
            PlayerEntity owner = world.getServer().getPlayerManager().getPlayer(this.portalOwnerUuid);
            if (owner != null) {
                owner.getInventory().insertStack(hatToRefund);
            } else {
                world.spawnEntity(new ItemEntity(
                        world,
                        pos.getX() + 0.5,
                        pos.getY() + 0.5,
                        pos.getZ() + 0.5,
                        hatToRefund
                ));
            }
        } else {
            world.spawnEntity(new ItemEntity(
                    world,
                    pos.getX() + 0.5,
                    pos.getY() + 0.5,
                    pos.getZ() + 0.5,
                    hatToRefund
            ));
        }

        world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1.0f, 0.5f);

        world.breakBlock(pos, false);
    }

    private boolean checkStructure(World world, BlockPos pos) {
        // 1. Check block directly underneath
        if (!world.getBlockState(pos.down()).isOf(ModBlocks.BB_LOG)) {
            return false;
        }

        // 2. Check surrounding 4 cardinal blocks for candles
        BlockPos[] surround = { pos.north(), pos.south(), pos.east(), pos.west() };
        for (BlockPos p : surround) {
            if (!world.getBlockState(p).isIn(net.minecraft.registry.tag.BlockTags.CANDLES)) {
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

    private void burpItems(World world, BlockPos pos) {
        Box box = new Box(pos).expand(0.3, 0.5, 0.3);
        List<ItemEntity> items = world.getEntitiesByClass(ItemEntity.class, box, EntityPredicates.VALID_ENTITY);

        if (!items.isEmpty()) {
            // Destroy 1 item as penalty
            ItemEntity penalty = items.remove(0);
            penalty.discard();

            // Spit remaining items back out
            for (ItemEntity item : items) {
                item.setVelocity((world.random.nextDouble() - 0.5) * 0.2, 0.3, (world.random.nextDouble() - 0.5) * 0.2);
            }
        }
    }

    public void triggerTeleport(PlayerEntity player) {
        if (!(player instanceof ServerPlayerEntity serverPlayer) || targetDimension == null) return;

        if (TeleportUtil.hasTeleportCooldown(serverPlayer)) {
            return;
        }

        TeleportUtil.updateTeleportCooldown(serverPlayer);

        if (this.isInstanceDimension) {
            UUID owner = (this.portalOwnerUuid != null) ? this.portalOwnerUuid : serverPlayer.getUuid();

            Identifier targetStructure = (this.structureId != null)
                    ? this.structureId
                    : new Identifier(DownTheRabbitHole.MOD_ID, "storybook/default_structure");

            TeleportUtil.teleportToPlayerInstance(
                    serverPlayer,
                    owner,
                    this.targetDimension,
                    targetStructure,
                    this.originDimension,
                    this.pos
            );
        } else {
            TeleportUtil.teleportFromHat(serverPlayer, this.targetDimension, this.pos);
        }
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
                case SPINNING -> state.setAndContinue(RawAnimation.begin().thenLoop("animation.hat.spin"));
                case PORTAL -> state.setAndContinue(RawAnimation.begin().thenLoop("animation.hat.portal_open"));
                default -> state.setAndContinue(RawAnimation.begin().thenLoop("animation.hat.idle"));
            };
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}