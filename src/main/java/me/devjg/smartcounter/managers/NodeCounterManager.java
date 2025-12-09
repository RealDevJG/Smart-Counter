package me.devjg.smartcounter.managers;

import me.devjg.smartcounter.SmartCounter;
import me.devjg.smartcounter.Utils;
import me.devjg.smartcounter.data.NodeData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class NodeCounterManager {
    private boolean enabled = false;
    private long tickDelta = 0L;
    private long firstActivation = 0L;

    private final Map<BlockPos, NodeData> nodes = new HashMap<>();

    public void toggle() {
        ServerData server = Minecraft.getInstance().getCurrentServer();
        if (server != null) {
            Utils.addChatMessage("§cNode counter is unavailable on servers. It is singleplayer only due to the inaccuracies of measuring server-side ticks from the client. \n§a/tickcounter is still available on servers");
            return;
        }

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
        TickCounterManager tcm = SmartCounter.get().tickCounterManager;

        if (tcm.isEnabled())
            tcm.toggle();

        Utils.addChatMessage("§aEnabled Node Counter");
    }

    private void onDisable() {
        Utils.addChatMessage("§cDisabled Node Counter");
    }

    private void resetNodeCounter() {
        nodes.clear();
    }

    public void explicitResetData() {
        resetNodeCounter();
        Utils.addChatMessage("Removed all nodes");
    }

    public void addedNewNode(BlockHitResult blockHitResult, BlockState blockState) {
        if (blockState.getBlock() instanceof RedStoneWireBlock) {
            int powerLevel = blockState.getValue(RedStoneWireBlock.POWER);
            int nodeId = nodes.size() + 1;

            BlockPos blockPos = blockHitResult.getBlockPos();
            nodes.put(blockPos, new NodeData(nodeId, powerLevel));

            addedNodeChatMessage(blockPos, nodes.size());
        }
    }

    private void addedNodeChatMessage(BlockPos blockPos, int nodeId) {
        Utils.addChatMessage(
            "Added node with ID %d at %s".formatted(nodeId, blockPos.toShortString())
        );
    }

    public Map<BlockPos, NodeData> getNodes() {
        return nodes;
    }

    public float getTickDelta() {
        return tickDelta;
    }

    public void resetDeltas() {
        tickDelta = 0L;
        firstActivation = Minecraft.getInstance().level.getGameTime();
    }

    public void changeDelta() {
        tickDelta = Minecraft.getInstance().level.getGameTime() - firstActivation;
    }
}
