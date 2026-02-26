package net.emanueljdf09.dtrhmod.world.features.tree.deco;

import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;

public class ModTreeDeco {
    public static final TreeDecoratorType<WwTreeDecorator> WW_TREE_DECORATOR =
            Registry.register(
                    Registries.TREE_DECORATOR_TYPE,
                    new Identifier(DownTheRabbitHole.MOD_ID, "ww_tree_decorator"),
                    new TreeDecoratorType<>(WwTreeDecorator.CODEC)
            );

    public static void register() {

    }
}
