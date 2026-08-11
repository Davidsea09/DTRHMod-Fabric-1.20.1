package net.emanueljdf09.dtrhmod.world.features.tree.deco;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.emanueljdf09.dtrhmod.block.ModBlocks;
import net.minecraft.block.AbstractPlantStemBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;

public class HangingLeavesTreeDeco extends TreeDecorator {

    public static final Codec<HangingLeavesTreeDeco> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(Codec.FLOAT.fieldOf("probability").forGetter(decorator -> decorator.probability))
                    .apply(instance, HangingLeavesTreeDeco::new));

    private final float probability;

    public HangingLeavesTreeDeco(float probability) {
        this.probability = probability;
    }

    @Override
    protected TreeDecoratorType<?> getType() {
        return ModTreeDeco.HANGING_LEAVES_DECORATOR;
    }

    @Override
    public void generate(Generator generator) {
        generator.getLeavesPositions().forEach(pos -> {
            BlockPos belowPos = pos.down();

            if (generator.isAir(belowPos) && generator.getRandom().nextFloat() < this.probability) {

                int age = generator.getRandom().nextInt(15);

                BlockState state = ModBlocks.WW_HANGING_LEAVES.getDefaultState()
                        .with(AbstractPlantStemBlock.AGE, age);

                generator.replace(belowPos, state);
            }
        });
    }
}