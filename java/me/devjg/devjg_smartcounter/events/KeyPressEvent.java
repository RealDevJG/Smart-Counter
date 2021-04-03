package me.devjg.devjg_smartcounter.events;

import me.devjg.devjg_smartcounter.SmartCounter;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class KeyPressEvent {
  @SubscribeEvent
  public void onKey(InputEvent.KeyInputEvent event) {
    if (SmartCounter.toggleTickCounter.isPressed())
      SmartCounter.isEnabled = !SmartCounter.isEnabled;
  }
}
