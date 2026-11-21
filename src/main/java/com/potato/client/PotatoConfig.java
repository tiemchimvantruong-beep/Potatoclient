package com.potato.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class PotatoConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("potatoclient.json");
    private static int accent = 0xFFFFA32A;

    private PotatoConfig() { }

    public static int accent() { return accent; }
    public static void setAccent(int argb) { accent = argb; save(); }

    public static void load() {
        if (!Files.exists(FILE)) { save(); return; }
        try {
            JsonObject root = GSON.fromJson(Files.readString(FILE, StandardCharsets.UTF_8), JsonObject.class);
            if (root == null) return;
            if (root.has("accent")) accent = root.get("accent").getAsInt();
            if (root.has("modules") && root.get("modules").isJsonObject()) {
                JsonObject modules = root.getAsJsonObject("modules");
                modules.entrySet().forEach(entry -> {
                    ClientModule module = ModuleRegistry.get(entry.getKey());
                    if (module != null && entry.getValue().isJsonPrimitive()) {
                        module.setEnabled(entry.getValue().getAsBoolean());
                    }
                });
            }
        } catch (Exception exception) {
            System.err.println("[Potato Client] Invalid config; defaults retained: " + exception.getMessage());
        }
    }

    public static void save() {
        try {
            Files.createDirectories(FILE.getParent());
            JsonObject root = new JsonObject();
            root.addProperty("accent", accent);
            JsonObject modules = new JsonObject();
            for (ClientModule module : ModuleRegistry.all()) {
                modules.addProperty(module.name(), module.enabled());
            }
            root.add("modules", modules);
            Files.writeString(FILE, GSON.toJson(root), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            System.err.println("[Potato Client] Could not save config: " + exception.getMessage());
        }
    }
}
