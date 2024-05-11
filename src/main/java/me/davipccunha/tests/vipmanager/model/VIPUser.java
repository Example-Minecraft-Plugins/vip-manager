package me.davipccunha.tests.vipmanager.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
public class VIPUser {
    @Getter
    final String name;
    final Map<VIPType, Long> vips = new HashMap<>();

    public void removeVIP(VIPType type) {
        vips.remove(type);
    }

    public boolean hasVIP(VIPType type) {
        return vips.containsKey(type);
    }

    public Set<VIPType> getVIPs() {
        return vips.keySet();
    }

    public long getExpirationTimestamp(VIPType type) {
        return vips.getOrDefault(type, 0L);
    }

    public void addTime(VIPType type, long time) {
        if (!vips.containsKey(type)) {
            vips.put(type, System.currentTimeMillis() + time);
            return;
        }

        vips.put(type, this.vips.get(type) + time);
    }

    public boolean removeTime(VIPType type, long time) {
        if (!vips.containsKey(type)) return false;

        if (this.vips.get(type) - time <= System.currentTimeMillis()) {
            vips.remove(type);
            return true;
        }

        vips.put(type, this.vips.get(type) - time);
        return false;
    }

    public boolean hasAnyVIP() {
        return !vips.isEmpty();
    }
}
