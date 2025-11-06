package me.devjg.smartcounter;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class Utils {
    public static final String CHAT_PREFIX = "§7[§eSmartCounter§7]§r ";

    public static void addChatMessage(String message) {
        MinecraftClient client = MinecraftClient.getInstance();

        client.execute(() -> {
            client.inGameHud.getChatHud().addMessage(Text.of(Utils.CHAT_PREFIX + message));
        });
    }
}
