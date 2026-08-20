package net.emanueljdf09.dtrhmod.entity;

import com.terraformersmc.terraform.boat.api.TerraformBoatType;
import com.terraformersmc.terraform.boat.api.TerraformBoatTypeRegistry;
import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.emanueljdf09.dtrhmod.block.ModBlocks;
import net.emanueljdf09.dtrhmod.item.ModItems;
import net.minecraft.item.Item;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class ModBoats {
    public static final Identifier TH_BOAT_ID = new Identifier(DownTheRabbitHole.MOD_ID, "th_boat");
    public static final Identifier TH_CHEST_BOAT_ID = new Identifier(DownTheRabbitHole.MOD_ID, "th_chest_boat");

    public static final Identifier BB_BOAT_ID = new Identifier(DownTheRabbitHole.MOD_ID, "bb_boat");
    public static final Identifier BB_CHEST_BOAT_ID = new Identifier(DownTheRabbitHole.MOD_ID, "bb_chest_boat");

    public static final Identifier WW_BOAT_ID = new Identifier(DownTheRabbitHole.MOD_ID, "ww_boat");
    public static final Identifier WW_CHEST_BOAT_ID = new Identifier(DownTheRabbitHole.MOD_ID, "ww_chest_boat");

    public static final Identifier HH_BOAT_ID = new Identifier(DownTheRabbitHole.MOD_ID, "hh_boat");
    public static final Identifier HH_CHEST_BOAT_ID = new Identifier(DownTheRabbitHole.MOD_ID, "hh_chest_boat");

    public static final RegistryKey<TerraformBoatType> TH_BOAT_KEY = TerraformBoatTypeRegistry.createKey(TH_BOAT_ID);
    public static final RegistryKey<TerraformBoatType> BB_BOAT_KEY = TerraformBoatTypeRegistry.createKey(BB_BOAT_ID);
    public static final RegistryKey<TerraformBoatType> WW_BOAT_KEY = TerraformBoatTypeRegistry.createKey(WW_BOAT_ID);
    public static final RegistryKey<TerraformBoatType> HH_BOAT_KEY = TerraformBoatTypeRegistry.createKey(HH_BOAT_ID);

    public static void registerBoats() {
        TerraformBoatType thBoat = new TerraformBoatType() {
            @Override
            public boolean isRaft() {
                return false;
            }

            @Override
            public Item getItem() {
                return ModItems.TH_BOAT;
            }

            @Override
            public Item getChestItem() {
                return ModItems.TH_CHEST_BOAT;
            }

            @Override
            public Item getPlanks() {
                return ModBlocks.TH_PLANKS.asItem();
            }
        };
        TerraformBoatType bbBoat = new TerraformBoatType() {
            @Override
            public boolean isRaft() {
                return false;
            }

            @Override
            public Item getItem() {
                return ModItems.BB_BOAT;
            }

            @Override
            public Item getChestItem() {
                return ModItems.BB_CHEST_BOAT;
            }

            @Override
            public Item getPlanks() {
                return ModBlocks.BB_PLANKS.asItem();
            }
        };
        TerraformBoatType wwBoat = new TerraformBoatType() {
            @Override
            public boolean isRaft() {
                return false;
            }

            @Override
            public Item getItem() {
                return ModItems.WW_BOAT;
            }

            @Override
            public Item getChestItem() {
                return ModItems.WW_CHEST_BOAT;
            }

            @Override
            public Item getPlanks() {
                return ModBlocks.WW_PLANKS.asItem();
            }
        };
        TerraformBoatType hhBoat = new TerraformBoatType() {
            @Override
            public boolean isRaft() {
                return false;
            }

            @Override
            public Item getItem() {
                return ModItems.HH_BOAT;
            }

            @Override
            public Item getChestItem() {
                return ModItems.HH_CHEST_BOAT;
            }

            @Override
            public Item getPlanks() {
                return ModBlocks.HH_PLANKS.asItem();
            }
        };

        Registry.register(TerraformBoatTypeRegistry.INSTANCE, TH_BOAT_KEY, thBoat);
        Registry.register(TerraformBoatTypeRegistry.INSTANCE, BB_BOAT_KEY, bbBoat);
        Registry.register(TerraformBoatTypeRegistry.INSTANCE, WW_BOAT_KEY, wwBoat);
        Registry.register(TerraformBoatTypeRegistry.INSTANCE, HH_BOAT_KEY, hhBoat);
    }
}
