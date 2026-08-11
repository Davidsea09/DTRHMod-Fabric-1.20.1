package net.emanueljdf09.dtrhmod.util;

import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class ModPackets {
    public static final Identifier OPEN_BOOK_PACKET_ID = new Identifier(DownTheRabbitHole.MOD_ID, "open_custom_book");
    public static final Identifier LOCKED_BIOME_TOAST_PACKET_ID = new Identifier(DownTheRabbitHole.MOD_ID, "locked_biome_toast");

    public static void sendOpenBookPacket(ServerPlayerEntity player, String triggerKey) {

        PacketByteBuf buf = PacketByteBufs.create();

        buf.writeString(triggerKey);

        ServerPlayNetworking.send(player, OPEN_BOOK_PACKET_ID, buf);
    }

    public static void sendLockedBiomeToastPacket(ServerPlayerEntity player) {
        PacketByteBuf buf = PacketByteBufs.create();
        ServerPlayNetworking.send(player, LOCKED_BIOME_TOAST_PACKET_ID, buf);
    }



}
