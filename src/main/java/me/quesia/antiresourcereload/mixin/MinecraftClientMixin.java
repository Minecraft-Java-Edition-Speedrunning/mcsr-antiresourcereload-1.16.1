package me.quesia.antiresourcereload.mixin;

import me.quesia.antiresourcereload.AntiResourceReload;
import me.quesia.antiresourcereload.mixin.access.RecipeManagerAccess;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ServerResourceManager;
import net.minecraft.server.command.CommandManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    private boolean hasLoadedTags;

    @Redirect(
            method = "method_29604",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/resource/ServerResourceManager;reload(Ljava/util/List;Lnet/minecraft/server/command/CommandManager$RegistrationEnvironment;ILjava/util/concurrent/Executor;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;"
            )
    )
    private CompletableFuture<ServerResourceManager> antiresourcereload$cachedReload(List<ResourcePack> dataPacks, CommandManager.RegistrationEnvironment registrationEnvironment, int i, Executor executor, Executor executor2) throws ExecutionException, InterruptedException {
        if (!dataPacks.equals(AntiResourceReload.DATAPACK_CACHE)) { AntiResourceReload.log("Using new data-packs, reloading."); }
        else if (AntiResourceReload.CACHE == null) { AntiResourceReload.log("Cached resources unavailable, reloading & caching."); }
        else {
            AntiResourceReload.log("Using cached server resources.");
            if (AntiResourceReload.HAS_SEEN_RECIPES) {
                ((RecipeManagerAccess) AntiResourceReload.CACHE.get().getRecipeManager()).invokeApply(AntiResourceReload.RECIPES, null, null);
            }
            AntiResourceReload.HAS_SEEN_RECIPES = false;
            AntiResourceReload.DATAPACK_CACHE = dataPacks;
            return AntiResourceReload.CACHE;
        }

        CompletableFuture<ServerResourceManager> reloaded = ServerResourceManager.reload(dataPacks, registrationEnvironment, i, executor, executor2);
        
        if (dataPacks.size() == 1) { AntiResourceReload.CACHE = reloaded; }

        return reloaded;
    }

    @Redirect(
            method = "startIntegratedServer(Ljava/lang/String;Lnet/minecraft/util/registry/RegistryTracker$Modifiable;Ljava/util/function/Function;Lcom/mojang/datafixers/util/Function4;ZLnet/minecraft/client/MinecraftClient$WorldLoadAction;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/resource/ServerResourceManager;loadRegistryTags()V"
            )
    )
    private void antiresourcereload$skipLoad(ServerResourceManager manager) throws ExecutionException, InterruptedException {
        if (AntiResourceReload.CACHE != null && manager == AntiResourceReload.CACHE.get()) {
            if (hasLoadedTags) return;
            hasLoadedTags = true;
        }
        manager.loadRegistryTags();
    }
}
