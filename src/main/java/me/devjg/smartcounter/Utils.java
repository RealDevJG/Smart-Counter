package me.devjg.smartcounter;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class Utils {
    public static final String CHAT_PREFIX = "§7[§eSmartCounter§7]§r ";

    public static void addChatMessage(String message) {
        Minecraft client = Minecraft.getInstance();

        client.execute(() -> {
            client.gui.getChat().addClientSystemMessage(Component.nullToEmpty(Utils.CHAT_PREFIX + message));
        });
    }
}
