package net.emanueljdf09.dtrhmod.datagen;

import net.emanueljdf09.dtrhmod.item.ModItems;
import net.emanueljdf09.dtrhmod.util.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> consumer) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.POCKETWATCH, 1)
                .pattern("NNN")
                .pattern("N N")
                .pattern(" C ")
                .input('N', Items.GOLD_NUGGET)
                .input('C', Items.CLOCK)
                .criterion(hasItem(Items.CLOCK), conditionsFromItem(Items.CLOCK))
                .offerTo(consumer, new Identifier(getRecipeName(ModItems.POCKETWATCH)));
    }
}
