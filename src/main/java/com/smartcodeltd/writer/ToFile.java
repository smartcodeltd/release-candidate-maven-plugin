package com.smartcodeltd.writer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URI;

public class ToFile extends Writer {
    private final OutputStreamWriter out;

    public ToFile(URI uri, String encoding) throws IOException {
        super(uri, encoding);

        out = new OutputStreamWriter(new FileOutputStream(new File(uri)), encoding);
    }

    @Override
    public void write(String message) {
        try {
            out.write(message);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Couldn't write to file: '%s'", uri), e);
        }
    }

    @Override
    public void close() {
        try {
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(String.format("Couldn't close the file: '%s'", uri), e);
        }
    }
}