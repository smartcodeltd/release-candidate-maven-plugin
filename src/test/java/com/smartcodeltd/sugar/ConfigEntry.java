package com.smartcodeltd.sugar;

public class ConfigEntry<T> {
    public static <T> ConfigEntry<T> configured(String name, T value) {
        return new ConfigEntry<T>(name, value);
    }

    public final String name;
    public final T value;

    // --

    private ConfigEntry(String name, T value) {
        this.name = name;
        this.value = value;
    }
}
