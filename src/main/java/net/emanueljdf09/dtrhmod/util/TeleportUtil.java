package net.emanueljdf09.dtrhmod.util;

import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.emanueljdf09.dtrhmod.block.ModBlocks;
import net.emanueljdf09.dtrhmod.block.custom.mirror.MirrorBlock;
import net.emanueljdf09.dtrhmod.block.custom.mirror.MirrorType;
import net.emanueljdf09.dtrhmod.util.components.ProgressionComponent;
import net.emanueljdf09.dtrhmod.util.components.Mirror.MirrorComponent;
import net.emanueljdf09.dtrhmod.world.biome.ModBiomes;
import net.emanueljdf09.dtrhmod.world.dimension.ModDimensions;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.*;

public class TeleportUtil {

    private static final Map<UUID, MirrorTranceData> ACTIVE_TRANCES = new HashMap<>();

    private static final Map<UUID, Long> TELEPORT_COOLDOWNS = new HashMap<>();
    private static final long COOLDOWN_MS = 5000; // 2.5 seconds cooldown

    public static boolean hasTeleportCooldown(ServerPlayerEntity player) {
        long currentTime = System.currentTimeMillis();
        long lastTeleport = TELEPORT_COOLDOWNS.getOrDefault(player.getUuid(), 0L);
        return (currentTime - lastTeleport) < COOLDOWN_MS;
    }

    public static void updateTeleportCooldown(ServerPlayerEntity player) {
        TELEPORT_COOLDOWNS.put(player.getUuid(), System.currentTimeMillis());
    }

    public static class MirrorTranceData {
        public final BlockPos mirrorPos;
        public final ServerWorld sourceWorld;
        public int ticksRemaining;

        public MirrorTranceData(BlockPos mirrorPos, ServerWorld sourceWorld, int durationTicks) {
            this.mirrorPos = mirrorPos;
            this.sourceWorld = sourceWorld;
            this.ticksRemaining = durationTicks;
        }
    }

    public static boolean isPlayerInTranceAt(UUID playerUuid, BlockPos mirrorPos) {
        MirrorTranceData data = ACTIVE_TRANCES.get(playerUuid);
        return data != null && data.mirrorPos.equals(mirrorPos);
    }

    public static void teleport(ServerPlayerEntity player, RegistryKey<World> targetDim,
                                double x, double y, double z) {

        MinecraftServer server = player.getServer();
        if (server == null) return;

        ServerWorld world = server.getWorld(targetDim);
        if (world == null) return;

        player.teleport(world, x, y, z, player.getYaw(), player.getPitch());
    }

