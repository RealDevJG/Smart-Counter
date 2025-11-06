package me.devjg.smartcounter.mixin;

import me.devjg.smartcounter.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DefaultRedstoneController;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DefaultRedstoneController.class)
public class DefaultRedstoneControllerMixin {
	@Inject(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
	private void onUpdatePowerStrength(World world, BlockPos pos, BlockState state, WireOrientation orientation, boolean blockAdded, CallbackInfo ci) {
		Utils.addChatMessage("State updated");
		// TODO: add node feature here (this fires when redstone changes power levels
	}
}
