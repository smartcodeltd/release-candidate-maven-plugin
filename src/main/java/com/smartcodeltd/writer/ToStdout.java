package com.smartcodeltd.writer;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;

public class ToStdout extends Writer {
    private final PrintStream out;

    public ToStdout(URI uri, String encoding) throws UnsupportedEncodingException {
        super(uri, encoding);

        out = new PrintStream(System.out, true, encoding);
    }

    @Override
    public void write(String message) {
        out.println(message);
    }

    @Override
    public void close() {
        out.close();
    }
}