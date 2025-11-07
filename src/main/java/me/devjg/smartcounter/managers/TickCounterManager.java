package me.devjg.smartcounter.managers;

import me.devjg.smartcounter.SmartCounter;
import me.devjg.smartcounter.Utils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.util.hit.BlockHitResult;

@Environment(EnvType.CLIENT)
public class TickCounterManager {
    private boolean enabled = false;
    private boolean consecutivePistons = false;

    private float countedTicks = 0f;

    public void toggle() {
        enabled = !enabled;

        if (enabled)
            onEnable();
        else
            onDisable();
    }

    public boolean isEnabled() {
        return enabled;
    }

    private void onEnable() {
        NodeCounterManager ncm = SmartCounter.get().nodeCounterManager;

        if (ncm.isEnabled())
            ncm.toggle();

        Utils.addChatMessage("§aEnabled Tick Counter");
    }

    private void onDisable() {
        resetTickCounter();
        Utils.addChatMessage("§cDisabled Tick Counter");
    }

    private void resetTickCounter() {
        consecutivePistons = false;
        countedTicks = 0;
    }

    public void explicitResetData() {
        resetTickCounter();
        Utils.addChatMessage("Cleared counted ticks");
    }

    public void countedNewTicks(BlockHitResult blockhitResult, BlockState blockState) {
        int addedTicks = countTicks(blockState, blockState.getBlock());
        countedTicks += addedTicks;

        if (addedTicks > 0)
            addTicksChatMessage();
    }

    private int countTicks(BlockState blockState, Block blockInstance) {
        if (blockInstance instanceof PistonBlock) {
            boolean wasConsecutive = consecutivePistons;
            consecutivePistons = true;
            return wasConsecutive ? 3 : 2;
        }

        consecutivePistons = false;

        if (blockInstance instanceof RepeaterBlock)
            return blockState.get(RepeaterBlock.DELAY) * 2;
        else if (blockInstance instanceof ComparatorBlock || blockInstance instanceof ObserverBlock || blockInstance instanceof RedstoneTorchBlock)
            return 2;

        return 0;
    }

    private void addTicksChatMessage() {
        float redstoneTicks = countedTicks / 2.0f;
        float gameTicks = countedTicks;
        float irlSeconds = countedTicks / 20.0f;

        Utils.addChatMessage(
            "RTs: %.1f | GTs: %.1f | S: %.2f".formatted(redstoneTicks, gameTicks, irlSeconds)
        );
    }
}
