package me.davipccunha.tests.vipmanager.listener;

import lombok.RequiredArgsConstructor;
import me.davipccunha.tests.vipmanager.VIPManagerPlugin;
import me.davipccunha.tests.vipmanager.model.VIPUser;
import me.davipccunha.tests.vipmanager.utils.VIPUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@RequiredArgsConstructor
public class PlayerJoinListener implements Listener {
    final VIPManagerPlugin plugin;

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        if (player == null) return;

        plugin.getVipCleaner().removeExpiredVIPs(player.getName());
    }
}
