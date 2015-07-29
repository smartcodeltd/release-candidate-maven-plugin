package com.smartcodeltd.writer;

import java.lang.reflect.Constructor;
import java.net.URI;
import java.nio.charset.Charset;

public abstract class Writer {
    public static Writer from(URI uri, Charset charset) {

        String className = writerClassNameFrom(uri);

        try {
            Class<?> theClass = Class.forName(className);
            Constructor<?> constructor = theClass.getConstructor(URI.class, Charset.class);

            return (Writer) constructor.newInstance(uri, charset);
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format("Couldn't find the '%s' writer.", className), e);
        }
    }

    protected final URI uri;
    protected final Charset charset;

    protected Writer(URI uri, Charset charset) {
        this.uri     = uri;
        this.charset = charset;
    }

    abstract public void write(String message);

    // --

    private static String writerClassNameFrom(URI uri) {
        if (isNotDefined(uri.getScheme()) && isDefined(uri.getPath())) {
            return toFullClassName(uri.getPath());
        }
        else if (isDefined(uri.getScheme())) {
            return toFullClassName(uri.getScheme());
        }

        throw new IllegalArgumentException(String.format("Couldn't derive a Writer name from uri: '%s'.", uri.toString()));
    }

    private static String toFullClassName(String writerClass) {
        return String.format(
                "com.smartcodeltd.writer.To%s%s",
                Character.toUpperCase(writerClass.charAt(0)),
                writerClass.substring(1)
        );
    }

    private static boolean isDefined(String value) {
        return value != null && ! value.isEmpty();
    }

    private static boolean isNotDefined(String value) {
        return ! isDefined(value);
    }
}