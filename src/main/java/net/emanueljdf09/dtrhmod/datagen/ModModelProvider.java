package net.emanueljdf09.dtrhmod.datagen;

import net.emanueljdf09.dtrhmod.block.ModBlocks;
import net.emanueljdf09.dtrhmod.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

        blockStateModelGenerator.registerFlowerbed(ModBlocks.LAWN_DAISY_PATCH);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {


        for (Item item : ModItems.STORYBOOK_ITEMS) {
            itemModelGenerator.register(item, Models.GENERATED);
        }

        itemModelGenerator.register(ModItems.POCKETWATCH, Models.GENERATED);

    }
}
