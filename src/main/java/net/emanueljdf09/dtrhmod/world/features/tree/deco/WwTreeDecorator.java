package net.emanueljdf09.dtrhmod.world.features.tree.deco;

import com.mojang.serialization.Codec;
import net.emanueljdf09.dtrhmod.block.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;

public class WwTreeDecorator extends TreeDecorator {
    public static final Codec<WwTreeDecorator> CODEC =
            Codec.unit(WwTreeDecorator::new);

    @Override
    protected TreeDecoratorType<?> getType() {
        return ModTreeDeco.WW_TREE_DECORATOR;
    }

    @Override
    public void generate(Generator generator) {
        Random random = generator.getRandom();

        // Loop through every single leaf block placed during tree generation
        for (BlockPos pos : generator.getLeavesPositions()) {

            // Drop hanging leaf strands from outer or lower leaves (35% chance per leaf)
            if (random.nextFloat() < 0.35f) {
                BlockPos currentPos = pos.down();

                // Random length for this hanging strand (2 to 4 blocks long)
                int strandLength = random.nextBetween(2, 4);

                for (int i = 0; i < strandLength; i++) {
                    if (generator.isAir(currentPos)) {
                        // Check if this is the very last block of the strand to apply the tip block
                        boolean isBottomTip = (i == strandLength - 1);

                        BlockState blockStateToPlace = isBottomTip
                                ? ModBlocks.WW_HANGING_LEAVES.getDefaultState()
                                : ModBlocks.WW_HANGING_LEAVES_PLANT.getDefaultState();

                        generator.replace(currentPos, blockStateToPlace);
                        currentPos = currentPos.down(); // Move down to continue the chain
                    } else {
                        break; // Stop if we hit a solid block or obstruction
                    }
                }
            }
        }
    }
}