    public static void teleportToPlayerInstance(
            ServerPlayerEntity player,
            UUID ownerUuid,
            RegistryKey<World> targetDimKey,
            Identifier structureId,
            RegistryKey<World> originDimKey,  // 🌟 Added origin dimension
            BlockPos originPos               // 🌟 Added origin position
    ) {
        MinecraftServer server = player.getServer();
        if (server == null) return;

        ServerWorld destWorld = server.getWorld(targetDimKey);
        if (destWorld == null) {
            System.out.println("[DTRH ERROR] Could not find world for dimension: " + targetDimKey.getValue());
            return;
        }

        StorybookInstanceManager manager = StorybookInstanceManager.getServerState(server);

        // 🌟 Pass originDimKey and originPos to the instance manager!
        BlockPos instancePos = manager.getOrCreatePlayerInstance(
                server,
                ownerUuid,
                targetDimKey,
                structureId,
                originDimKey,
                originPos
        );

        System.out.println("[DTRH DEBUG] Teleporting player to: " + instancePos.toString() + " in " + targetDimKey.getValue());

        destWorld.getChunkManager().addTicket(
                ChunkTicketType.START,
                new ChunkPos(instancePos),
                2,
                Unit.INSTANCE
        );

        teleport(player, targetDimKey, instancePos.getX() + 0.5, instancePos.getY() + 1.0, instancePos.getZ() + 0.5);
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 200, 0));
    }

    public static void teleportFromHat(ServerPlayerEntity player, RegistryKey<World> targetDim, BlockPos targetPos) {
        MinecraftServer server = player.getServer();
        if (server == null) return;

        ServerWorld destWorld = server.getWorld(targetDim);
        if (destWorld == null) return;

        destWorld.getChunkManager().addTicket(
                net.minecraft.server.world.ChunkTicketType.START,
                new ChunkPos(targetPos),
                2,
                net.minecraft.util.Unit.INSTANCE
        );

        int spawnY = destWorld.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, targetPos).getY();
        if (spawnY <= destWorld.getBottomY()) {
            spawnY = 80;
        }

        teleport(player, targetDim, targetPos.getX() + 0.5, spawnY + 1.0, targetPos.getZ() + 0.5);
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 200, 0));
    }

    public static void checkAndHandleVoidReturn(ServerPlayerEntity player) {
        if (player.getWorld().getRegistryKey().equals(ModDimensions.STORYBOOK_LEVEL_KEY) && player.getY() < -5) {
            MinecraftServer server = player.getServer();
            if (server == null) return;

            StorybookInstanceManager manager = StorybookInstanceManager.getServerState(server);
            StorybookInstanceManager.ReturnLocation returnLoc = manager.getReturnLocation(player.getUuid());

            if (returnLoc != null) {
                ServerWorld originWorld = server.getWorld(returnLoc.dimension());
                if (originWorld != null) {

                    // 🌟 Offset landing spot 2 blocks NORTH of the origin hat position
                    BlockPos originPos = returnLoc.pos();
                    double spawnX = originPos.getX() + 0.5;
                    double spawnY = originPos.getY() + 1.0;
                    double spawnZ = originPos.getZ() - 1.5; // Offset away from the hat!

                    // Set teleport cooldown so landing doesn't re-trigger portal
                    updateTeleportCooldown(player);

                    player.teleport(
                            originWorld,
                            spawnX,
                            spawnY,
                            spawnZ,
                            player.getYaw(),
                            player.getPitch()
                    );

                    player.setVelocity(0, 0, 0);
                    player.fallDistance = 0.0f;
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 60, 0));
                }
            }
        }
    }

    public static BlockPos teleportToBiome(ServerPlayerEntity player, RegistryKey<World> targetDim, RegistryKey<Biome> primaryBiome, RegistryKey<Biome>... fallbacks) {
        MinecraftServer server = player.getServer();
        if (server == null) return null;

        ServerWorld destWorld = server.getWorld(targetDim);
        if (destWorld == null) return null;

        destWorld.getChunkManager().addTicket(
                net.minecraft.server.world.ChunkTicketType.START,
                new net.minecraft.util.math.ChunkPos(BlockPos.ORIGIN),
                1,
                net.minecraft.util.Unit.INSTANCE
        );
        BlockPos targetPos = locateBiomePos(destWorld, primaryBiome);

        if (targetPos == null && fallbacks != null) {
            for (RegistryKey<Biome> fallbackKey : fallbacks) {
                targetPos = locateBiomePos(destWorld, fallbackKey);
                if (targetPos != null) break;
            }
        }

        if (targetPos == null) {
            targetPos = new BlockPos(0, 80, 0);
            System.out.println("DTRH MOD: All biomes failed. Using hard fallback at 0, 80, 0");
        }

        int spawnY = destWorld.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, targetPos).getY();
        if (spawnY <= destWorld.getBottomY()) {
            spawnY = 90;
        } else {
            spawnY += 1;
        }

        BlockPos finalDestination = new BlockPos(targetPos.getX(), spawnY, targetPos.getZ());

        player.teleport(destWorld, finalDestination.getX() + 0.5, finalDestination.getY(), finalDestination.getZ() + 0.5, player.getYaw(), player.getPitch());
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 200, 0));

        return finalDestination;
    }

    public static void teleportToWonderland(ServerPlayerEntity player) {
        ProgressionComponent component = ModComponents.PROGRESSION_COMPONENT.get(player);
        BlockPos savedPos = component.getWonderlandSpawn();

        if (savedPos != null) {

            TeleportUtil.teleport(player, ModDimensions.WONDERLAND_LEVEL_KEY,
                    savedPos.getX() + 0.5, savedPos.getY(), savedPos.getZ() + 0.5);
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 300, 0));
        } else {

            BlockPos found = TeleportUtil.teleportToBiome(player,
                    ModDimensions.WONDERLAND_LEVEL_KEY,
                    ModBiomes.TULGEY_FOREST,
                    ModBiomes.TEAR_LAKE_VALLEY
            );
            component.setWonderlandSpawn(found);
        }
    }

    private static BlockPos locateBiomePos(ServerWorld world, RegistryKey<Biome> biomeKey) {
        BlockPos searchOrigin = new BlockPos(8, 64, 8);
        var result = world.locateBiome(entry -> entry.matchesKey(biomeKey), searchOrigin, 10000, 8, 64);
        return result != null ? result.getFirst() : null;
    }

    public static void startMirrorTrance(ServerPlayerEntity player, BlockPos clickedPos, ServerWorld world) {
        UUID uuid = player.getUuid();
        if (ACTIVE_TRANCES.containsKey(uuid)) return;

        player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 100, 0, false, false, false));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 255, false, false, false));

        world.playSound(null, clickedPos, net.minecraft.sound.SoundEvents.BLOCK_PORTAL_TRAVEL, net.minecraft.sound.SoundCategory.BLOCKS, 0.4f, 0.5f);

        // Warm up the destination chunk immediately during the 3 second countdown sequence
        MinecraftServer server = player.getServer();
        if (server != null) {
            RegistryKey<World> targetDimKey = (world.getRegistryKey() == World.OVERWORLD) ? ModDimensions.WONDERLAND_LEVEL_KEY : World.OVERWORLD;
            ServerWorld targetWorld = server.getWorld(targetDimKey);
            if (targetWorld != null) {
                targetWorld.getChunkManager().addTicket(
                        net.minecraft.server.world.ChunkTicketType.START,
                        new ChunkPos(clickedPos),
                        2,
                        net.minecraft.util.Unit.INSTANCE
                );
            }
        }

        ACTIVE_TRANCES.put(uuid, new MirrorTranceData(clickedPos, world, 60));
    }

    public static void tickMirrorTrances(MinecraftServer server) {
        if (ACTIVE_TRANCES.isEmpty()) return;

        List<UUID> completedTrances = new ArrayList<>();

        for (Map.Entry<UUID, MirrorTranceData> entry : ACTIVE_TRANCES.entrySet()) {
            UUID playerUuid = entry.getKey();
            MirrorTranceData data = entry.getValue();
            ServerPlayerEntity player = server.getPlayerManager().getPlayer(playerUuid);

            if (player == null) {
                completedTrances.add(playerUuid);
                continue;
            }

            data.ticksRemaining--;

            if (data.ticksRemaining <= 0) {
                handleMirrorTeleport(player, data.mirrorPos, data.sourceWorld);
                player.removeStatusEffect(StatusEffects.BLINDNESS);
                player.removeStatusEffect(StatusEffects.SLOWNESS);
                completedTrances.add(playerUuid);
            }
        }

        for (UUID uuid : completedTrances) {
            ACTIVE_TRANCES.remove(uuid);
        }
    }

    public static void handleMirrorTeleport(ServerPlayerEntity player, BlockPos clickedPos, ServerWorld sourceWorld) {
        RegistryKey<World> currentDimKey = sourceWorld.getRegistryKey();

        ServerWorld overworld = player.getServer().getWorld(World.OVERWORLD);
        ServerWorld wonderland = player.getServer().getWorld(ModDimensions.WONDERLAND_LEVEL_KEY);
        if (overworld == null || wonderland == null) return;

        MirrorComponent overworldNetwork = ModComponents.MIRROR_COMPONENT.get(overworld);
        MirrorComponent wonderlandNetwork = ModComponents.MIRROR_COMPONENT.get(wonderland);

        MirrorComponent currentNetwork = (currentDimKey == World.OVERWORLD) ? overworldNetwork : wonderlandNetwork;
        Optional<BlockPos> destinationOpt = currentNetwork.getDestination(clickedPos);

        RegistryKey<World> targetDim = (currentDimKey == World.OVERWORLD) ? ModDimensions.WONDERLAND_LEVEL_KEY : World.OVERWORLD;
        ServerWorld targetWorld = (currentDimKey == World.OVERWORLD) ? wonderland : overworld;

        BlockPos targetPos;

        // 1. PATH A: Already Linked
        if (destinationOpt.isPresent()) {
            targetPos = destinationOpt.get();
            teleportPlayerToMirrorFront(player, targetWorld, targetPos);
            return;
        }

        // 2. PATH B: Structural Radius Search Scan
        BlockPos existingMirrorNearby = findMirrorInRadiusColumn(targetWorld, clickedPos, 8);
        if (existingMirrorNearby != null) {
            targetPos = existingMirrorNearby;

            overworldNetwork.linkMirrors((currentDimKey == World.OVERWORLD) ? clickedPos : targetPos, (currentDimKey == World.OVERWORLD) ? targetPos : clickedPos);
            wonderlandNetwork.linkMirrors((currentDimKey == World.OVERWORLD) ? targetPos : clickedPos, (currentDimKey == World.OVERWORLD) ? clickedPos : targetPos);

            teleportPlayerToMirrorFront(player, targetWorld, targetPos);
            return;
        }

        // 3. PATH C: Fallback Generation
        BlockState sourceState = sourceWorld.getBlockState(clickedPos);
        MirrorType sourceMirrorType = MirrorType.wonderland;
        Direction mirrorFacing = Direction.NORTH;

        if (sourceState.isOf(ModBlocks.MIRROR_BLOCK)) {
            sourceMirrorType = sourceState.get(MirrorBlock.TYPE);
            mirrorFacing = sourceState.get(MirrorBlock.FACING);
        }

        int spawnY = targetWorld.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, clickedPos).getY();
        // Fallback safety to stop bedrock generation loops
        if (spawnY <= targetWorld.getBottomY()) {
            spawnY = (targetDim == World.OVERWORLD) ? 70 : 90;
        }
        targetPos = new BlockPos(clickedPos.getX(), spawnY, clickedPos.getZ());

        BlockPos actualExitMirrorPos = targetPos;

        // --- BRANCH C1: STRUCTURE PORTAL ROOM TYPE ---
        if (sourceMirrorType == MirrorType.structure) {
            MinecraftServer server = player.getServer();
            if (server != null) {
                StructureTemplateManager templateManager = server.getStructureTemplateManager();

                Identifier targetTemplateId = (targetDim == World.OVERWORLD)
                        ? new Identifier(DownTheRabbitHole.MOD_ID, "portal/mirror_room_ruined")
                        : new Identifier(DownTheRabbitHole.MOD_ID, "portal/mirror_room_complete");

                Optional<StructureTemplate> templateOpt = templateManager.getTemplate(targetTemplateId);

                if (templateOpt.isPresent()) {
                    BlockRotation targetRotation = BlockRotation.NONE;
                    switch (mirrorFacing) {
                        case SOUTH -> targetRotation = BlockRotation.CLOCKWISE_180;
                        case WEST -> targetRotation = BlockRotation.COUNTERCLOCKWISE_90;
                        case EAST -> targetRotation = BlockRotation.CLOCKWISE_90;
                    }

                    StructurePlacementData placementData = new StructurePlacementData().setRotation(targetRotation);
                    templateOpt.get().place(targetWorld, targetPos, targetPos, placementData, targetWorld.getRandom(), 3);

                    boolean mirrorFound = false;
                    BlockPos.Mutable mutablePos = new BlockPos.Mutable();

                    for (int x = -8; x < 8; x++) {
                        for (int y = -2; y < 10; y++) {
                            for (int z = -8; z < 8; z++) {
                                mutablePos.set(targetPos.getX() + x, targetPos.getY() + y, targetPos.getZ() + z);

                                BlockState checkState = targetWorld.getBlockState(mutablePos);
                                if (checkState.isOf(ModBlocks.MIRROR_BLOCK) && checkState.get(MirrorBlock.HALF) == DoubleBlockHalf.LOWER) {
                                    actualExitMirrorPos = mutablePos.toImmutable();
                                    mirrorFound = true;
                                    break;
                                }
                            }
                            if (mirrorFound) break;
                        }
                        if (mirrorFound) break;
                    }

                    BlockState exitLowerState = targetWorld.getBlockState(actualExitMirrorPos);
                    BlockState exitUpperState = targetWorld.getBlockState(actualExitMirrorPos.up());

                    if (exitLowerState.isOf(ModBlocks.MIRROR_BLOCK)) {
                        targetWorld.setBlockState(actualExitMirrorPos, exitLowerState.with(MirrorBlock.TYPE, MirrorType.wonderland), 3);
                    }
                    if (exitUpperState.isOf(ModBlocks.MIRROR_BLOCK)) {
                        targetWorld.setBlockState(actualExitMirrorPos.up(), exitUpperState.with(MirrorBlock.TYPE, MirrorType.wonderland), 3);
                    }
                }
            }
        }
        // --- BRANCH C2: STANDALONE PORTAL TYPE ---
        else {
            BlockState lowerState = ModBlocks.MIRROR_BLOCK.getDefaultState()
                    .with(MirrorBlock.HALF, DoubleBlockHalf.LOWER)
                    .with(MirrorBlock.TYPE, MirrorType.wonderland)
                    .with(MirrorBlock.FACING, mirrorFacing);

            targetWorld.setBlockState(targetPos, lowerState, 3);
            targetWorld.setBlockState(targetPos.up(), lowerState.with(MirrorBlock.HALF, DoubleBlockHalf.UPPER), 3);

            actualExitMirrorPos = targetPos;
        }

        // FORCE NORMALIZE BOTH BLOCKS BEFORE DATA-LINK SYNC RUNS
        BlockState sourceLowerState = sourceWorld.getBlockState(clickedPos);
        BlockState sourceUpperState = sourceWorld.getBlockState(clickedPos.up());
        if (sourceLowerState.isOf(ModBlocks.MIRROR_BLOCK)) {
            sourceWorld.setBlockState(clickedPos, sourceLowerState.with(MirrorBlock.TYPE, MirrorType.wonderland), 3);
        }
        if (sourceUpperState.isOf(ModBlocks.MIRROR_BLOCK)) {
            sourceWorld.setBlockState(clickedPos.up(), sourceUpperState.with(MirrorBlock.TYPE, MirrorType.wonderland), 3);
        }

        overworldNetwork.linkMirrors((currentDimKey == World.OVERWORLD) ? clickedPos : actualExitMirrorPos, (currentDimKey == World.OVERWORLD) ? actualExitMirrorPos : clickedPos);
        wonderlandNetwork.linkMirrors((currentDimKey == World.OVERWORLD) ? actualExitMirrorPos : clickedPos, (currentDimKey == World.OVERWORLD) ? clickedPos : actualExitMirrorPos);

        targetPos = actualExitMirrorPos;
        teleportPlayerToMirrorFront(player, targetWorld, targetPos);
    }

    private static void teleportPlayerToMirrorFront(ServerPlayerEntity player, ServerWorld targetWorld, BlockPos targetPos) {
        BlockState targetState = targetWorld.getBlockState(targetPos);
        Direction facing = Direction.NORTH;

        if (targetState.isOf(ModBlocks.MIRROR_BLOCK)) {
            facing = targetState.get(MirrorBlock.FACING);
        }

        double teleportX = targetPos.getX() + 0.5;
        double teleportY = targetPos.getY();
        double teleportZ = targetPos.getZ() + 0.5;
        float targetYaw = player.getYaw();

        switch (facing) {
            case NORTH -> { teleportZ -= 1.2; targetYaw = 180.0F; }
            case SOUTH -> { teleportZ += 1.2; targetYaw = 0.0F; }
            case WEST  -> { teleportX -= 1.2; targetYaw = 90.0F; }
            case EAST  -> { teleportX += 1.2; targetYaw = 270.0F; }
        }

        MinecraftServer server = player.getServer();
        if (server != null) {
            player.teleport(targetWorld, teleportX, teleportY, teleportZ, targetYaw, player.getPitch());
        }

        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 200, 0));
    }

    @Nullable
    private static BlockPos findMirrorInRadiusColumn(ServerWorld targetWorld, BlockPos center, int radius) {
        MirrorComponent targetNetwork = ModComponents.MIRROR_COMPONENT.get(targetWorld);
        BlockPos.Mutable mutablePos = new BlockPos.Mutable();

        int minY = targetWorld.getBottomY();
        int maxY = targetWorld.getTopY();

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                for (int y = minY; y < maxY; y++) {
                    mutablePos.set(center.getX() + x, y, center.getZ() + z);
                    BlockState state = targetWorld.getBlockState(mutablePos);

                    if (state.isOf(ModBlocks.MIRROR_BLOCK)) {
                        BlockPos lowerBasePos = (state.get(MirrorBlock.HALF) == DoubleBlockHalf.LOWER) ? mutablePos.toImmutable() : mutablePos.down().toImmutable();
                        BlockState verifiedState = targetWorld.getBlockState(lowerBasePos);

                        if (verifiedState.isOf(ModBlocks.MIRROR_BLOCK) && verifiedState.get(MirrorBlock.HALF) == DoubleBlockHalf.LOWER) {
                            MirrorType type = verifiedState.get(MirrorBlock.TYPE);
                            if (type == MirrorType.wonderland || type == MirrorType.structure) {
                                if (targetNetwork.getDestination(lowerBasePos).isEmpty()) {
                                    return lowerBasePos;
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
}

