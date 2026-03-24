package me.devjg.smartcounter.commands;

import com.mojang.brigadier.CommandDispatcher;
import me.devjg.smartcounter.SmartCounter;
import me.devjg.smartcounter.managers.TickCounterManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.commands.CommandBuildContext;

@Environment(EnvType.CLIENT)
public class TickCounterCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandBuildContext commandRegistryAccess) {
        var command = ClientCommands.literal("tickcounter").executes(context -> toggleTickCounter(context.getSource()));
        dispatcher.register(command);
    }

    private static int toggleTickCounter(FabricClientCommandSource source) {
        TickCounterManager tcm = SmartCounter.get().getTickCounterManager();
        tcm.toggle();

        return 1;
    }
}
