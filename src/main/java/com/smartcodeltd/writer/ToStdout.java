package com.smartcodeltd.writer;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.Charset;

public class ToStdout extends Writer {
    private final PrintStream out;

    public ToStdout(URI uri, Charset encoding) throws UnsupportedEncodingException {
        super(uri, encoding);

        out = new PrintStream(System.out, true, encoding.name());
    }

    @Override
    public void write(String message) {
        out.println(message);
    }
}