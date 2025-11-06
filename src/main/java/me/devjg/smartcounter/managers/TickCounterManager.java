package me.devjg.smartcounter.managers;

import me.devjg.smartcounter.Utils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;

@Environment(EnvType.CLIENT)
public class TickCounterManager {
    private boolean enabled = false;
    private boolean consecutivePistons = false;

    private float countedTicks = 0f;

    public boolean toggle() {
        enabled = !enabled;

        if (!enabled)
            onDisable();

        return enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    private void onDisable() {
        resetTickCounter();
    }

    private void resetTickCounter() {
        consecutivePistons = false;
        countedTicks = 0;
    }

    public void explicitResetTicks() {
        Utils.addChatMessage("Cleared counted ticks");
        resetTickCounter();
    }

    public void countedNewTicks(BlockState blockState, Block blockInstance) {
        countedTicks += countTicks(blockState, blockInstance);
        addTicksChatMessage();
    }

    private int countTicks(BlockState blockState, Block hitBlock) {
        if (hitBlock instanceof PistonBlock) {
            boolean wasConsecutive = consecutivePistons;
            consecutivePistons = true;
            return wasConsecutive ? 3 : 2;
        }

        consecutivePistons = false;

        if (hitBlock instanceof RepeaterBlock)
            return blockState.get(RepeaterBlock.DELAY) * 2;
        else if (hitBlock instanceof ComparatorBlock || hitBlock instanceof ObserverBlock || hitBlock instanceof RedstoneTorchBlock)
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
