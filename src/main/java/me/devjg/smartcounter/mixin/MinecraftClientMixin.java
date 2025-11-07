package me.devjg.smartcounter.mixin;

import me.devjg.smartcounter.SmartCounter;
import me.devjg.smartcounter.managers.NodeCounterManager;
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
import java.util.function.BiConsumer;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow @Nullable public HitResult crosshairTarget;
    @Shadow @Nullable public ClientWorld world;

    @Unique private long lastClickTimeNanos = -1L;
    @Unique private static final long CLICK_DELAY_NANOS = TimeUnit.MILLISECONDS.toNanos(130);

    @Inject(method = "doAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/hit/BlockHitResult;getBlockPos()Lnet/minecraft/util/math/BlockPos;"), cancellable = true)
    private void onDoAttack(CallbackInfoReturnable<Boolean> cir) {
       if (SmartCounter.get().isAnyManagerActive() && hasntClickedTooFast(cir)) {
            resetActiveManagerData();
            cir.cancel();
        }
    }

    @Inject(method = "handleBlockBreaking", at = @At("HEAD"), cancellable = true)
    private void onHandleBlockBreaking(boolean breaking, CallbackInfo ci) {
        if (SmartCounter.get().isAnyManagerActive() && breaking && hasntClickedTooFast(ci)) {
            resetActiveManagerData();
            ci.cancel();
        }
    }

    @Inject(method = "doItemUse", at = @At("HEAD"), cancellable = true)
    private void onDoItemUse(CallbackInfo ci) {
        TickCounterManager tcm = SmartCounter.get().getTickCounterManager();
        NodeCounterManager ncm = SmartCounter.get().getNodeCounterManager();

        if (tcm.isEnabled() && hasntClickedTooFast(ci)) {
            processIfValidHitResult(tcm::countedNewTicks);
            ci.cancel();
        }
        else if (ncm.isEnabled() && hasntClickedTooFast(ci)) {
            processIfValidHitResult(ncm::addedNewNode);
            ci.cancel();
        }
    }

    @Unique
    private boolean hasntClickedTooFast(CallbackInfo ci) {
        long now = System.nanoTime();
        long last = lastClickTimeNanos;

        if (lastClickTimeNanos >= 0 && now - last < CLICK_DELAY_NANOS) {
            ci.cancel();
            return false;
        }

        lastClickTimeNanos = now;
        return true;
    }

    @Unique
    private void processIfValidHitResult(BiConsumer<BlockHitResult, BlockState> action) {
        if (world == null)
            return;

        HitResult hitResult = crosshairTarget;

        if (hitResult instanceof BlockHitResult blockHitResult) {
            BlockPos blockPos = blockHitResult.getBlockPos();
            BlockState blockState = world.getBlockState(blockPos);

            action.accept(blockHitResult, blockState);
        }
    }

    @Unique
    private void resetActiveManagerData() {
        TickCounterManager tcm = SmartCounter.get().getTickCounterManager();
        NodeCounterManager ncm = SmartCounter.get().getNodeCounterManager();

        if (tcm.isEnabled())
            tcm.explicitResetData();
        else if (ncm.isEnabled())
            ncm.explicitResetData();
    }
}
