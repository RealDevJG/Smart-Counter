package me.devjg.smartcounter.managers;

import me.devjg.smartcounter.Utils;
import net.minecraft.block.*;

public class TickCounterManager {
    public static boolean enabled = false;
    private static float countedTicks = 0;
    private static boolean consecutivePistons = false;

    public static boolean toggle() {
        enabled = !enabled;

        if (!enabled)
            tickCounterDisabled();

        return enabled;
    }

    private static void tickCounterDisabled() {
        resetTickCounter();
    }

    private static void resetTickCounter() {
        consecutivePistons = false;
        countedTicks = 0;
    }

    public static void explicitResetTicks() {
        Utils.addChatMessage("Cleared counted ticks");
        resetTickCounter();
    }

    public static void countedNewTicks(BlockState blockState, Block blockInstance) {
        countedTicks += countTicks(blockState, blockInstance);
        addTicksChatMessage();
    }

    private static int countTicks(BlockState blockState, Block hitBlock) {
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

        Utils.addChatMessage(blockState.getBlock().getClass().getSimpleName());
        return 0;
    }

    private static void addTicksChatMessage() {
        Utils.addChatMessage(
            "RTs: %.1f | GTs: %.1f | S: %.2f".formatted(countedTicks/2, countedTicks, countedTicks/20)
        );
    }
}
