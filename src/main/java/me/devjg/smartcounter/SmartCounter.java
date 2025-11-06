package me.devjg.smartcounter;

import me.devjg.smartcounter.commands.TickCounterCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmartCounter implements ModInitializer {
	public static final String MOD_ID = "devjg-smart-counter";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Loading DevJG's Smart Counter mod");

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, t) -> {
			TickCounterCommand.register(dispatcher, registryAccess);
		});
	}
}
