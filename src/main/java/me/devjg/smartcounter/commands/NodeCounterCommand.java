package me.devjg.smartcounter.commands;

import com.mojang.brigadier.CommandDispatcher;
import me.devjg.smartcounter.SmartCounter;
import me.devjg.smartcounter.managers.NodeCounterManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.commands.CommandBuildContext;

@Environment(EnvType.CLIENT)
public class NodeCounterCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandBuildContext commandRegistryAccess) {
        var command = ClientCommands.literal("nodecounter").executes(context -> toggleNodeCounter(context.getSource()));
        dispatcher.register(command);
    }

    private static int toggleNodeCounter(FabricClientCommandSource source) {
        NodeCounterManager ncm = SmartCounter.get().getNodeCounterManager();
        ncm.toggle();

        return 1;
    }
}
