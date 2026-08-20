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
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.text.Text;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class TeleportUtil {

    private static final Map<UUID, MirrorTranceData> ACTIVE_TRANCES = new HashMap<>();
    private static final Map<UUID, HatTranceData> ACTIVE_HAT_TRANCES = new HashMap<>();
    private static final Map<UUID, BlockTranceData> ACTIVE_BLOCK_TRANCES = new HashMap<>();

    private static final Set<UUID> TELEPORTING_PLAYERS = new HashSet<>();

    private static final Map<UUID, Long> TELEPORT_COOLDOWNS = new HashMap<>();
    private static final long COOLDOWN_MS = 5000;

    public static boolean hasTeleportCooldown(ServerPlayerEntity player) {
        long currentTime = System.currentTimeMillis();
        long lastTeleport = TELEPORT_COOLDOWNS.getOrDefault(player.getUuid(), 0L);
        return (currentTime - lastTeleport) < COOLDOWN_MS;
    }

    public static void updateTeleportCooldown(ServerPlayerEntity player) {
        TELEPORT_COOLDOWNS.put(player.getUuid(), System.currentTimeMillis());
    }


    public static class HatTranceData {
        public final BlockPos hatPos;
        public final RegistryKey<World> targetDim;
        public final boolean isInstance;
        public final Identifier structureId;
        public final RegistryKey<World> originDim;
        public final UUID ownerUuid;
        public int ticksRemaining;

        public HatTranceData(BlockPos hatPos, RegistryKey<World> targetDim, boolean isInstance, Identifier structureId, RegistryKey<World> originDim, UUID ownerUuid, int durationTicks) {
            this.hatPos = hatPos;
            this.targetDim = targetDim;
            this.isInstance = isInstance;
            this.structureId = structureId;
            this.originDim = originDim;
            this.ownerUuid = ownerUuid;
            this.ticksRemaining = durationTicks;
        }
    }

    public static void startHatTrance(ServerPlayerEntity player, BlockPos hatPos, RegistryKey<World> targetDim, boolean isInstance, Identifier structureId, RegistryKey<World> originDim, UUID ownerUuid) {
        UUID uuid = player.getUuid();
        if (ACTIVE_HAT_TRANCES.containsKey(uuid)) return;

        player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 40, 0, false, false, false));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 40, 255, false, false, false));

        player.getWorld().playSound(null, hatPos, SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.BLOCKS, 0.4f, 0.5f);

        ACTIVE_HAT_TRANCES.put(uuid, new HatTranceData(hatPos, targetDim, isInstance, structureId, originDim, ownerUuid, 30)); // 30 ticks = 1.5 seconds
    }

    public static void tickHatTrances(MinecraftServer server) {
        if (ACTIVE_HAT_TRANCES.isEmpty()) return;

        List<UUID> completed = new ArrayList<>();

        for (Map.Entry<UUID, HatTranceData> entry : ACTIVE_HAT_TRANCES.entrySet()) {
            UUID playerUuid = entry.getKey();
            HatTranceData data = entry.getValue();
            ServerPlayerEntity player = server.getPlayerManager().getPlayer(playerUuid);

            if (player == null) {
                completed.add(playerUuid);
                continue;
            }

            data.ticksRemaining--;

            if (data.ticksRemaining <= 0) {
                if (data.isInstance) {
                    teleportToPlayerInstance(player, data.ownerUuid, data.targetDim, data.structureId, data.originDim, data.hatPos);
                } else {
                    teleportFromHat(player, data.targetDim, data.hatPos);
                }

                player.removeStatusEffect(StatusEffects.BLINDNESS);
                player.removeStatusEffect(StatusEffects.SLOWNESS);
                completed.add(playerUuid);
            }
        }

        for (UUID uuid : completed) {
            ACTIVE_HAT_TRANCES.remove(uuid);
        }
    }

    public static class BlockTranceData {
        public final BlockPos blockPos;
        public final ServerWorld sourceWorld;
        public final Runnable onComplete;
        public int ticksRemaining;

        public BlockTranceData(BlockPos blockPos, ServerWorld sourceWorld, Runnable onComplete, int durationTicks) {
            this.blockPos = blockPos;
            this.sourceWorld = sourceWorld;
            this.onComplete = onComplete;
            this.ticksRemaining = durationTicks;
        }
    }

    public static void startBlockTrance(ServerPlayerEntity player, BlockPos blockPos, ServerWorld world, Runnable onComplete, int durationTicks) {
        UUID uuid = player.getUuid();
        if (ACTIVE_BLOCK_TRANCES.containsKey(uuid)) return;

        player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, durationTicks + 10, 0, false, false, false));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, durationTicks + 10, 255, false, false, false));

        world.playSound(null, blockPos, SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.BLOCKS, 0.4f, 0.5f);

        ACTIVE_BLOCK_TRANCES.put(uuid, new BlockTranceData(blockPos, world, onComplete, durationTicks));
    }

    public static void tickBlockTrances(MinecraftServer server) {
        if (ACTIVE_BLOCK_TRANCES.isEmpty()) return;

        List<UUID> completedTrances = new ArrayList<>();

        for (Map.Entry<UUID, BlockTranceData> entry : ACTIVE_BLOCK_TRANCES.entrySet()) {
            UUID playerUuid = entry.getKey();
            BlockTranceData data = entry.getValue();
            ServerPlayerEntity player = server.getPlayerManager().getPlayer(playerUuid);

            if (player == null) {
                completedTrances.add(playerUuid);
                continue;
            }

            data.ticksRemaining--;

            if (data.ticksRemaining <= 0) {
                if (data.onComplete != null) {
                    data.onComplete.run();
                }
                player.removeStatusEffect(StatusEffects.BLINDNESS);
                player.removeStatusEffect(StatusEffects.SLOWNESS);
                completedTrances.add(playerUuid);
            }
        }

        for (UUID uuid : completedTrances) {
            ACTIVE_BLOCK_TRANCES.remove(uuid);
        }
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

        player.setVelocity(Vec3d.ZERO);
        player.fallDistance = 0.0f;

        player.teleport(world, x, y, z, player.getYaw(), player.getPitch());


    }

    public static void teleportToPlayerInstance(
            ServerPlayerEntity player,
            UUID ownerUuid,
            RegistryKey<World> targetDimKey,
            Identifier structureId,
            RegistryKey<World> originDimKey,
            BlockPos originPos
    ) {
        if (TELEPORTING_PLAYERS.contains(player.getUuid())) return;
        TELEPORTING_PLAYERS.add(player.getUuid());

        MinecraftServer server = player.getServer();
        if (server == null) {
            TELEPORTING_PLAYERS.remove(player.getUuid());
            return;
        }

        ServerWorld destWorld = server.getWorld(targetDimKey);
        if (destWorld == null) {
            System.out.println("[DTRH ERROR] Could not find world for dimension: " + targetDimKey.getValue());
            return;
        }

        StorybookInstanceManager manager = StorybookInstanceManager.getServerState(server);

        manager.registerVisitorReturn(player.getUuid(), originDimKey, originPos);

        BlockPos instancePos = manager.getOrCreatePlayerInstance(
                server,
                ownerUuid,
                targetDimKey,
                structureId,
                originDimKey,
                originPos
        );

        System.out.println("[DTRH DEBUG] Teleporting player to: " + instancePos.toString() + " in " + targetDimKey.getValue());

        ChunkPos chunkPos = new ChunkPos(instancePos);
        destWorld.getChunkManager().addTicket(
                ChunkTicketType.START,
                chunkPos,
                2,
                Unit.INSTANCE
        );

        teleport(player, targetDimKey, instancePos.getX() + 0.5, instancePos.getY() + 1.0, instancePos.getZ() + 0.5);
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 200, 0));

        server.execute(() -> {

            server.getWorld(targetDimKey).getServer().submit(() -> {

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        TELEPORTING_PLAYERS.remove(player.getUuid());
                    }
                }, 1000);
            });
        });
    }

    public static void teleportFromHat(ServerPlayerEntity player, RegistryKey<World> targetDim, BlockPos targetPos) {
        if (TELEPORTING_PLAYERS.contains(player.getUuid())) return;
        TELEPORTING_PLAYERS.add(player.getUuid());

        MinecraftServer server = player.getServer();
        if (server == null) {
            TELEPORTING_PLAYERS.remove(player.getUuid());
            return;
        }

        ServerWorld destWorld = server.getWorld(targetDim);
        if (destWorld == null) return;

        BlockPos finalTargetPos = targetPos;
        if (targetDim.equals(World.END)) {
            finalTargetPos = new BlockPos(0, targetPos.getY() + 10, 0);
        }

        ChunkPos chunkPos = new ChunkPos(finalTargetPos);
        destWorld.getChunkManager().addTicket(
                ChunkTicketType.START,
                chunkPos,
                2,
                Unit.INSTANCE
        );

        destWorld.getChunk(chunkPos.x, chunkPos.z);

        BlockPos surfacePos = findCustomSurface(destWorld, targetPos.getX(), targetPos.getZ(), 120);

        teleport(player, targetDim, surfacePos.getX() + 0.5, surfacePos.getY() + 1.0, surfacePos.getZ() + 0.5);
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 600, 0));

        server.execute(() -> {

            server.getWorld(targetDim).getServer().submit(() -> {

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        TELEPORTING_PLAYERS.remove(player.getUuid());
                    }
                }, 1000);
            });
        });
    }

    public static void checkAndHandleVoidReturn(ServerPlayerEntity player) {
        if (player.getWorld().getRegistryKey().equals(ModDimensions.STORYBOOK_LEVEL_KEY) && player.getY() < -5) {
            MinecraftServer server = player.getServer();
            if (server == null) return;

            StorybookInstanceManager manager = StorybookInstanceManager.getServerState(server);

            StorybookInstanceManager.ReturnLocation returnLoc = manager.getReturnLocation(player.getUuid());

            if (returnLoc == null || (returnLoc.dimension().equals(World.OVERWORLD) && returnLoc.pos().equals(new BlockPos(0, 64, 0)))) {
                double playerX = player.getX();
                int instanceIndex = (int) Math.floor((playerX + 2500) / 5000);

                for (Map.Entry<UUID, BlockPos> entry : manager.getPlayerInstancesMap().entrySet()) {
                    BlockPos instanceCenter = entry.getValue();
                    if (Math.abs(instanceCenter.getX() - playerX) < 2500) {
                        StorybookInstanceManager.ReturnLocation ownerReturn = manager.getReturnLocation(entry.getKey());
                        if (ownerReturn != null) {
                            returnLoc = ownerReturn;
                            break;
                        }
                    }
                }
            }

            if (returnLoc != null) {
                ServerWorld originWorld = server.getWorld(returnLoc.dimension());
                if (originWorld != null) {
                    BlockPos originPos = returnLoc.pos();
                    double spawnX = originPos.getX() + 0.5;

                    int safeY = originWorld.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, originPos).getY();
                    if (safeY <= originWorld.getBottomY()) safeY = 80;

                    updateTeleportCooldown(player);
                    player.teleport(originWorld, spawnX, safeY + 1.0, originPos.getZ() - 1.5, player.getYaw(), player.getPitch());
                    player.setVelocity(0, 0, 0);
                    player.fallDistance = 0.0f;
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 60, 0));
                    return;
                }
            }

            ServerWorld overworld = server.getOverworld();
            if (overworld != null) {
                BlockPos worldSpawn = overworld.getSpawnPos();
                int safeSpawnY = overworld.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, worldSpawn).getY();

                updateTeleportCooldown(player);
                player.teleport(overworld, worldSpawn.getX() + 0.5, safeSpawnY + 1.0, worldSpawn.getZ() + 0.5, player.getYaw(), player.getPitch());
                player.setVelocity(0, 0, 0);
                player.fallDistance = 0.0f;
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 60, 0));
            }
        }
    }

    public static void checkAndHandleWonderlandVoidReturn(ServerPlayerEntity player) {
        if (player.getWorld().getRegistryKey().equals(ModDimensions.WONDERLAND_LEVEL_KEY) && player.getY() < -5) {
            MinecraftServer server = player.getServer();
            if (server == null) return;

            BlockPos spawnPos = player.getSpawnPointPosition();
            RegistryKey<World> spawnDim = player.getSpawnPointDimension();
            boolean isSpawnForced = player.isSpawnForced();
            float spawnAngle = player.getSpawnAngle();

            ServerWorld targetWorld = spawnDim != null ? server.getWorld(spawnDim) : null;
            if (targetWorld == null) {
                targetWorld = server.getOverworld();
            }

            boolean successfullyTeleported = false;

            if (spawnPos != null && targetWorld != null) {
                var respawnPos = PlayerEntity.findRespawnPosition(targetWorld, spawnPos, spawnAngle, isSpawnForced, false);

                if (respawnPos.isPresent()) {
                    Vec3d pos = respawnPos.get();

                    updateTeleportCooldown(player);
                    player.teleport(
                            targetWorld,
                            pos.getX(),
                            pos.getY(),
                            pos.getZ(),
                            spawnAngle,
                            0.0f
                    );

                    successfullyTeleported = true;
                } else {

                    player.sendMessage(Text.translatable("block.minecraft.spawn_obstructed"), true);
                }
            }

            if (!successfullyTeleported && targetWorld != null) {
                BlockPos worldSpawn = targetWorld.getSpawnPos();

                updateTeleportCooldown(player);
                player.teleport(
                        targetWorld,
                        worldSpawn.getX() + 0.5,
                        worldSpawn.getY(),
                        worldSpawn.getZ() + 0.5,
                        player.getYaw(),
                        player.getPitch()
                );
            }

            player.setVelocity(0, 0, 0);
            player.fallDistance = 0.0f;
            return;
        }
    }

    public static void teleportToWonderland(ServerPlayerEntity player) {
        ProgressionComponent component = ModComponents.PROGRESSION_COMPONENT.get(player);
        BlockPos savedPos = component.getWonderlandSpawn();

        if (savedPos != null) {

            TeleportUtil.teleport(player, ModDimensions.WONDERLAND_LEVEL_KEY,
                    savedPos.getX() + 0.5, savedPos.getY() + 5, savedPos.getZ() + 0.5);
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 600, 0));
        } else {

            BlockPos found = TeleportUtil.teleportToSafeBiomeWithStructure(
                    player,
                    ModDimensions.WONDERLAND_LEVEL_KEY,
                    ModBiomes.TULGEY_WOOD,
                    null,
                    ModBiomes.VALE_OF_TEARS
            );
            component.setWonderlandSpawn(found);
        }
    }

    private static BlockPos locateBiomePos(ServerWorld world, RegistryKey<Biome> biomeKey) {
        BlockPos searchOrigin = new BlockPos(8, 64, 8);

        var result = world.locateBiome(holder -> holder.getKey().map(key -> key.equals(biomeKey)).orElse(false), searchOrigin, 10000, 32, 64);

        if (result != null) {
            BlockPos initialPos = result.getFirst();

           BlockPos.Mutable mutable = new BlockPos.Mutable();
            for (int x = -48; x <= 48; x += 16) {
                for (int z = -48; z <= 48; z += 16) {
                    mutable.set(initialPos.getX() + x, 64, initialPos.getZ() + z);
                    var currentBiomeHolder = world.getBiome(mutable);

                    boolean matches = currentBiomeHolder.getKey().map(key -> key.equals(biomeKey)).orElse(false);
                    if (matches) {
                        return mutable.toImmutable();
                    }
                }
            }
            return initialPos;
        }
        return null;
    }

    public static BlockPos teleportToSafeBiomeWithStructure(ServerPlayerEntity player, RegistryKey<World> targetDim, RegistryKey<Biome> primaryBiome, @Nullable Identifier structureToSpawn, RegistryKey<Biome>... fallbacks) {
        MinecraftServer server = player.getServer();
        if (server == null) return null;

        ServerWorld destWorld = server.getWorld(targetDim);
        if (destWorld == null) return null;

        BlockPos targetPos = locateBiomePos(destWorld, primaryBiome);

        if (targetPos == null && fallbacks != null) {
            for (RegistryKey<Biome> fallbackKey : fallbacks) {
                targetPos = locateBiomePos(destWorld, fallbackKey);
                if (targetPos != null) break;
            }
        }

        if (targetPos == null) {
            targetPos = new BlockPos(0, 80, 0);
        }

        ChunkPos chunkPos = new ChunkPos(targetPos);
        destWorld.getChunkManager().addTicket(ChunkTicketType.START, chunkPos, 2, Unit.INSTANCE);
        destWorld.getChunk(chunkPos.x, chunkPos.z);

        BlockPos surfacePos = findCustomSurface(destWorld, targetPos.getX(), targetPos.getZ(), 120);
        System.out.println("[DTRH DEBUG] Helper Scanner Found Surface at: " + surfacePos);

       clearLogsAndLeavesInRadius(destWorld, surfacePos, 1, 20);

        if (structureToSpawn != null) {
            StructureTemplateManager templateManager = server.getStructureTemplateManager();
            Optional<StructureTemplate> templateOpt = templateManager.getTemplate(structureToSpawn);

            if (templateOpt.isPresent()) {
                StructurePlacementData placementData = new StructurePlacementData();
                templateOpt.get().place(destWorld, surfacePos, surfacePos, placementData, destWorld.getRandom(), 3);
            }
        }

        int spawnOffset = 10;
        double spawnX = surfacePos.getX() + 0.5;
        double spawnY = surfacePos.getY() + 1.0 + spawnOffset;
        double spawnZ = surfacePos.getZ() + 0.5;

        player.teleport(destWorld, spawnX, spawnY, spawnZ, player.getYaw(), player.getPitch());
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 600, 0));

        return surfacePos;
    }

    public static void clearLogsAndLeavesInRadius(ServerWorld world, BlockPos center, int horizontalRadius, int verticalHeight) {
        System.out.println("[DTRH DEBUG] Clearing logs and leaves around: " + center);

        BlockPos.Mutable mutable = new BlockPos.Mutable();
        int logsRemoved = 0;
        int leavesRemoved = 0;

        for (int x = -horizontalRadius; x <= horizontalRadius; x++) {
            for (int z = -horizontalRadius; z <= horizontalRadius; z++) {
                for (int y = 0; y <= verticalHeight; y++) {
                    mutable.set(center.getX() + x, center.getY() + y, center.getZ() + z);

                    BlockState state = world.getBlockState(mutable);

                    if (state.isIn(BlockTags.LOGS)) {
                        world.removeBlock(mutable, false);
                        logsRemoved++;
                    } else if (state.isIn(BlockTags.LEAVES)) {
                        world.removeBlock(mutable, false);
                        leavesRemoved++;
                    }
                }
            }
        }

        System.out.println("[DTRH DEBUG] Clear finished! Logs removed: " + logsRemoved + ", Leaves removed: " + leavesRemoved);
    }

    public static BlockPos findCustomSurface(ServerWorld world, int x, int z, int startY) {
        BlockPos.Mutable scanPos = new BlockPos.Mutable(x, startY, z);

        while (scanPos.getY() > world.getBottomY()) {
            BlockState state = world.getBlockState(scanPos);

            if (world.getRegistryKey() == World.END) {
                if (state.isSolid() && state.isOf(Blocks.END_STONE) ) {
                    BlockState blockAbove = world.getBlockState(scanPos.up());
                    if (blockAbove.isAir()) {
                        return scanPos.toImmutable();
                    }
                }
            }

            if (state.isSolid() && !state.isIn(BlockTags.LEAVES) && !state.isIn(BlockTags.LOGS) && !state.isOf(Blocks.BEDROCK) ) {
                BlockState blockAbove = world.getBlockState(scanPos.up());
                if (blockAbove.isAir()) {
                    return scanPos.toImmutable();
                }
            }

            scanPos.move(Direction.DOWN);
        }

        return new BlockPos(x, 80, z);
    }


    public static void startMirrorTrance(ServerPlayerEntity player, BlockPos clickedPos, ServerWorld world) {
        UUID uuid = player.getUuid();
        if (ACTIVE_TRANCES.containsKey(uuid)) return;

        player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 100, 0, false, false, false));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 255, false, false, false));

        world.playSound(null, clickedPos, SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.BLOCKS, 0.4f, 0.5f);

        MinecraftServer server = player.getServer();
        if (server != null) {
            RegistryKey<World> targetDimKey = (world.getRegistryKey() == World.OVERWORLD) ? ModDimensions.WONDERLAND_LEVEL_KEY : World.OVERWORLD;
            ServerWorld targetWorld = server.getWorld(targetDimKey);
            if (targetWorld != null) {
                targetWorld.getChunkManager().addTicket(
                        ChunkTicketType.START,
                        new ChunkPos(clickedPos),
                        2,
                        Unit.INSTANCE
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

        if (destinationOpt.isPresent()) {
            targetPos = destinationOpt.get();
            teleportPlayerToMirrorFront(player, targetWorld, targetPos);
            return;
        }

        BlockPos existingMirrorNearby = findMirrorInRadiusColumn(targetWorld, clickedPos, 8);
        if (existingMirrorNearby != null) {
            targetPos = existingMirrorNearby;

            overworldNetwork.linkMirrors((currentDimKey == World.OVERWORLD) ? clickedPos : targetPos, (currentDimKey == World.OVERWORLD) ? targetPos : clickedPos);
            wonderlandNetwork.linkMirrors((currentDimKey == World.OVERWORLD) ? targetPos : clickedPos, (currentDimKey == World.OVERWORLD) ? clickedPos : targetPos);

            teleportPlayerToMirrorFront(player, targetWorld, targetPos);
            return;
        }

        BlockState sourceState = sourceWorld.getBlockState(clickedPos);
        MirrorType sourceMirrorType = MirrorType.wonderland;
        Direction mirrorFacing = Direction.NORTH;

        if (sourceState.isOf(ModBlocks.MIRROR_BLOCK)) {
            sourceMirrorType = sourceState.get(MirrorBlock.TYPE);
            mirrorFacing = sourceState.get(MirrorBlock.FACING);
        }

        int spawnY = targetWorld.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, clickedPos).getY();

        if (spawnY <= targetWorld.getBottomY()) {
            spawnY = (targetDim == World.OVERWORLD) ? 70 : 90;
        }
        targetPos = new BlockPos(clickedPos.getX(), spawnY, clickedPos.getZ());

        BlockPos actualExitMirrorPos = targetPos;

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

        else {
            BlockState lowerState = ModBlocks.MIRROR_BLOCK.getDefaultState()
                    .with(MirrorBlock.HALF, DoubleBlockHalf.LOWER)
                    .with(MirrorBlock.TYPE, MirrorType.wonderland)
                    .with(MirrorBlock.FACING, mirrorFacing);

            targetWorld.setBlockState(targetPos, lowerState, 3);
            targetWorld.setBlockState(targetPos.up(), lowerState.with(MirrorBlock.HALF, DoubleBlockHalf.UPPER), 3);

            actualExitMirrorPos = targetPos;
        }

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

