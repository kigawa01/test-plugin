package net.kigawa.testplugin;

import org.bukkit.plugin.java.JavaPlugin;

public final class TestPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("enable " + getName());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
