package net.emanueljdf09.dtrhmod.world.dimension;

import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;

import java.util.OptionalLong;

public class ModDimensions {
    public static final RegistryKey<DimensionOptions> WONDERLAND_KEY = RegistryKey.of(RegistryKeys.DIMENSION,
            new Identifier(DownTheRabbitHole.MOD_ID, "wonderland"));
    public static final RegistryKey<World> WONDERLAND_LEVEL_KEY = RegistryKey.of(RegistryKeys.WORLD,
            new Identifier(DownTheRabbitHole.MOD_ID, "wonderland"));
    public static final RegistryKey<DimensionType> WONDERLAND_DIM_TYPE = RegistryKey.of(RegistryKeys.DIMENSION_TYPE,
            new Identifier(DownTheRabbitHole.MOD_ID, "wonderland_type"));


    public static final RegistryKey<DimensionOptions> EXTERIOR_KEY = RegistryKey.of(RegistryKeys.DIMENSION,
            new Identifier(DownTheRabbitHole.MOD_ID, "exterior"));
    public static final RegistryKey<World> EXTERIOR_LEVEL_KEY = RegistryKey.of(RegistryKeys.WORLD,
            new Identifier(DownTheRabbitHole.MOD_ID, "exterior"));
    public static final RegistryKey<DimensionType> EXTERIOR_DIM_TYPE = RegistryKey.of(RegistryKeys.DIMENSION_TYPE,
            new Identifier(DownTheRabbitHole.MOD_ID, "exterior_type"));

    public static final RegistryKey<DimensionOptions> STORYBOOK_KEY = RegistryKey.of(RegistryKeys.DIMENSION,
            new Identifier(DownTheRabbitHole.MOD_ID, "storybook"));
    public static final RegistryKey<World> STORYBOOK_LEVEL_KEY = RegistryKey.of(RegistryKeys.WORLD,
            new Identifier(DownTheRabbitHole.MOD_ID, "storybook"));
    public static final RegistryKey<DimensionType> STORYBOOK_DIM_TYPE = RegistryKey.of(RegistryKeys.DIMENSION_TYPE,
            new Identifier(DownTheRabbitHole.MOD_ID, "storybook_type"));

    public static void bootstrapType(Registerable<DimensionType> context) {
        context.register(WONDERLAND_DIM_TYPE, new DimensionType(
                OptionalLong.of(12900), // fixedTime
                true, //hasSkyLight
                false, //hasCeiling
                false, //ultraWarm
                true, //natural
                1.0, //coordinateSacel
                true, //bedWorks
                true, //respawnAnchorWorks
                0, //minY
                256, //MaxHeight
                256, //logicalHeight
                BlockTags.INFINIBURN_OVERWORLD, //infiniburn
                DimensionTypes.OVERWORLD_ID, //effectsLocation
                0.3f, //ambientlight              piglinsafe   Raids
                new DimensionType.MonsterSettings(true, false, UniformIntProvider.create(0, 7), 7)));

        context.register(EXTERIOR_DIM_TYPE, new DimensionType(
                OptionalLong.of(6000), // fixedTime
                false, //hasSkyLight
                false, //hasCeiling
                false, //ultraWarm
                false, //natural
                1.0, //coordinateScale
                false, //bedWorks
                false, //respawnAnchorWorks
                0, //minY
                256, //MaxHeight
                256, //logicalHeight
                BlockTags.INFINIBURN_OVERWORLD, //infiniburn
                DimensionTypes.THE_END_ID, //effectsLocation
                0.3f, //ambientlight              piglinsafe   Raids
                new DimensionType.MonsterSettings(true, false, UniformIntProvider.create(0, 0), 0)));

        context.register(STORYBOOK_DIM_TYPE, new DimensionType(
                OptionalLong.of(6000),
                true,
                false, //hasCeiling
                false, //ultraWarm
                false, //natural
                1.0, //coordinateScale
                false, //bedWorks
                false, //respawnAnchorWorks
                0, //minY
                256, //MaxHeight
                256, //logicalHeight
                BlockTags.INFINIBURN_OVERWORLD, //infiniburn
                DimensionTypes.OVERWORLD_ID, //effectsLocation
                0.3f, //ambientlight              piglinsafe   Raids
                new DimensionType.MonsterSettings(true, false, UniformIntProvider.create(0, 0), 0)));
    }
}
