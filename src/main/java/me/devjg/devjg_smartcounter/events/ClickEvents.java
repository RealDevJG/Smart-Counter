package me.devjg.devjg_smartcounter.events;

import me.devjg.devjg_smartcounter.SmartCounter;
import net.minecraft.block.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ClickEvents {
  // left click
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

  // right click
  @SubscribeEvent
  public void newOnInteract(PlayerInteractEvent event) {
    if (SmartCounter.isEnabled && event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
      event.setCanceled(true);
      final Block block = event.world.getBlockState(event.pos).getBlock();

      SmartCounter.add(getBlockTicks(block, event));
    }
  }

  private float getBlockTicks(Block _block, PlayerInteractEvent event) {
    final float extraTick = _block instanceof BlockPistonBase && SmartCounter.inRow ? 0.5F : 0F;

    if (_block instanceof BlockRedstoneRepeater)
    {
      SmartCounter.inRow = false;
      return event.world.getBlockState(event.pos).getValue(BlockRedstoneRepeater.DELAY);
    }
    else if (_block instanceof BlockRedstoneComparator || _block instanceof BlockRedstoneTorch ||
        _block instanceof BlockTripWireHook)
    {
      SmartCounter.inRow = false;
      return 1;
    }
    else if (_block instanceof BlockPistonBase)
    {
      SmartCounter.add(extraTick);
      SmartCounter.inRow = !SmartCounter.inRow;
      return 1;
    }
    return 0;
  }
}
