package me.devjg.smartcounter.commands;

import com.mojang.brigadier.CommandDispatcher;
import me.devjg.smartcounter.SmartCounter;
import me.devjg.smartcounter.managers.NodeCounterManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;

@Environment(EnvType.CLIENT)
public class NodeCounterCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess) {
        dispatcher.register(
            ClientCommandManager.literal("nodecounter").executes(context -> toggleNodeCounter(context.getSource()))
        );
    }

    private static int toggleNodeCounter(FabricClientCommandSource source) {
        NodeCounterManager ncm = SmartCounter.get().getNodeCounterManager();
        ncm.toggle();

        return 1;
    }
}
