package net.emanueljdf09.dtrhmod.item.custom;

import net.emanueljdf09.dtrhmod.item.ModItems;
import net.emanueljdf09.dtrhmod.menu.screen.BookCoverScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class OnceUponATimeBookItem extends Item {
    public OnceUponATimeBookItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack masterBook = user.getStackInHand(hand);
        Hand otherHand = hand == Hand.MAIN_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND;
        ItemStack offhandItem = user.getStackInHand(otherHand);

        if (!offhandItem.isEmpty()) {
            NbtCompound nbt = masterBook.getOrCreateNbt();
            String nbtKey = null;
            String messageName = null;

            if (offhandItem.isOf(ModItems.SNOW_WHITE_STORYBOOK)) {
                nbtKey = "unlocked_snow_white";
                messageName = "§cSnow White";
            } else if (offhandItem.isOf(ModItems.CINDERELLA_STORYBOOK)) {
                nbtKey = "unlocked_cinderella";
                messageName = "§bCinderella";
            } else if (offhandItem.isOf(ModItems.AURORA_STORYBOOK)) {
                nbtKey = "unlocked_aurora";
                messageName = "§dSleeping Beauty";
            } else if (offhandItem.isOf(ModItems.RED_RIDING_HOOD_STORYBOOK)) {
                nbtKey = "unlocked_red_riding_hood";
                messageName = "§4Red Riding Hood";
            }

            if (nbtKey != null) {
                if (nbt.getBoolean(nbtKey)) {
                    if (world.isClient) {
                        user.sendMessage(Text.literal("§eYour Master Book already contains the chapter of " + messageName + "§e."), true);
                    }
                    return TypedActionResult.fail(masterBook);
                }

                if (!world.isClient) {
                    nbt.putBoolean(nbtKey, true);

                    if (!user.isCreative()) {
                        offhandItem.decrement(1);
                    }

                    user.sendMessage(Text.literal("§6✨ A new chapter has bound to the book: " + messageName + " §6✨"), false);
                }

                world.playSound(user, user.getBlockPos(), SoundEvents.ITEM_BOOK_PAGE_TURN, SoundCategory.PLAYERS, 1.0F, 0.5F);
                world.playSound(user, user.getBlockPos(), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 1.0F, 1.2F);

                return TypedActionResult.success(masterBook, world.isClient());
            }
        }

        if (world.isClient) {
            openBookScreen(masterBook);
        }

        return TypedActionResult.success(masterBook, world.isClient());
    }

    @Environment(EnvType.CLIENT)
    private void openBookScreen(ItemStack stack) {
        MinecraftClient.getInstance().setScreen(new BookCoverScreen(stack));
    }
}
