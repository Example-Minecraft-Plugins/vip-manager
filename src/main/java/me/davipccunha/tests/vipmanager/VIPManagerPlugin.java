package me.davipccunha.tests.vipmanager;

import lombok.Getter;
import me.davipccunha.tests.vipmanager.command.VIPCommand;
import me.davipccunha.tests.vipmanager.manager.VIPCleaner;
import me.davipccunha.tests.vipmanager.model.VIPUser;
import me.davipccunha.utils.cache.RedisCache;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class VIPManagerPlugin extends JavaPlugin {
    private RedisCache<VIPUser> VIPUserCache;
    private final VIPCleaner vipCleaner = new VIPCleaner(this);

    @Override
    public void onEnable() {
        this.init();
        getLogger().info("VIP Manager Plugin loaded!");
    }

    public void onDisable() {
        getLogger().info("VIP Manager Plugin unloaded!");
    }

    private void init() {
        saveDefaultConfig();
        this.registerListeners(
        );
        this.registerCommands();

        this.loadCaches();

        this.startRepeatingTasks();
    }

    private void registerListeners(Listener... listeners) {
        PluginManager pluginManager = getServer().getPluginManager();
        for (Listener listener : listeners) pluginManager.registerEvents(listener, this);
    }

    private void registerCommands() {
        getCommand("vip").setExecutor(new VIPCommand(this));
    }

    private void loadCaches() {
        this.VIPUserCache = new RedisCache<>("vip-users", VIPUser.class);
    }

    private void startRepeatingTasks() {
        getServer().getScheduler().runTaskTimerAsynchronously(this, () -> {
            for (VIPUser user : this.VIPUserCache.getValues())
                this.vipCleaner.removeExpiredVIPs(user.getName());
        }, 0, 20L * 60 * 60);
    }
}
