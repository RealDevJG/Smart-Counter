package me.devjg.devjg_smartcounter.events;

import me.devjg.devjg_smartcounter.SmartCounter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ClickEvents {
  @SubscribeEvent
  public void onBlockBreak(BlockEvent.BreakEvent event) {
    if (SmartCounter.isEnabled)
      event.setCanceled(true);
  }

  @SubscribeEvent
  public void onAttack(TickEvent.ClientTickEvent event) {
    KeyBinding attack = Minecraft.getMinecraft().gameSettings.keyBindAttack;
    if (SmartCounter.isEnabled) {
      if (event.phase == TickEvent.Phase.END && attack.isKeyDown())
        SmartCounter.clear();
    }
  }
}
