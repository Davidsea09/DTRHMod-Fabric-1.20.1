package net.emanueljdf09.dtrhmod.item;

import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.emanueljdf09.dtrhmod.block.ModBlocks;
import net.emanueljdf09.dtrhmod.item.custom.StoryBook;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.List;

public class ModItems {

    public static final Item POCKETWATCH = registerItem("pocketwatch", new Item(new FabricItemSettings()));
    public static final Item CINDERELLA_STORYBOOK = registerItem("cinderella_storybook",
            new StoryBook(new Item.Settings().maxCount(1), "adventure"));

    public static final Item FAIRY_TALE_STORYBOOK = registerItem("fairy_tale_storybook",
            new StoryBook(new Item.Settings().maxCount(1), "fairy_tale"));

    public static final Item MYSTERY_STORYBOOK = registerItem("mystery_storybook",
            new StoryBook(new Item.Settings().maxCount(1), "mystery"));

    public static final Item HISTORY_STORYBOOK = registerItem("history_storybook",
            new StoryBook(new Item.Settings().maxCount(1), "history"));

    public static final Item SCIENCE_STORYBOOK = registerItem("science_storybook",
            new StoryBook(new Item.Settings().maxCount(1), "science"));


    public static final List<Item> STORYBOOK_ITEMS = List.of(
            CINDERELLA_STORYBOOK,
            FAIRY_TALE_STORYBOOK,
            MYSTERY_STORYBOOK,
            HISTORY_STORYBOOK,
            SCIENCE_STORYBOOK
    );

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(DownTheRabbitHole.MOD_ID, name), item);
    }



    public static void registerModItems() {
        DownTheRabbitHole.LOGGER.info("Registerings Mod Items for " + DownTheRabbitHole.MOD_ID);
    }
}
