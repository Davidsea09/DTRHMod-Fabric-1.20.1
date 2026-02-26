package net.emanueljdf09.dtrhmod;

import net.emanueljdf09.dtrhmod.block.ModBlocks;
import net.emanueljdf09.dtrhmod.item.ModItemGroups;
import net.emanueljdf09.dtrhmod.item.ModItems;
import net.emanueljdf09.dtrhmod.world.features.tree.deco.ModTreeDeco;
import net.fabricmc.api.ModInitializer;


import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.emanueljdf09.dtrhmod.world.dimension.ModDimensions.EXTERIOR_LEVEL_KEY;

public class DownTheRabbitHole implements ModInitializer {
	public static final String MOD_ID = "dtrhmod";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModItemGroups.registerItemGroups();
		ModBlocks.registerModBlock();
		ModTreeDeco.register();

		StrippableBlockRegistry.register(ModBlocks.TH_LOG, ModBlocks.STRIPPED_TH_LOG);
		StrippableBlockRegistry.register(ModBlocks.TH_WOOD, ModBlocks.STRIPPED_TH_WOOD);

		FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.TH_LOG, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.STRIPPED_TH_WOOD, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.STRIPPED_TH_LOG, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.TH_WOOD, 5, 5);

		FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.TH_PLANKS, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.TH_LEAVES, 30, 60);

		StrippableBlockRegistry.register(ModBlocks.BB_LOG, ModBlocks.STRIPPED_BB_LOG);
		StrippableBlockRegistry.register(ModBlocks.BB_WOOD, ModBlocks.STRIPPED_BB_WOOD);

		FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.BB_LOG, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.STRIPPED_BB_WOOD, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.STRIPPED_BB_LOG, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.BB_WOOD, 5, 5);

		FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.BB_PLANKS, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.BB_LEAVES, 30, 60);

		StrippableBlockRegistry.register(ModBlocks.WW_LOG, ModBlocks.STRIPPED_WW_LOG);
		StrippableBlockRegistry.register(ModBlocks.WW_WOOD, ModBlocks.STRIPPED_WW_WOOD);

		FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.WW_LOG, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.STRIPPED_WW_WOOD, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.STRIPPED_WW_LOG, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.WW_WOOD, 5, 5);

		FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.WW_PLANKS, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.WW_LEAVES, 30, 60);
		FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.WW_HANGING_LEAVES, 30, 60);
		FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.WW_HANGING_LEAVES_PLANT, 30, 60);

		LOGGER.info("Follow the tunnel...");

		ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
			if (entity instanceof ServerPlayerEntity player) {
				if (world.getRegistryKey().equals(EXTERIOR_LEVEL_KEY)) {

					BlockPos spawnPos = new BlockPos(0, 64, 0);

					if (world.getBlockState(spawnPos).isAir()) {

						world.getStructureTemplateManager().getTemplate(new Identifier(DownTheRabbitHole.MOD_ID, "exterior/intro_beta"))
								.ifPresentOrElse(template -> {
									StructurePlacementData data = new StructurePlacementData();
									template.place(world, spawnPos, spawnPos, data, world.getRandom(), 3);

									world.setSpawnPos(new BlockPos(10, 68, 5), 0.0f);

									System.out.println("[DTRH] Successfully placed intro_beta at 0, 64, 0");
								}, () -> {
									System.err.println("[DTRH] ERROR: Could not find NBT file at data/dtrhmod/structures/intro_beta.nbt");
								});
					}
				}
			}
		});

	}

}