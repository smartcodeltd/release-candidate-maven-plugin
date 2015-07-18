package com.smartcodeltd.writer;

import java.lang.reflect.Constructor;
import java.net.URI;

public class Writers {
    public Writer from(URI uri, String encoding) {

        String className = writerClassNameFrom(uri);

        try {
            Class<?> theClass = Class.forName(className);
            Constructor<?> constructor = theClass.getConstructor(URI.class, String.class);

            return (Writer) constructor.newInstance(uri, encoding);
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format("Couldn't find the '%s' writer.", className), e);
        }
    }

    private String writerClassNameFrom(URI uri) {
        if (isNotDefined(uri.getScheme()) && isDefined(uri.getPath())) {
            return toFullClassName(uri.getPath());
        }
        else if (isDefined(uri.getScheme())) {
            return toFullClassName(uri.getScheme());
        }

        throw new IllegalArgumentException(String.format("Couldn't derive a Writer name from uri: '%s'.", uri.toString()));
    }

    private String toFullClassName(String writerClass) {
        return String.format(
                "com.smartcodeltd.writer.To%s%s",
                Character.toUpperCase(writerClass.charAt(0)),
                writerClass.substring(1)
        );
    }

    private boolean isDefined(String value) {
        return value != null && ! value.isEmpty();
    }

    private boolean isNotDefined(String value) {
        return ! isDefined(value);
    }
}