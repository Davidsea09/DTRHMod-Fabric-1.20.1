package net.emanueljdf09.dtrhmod.item;

import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.emanueljdf09.dtrhmod.block.ModBlocks;
import net.emanueljdf09.dtrhmod.item.custom.StoryBook;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
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
                        entries.add(ModBlocks.LAWN_DAISY_PATCH.asItem());
                        entries.add(ModItems.FAIRY_TALE_STORYBOOK.getDefaultStack());
                        entries.add(ModItems.CINDERELLA_STORYBOOK.getDefaultStack());
                        entries.add(ModItems.MYSTERY_STORYBOOK.getDefaultStack());
                        entries.add(ModItems.SCIENCE_STORYBOOK.getDefaultStack());
                        entries.add(ModItems.HISTORY_STORYBOOK.getDefaultStack());
                    }).build());


    public static void registerItemGroups() {
        DownTheRabbitHole.LOGGER.info("Registering Item Groups for " + DownTheRabbitHole.MOD_ID);
    }
}
