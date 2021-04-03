package me.devjg.devjg_smartcounter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class StringRenderer extends Gui {
  @SubscribeEvent
  public void renderTextOnScreen(RenderGameOverlayEvent.Post event) {
    if (SmartCounter.isEnabled) {
      final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
      final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());

      final int width = scaledResolution.getScaledWidth(), height = scaledResolution.getScaledHeight();
      final float rt = SmartCounter.currentRt;

      String ticksText = "RT: %.1f | GT: %.1f | S: %.2f";
      int textWidth = fontRenderer.getStringWidth(ticksText);

      fontRenderer.drawString(String.format(ticksText, rt, rt * 2, rt / 10),
          (width - (float) textWidth+8) / 2, height - 40, 0x55b9f3, true);
    }
    if (SmartCounter.currentRt < 0)
      SmartCounter.clear();
  }
}
