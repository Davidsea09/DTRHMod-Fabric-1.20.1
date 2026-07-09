package net.emanueljdf09.dtrhmod.block;

import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.emanueljdf09.dtrhmod.block.custom.*;
import net.emanueljdf09.dtrhmod.block.custom.mirror.MirrorBlock;
import net.emanueljdf09.dtrhmod.item.block.ExteriorChestItem;
import net.emanueljdf09.dtrhmod.world.features.tree.BbSaplingGenerator;
import net.emanueljdf09.dtrhmod.world.features.tree.ThSaplingGenerator;
import net.emanueljdf09.dtrhmod.world.features.tree.WwSaplingGenerator;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {

    public static final Block LAWN_DAISY_PATCH = registerBlockWithItem("lawn_daisy_patch",
            new FlowerbedBlock(FabricBlockSettings.create().mapColor(MapColor.OFF_WHITE).noCollision().sounds(BlockSoundGroup.PINK_PETALS).pistonBehavior(PistonBehavior.DESTROY)));

    public static final Block RABBIT_HOLE = registerBlockWithItem("rabbit_hole",
            new RabbitHoleBlock(FabricBlockSettings.copyOf(Blocks.MOSS_BLOCK).noCollision()));

    public static final Block EXTERIOR_PORTAL = registerBlockWithItem("exterior_portal",
            new ExteriorPortal(FabricBlockSettings.copyOf(Blocks.MOSS_BLOCK).noCollision()));

    public static final Block EXTERIOR_CHEST = registerBlock("exterior_chest",
            new ExteriorChest(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS)));

    public static final Block EXTERIOR_DOOR = registerBlockWithItem("exterior_door",
            new ExteriorDoor(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS)));

    public static final Block TEAPOT_BLOCK = registerBlockWithItem("teapot_block",
            new TeapotBlock(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS)));

    public static final Block MIRROR_BLOCK = registerBlockWithItem("mirror_block",
            new MirrorBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)
                    .suffocates((state, world, pos) -> false)
                    .blockVision((state, world, pos) -> false)));

    public static final Block WONDER_GRASS = registerBlockWithItem("wonder_grass",
            new WonderGrassBlock(FabricBlockSettings.copyOf(Blocks.GRASS_BLOCK)));
    public static final Block WONDER_DIRT = registerBlockWithItem("wonder_dirt",
            new Block(FabricBlockSettings.copyOf(Blocks.DIRT)));


    public static final Block TH_SAPLING = registerBlockWithItem("th_sapling",
            new SaplingBlock(new ThSaplingGenerator(), FabricBlockSettings.copyOf(Blocks.OAK_SAPLING)));
    public static final Block TH_LOG = registerBlockWithItem("th_log",
            new PillarBlock(FabricBlockSettings.copyOf(Blocks.OAK_LOG).strength(4f)));
    public static final Block TH_WOOD = registerBlockWithItem("th_wood",
            new PillarBlock(FabricBlockSettings.copyOf(Blocks.OAK_WOOD).strength(4f)));
    public static final Block STRIPPED_TH_LOG = registerBlockWithItem("stripped_th_log",
            new PillarBlock(FabricBlockSettings.copyOf(Blocks.STRIPPED_OAK_LOG).strength(4f)));
    public static final Block STRIPPED_TH_WOOD = registerBlockWithItem("stripped_th_wood",
            new PillarBlock(FabricBlockSettings.copyOf(Blocks.STRIPPED_OAK_WOOD).strength(4f)));

    public static final Block TH_PLANKS = registerBlockWithItem("th_planks",
            new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS).strength(4f)));
    public static final Block TH_STAIRS = registerBlockWithItem("th_stairs",
            new StairsBlock(ModBlocks.TH_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS).strength(4f)));
    public static final Block TH_SLABS = registerBlockWithItem("th_slabs",
            new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB).strength(4f)));
    public static final Block TH_BUTTON = registerBlockWithItem("th_button",
            new ButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON), BlockSetType.OAK, 10, true));
    public static final Block TH_PRESSURE_PLATE = registerBlockWithItem("th_pressure_plate",
            new PressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING,
                    FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE), BlockSetType.OAK));

    public static final Block TH_FENCE = registerBlockWithItem("th_fence",
            new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE)));
    public static final Block TH_FENCE_GATE = registerBlockWithItem("th_fence_gate",
            new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE), WoodType.CRIMSON));
    public static final Block TH_WALL= registerBlockWithItem("th_wall",
            new WallBlock(FabricBlockSettings.copyOf(Blocks.BRICK_WALL)));

    public static final Block TH_DOOR = registerBlockWithItem("th_door",
            new DoorBlock(FabricBlockSettings.copyOf(Blocks.OAK_DOOR), BlockSetType.OAK));
    public static final Block TH_TRAPDOOR = registerBlockWithItem("th_trapdoor",
            new TrapdoorBlock(FabricBlockSettings.copyOf(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));

    public static final Block TH_LEAVES = registerBlockWithItem("th_leaves",
            new LeavesBlock(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES).strength(0.2f).nonOpaque()));

    public static final Block WW_SAPLING = registerBlockWithItem("ww_sapling",
            new SaplingBlock(new WwSaplingGenerator(), FabricBlockSettings.copyOf(Blocks.OAK_SAPLING)));
    public static final Block WW_LOG = registerBlockWithItem("ww_log",
            new PillarBlock(FabricBlockSettings.copyOf(Blocks.OAK_LOG).strength(4f)));
    public static final Block WW_WOOD = registerBlockWithItem("ww_wood",
            new PillarBlock(FabricBlockSettings.copyOf(Blocks.OAK_WOOD).strength(4f)));
    public static final Block STRIPPED_WW_LOG = registerBlockWithItem("stripped_ww_log",
            new PillarBlock(FabricBlockSettings.copyOf(Blocks.STRIPPED_OAK_LOG).strength(4f)));
    public static final Block STRIPPED_WW_WOOD = registerBlockWithItem("stripped_ww_wood",
            new PillarBlock(FabricBlockSettings.copyOf(Blocks.STRIPPED_OAK_WOOD).strength(4f)));

    public static final Block WW_PLANKS = registerBlockWithItem("ww_planks",
            new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS).strength(4f)));
    public static final Block WW_STAIRS = registerBlockWithItem("ww_stairs",
            new StairsBlock(ModBlocks.WW_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS).strength(4f)));
    public static final Block WW_SLABS = registerBlockWithItem("ww_slabs",
            new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB).strength(4f)));
    public static final Block WW_BUTTON = registerBlockWithItem("ww_button",
            new ButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON), BlockSetType.OAK, 10, true));
    public static final Block WW_PRESSURE_PLATE = registerBlockWithItem("ww_pressure_plate",
            new PressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING,
                    FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE), BlockSetType.OAK));

    public static final Block WW_FENCE = registerBlockWithItem("ww_fence",
            new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE)));
    public static final Block WW_FENCE_GATE = registerBlockWithItem("ww_fence_gate",
            new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE), WoodType.CRIMSON));
    public static final Block WW_WALL= registerBlockWithItem("ww_wall",
            new WallBlock(FabricBlockSettings.copyOf(Blocks.BRICK_WALL)));

    public static final Block WW_DOOR = registerBlockWithItem("ww_door",
            new DoorBlock(FabricBlockSettings.copyOf(Blocks.OAK_DOOR), BlockSetType.OAK));
    public static final Block WW_TRAPDOOR = registerBlockWithItem("ww_trapdoor",
            new TrapdoorBlock(FabricBlockSettings.copyOf(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));

    public static final Block WW_LEAVES = registerBlockWithItem("ww_leaves",
            new LeavesBlock(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES).strength(0.2f).nonOpaque()));
    public static final Block WW_HANGING_LEAVES = registerBlockWithItem("ww_hanging_leaves",
                new WHangingLeavesHeadBlock(FabricBlockSettings.create().sounds(BlockSoundGroup.VINE).nonOpaque().noCollision().ticksRandomly().breakInstantly().pistonBehavior(PistonBehavior.DESTROY)));
