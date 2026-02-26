package net.emanueljdf09.dtrhmod.datagen;

import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.emanueljdf09.dtrhmod.block.ModBlocks;
import net.emanueljdf09.dtrhmod.item.ModItems;
import net.emanueljdf09.dtrhmod.world.dimension.ModDimensions;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.ChangedDimensionCriterion;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class ModAdvancementProvider extends FabricAdvancementProvider {
    public ModAdvancementProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateAdvancement(Consumer<Advancement> consumer) {

        Advancement root = Advancement.Builder.create()
                .display(
                        ModItems.POCKETWATCH,
                        Text.literal("Down the Rabbit hole"),
                        Text.literal("Root advancement"),
                        new Identifier("textures/gui/advancements/backgrounds/adventure.png"),
                        AdvancementFrame.TASK,
                        false, false, false
                )
                .criterion("entered_exterior",
                        ChangedDimensionCriterion.Conditions.to(ModDimensions.EXTERIOR_LEVEL_KEY))
                .build(consumer, DownTheRabbitHole.MOD_ID + ":root");


        Advancement portal = Advancement.Builder.create()
                .parent(root)
                .display(
                        ModBlocks.RABBIT_HOLE.asItem(),
                        Text.literal("Follow the tunnel..."),
                        Text.literal("...into the portal"),
                        null,
                        AdvancementFrame.GOAL,
                        true, true, false
                )
                .criterion("entered_exterior",
                        ChangedDimensionCriterion.Conditions.to(ModDimensions.EXTERIOR_LEVEL_KEY))
                .build(consumer, DownTheRabbitHole.MOD_ID + ":exterior_complete");
    }
}
