package com.smartcodeltd.sugar;

public class ConfigEntry<T> {
    public final String name;
    public final T value;

    public ConfigEntry(String name, T value) {
        this.name = name;
        this.value = value;
    }
}
