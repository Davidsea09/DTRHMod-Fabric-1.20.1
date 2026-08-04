package net.emanueljdf09.dtrhmod.item;

import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.emanueljdf09.dtrhmod.block.ModBlocks;
import net.emanueljdf09.dtrhmod.util.ModEffects;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {

    public static final ItemGroup DTRH_GROUP = Registry.register(Registries.ITEM_GROUP,
            new Identifier(DownTheRabbitHole.MOD_ID, "dtrh"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.dtrh"))
                    .icon(() -> new ItemStack(ModItems.POCKETWATCH)).entries((displayContext, entries) -> {

                        entries.add(ModItems.POCKETWATCH);
                        entries.add(ModItems.EXTERIOR_KEY);
                        entries.add(ModItems.EMPTY_CUP);
                        entries.add(ModItems.FILLED_TEA_CUP);
                        entries.add(ModBlocks.TEAPOT_BLOCK.asItem());
                        entries.add(ModBlocks.BLUE_MUSHROOM.asItem());
                        entries.add(ModBlocks.BLUE_MUSHROOM_BLOCK.asItem());
                        entries.add(ModBlocks.YELLOW_MUSHROOM_BLOCK.asItem());
                        entries.add(ModBlocks.YELLOW_MUSHROOM.asItem());
                        entries.add(ModBlocks.MAGENTA_MUSHROOM_BLOCK.asItem());
                        entries.add(ModBlocks.MAGENTA_MUSHROOM.asItem());

                        entries.add(ModItems.DRINK_ME);
                        entries.add(ModItems.EAT_ME);
                        entries.add(ModItems.WHITE_RABBIT_SPAWN_EGG);
                        entries.add(ModItems.WEEPING_PLAYERS_SPAWN_EGG);

                        entries.add(ModBlocks.MAD_HATTER_HAT.asItem());
                        entries.add(ModItems.RED_RIDING_HOOD_STORYBOOK.getDefaultStack());
                        entries.add(ModItems.CINDERELLA_STORYBOOK.getDefaultStack());
                        entries.add(ModItems.SNOW_WHITE_STORYBOOK.getDefaultStack());
                        entries.add(ModItems.JACK_AND_THE_BEANSTALK_STORYBOOK.getDefaultStack());
                        entries.add(ModItems.RAPUNZEL_STORYBOOK.getDefaultStack());
                        entries.add(ModItems.AURORA_STORYBOOK.getDefaultStack());
                        entries.add(ModItems.THE_LITTLE_MERMAID_STORYBOOK.getDefaultStack());
                        entries.add(ModItems.THREE_LITTLE_PIGS_STORYBOOK.getDefaultStack());



                        entries.add(ModBlocks.TH_SAPLING.asItem());
                        entries.add(ModBlocks.TH_LEAVES.asItem());
                        entries.add(ModBlocks.TH_LOG.asItem());
                        entries.add(ModBlocks.TH_WOOD.asItem());
                        entries.add(ModBlocks.STRIPPED_TH_LOG.asItem());
                        entries.add(ModBlocks.STRIPPED_TH_WOOD.asItem());
                        entries.add(ModBlocks.TH_PLANKS.asItem());
                        entries.add(ModBlocks.TH_STAIRS.asItem());
                        entries.add(ModBlocks.TH_SLABS.asItem());
                        entries.add(ModBlocks.TH_FENCE.asItem());
                        entries.add(ModBlocks.TH_FENCE_GATE.asItem());
                        entries.add(ModBlocks.TH_WALL.asItem());
                        entries.add(ModBlocks.TH_DOOR.asItem());
                        entries.add(ModBlocks.TH_TRAPDOOR.asItem());
                        entries.add(ModBlocks.TH_BUTTON.asItem());
                        entries.add(ModBlocks.TH_PRESSURE_PLATE.asItem());

                        entries.add(ModBlocks.WW_SAPLING.asItem());
                        entries.add(ModBlocks.WW_LEAVES.asItem());
                        entries.add(ModBlocks.WW_HANGING_LEAVES.asItem());
                        entries.add(ModBlocks.WW_LOG.asItem());
                        entries.add(ModBlocks.WW_WOOD.asItem());
                        entries.add(ModBlocks.STRIPPED_WW_LOG.asItem());
                        entries.add(ModBlocks.STRIPPED_WW_WOOD.asItem());
                        entries.add(ModBlocks.WW_PLANKS.asItem());
                        entries.add(ModBlocks.WW_STAIRS.asItem());
                        entries.add(ModBlocks.WW_SLABS.asItem());
                        entries.add(ModBlocks.WW_FENCE.asItem());
                        entries.add(ModBlocks.WW_FENCE_GATE.asItem());
                        entries.add(ModBlocks.WW_WALL.asItem());
                        entries.add(ModBlocks.WW_DOOR.asItem());
                        entries.add(ModBlocks.WW_TRAPDOOR.asItem());
                        entries.add(ModBlocks.WW_BUTTON.asItem());
                        entries.add(ModBlocks.WW_PRESSURE_PLATE.asItem());

                        entries.add(ModBlocks.BB_SAPLING.asItem());
                        entries.add(ModBlocks.BB_LEAVES.asItem());
                        entries.add(ModBlocks.BB_LOG.asItem());
                        entries.add(ModBlocks.BB_WOOD.asItem());
                        entries.add(ModBlocks.STRIPPED_BB_LOG.asItem());
                        entries.add(ModBlocks.STRIPPED_BB_WOOD.asItem());
                        entries.add(ModBlocks.BB_PLANKS.asItem());
                        entries.add(ModBlocks.BB_STAIRS.asItem());
                        entries.add(ModBlocks.BB_SLABS.asItem());
                        entries.add(ModBlocks.BB_FENCE.asItem());
                        entries.add(ModBlocks.BB_FENCE_GATE.asItem());
                        entries.add(ModBlocks.BB_WALL.asItem());
                        entries.add(ModBlocks.BB_DOOR.asItem());
                        entries.add(ModBlocks.BB_TRAPDOOR.asItem());
                        entries.add(ModBlocks.BB_BUTTON.asItem());
                        entries.add(ModBlocks.BB_PRESSURE_PLATE.asItem());


                        entries.add(ModBlocks.LAWN_DAISY_PATCH.asItem());
                        entries.add(ModBlocks.WONDER_DIRT.asItem());
                        entries.add(ModBlocks.WONDER_GRASS.asItem());
                        entries.add(ModBlocks.RABBIT_HOLE.asItem());
                        entries.add(ModItems.EXTERIOR_CHEST_ITEM);
                        entries.add(ModBlocks.EXTERIOR_DOOR.asItem());
                        entries.add(ModBlocks.EXTERIOR_PORTAL.asItem());
                        entries.add(ModBlocks.MIRROR_BLOCK.asItem());

                    }).build());


    public static void registerItemGroups() {
        DownTheRabbitHole.LOGGER.info("Registering Item Groups for " + DownTheRabbitHole.MOD_ID);
    }
}
