package com.smartcodeltd.domain;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class SystemTime {

    private final DateTime now;

    public SystemTime() {
        this.now = DateTime.now();
    }

    public SystemTime(String now) {
        this.now = DateTime.parse(now);
    }

    public String as(String template) {
        return DateTimeFormat.forPattern(template).print(now);
    }
}