public static final Block WW_HANGING_LEAVES_PLANT = registerBlockWithItem("ww_hanging_leaves_plant",
                new WHangingLeavesBodyBlock(FabricBlockSettings.create().sounds(BlockSoundGroup.VINE).nonOpaque().noCollision().ticksRandomly().breakInstantly().pistonBehavior(PistonBehavior.DESTROY)));

    public static final Block BB_SAPLING = registerBlockWithItem("bb_sapling",
            new SaplingBlock(new BbSaplingGenerator(), FabricBlockSettings.copyOf(Blocks.OAK_SAPLING)));
    public static final Block BB_LOG = registerBlockWithItem("bb_log",
            new PillarBlock(FabricBlockSettings.copyOf(Blocks.OAK_LOG).strength(4f)));
    public static final Block BB_WOOD = registerBlockWithItem("bb_wood",
            new PillarBlock(FabricBlockSettings.copyOf(Blocks.OAK_WOOD).strength(4f)));
    public static final Block STRIPPED_BB_LOG = registerBlockWithItem("stripped_bb_log",
            new PillarBlock(FabricBlockSettings.copyOf(Blocks.STRIPPED_OAK_LOG).strength(4f)));
    public static final Block STRIPPED_BB_WOOD = registerBlockWithItem("stripped_bb_wood",
            new PillarBlock(FabricBlockSettings.copyOf(Blocks.STRIPPED_OAK_WOOD).strength(4f)));

    public static final Block BB_PLANKS = registerBlockWithItem("bb_planks",
            new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS).strength(4f)));
    public static final Block BB_STAIRS = registerBlockWithItem("bb_stairs",
            new StairsBlock(ModBlocks.BB_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS).strength(4f)));
    public static final Block BB_SLABS = registerBlockWithItem("bb_slabs",
            new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB).strength(4f)));
    public static final Block BB_BUTTON = registerBlockWithItem("bb_button",
            new ButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON), BlockSetType.OAK, 10, true));
    public static final Block BB_PRESSURE_PLATE = registerBlockWithItem("bb_pressure_plate",
            new PressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING,
                    FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE), BlockSetType.OAK));

    public static final Block BB_FENCE = registerBlockWithItem("bb_fence",
            new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE)));
    public static final Block BB_FENCE_GATE = registerBlockWithItem("bb_fence_gate",
            new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE), WoodType.CRIMSON));
    public static final Block BB_WALL= registerBlockWithItem("bb_wall",
            new WallBlock(FabricBlockSettings.copyOf(Blocks.BRICK_WALL)));

    public static final Block BB_DOOR = registerBlockWithItem("bb_door",
            new DoorBlock(FabricBlockSettings.copyOf(Blocks.OAK_DOOR), BlockSetType.OAK));
    public static final Block BB_TRAPDOOR = registerBlockWithItem("bb_trapdoor",
            new TrapdoorBlock(FabricBlockSettings.copyOf(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));

    public static final Block BB_LEAVES = registerBlockWithItem("bb_leaves",
            new LeavesBlock(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES).strength(0.2f).nonOpaque()));


    private static Block registerBlock(String name, Block block) {
        return Registry.register(Registries.BLOCK, new Identifier(DownTheRabbitHole.MOD_ID, name), block);
    }

    private static Block registerBlockWithItem(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(DownTheRabbitHole.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, new Identifier(DownTheRabbitHole.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));
    }

    public static void registerModBlock() {
        DownTheRabbitHole.LOGGER.info("Registering ModBlocks for " + DownTheRabbitHole.MOD_ID);
    }

}
