package net.emanueljdf09.dtrhmod.item.custom;

import net.emanueljdf09.dtrhmod.item.ModItems;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.packet.s2c.play.OpenWrittenBookS2CPacket;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class StoryBook extends WrittenBookItem {

    private final String storyType;

    public static final Map<String, StoryData> STORIES = new HashMap<>();

    static {
        STORIES.put("adventure", new StoryData(
                "Cenerentola",
                "G.Basile",
                splitIntoPages("""
                Zezzolla, a young noblewoman, was persuaded by her governess to kill her stepmother so the governess could take her place. 
                But once the deed was done, the new stepmother brought her six own daughters, who treated Zezzolla cruelly and made her sleep by the ashes, calling her Cenerentola. 
                When her father brought her a date tree, she cared for it until a fairy appeared from within and gave her beautiful clothes to attend a royal feast. 
                The king fell in love with her, but she fled each night before he could learn her name. On the last night, she lost a slipper, and the king vowed to find its owner. 
                When the slipper fit her perfectly, he married her, and Cenerentola’s days of hardship ended forever.
                """)
        ));

        STORIES.put("fairy_tale", new StoryData(
                "Tales of the Enchanted Kingdom",
                "L. Everafter",
                splitIntoPages("""
                        In a far away kingdom, where the skies were painted gold at dawn, lived a kind princess.
                        She wandered into the forest one day and met a talking fox with eyes like emeralds.
                        The fox told her of a curse that could only be broken by laughter and friendship.
                        And so, she brought joy to the kingdom, breaking the curse and dancing under the stars.
                        """)
        ));

        STORIES.put("mystery", new StoryData(
                "The Stormy Night",
                "C. Clue",
                splitIntoPages("""
                        It was a dark and stormy night. Lightning flashed, illuminating the old manor house.
                        Detective Rowan arrived just as the clock struck midnight.
                        Everyone had a motive. Everyone had a secret.
                        By dawn, the truth would be revealed — but only to those willing to face it.
                        """)
        ));

        STORIES.put("history", new StoryData(
                "Chronicles of the Ancients",
                "H. Archivist",
                splitIntoPages("""
                        Long ago, before the kingdoms rose and fell, there were empires of unimaginable power.
                        They carved their histories into stone, built monuments that reached the skies.
                        Wars were fought, peace was brokered, and legends were born.
                        Though their empires crumbled, their stories live on in the whispers of the wind.
                        """)
        ));

        STORIES.put("science", new StoryData(
                "Discoveries of the Mind",
                "D. Newtonson",
                splitIntoPages("""
                        The laws of physics govern everything we see, yet they hide wonders beyond imagination.
                        From the smallest atom to the largest galaxy, patterns emerge, waiting to be understood.
                        Each discovery changes the way we see the universe — and ourselves.
                        And still, the greatest mystery is how much there is left to learn.
                        """)
        ));
    }



    public StoryBook(Settings settings, String storyType) {
        super(settings);
        this.storyType = storyType;
    }





    @Override
    public ItemStack getDefaultStack() {
        ItemStack stack = new ItemStack(this);
        stack.setNbt(createBookNbt(storyType));
        return stack;
    }

    private static NbtCompound createBookNbt(String storyType) {
        StoryData data = STORIES.getOrDefault(storyType,
                new StoryData("Untitled Story", "Unknown", Collections.singletonList("This storybook is blank."))
        );

        NbtCompound nbt = new NbtCompound();
        nbt.putString("title", data.title);
        nbt.putString("author", data.author);
        nbt.put("pages", createPages(data.pages));
        nbt.putBoolean("resolved", true);
        return nbt;
    }

    private static NbtList createPages(List<String> pageTexts) {
        NbtList pages = new NbtList();
        for (String text : pageTexts) {
            pages.add(NbtString.of(Text.Serializer.toJson(Text.literal(text))));
        }
        return pages;
    }

    private static List<String> splitIntoPages(String text) {
        List<String> pages = new ArrayList<>();
        String[] words = text.trim().split("\\s+");
        StringBuilder currentPage = new StringBuilder();

        for (String word : words) {
            if (currentPage.length() + word.length() + 1 > 255) {
                pages.add(currentPage.toString().trim());
                currentPage.setLength(0);
            }
            currentPage.append(word).append(" ");
        }
        if (!currentPage.isEmpty()) {
            pages.add(currentPage.toString().trim());
        }

        return pages;
    }


    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (world.isClient) {
            net.minecraft.client.MinecraftClient.getInstance()
                    .setScreen(new net.minecraft.client.gui.screen.ingame.BookScreen(
                            new net.minecraft.client.gui.screen.ingame.BookScreen.WrittenBookContents(stack)
                    ));
        }

        return TypedActionResult.success(stack, world.isClient());
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (stack.hasNbt() && stack.getNbt().contains("title") && stack.getNbt().contains("author")) {
            tooltip.add(Text.literal("Title: " + stack.getNbt().getString("title")));
            tooltip.add(Text.literal("Author: " + stack.getNbt().getString("author")));
        } else {
            tooltip.add(Text.literal("Storybook (Unknown)"));
        }
    }

    private record StoryData(String title, String author, List<String> pages) {}

}
