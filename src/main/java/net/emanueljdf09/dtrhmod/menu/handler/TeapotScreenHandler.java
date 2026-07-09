package net.emanueljdf09.dtrhmod.menu.handler;

import net.emanueljdf09.dtrhmod.block.entity.TeapotBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class TeapotScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;
    private final TeapotBlockEntity teapotBlockEntity;

    public TeapotScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(7), new ArrayPropertyDelegate(2));
    }

    public TeapotScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(null, syncId); // Replace null with your ScreenHandlerType registry reference later
        checkSize(inventory, 7);
        this.inventory = inventory;
        this.propertyDelegate = propertyDelegate;

        inventory.onOpen(playerInventory.player);
        this.addProperties(propertyDelegate);

        // --- 1. Top Three Ingredient Slots ---
        // Estimated X coordinates: 26, 44, 62. Y coordinate: 17
        for (int i = 0; i < 3; ++i) {
            this.addSlot(new Slot(inventory, i, 26 + (i * 18), 17));
        }

        // --- 2. Bottom Three Specialty Slots (Cup, Base, Pouch) ---
        // Estimated X coordinates: 20, 44, 68. Y coordinate: 53
        this.addSlot(new Slot(inventory, 3, 20, 53)); // Cup slot
        this.addSlot(new Slot(inventory, 4, 44, 53)); // Middle base slot
        this.addSlot(new Slot(inventory, 5, 68, 53)); // Pouch slot

        // --- 3. Output Slot ---
        // Estimated X/Y coordinates based on arrow placement
        this.addSlot(new Slot(inventory, 6, 124, 35));

        // --- 4. Player Inventory Squares (3x9 Grid) ---
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        // --- 5. Player Hotbar (1x9 Grid) ---
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
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
        // Handle shift-clicking items here safely
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }
}

