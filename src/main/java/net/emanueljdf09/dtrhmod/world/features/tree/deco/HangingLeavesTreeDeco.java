package net.emanueljdf09.dtrhmod.world.features.tree.deco;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.emanueljdf09.dtrhmod.block.ModBlocks;
import net.minecraft.block.AbstractPlantStemBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;

public class HangingLeavesTreeDeco extends TreeDecorator {

    public static final Codec<HangingLeavesTreeDeco> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(Codec.FLOAT.fieldOf("probability").forGetter(decorator -> decorator.probability),
                            Codec.INT.fieldOf("min_length").forGetter(decorator -> decorator.minLength),
                            Codec.INT.fieldOf("max_length").forGetter(decorator -> decorator.maxLength))
                    .apply(instance, HangingLeavesTreeDeco::new));

    private final float probability;
    private final int minLength;
    private final int maxLength;

    public HangingLeavesTreeDeco(float probability, int minLength, int maxLength) {
        this.probability = probability;
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    @Override
    protected TreeDecoratorType<?> getType() {
        return ModTreeDeco.HANGING_LEAVES_DECORATOR;
    }

    @Override
    public void generate(Generator generator) {
        generator.getLeavesPositions().forEach(pos -> {
            if (generator.getRandom().nextFloat() < this.probability) {

                int length = MathHelper.nextInt(generator.getRandom(), this.minLength, this.maxLength);

                BlockPos.Mutable currentPos = pos.down().mutableCopy();

                for (int i = 0; i < length; i++) {
                    if (generator.isAir(currentPos)) {
                        boolean isTip = (i == length - 1);

                        BlockState state;
                        if (isTip) {
                            int age = generator.getRandom().nextInt(15);
                            state = ModBlocks.WW_HANGING_LEAVES.getDefaultState()
                                    .with(AbstractPlantStemBlock.AGE, age);
                        } else {
                            state = ModBlocks.WW_HANGING_LEAVES_PLANT.getDefaultState();
                        }

                        generator.replace(currentPos, state);
                        currentPos.move(Direction.DOWN);
                    } else {
                        break;
                    }
                }
            }
        });
    }
}