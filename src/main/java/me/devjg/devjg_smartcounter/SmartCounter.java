package me.devjg.devjg_smartcounter;

import me.devjg.devjg_smartcounter.events.ClickEvents;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

@Mod(modid = "devjg_smartcounter", version = "1.2.5", acceptedMinecraftVersions = "[1.8,]")
public class SmartCounter {
  public static boolean isEnabled = false, inRow = false;
  public static float currentRt = 0;
  public static KeyBinding toggleTickCounter;

  @Mod.EventHandler
  public void init(FMLInitializationEvent event) {
    ClientCommandHandler.instance.registerCommand(new CmdCountTicks());

    MinecraftForge.EVENT_BUS.register(this);
    MinecraftForge.EVENT_BUS.register(new ClickEvents());
    MinecraftForge.EVENT_BUS.register(new StringRenderer());

    toggleTickCounter = new KeyBinding("Toggle Smart-Counter", Keyboard.KEY_NONE, "https://discord.gg/b35rQvS");
    ClientRegistry.registerKeyBinding(toggleTickCounter);
  }

  public static void add(float _rt) {
    currentRt += _rt;
  }

  public static void clear() {
    SmartCounter.inRow = false;
    currentRt = 0;
  }

  @SubscribeEvent
  public void onKey(InputEvent.KeyInputEvent event) {
    if (toggleTickCounter.isPressed())
      isEnabled = !isEnabled;
  }
}
