package me.devjg.smartcounter.mixin;

import me.devjg.smartcounter.SmartCounter;
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

import java.util.concurrent.TimeUnit;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow @Nullable public HitResult crosshairTarget;
    @Shadow @Nullable public ClientWorld world;

    @Unique private long lastClickTimeNanos = -1L;
    @Unique private static final long CLICK_DELAY = TimeUnit.MILLISECONDS.toNanos(100);

    @Inject(method = "doAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/hit/BlockHitResult;getBlockPos()Lnet/minecraft/util/math/BlockPos;"), cancellable = true)
    private void onDoAttack(CallbackInfoReturnable<Boolean> cir) {
        TickCounterManager tcm = SmartCounter.get().getTickCounterManager();

        if (tcm.isEnabled()) {
            tcm.explicitResetTicks();
            cir.cancel();
        }
    }

    @Inject(method = "handleBlockBreaking", at = @At("HEAD"), cancellable = true)
    private void onHandleBlockBreaking(boolean breaking, CallbackInfo ci) {
        TickCounterManager tcm = SmartCounter.get().getTickCounterManager();

        if (breaking && tcm.isEnabled()) {
            tcm.explicitResetTicks();
            ci.cancel();
        }
    }

    @Inject(method = "doItemUse", at = @At("HEAD"), cancellable = true)
    private void onDoItemUse(CallbackInfo ci) {
        TickCounterManager tcm = SmartCounter.get().getTickCounterManager();

        if (!tcm.isEnabled() || world == null)
            return;

        long now = System.nanoTime();

        if (lastClickTimeNanos >= 0 && now - lastClickTimeNanos < CLICK_DELAY) {
            ci.cancel();
            return;
        }

        lastClickTimeNanos = now;
        HitResult hitResult = crosshairTarget;

        if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHitResult = (BlockHitResult)hitResult;
            BlockPos blockPos = blockHitResult.getBlockPos();

            BlockState blockState = world.getBlockState(blockPos);
            Block blockInstance = blockState.getBlock();

            tcm.countedNewTicks(blockState, blockInstance);
        }

        ci.cancel();
    }
}
