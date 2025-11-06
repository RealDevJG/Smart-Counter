package me.devjg.smartcounter.commands;

import com.mojang.brigadier.CommandDispatcher;
import me.devjg.smartcounter.SmartCounter;
import me.devjg.smartcounter.Utils;
import me.devjg.smartcounter.managers.TickCounterManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

@Environment(EnvType.CLIENT)
public class TickCounterCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess) {
        dispatcher.register(
            CommandManager.literal("tickcounter").executes(context -> toggleTickCounter(context.getSource()))
        );
    }

    private static int toggleTickCounter(ServerCommandSource source) {
        TickCounterManager tcm = SmartCounter.get().getTickCounterManager();
        boolean enabled = tcm.toggle();

        if (enabled)
            Utils.addChatMessage("§aEnabled Tick Counter");
        else
            Utils.addChatMessage( "§cDisabled Tick Counter");

        return 1;
    }
}
