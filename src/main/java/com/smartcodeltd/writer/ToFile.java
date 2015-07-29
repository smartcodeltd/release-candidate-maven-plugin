package com.smartcodeltd.writer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.nio.charset.Charset;


public class ToFile extends Writer {
    private final OutputStreamWriter out;

    public ToFile(URI uri, Charset charset) throws IOException {
        super(uri, charset);

        // todo: replace with Guava

        out = new OutputStreamWriter(new FileOutputStream(new File(uri)), charset);
    }

    @Override
    public void write(String message) {
        // todo: replace with Guava

        try {
            out.write(message);
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(String.format("Couldn't write to file: '%s'", uri), e);
        }
    }
}