package net.emanueljdf09.dtrhmod.menu.handler;

import net.emanueljdf09.dtrhmod.block.entity.TeapotBlockEntity;
import net.emanueljdf09.dtrhmod.item.ModItems;
import net.emanueljdf09.dtrhmod.menu.ModScreenHandlers;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class TeapotScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;
    private final TeapotBlockEntity teapotBlockEntity;

    public TeapotScreenHandler(int syncId, PlayerInventory inventory, PacketByteBuf buf) {
        this(syncId, inventory, inventory.player.getWorld().getBlockEntity(buf.readBlockPos()),
                new ArrayPropertyDelegate(2));
    }

    public TeapotScreenHandler(int syncId, PlayerInventory playerInventory,
                               BlockEntity blockEntity, PropertyDelegate propertyDelegate) {
        super(ModScreenHandlers.TEAPOT_SCREEN_HANDLER, syncId);
        checkSize(((Inventory) blockEntity), 6);
        this.inventory = ((Inventory) blockEntity);
        inventory.onOpen(playerInventory.player);
        this.propertyDelegate = propertyDelegate;
        this.teapotBlockEntity = ((TeapotBlockEntity) blockEntity);

        this.addSlot(new Slot(inventory, 0, 27, 34));
        this.addSlot(new Slot(inventory, 1, 45, 34));
        this.addSlot(new Slot(inventory, 2, 63, 34));

        this.addSlot(new Slot(inventory, 3, 27, 12) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isOf(Items.WATER_BUCKET)
                        || stack.isOf(Items.LAVA_BUCKET)
                        || stack.isOf(Items.MILK_BUCKET);
            }
        });

        this.addSlot(new Slot(inventory, 4, 63, 56) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isOf(ModItems.EMPTY_CUP);
            }
        });

        this.addSlot(new Slot(inventory, 5, 130, 35) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }
        });


        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        addProperties(propertyDelegate);

    }

    public boolean isCrafting() {
        return this.propertyDelegate.get(0) > 0;
    }

    public int getScaledProgress(int maxPixels) {
        int progress = this.propertyDelegate.get(0);
        int maxProgress = this.propertyDelegate.get(1);
        return maxProgress != 0 && progress != 0 ? progress * maxPixels / maxProgress : 0;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slotIndex) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);

        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();

            if (slotIndex < 6) {
                if (!this.insertItem(originalStack, 6, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {

                if (originalStack.isOf(Items.WATER_BUCKET)) {
                    if (!this.insertItem(originalStack, 3, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (originalStack.isOf(Items.LAVA_BUCKET)) {
                    if (!this.insertItem(originalStack, 3, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (originalStack.isOf(Items.MILK_BUCKET)) {
                    if (!this.insertItem(originalStack, 3, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (originalStack.isOf(ModItems.EMPTY_CUP)) {
                    if (!this.insertItem(originalStack, 4, 5, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.insertItem(originalStack, 0, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

}

