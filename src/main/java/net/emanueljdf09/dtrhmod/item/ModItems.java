package net.emanueljdf09.dtrhmod.item;

import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.emanueljdf09.dtrhmod.block.ModBlocks;
import net.emanueljdf09.dtrhmod.entity.ModEntities;
import net.emanueljdf09.dtrhmod.item.block.ExteriorChestItem;
import net.emanueljdf09.dtrhmod.item.custom.DynamicTeaCupItem;
import net.emanueljdf09.dtrhmod.item.custom.StoryBook;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.List;

public class ModItems {

    public static final Item POCKETWATCH = registerItem("pocketwatch", new Item(new FabricItemSettings()));

    public static final Item EXTERIOR_KEY = registerItem("exterior_key", new Item(new FabricItemSettings()));

    public static BlockItem EXTERIOR_CHEST_ITEM = registerItem("exterior_chest", new ExteriorChestItem(ModBlocks.EXTERIOR_CHEST, new FabricItemSettings()));

    public static final Item CINDERELLA_STORYBOOK = registerItem("cinderella_storybook",
            new StoryBook(new Item.Settings().maxCount(1), "cinderella"));

    public static final Item RED_RIDING_HOOD_STORYBOOK = registerItem("red_riding_hood_storybook",
            new StoryBook(new Item.Settings().maxCount(1), "red_riding_hood"));

    public static final Item SNOW_WHITE_STORYBOOK = registerItem("snow_white_storybook",
            new StoryBook(new Item.Settings().maxCount(1), "snow_white"));

    public static final Item THREE_LITTLE_PIGS_STORYBOOK = registerItem("three_little_pigs_storybook",
            new StoryBook(new Item.Settings().maxCount(1), "three_little_pigs"));

    public static final Item JACK_AND_THE_BEANSTALK_STORYBOOK = registerItem("jack_and_the_beanstalk_storybook",
            new StoryBook(new Item.Settings().maxCount(1), "jack_and_the_beanstalk"));

    public static final Item THE_LITTLE_MERMAID_STORYBOOK = registerItem("the_little_mermaid_storybook",
            new StoryBook(new Item.Settings().maxCount(1), "the_little_mermaid"));

    public static final Item AURORA_STORYBOOK = registerItem("aurora_storybook",
            new StoryBook(new Item.Settings().maxCount(1), "aurora"));

    public static final Item RAPUNZEL_STORYBOOK = registerItem("rapunzel_storybook",
            new StoryBook(new Item.Settings().maxCount(1), "rapunzel"));

    public static final Item DRINK_ME = registerItem("drink_me",
            new Item(new Item.Settings().food(ModFoodComponent.DRINK_ME)));
    public static final Item EAT_ME = registerItem("eat_me",
            new Item(new Item.Settings().food(ModFoodComponent.EAT_ME)));

    public static final Item EMPTY_CUP = registerItem("empty_cup",
            new Item(new Item.Settings()));
    public static final Item FILLED_TEA_CUP = registerItem("filled_tea_cup",
            new DynamicTeaCupItem(new FabricItemSettings().maxCount(16)));

    public static final Item WHITE_RABBIT_SPAWN_EGG = registerItem("white_rabbit_spawn_egg",
            new SpawnEggItem(ModEntities.WHITE_RABBIT, 0x057E36, 0x100000,
                    new FabricItemSettings()));

    public static final Item WEEPING_PLAYERS_SPAWN_EGG = registerItem("weeping_players_spawn_egg",
            new SpawnEggItem(ModEntities.WEEPING_PLAYER, 0x057E36, 0x100000,
                    new FabricItemSettings()));

    public static final Item WW_SIGN = registerItem("ww_sign",
            new SignItem(new FabricItemSettings().maxCount(16), ModBlocks.WW_SIGN, ModBlocks.WALL_WW_SIGN));

    public static final Item HANGING_WW_SIGN = registerItem("hanging_ww_sign",
            new HangingSignItem(ModBlocks.WW_HANGING_SIGN, ModBlocks.WALL_WW_HANGING_SIGN, new FabricItemSettings().maxCount(16)));

    public static final Item TH_SIGN = registerItem("th_sign",
            new SignItem(new FabricItemSettings().maxCount(16), ModBlocks.TH_SIGN, ModBlocks.WALL_TH_SIGN));

    public static final Item HANGING_TH_SIGN = registerItem("hanging_th_sign",
            new HangingSignItem(ModBlocks.TH_HANGING_SIGN, ModBlocks.WALL_TH_HANGING_SIGN, new FabricItemSettings().maxCount(16)));

    public static final Item BB_SIGN = registerItem("bb_sign",
            new SignItem(new FabricItemSettings().maxCount(16), ModBlocks.BB_SIGN, ModBlocks.WALL_BB_SIGN));

    public static final Item HANGING_BB_SIGN = registerItem("hanging_bb_sign",
            new HangingSignItem(ModBlocks.BB_HANGING_SIGN, ModBlocks.WALL_BB_HANGING_SIGN, new FabricItemSettings().maxCount(16)));


    public static final List<Item> STORYBOOK_ITEMS = List.of(
            CINDERELLA_STORYBOOK,
            RED_RIDING_HOOD_STORYBOOK,
            SNOW_WHITE_STORYBOOK,
            RAPUNZEL_STORYBOOK,
            THE_LITTLE_MERMAID_STORYBOOK,
            THREE_LITTLE_PIGS_STORYBOOK,
            AURORA_STORYBOOK,
            JACK_AND_THE_BEANSTALK_STORYBOOK
    );

    private static <T extends Item> T registerItem(String name, T item) {
        return Registry.register(Registries.ITEM, new Identifier(DownTheRabbitHole.MOD_ID, name), item);
    }



    public static void registerModItems() {
        DownTheRabbitHole.LOGGER.info("Registerings Mod Items for " + DownTheRabbitHole.MOD_ID);
    }
}
