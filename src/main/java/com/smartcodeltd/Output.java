package com.smartcodeltd;

import com.smartcodeltd.writer.Writer;
import com.smartcodeltd.writer.Writers;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.ReaderFactory;
import org.codehaus.plexus.util.StringUtils;

import java.net.URI;
import java.util.regex.Pattern;

public class Output {
    private final static String default_writer_uri = "stdout";
    private final static String default_template   = "{{ version }}";
    private final static String default_encoding   = ReaderFactory.UTF_8;

    @Parameter(defaultValue = default_writer_uri,  required = true)
    private String uri;

    @Parameter(defaultValue = default_template, required = true)
    private String template;

    @Parameter(defaultValue = default_encoding, required = true)
    private String encoding;

    private Writer writer;
    private Writers aWriter = new Writers();

    public Output() {
        this(default_writer_uri, default_template, default_encoding);
    }

    public Output(String uri, String template, String encoding) {
        this.uri      = uri;
        this.template = template;
        this.encoding = encoding;

        this.writer = writerFor(uri);
    }

    public void setUri(String uri) {
        this.uri    = uri;
        this.writer = writerFor(uri);
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public void write(String version) {
        writer.write(templated(version));
    }

    public void close() {
        writer.close();
    }

    // --

    private static final Pattern leading_whitespace = Pattern.compile("^\\s+", Pattern.MULTILINE);

    private Writer writerFor(String uri) {
        return aWriter.from(URI.create(uri), encoding);
    }

    private String templated(String version) {
        return leading_whitespace.matcher(template).replaceAll("")
                .replace("{{ version }}", version)
                .replace("{{ api_version }}", api(version));
    }

    private String api(String version) {
        int hyphenPosition = version.indexOf("-");

        return hyphenPosition > 0 ?
                version.substring(0, hyphenPosition) :
                version;
    }
}