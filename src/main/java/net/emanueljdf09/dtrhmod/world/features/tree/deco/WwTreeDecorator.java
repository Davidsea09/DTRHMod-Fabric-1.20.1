package net.emanueljdf09.dtrhmod.world.features.tree.deco;

import com.mojang.serialization.Codec;
import net.emanueljdf09.dtrhmod.block.ModBlocks;
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

        // Loop through every single leaf block placed by the Acacia Foliage Placer
        for (BlockPos pos : generator.getLeavesPositions()) {

            // Only drop trails from the outer/lower leaves to keep it looking realistic (35% chance)
            if (random.nextFloat() < 0.35f) {
                BlockPos currentPos = pos.down();

                // Determine a random length for this willow strand (e.g., 2 to 4 blocks long)
                int strandLength = random.nextBetween(2, 4);

                for (int i = 0; i < strandLength; i++) {
                    if (generator.isAir(currentPos)) {
                        generator.replace(currentPos, ModBlocks.WW_HANGING_LEAVES.getDefaultState());
                        currentPos = currentPos.down(); // Move down to continue the chain
                    } else {
                        break; // Stop drawing this strand if we hit a solid block or another leaf
                    }
                }
            }
        }
    }
}
