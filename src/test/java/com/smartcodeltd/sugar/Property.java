package com.smartcodeltd.sugar;

// where are case classes when you need them ...
public class Property {
    public final String name;
    public final String value;

    public Property(String name, String value) {
        this.name  = name;
        this.value = value;
    }

    public static Property property(String key, String value) {
        return new Property(key, value);
    }
}