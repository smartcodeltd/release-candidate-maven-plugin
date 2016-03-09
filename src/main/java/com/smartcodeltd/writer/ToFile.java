package com.smartcodeltd.writer;

import com.google.common.io.Files;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;

public class ToFile extends Writer {
    private final BufferedWriter out;

    public ToFile(URI uri, Charset charset) throws IOException {
        super(uri, charset);

        File destination = new File(uri);

        Files.createParentDirs(destination);

        out = Files.newWriter(new File(uri), charset);
    }

    @Override
    public void write(String message) {
        try {
            out.write(message);
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(String.format("Couldn't write to file: '%s'", uri), e);
        }
    }
}