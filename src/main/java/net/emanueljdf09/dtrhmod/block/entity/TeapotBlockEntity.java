package net.emanueljdf09.dtrhmod.block.entity;

import net.emanueljdf09.dtrhmod.block.ModBlockEntities;
import net.emanueljdf09.dtrhmod.item.ModItems;
import net.emanueljdf09.dtrhmod.menu.handler.TeapotScreenHandler;
import net.emanueljdf09.dtrhmod.recipe.TeapotRecipe;
import net.emanueljdf09.dtrhmod.util.ImplementedInventory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class TeapotBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(6, ItemStack.EMPTY);

    private static final int INPUT_SLOT_1 = 0;
    private static final int INPUT_SLOT_2 = 1;
    private static final int INPUT_SLOT_3 = 2;
    private static final int BUCKET_SLOT = 3;
    private static final int CUP_SLOT = 4;
    private static final int OUTPUT_SLOT = 5;


    protected final PropertyDelegate propertyDelegate;

    private int progress = 0;
    private int maxProgress = 200;


    public TeapotBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TEAPOT_BLOCK_ENTITY, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> TeapotBlockEntity.this.progress;
                    case 1 -> TeapotBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> TeapotBlockEntity.this.progress = value;
                    case 1 -> TeapotBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int size() {
                return 2;
            }
        };
    }


    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {

        buf.writeBlockPos(this.pos);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("container.dtrhmod.teapot");
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, this.inventory);
        this.progress = nbt.getInt("TeapotProgress");
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.inventory);
        nbt.putInt("TeapotProgress", this.progress);
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        if (world.isClient()) {
            return;
        }

        if (isOutputSlotEmptyOrRecievable() && this.hasRecipe() && isHeatSourceUnderneath(world, pos)) {
            this.increaseCraftProgress();
            markDirty(world, pos, state);

            if (hasCraftingFinished()) {
                this.craftItem();
                this.resetProgress();
            }
        } else {
            if (this.progress > 0) {
                this.resetProgress();
                markDirty(world, pos, state);
            }
        }
    }

    private boolean isHeatSourceUnderneath(World world, BlockPos pos) {
        BlockState stateBelow = world.getBlockState(pos.down());

        return stateBelow.isIn(BlockTags.FIRE)
                || stateBelow.isIn(BlockTags.CAMPFIRES)
                || stateBelow.isOf(Blocks.MAGMA_BLOCK);
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private void craftItem() {
        if (this.getWorld() == null || !this.hasRecipe()) return;

        ItemStack dynamicOutput = this.createDynamicTeaStackGrid();
        ItemStack currentOutput = this.getStack(OUTPUT_SLOT);

        if (currentOutput.isEmpty()) {
            this.setStack(OUTPUT_SLOT, dynamicOutput);
        }
        else if (currentOutput.isOf(dynamicOutput.getItem()) && ItemStack.canCombine(currentOutput, dynamicOutput)) {
            currentOutput.increment(dynamicOutput.getCount());
        }

        for (int i = 0; i < 3; i++) {
            if (!this.getStack(i).isEmpty()) {
                this.getStack(i).decrement(1);
            }
        }

        this.getStack(CUP_SLOT).decrement(1);

        ItemStack bucketStack = this.getStack(BUCKET_SLOT);
        if (bucketStack.getItem().hasRecipeRemainder()) {
            this.setStack(BUCKET_SLOT, new ItemStack(bucketStack.getItem().getRecipeRemainder()));
        } else {
            bucketStack.decrement(1);
        }
    }

    private ItemStack createDynamicTeaStackGrid() {
        ItemStack resultCup = new ItemStack(ModItems.FILLED_TEA_CUP);
        NbtCompound nbt = resultCup.getOrCreateNbt();
        NbtList effectsList = new NbtList();

        // Identify our actual fluid modifier item type from the real slot
        Item fluidItem = this.getStack(BUCKET_SLOT).getItem();

        for (int i = 0; i < 3; i++) {
            ItemStack slotStack = this.getStack(i);
            if (!slotStack.isEmpty()) {
                SimpleInventory singleSlotInv = new SimpleInventory(6);
                singleSlotInv.setStack(0, slotStack.copy());

                singleSlotInv.setStack(3, new ItemStack(net.minecraft.item.Items.WATER_BUCKET));
                singleSlotInv.setStack(4, this.getStack(CUP_SLOT).copy());

                Optional<TeapotRecipe> singleRecipe = this.getWorld().getRecipeManager()
                        .getFirstMatch(TeapotRecipe.Type.INSTANCE, singleSlotInv, this.getWorld());

                if (singleRecipe.isPresent()) {
                    for (StatusEffectInstance baseEffect : singleRecipe.get().getEffects()) {

                        int finalDuration = baseEffect.getDuration();
                        int finalAmplifier = baseEffect.getAmplifier();

                        if (fluidItem == net.minecraft.item.Items.LAVA_BUCKET) {
                            finalAmplifier = Math.max(finalAmplifier, 1);
                        } else if (fluidItem == net.minecraft.item.Items.MILK_BUCKET) {
                            finalDuration = 600;
                        }

                        StatusEffectInstance modifiedEffect = new StatusEffectInstance(
                                baseEffect.getEffectType(),
                                finalDuration,
                                finalAmplifier,
                                baseEffect.isAmbient(),
                                baseEffect.shouldShowParticles()
                        );

                        NbtCompound effectNbt = new NbtCompound();
                        modifiedEffect.writeNbt(effectNbt);
                        effectsList.add(effectNbt);
                    }
                }
            }
        }

        if (!effectsList.isEmpty()) {
            nbt.put("Effects", effectsList);
            nbt.putString("FluidUsed", Registries.ITEM.getId(fluidItem).toString());
        }
        return resultCup;
    }

    private boolean hasCraftingFinished() {
        return progress >= maxProgress;
    }

    private void increaseCraftProgress() {
        progress++;
    }

    private boolean hasRecipe() {
        if (this.getWorld() == null) return false;

        ItemStack fluidStack = this.getStack(BUCKET_SLOT);
        if (fluidStack.isEmpty() || fluidStack.isOf(net.minecraft.item.Items.BUCKET) || this.getStack(CUP_SLOT).isEmpty()) {
            return false;
        }

        boolean hasValidIngredient = false;
        for (int i = 0; i < 3; i++) {
            ItemStack slotStack = this.getStack(i);
            if (!slotStack.isEmpty()) {
                SimpleInventory singleSlotInv = new SimpleInventory(6);
                singleSlotInv.setStack(0, slotStack.copy());

                singleSlotInv.setStack(3, new ItemStack(Items.WATER_BUCKET));
                singleSlotInv.setStack(4, this.getStack(CUP_SLOT).copy());

                Optional<TeapotRecipe> singleRecipe = this.getWorld().getRecipeManager()
                        .getFirstMatch(TeapotRecipe.Type.INSTANCE, singleSlotInv, this.getWorld());

                if (singleRecipe.isPresent()) {
                    hasValidIngredient = true;
                } else {
                    return false;
                }
            }
        }

        return hasValidIngredient && isOutputSlotEmptyOrRecievable();
    }

    private boolean isOutputSlotEmptyOrRecievable() {
        return this.getStack(OUTPUT_SLOT).isEmpty() || this.getStack(OUTPUT_SLOT).getCount() < this.getStack(OUTPUT_SLOT).getMaxCount();
    }


    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new TeapotScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }
}
