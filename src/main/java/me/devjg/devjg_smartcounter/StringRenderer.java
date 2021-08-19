package me.devjg.devjg_smartcounter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class StringRenderer extends Gui {
  @SubscribeEvent
  public void renderTextOnScreen(RenderGameOverlayEvent.Text event) {
    if (SmartCounter.isEnabled) {
      final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
      final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());

      final int width = scaledResolution.getScaledWidth(), height = scaledResolution.getScaledHeight();
      final float rt = SmartCounter.currentRt;
      String ticksText = String.format("RT: %.1f | GT: %.1f | S: %.2f", rt, rt * 2, rt / 10);
      float textWidth = fontRenderer.getStringWidth(ticksText);

      fontRenderer.drawString(ticksText, (width - textWidth) / 2, height - 50, 0x429dff, true);
    }
  }
}
