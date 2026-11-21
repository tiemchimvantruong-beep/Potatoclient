package com.potato.client;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ModuleRegistry {
    private static final Map<String, ClientModule> MODULES = new LinkedHashMap<>();
    private static boolean registered;

    private ModuleRegistry() { }

    public static void registerDefaults() {
        if (registered) return;
        registered = true;
        add("Watermark", "Client", true);
        add("ArrayList", "Client", true);
        add("Coordinates", "Render", true);
        add("ArmorHUD", "Render", true);
        add("StorageESP", "Raid", false);
        add("StashFinder", "Raid", false);
        add("PlayerESP", "Render", false);
        add("Tracers", "Render", false);
        add("Fullbright", "Render", false);
        add("OreSim", "OreSim", false);
        add("AutoTotem", "Combat", false);
        add("AutoArmor", "Combat", false);
        add("TriggerBot", "Combat", false);
        add("Freecam", "Misc", false);
    }

    private static void add(String name, String category, boolean enabled) {
        MODULES.put(name, new ClientModule(name, category, enabled));
    }

    public static Collection<ClientModule> all() { return MODULES.values(); }
    public static ClientModule get(String name) { return MODULES.get(name); }
}
