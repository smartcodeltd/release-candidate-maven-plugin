package com.smartcodeltd.sugar;

// where are case classes when you need them ...
public class Property {
    public static Property property(String key, String value) {
        return new Property(key, value);
    }

    public final String name;
    public final String value;

    // --

    private Property(String name, String value) {
        this.name  = name;
        this.value = value;
    }
}