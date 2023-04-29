package me.quesia.antiresourcereload;

import com.google.gson.JsonElement;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ServerResourceManager;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class AntiResourceReload implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger(FabricLoader.getInstance().getModContainer("antiresourcereload").orElseThrow(RuntimeException::new).getMetadata().getName());
    public static CompletableFuture<ServerResourceManager> CACHE;
    public static List<ResourcePack> DATAPACK_CACHE = new ArrayList<>();
    public static Map<Identifier, JsonElement> RECIPES;
    public static boolean HAS_SEEN_RECIPES;

    public static void log(String message) {
        LOGGER.info("[" + LOGGER.getName() + "] " + message);
    }

    @Override
    public void onInitialize() {
        log("Initializing.");
    }
}
