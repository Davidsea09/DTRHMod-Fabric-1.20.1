package net.emanueljdf09.dtrhmod;

import net.emanueljdf09.dtrhmod.block.ModBlockCollections;
import net.emanueljdf09.dtrhmod.block.ModBlockEntities;
import net.emanueljdf09.dtrhmod.block.ModBlocks;
import net.emanueljdf09.dtrhmod.entity.ModBoats;
import net.emanueljdf09.dtrhmod.entity.ModEntities;
import net.emanueljdf09.dtrhmod.entity.ModSpawners;
import net.emanueljdf09.dtrhmod.item.ModItemGroups;
import net.emanueljdf09.dtrhmod.item.ModItems;
import net.emanueljdf09.dtrhmod.menu.ModScreenHandlers;
import net.emanueljdf09.dtrhmod.recipe.ModRecipes;
import net.emanueljdf09.dtrhmod.util.*;
import net.emanueljdf09.dtrhmod.util.components.ProgressionComponent;
import net.emanueljdf09.dtrhmod.world.dimension.ModDimensions;
import net.emanueljdf09.dtrhmod.world.features.tree.deco.ModTreeDeco;
import net.emanueljdf09.dtrhmod.world.structures.ModStructurePieces;
import net.emanueljdf09.dtrhmod.world.structures.ModStructureProcessors;
import net.emanueljdf09.dtrhmod.world.structures.ModStructures;
import net.fabricmc.api.ModInitializer;


import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import static net.emanueljdf09.dtrhmod.world.dimension.ModDimensions.EXTERIOR_LEVEL_KEY;

public class DownTheRabbitHole implements ModInitializer {
	public static final String MOD_ID = "dtrhmod";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModItemGroups.registerItemGroups();
		ModBlockCollections.registerModBlockCollections();
		ModBlocks.registerModBlock();
		ModBlockEntities.registerBlockEnities();
		ModBoats.registerBoats();
		ModParticles.registerParticles();
		ModEntities.registerModEntities();
		//ModSpawners.registerModSpawners();
		ModEffects.registerModEffects();
		ModEffects.registerBrewingRecipes();
		ModTreeDeco.register();
		ModStructureProcessors.register();
		ModStructurePieces.register();
		ModStructures.registerStructures();
		WonderlandProgressionUtil.register();
		ModCommands.register();
		ModScreenHandlers.registerScreenHandlers();
		ModRecipes.registerRecipes();

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

		ServerTickEvents.START_SERVER_TICK.register(TeleportUtil::tickMirrorTrances);
		ServerTickEvents.START_SERVER_TICK.register(TeleportUtil::tickHatTrances);
		ServerTickEvents.START_SERVER_TICK.register(TeleportUtil::tickBlockTrances);

		ServerTickEvents.END_WORLD_TICK.register(world -> {
			if (world.getRegistryKey().equals(ModDimensions.STORYBOOK_LEVEL_KEY)) {

				for (ServerPlayerEntity player : new ArrayList<>(world.getPlayers())) {
					TeleportUtil.checkAndHandleVoidReturn(player);
				}
			}

			if (world.getRegistryKey().equals(ModDimensions.WONDERLAND_LEVEL_KEY)) {
				for (ServerPlayerEntity player : new ArrayList<>(world.getPlayers())) {
					TeleportUtil.checkAndHandleWonderlandVoidReturn(player);
				}
			}
		});

		ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
			if (entity instanceof ServerPlayerEntity player) {

				if (world.getRegistryKey().equals(EXTERIOR_LEVEL_KEY)) {

					BlockPos spawnPos = new BlockPos(0, 62, 0);

					world.setSpawnPos(new BlockPos(3, 70, 2), 0.0f);

						world.getStructureTemplateManager().getTemplate(new Identifier(DownTheRabbitHole.MOD_ID, "exterior/exterior_room"))
								.ifPresentOrElse(template -> {
									StructurePlacementData data = new StructurePlacementData()
											.setIgnoreEntities(false)
											.setUpdateNeighbors(true);
									template.place(world, spawnPos, spawnPos, data, world.getRandom(), 3);

									world.updateNeighbors(spawnPos, ModBlocks.EXTERIOR_DOOR);

									System.out.println("[DTRH] Successfully placed exterior_room at 0, 62, 0");
								}, () -> {
									System.err.println("[DTRH] ERROR: Could not find NBT file at data/dtrhmod/structures/exterior_room.nbt");
								});

						ProgressionComponent component = ModComponents.PROGRESSION_COMPONENT.get(player);

							if (!component.hasDoneExterior()) {
								ModPackets.sendOpenBookPacket(player, "wonderland_intro");
							}

				}
			}
		});

	}

}