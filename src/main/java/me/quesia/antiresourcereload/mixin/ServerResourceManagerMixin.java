package me.quesia.antiresourcereload.mixin;

import me.quesia.antiresourcereload.AntiResourceReload;
import net.minecraft.resource.ReloadableResourceManager;
import net.minecraft.resource.ServerResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.concurrent.ExecutionException;

@Mixin(ServerResourceManager.class)
public abstract class ServerResourceManagerMixin {
    @Redirect(
            method = "close",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/resource/ReloadableResourceManager;close()V"
            )
    )
    private void antiresourcereload$keepOpened(ReloadableResourceManager instance) throws ExecutionException, InterruptedException {
        // noinspection ConstantConditions - I think mixin is confusing for intellij here
        if (AntiResourceReload.CACHE == null || (Object) this != AntiResourceReload.CACHE.get()) {
            instance.close();
        }
    }
}
