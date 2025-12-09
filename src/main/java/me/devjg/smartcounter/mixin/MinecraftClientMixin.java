package me.devjg.smartcounter.mixin;

import me.devjg.smartcounter.SmartCounter;
import me.devjg.smartcounter.managers.NodeCounterManager;
import me.devjg.smartcounter.managers.TickCounterManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
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

@Mixin(Minecraft.class)
public abstract class MinecraftClientMixin {
    @Shadow @Nullable public ClientLevel level;
    @Shadow @Nullable public HitResult hitResult;

    @Unique private long lastClickTimeNanos = -1L;
    @Unique private static final long CLICK_DELAY_NANOS = TimeUnit.MILLISECONDS.toNanos(130);

    @Inject(method = "startAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/BlockHitResult;getBlockPos()Lnet/minecraft/core/BlockPos;"), cancellable = true)
    private void onDoAttack(CallbackInfoReturnable<Boolean> cir) {
       if (SmartCounter.get().isAnyManagerActive() && hasntClickedTooFast(cir)) {
            resetActiveManagerData();
            cir.cancel();
        }
    }

    @Inject(method = "continueAttack", at = @At("HEAD"), cancellable = true)
    private void onHandleBlockBreaking(boolean breaking, CallbackInfo ci) {
        if (SmartCounter.get().isAnyManagerActive() && breaking && hasntClickedTooFast(ci)) {
            resetActiveManagerData();
            ci.cancel();
        }
    }

    @Inject(method = "startUseItem", at = @At("HEAD"), cancellable = true)
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
        if (level == null)
            return;

        if (hitResult instanceof BlockHitResult blockHitResult) {
            BlockPos blockPos = blockHitResult.getBlockPos();
            BlockState blockState = level.getBlockState(blockPos);

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
