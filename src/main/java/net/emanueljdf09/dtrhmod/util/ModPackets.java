package net.emanueljdf09.dtrhmod.util;

import net.emanueljdf09.dtrhmod.DownTheRabbitHole;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class ModPackets {
    public static final Identifier OPEN_BOOK_PACKET_ID = new Identifier(DownTheRabbitHole.MOD_ID, "open_custom_book");

    public static void sendOpenBookPacket(ServerPlayerEntity player, String triggerKey) {
        // 1. Create an empty data byte buffer (a container for bytes)
        PacketByteBuf buf = PacketByteBufs.create();

        // 2. Write your custom data string into it (e.g., "wonderland_intro")
        buf.writeString(triggerKey);

        // 3. Send the payload across the network to this specific player's client
        ServerPlayNetworking.send(player, OPEN_BOOK_PACKET_ID, buf);
    }



}
