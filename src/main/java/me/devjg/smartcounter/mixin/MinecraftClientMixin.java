package me.devjg.smartcounter.mixin;

import me.devjg.smartcounter.managers.TickCounterManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow @Nullable public HitResult crosshairTarget;
    @Shadow @Nullable public ClientWorld world;

    @Unique private static long lastClickTimeMillis = -1;
    @Unique private static final long CLICK_DELAY = 100;

    @Inject(method = "doAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/hit/BlockHitResult;getBlockPos()Lnet/minecraft/util/math/BlockPos;"), cancellable = true)
    private void onDoAttack(CallbackInfoReturnable<Boolean> cir) {
        if (TickCounterManager.enabled) {
            TickCounterManager.explicitResetTicks();
            cir.cancel();
        }
    }

    @Inject(method = "handleBlockBreaking", at = @At("HEAD"), cancellable = true)
    private void onHandleBlockBreaking(boolean breaking, CallbackInfo ci) {
        if (breaking && TickCounterManager.enabled) {
            TickCounterManager.explicitResetTicks();
            ci.cancel();
        }
    }

    @Inject(method = "doItemUse", at = @At("HEAD"), cancellable = true)
    private void onDoItemUse(CallbackInfo ci) {
        if (TickCounterManager.enabled) {
            if (world == null)
                return;

            long currentTimeMillis = System.currentTimeMillis();

            if (currentTimeMillis - lastClickTimeMillis < CLICK_DELAY) {
                ci.cancel();
                return;
            }

            lastClickTimeMillis = currentTimeMillis;
            HitResult hitResult = crosshairTarget;

            if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK) {
                BlockHitResult blockHitResult = (BlockHitResult)hitResult;
                BlockPos blockPos = blockHitResult.getBlockPos();

                BlockState blockState = world.getBlockState(blockPos);
                Block blockInstance = blockState.getBlock();

                TickCounterManager.countedNewTicks(blockState, blockInstance);
            }

            ci.cancel();
        }
    }
}
