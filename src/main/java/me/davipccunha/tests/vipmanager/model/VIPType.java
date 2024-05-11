package me.davipccunha.tests.vipmanager.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VIPType {

    VIP("§a[VIP]"),
    MVP("§b[MVP]"),
    PLUNCKY("§e[Pluncky]");

    private final String prefix;

    public static VIPType getByName(String name) {
        for (VIPType type : values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }
}