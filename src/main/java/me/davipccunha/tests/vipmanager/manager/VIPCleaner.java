package me.davipccunha.tests.vipmanager.manager;

import lombok.RequiredArgsConstructor;
import me.davipccunha.tests.vipmanager.VIPManagerPlugin;
import me.davipccunha.tests.vipmanager.model.VIPType;
import me.davipccunha.tests.vipmanager.model.VIPUser;
import me.davipccunha.tests.vipmanager.utils.VIPUtils;
import me.davipccunha.utils.cache.RedisCache;

@RequiredArgsConstructor
public class VIPCleaner {
    private final VIPManagerPlugin plugin;

    public void removeExpiredVIPs(String playerName) {
        final RedisCache<VIPUser> cache = this.plugin.getVIPUserCache();

        final VIPUser vipUser = cache.get(playerName.toLowerCase());
        if (vipUser == null) return;

        for (VIPType vipType : vipUser.getVIPs()) {
            if (vipUser.getExpirationTimestamp(vipType) <= System.currentTimeMillis()) {
                vipUser.removeVIP(vipType);
                VIPUtils.executeCommandsFromConfig(this.plugin.getConfig(), playerName, vipType, "deactivation");
            }
        }

        cache.add(playerName.toLowerCase(), vipUser);

        if (!vipUser.hasAnyVIP()) {
            cache.remove(playerName.toLowerCase());
            return;
        }
    }
}
