package me.devjg.devjg_smartcounter;

import me.devjg.devjg_smartcounter.events.KeyPressEvent;
import me.devjg.devjg_smartcounter.events.LeftClickEvent;
import me.devjg.devjg_smartcounter.events.RightClickEvent;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.lwjgl.input.Keyboard;

@Mod(modid = "devjg_smartcounter", version = "1.2.4", acceptedMinecraftVersions = "[1.8,]")
public class SmartCounter {
  public static boolean
      isEnabled = false,
      inRow = false;
  public static float currentRt = 0;
  public static KeyBinding toggleTickCounter;

  @Mod.EventHandler
  public void init(FMLInitializationEvent event) {
    ClientCommandHandler.instance.registerCommand(new CmdCountTicks());

    MinecraftForge.EVENT_BUS.register(new RightClickEvent());
    MinecraftForge.EVENT_BUS.register(new LeftClickEvent());

    MinecraftForge.EVENT_BUS.register(new StringRenderer());
    MinecraftForge.EVENT_BUS.register(new KeyPressEvent());

    toggleTickCounter = new KeyBinding(
        "Toggle Smart-Counter",
        Keyboard.KEY_M,
        "https://discord.gg/b35rQvS");
    ClientRegistry.registerKeyBinding(toggleTickCounter);
  }

  public static void add(float _rt) {
    currentRt += _rt;
  }

  public static void clear() {
    SmartCounter.inRow = false;
    currentRt = 0;
  }
}
