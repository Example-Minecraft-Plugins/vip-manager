package me.davipccunha.tests.vipmanager.utils;

import me.davipccunha.tests.vipmanager.model.VIPType;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class VIPUtils {
    public static void executeCommandsFromConfig(FileConfiguration config, String playerName, VIPType type, String stringListKey) {
        final ConfigurationSection vipConfig = config.getConfigurationSection("vips." + type);
        if (vipConfig == null) {
            Bukkit.getLogger().warning("VIPConfigUtils: VIPType " + type + " not found in config.");
            return;
        }

        for (String cmd : vipConfig.getStringList(stringListKey)) {
            String command = cmd.replaceAll("%player%", playerName);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }
    }
}
