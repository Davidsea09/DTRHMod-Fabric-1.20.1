package net.emanueljdf09.dtrhmod.block;

import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.fabricmc.fabric.api.object.builder.v1.block.type.WoodTypeBuilder;
import net.minecraft.block.BlockSetType;
import net.minecraft.block.WoodType;
import net.minecraft.util.Identifier;


public class ModBlockCollections {
    public static final BlockSetType TH_WOOD_SET = new BlockSetType(DownTheRabbitHole.MOD_ID + "th");
    public static final BlockSetType BB_WOOD_SET = new BlockSetType(DownTheRabbitHole.MOD_ID + "bb");
    public static final BlockSetType WW_WOOD_SET = new BlockSetType(DownTheRabbitHole.MOD_ID + "ww");
    public static final BlockSetType HH_WOOD_SET = new BlockSetType(DownTheRabbitHole.MOD_ID + "hh");

    public static final WoodType TH_WOOD_TYPE = register(new Identifier(DownTheRabbitHole.MOD_ID, "th"), TH_WOOD_SET);
    public static final WoodType BB_WOOD_TYPE = register(new Identifier(DownTheRabbitHole.MOD_ID, "bb"), BB_WOOD_SET);
    public static final WoodType WW_WOOD_TYPE = register(new Identifier(DownTheRabbitHole.MOD_ID, "ww"), WW_WOOD_SET);
    public static final WoodType HH_WOOD_TYPE = register(new Identifier(DownTheRabbitHole.MOD_ID, "hh"), HH_WOOD_SET);


    private static WoodType register(Identifier id, BlockSetType setType) {
        return new WoodTypeBuilder().register(id, setType);
    }

    public static void registerModBlockCollections() {
        DownTheRabbitHole.LOGGER.info("Registering Mod Blocks Collections for " + DownTheRabbitHole.MOD_ID);
    }
}