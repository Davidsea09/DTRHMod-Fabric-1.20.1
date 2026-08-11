package net.emanueljdf09.dtrhmod.item.custom;


import net.emanueljdf09.dtrhmod.menu.screen.BookCoverScreen;
import net.emanueljdf09.dtrhmod.util.ModUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Language;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class StoryBook extends WrittenBookItem {

    private final String storyType;

    public static final Map<String, StoryData> STORIES = new HashMap<>();

    static {
        STORIES.put("cinderella",  new StoryData("Cinderella", "Aschenputtel",  "Jacob & Wilhelm Grimm"));
        STORIES.put("red_riding_hood", new StoryData("Little Red Riding Hood", "Rotkäppchen", "Jacob & Wilhelm Grimm"));
        STORIES.put("snow_white",    new StoryData("Snow White", "Schneewittchen", "Jacob & Wilhelm Grimm"));
        STORIES.put("three_little_pigs",    new StoryData("The Three Little Pigs", "Die drei kleinen Schweinchen", "Traditional English"));
        STORIES.put("jack_and_the_beanstalk",    new StoryData("Jack and the Beanstalk", "Jack and the Beanstalk", "Traditional English"));
        STORIES.put("aurora",     new StoryData("The Sleeping Beauty", "Dornröschen", "Jacob & Wilhelm Grimm"));
        STORIES.put("the_little_mermaid",   new StoryData("The Little Mermaid", "Den lille Havfrue", "Hans Christian Andersen"));
        STORIES.put("rapunzel",       new StoryData("Rapunzel", "Rapunzel", "Jacob & Wilhelm Grimm"));
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
                new StoryData("Untitled Story", "Unknown", "Unknown")
        );


        Language language = Language.getInstance();
        String titleKey = "storybook." + storyType + ".title";
        String ogTitleKey = "storybook." + storyType + ".ogTitle";
        String authorKey = "storybook." + storyType + ".author";
        String textKey = "storybook." + storyType + ".text";

        String finalTitle = language.hasTranslation(titleKey) ? language.get(titleKey) : data.title();
        String finalOgTitle = language.hasTranslation(ogTitleKey) ? language.get(ogTitleKey) : data.ogTitle();
        String finalAuthor = language.hasTranslation(authorKey) ? language.get(authorKey) : data.author();

        String translatedText = language.get(textKey);
        List<String> finalPages;

        if (!translatedText.equals(textKey)) {
            finalPages = ModUtils.splitIntoPages(translatedText);
        } else {
            finalPages = Collections.singletonList("The pages of this book are missing or unreadable.");
        }

        NbtCompound nbt = new NbtCompound();
        nbt.putString("title", finalTitle);
        nbt.putString("ogTitle", finalOgTitle);
        nbt.putString("author", finalAuthor);
        nbt.put("pages", createPages(finalPages));
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

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (world.isClient) {
            MinecraftClient.getInstance()
                    .setScreen(new BookCoverScreen(stack
                    ));
        }

        return TypedActionResult.success(stack, world.isClient());
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (stack.hasNbt() && Objects.requireNonNull(stack.getNbt()).contains("title") && stack.getNbt().contains("author")) {
            tooltip.add(Text.literal(stack.getNbt().getString("ogTitle")));
            tooltip.add(Text.literal("Author: " + stack.getNbt().getString("author")));
        } else {
            tooltip.add(Text.literal("Storybook (Unknown)"));
        }
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return false;
    }

    public record StoryData(String title, String ogTitle, String author) {}

}
