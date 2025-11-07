package me.devjg.smartcounter.mixin;

import me.devjg.smartcounter.SmartCounter;
import me.devjg.smartcounter.Utils;
import me.devjg.smartcounter.data.NodeData;
import me.devjg.smartcounter.managers.NodeCounterManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DefaultRedstoneController;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(DefaultRedstoneController.class)
public class DefaultRedstoneControllerMixin {
	@Inject(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
	private void onUpdatePowerStrength(World world, BlockPos pos, BlockState state, WireOrientation orientation, boolean blockAdded, CallbackInfo ci) {
		NodeCounterManager ncm = SmartCounter.get().nodeCounterManager;

		var nodes = ncm.getNodes();

		if (nodes.isEmpty())
			return;

		if (nodes.containsKey(pos)) {
			NodeData nodeData = nodes.get(pos);
			int nodeId = nodeData.id();

			if (Objects.equals(nodeData.powerLevel(), state.get(RedstoneWireBlock.POWER))) {
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
