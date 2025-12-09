package me.devjg.smartcounter.mixin;

import me.devjg.smartcounter.SmartCounter;
import me.devjg.smartcounter.Utils;
import me.devjg.smartcounter.data.NodeData;
import me.devjg.smartcounter.managers.NodeCounterManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.DefaultRedstoneWireEvaluator;
import net.minecraft.world.level.redstone.Orientation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(DefaultRedstoneWireEvaluator.class)
public class DefaultRedstoneControllerMixin {
	@Inject(method = "updatePowerStrength", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"))
	private void onUpdatePowerStrength(Level world, BlockPos pos, BlockState state, Orientation orientation, boolean blockAdded, CallbackInfo ci) {
		NodeCounterManager ncm = SmartCounter.get().nodeCounterManager;

		var nodes = ncm.getNodes();

		if (nodes.isEmpty())
			return;

		if (nodes.containsKey(pos)) {
			NodeData nodeData = nodes.get(pos);
			int nodeId = nodeData.id();

			if (Objects.equals(nodeData.powerLevel(), state.getValue(RedStoneWireBlock.POWER))) {
				if (nodeId == 1)
					ncm.resetDeltas();
				else
					ncm.changeDelta();

				float gtDelta = ncm.getTickDelta();
				float rtDelta = gtDelta / 2f;
				float sDelta = gtDelta / 20.0f;

				Utils.addChatMessage(
					"Node %d activated with RTs: %.1f | GTs: %.1f | S: %.2f delta".formatted(nodeId, rtDelta, gtDelta, sDelta)
				);
			}
		}
	}
}
