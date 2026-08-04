package net.emanueljdf09.dtrhmod.block;

import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.emanueljdf09.dtrhmod.block.custom.ExteriorChest;
import net.emanueljdf09.dtrhmod.block.entity.*;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;


public class ModBlockEntities {
    public static final BlockEntityType<ExteriorDoorEntity> EXTERIOR_DOOR_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(DownTheRabbitHole.MOD_ID, "exterior_door_be"),
                    FabricBlockEntityTypeBuilder.create(ExteriorDoorEntity::new,
                            ModBlocks.EXTERIOR_DOOR).build());

    public static final BlockEntityType<ExteriorChestEntity> EXTERIOR_CHEST_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(DownTheRabbitHole.MOD_ID, "exterior_chest_be"),
                    FabricBlockEntityTypeBuilder.create(ExteriorChestEntity::new,
                            ModBlocks.EXTERIOR_CHEST).build());

    public static final BlockEntityType<MirrorBlockEntity> MIRROR_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(DownTheRabbitHole.MOD_ID, "mirror_block_be"),
                    FabricBlockEntityTypeBuilder.create(MirrorBlockEntity::new,
                            ModBlocks.MIRROR_BLOCK).build());

    public static final BlockEntityType<RabbitHoleBlockEntity> RABBIT_HOLE_BLOCK_ENTITY=
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(DownTheRabbitHole.MOD_ID, "rabbit_hole_be"),
                    FabricBlockEntityTypeBuilder.create(RabbitHoleBlockEntity::new,
                            ModBlocks.RABBIT_HOLE).build());

    public static final BlockEntityType<TeapotBlockEntity> TEAPOT_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(DownTheRabbitHole.MOD_ID, "teapot_block_be"),
                    FabricBlockEntityTypeBuilder.create(TeapotBlockEntity::new,
                            ModBlocks.TEAPOT_BLOCK).build());

    public static final BlockEntityType<MadHatterHatBlockEntity> MAD_HATTER_HAT =
            Registry.register(
                    Registries.BLOCK_ENTITY_TYPE,
                    new Identifier(DownTheRabbitHole.MOD_ID, "mad_hatter_hat_be"),
                    FabricBlockEntityTypeBuilder.create(MadHatterHatBlockEntity::new, ModBlocks.MAD_HATTER_HAT).build()
            );



    public static void registerBlockEnities() {
        DownTheRabbitHole.LOGGER.info("Registering Block Entities for" + DownTheRabbitHole.MOD_ID);
    }
}
