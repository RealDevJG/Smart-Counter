package me.devjg.smartcounter;

import me.devjg.smartcounter.commands.NodeCounterCommand;
import me.devjg.smartcounter.commands.TickCounterCommand;
import me.devjg.smartcounter.managers.NodeCounterManager;
import me.devjg.smartcounter.managers.TickCounterManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmartCounter implements ModInitializer {
    public static final String MOD_ID = "devjg-smart-counter";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static SmartCounter INSTANCE;

    public TickCounterManager tickCounterManager;
    public NodeCounterManager nodeCounterManager;

    @Override
    public void onInitialize() {
        LOGGER.info("Loading DevJG's Smart Counter mod");

        INSTANCE = this;
        tickCounterManager = new TickCounterManager();
        nodeCounterManager = new NodeCounterManager();

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            TickCounterCommand.register(dispatcher, registryAccess);
            NodeCounterCommand.register(dispatcher, registryAccess);
        });
    }

    public static SmartCounter get() {
        return INSTANCE;
    }

    public boolean isAnyManagerActive() {
        return tickCounterManager.isEnabled() || nodeCounterManager.isEnabled();
    }

    public TickCounterManager getTickCounterManager() {
        return tickCounterManager;
    }

    public NodeCounterManager getNodeCounterManager() {
        return nodeCounterManager;
    }
}
