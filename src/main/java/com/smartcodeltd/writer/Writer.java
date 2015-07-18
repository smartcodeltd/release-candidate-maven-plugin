package com.smartcodeltd.writer;

import java.net.URI;

public abstract class Writer {
    protected final URI uri;
    protected final String encoding;

    public Writer(URI uri, String encoding) {
        this.uri      = uri;
        this.encoding = encoding;
    }

    abstract public void write(String message);

    // no-op
    public void close() {}
}