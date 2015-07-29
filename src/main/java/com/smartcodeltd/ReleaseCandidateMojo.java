package com.smartcodeltd;

import com.smartcodeltd.domain.Version;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.net.URI;
import java.nio.charset.Charset;

abstract public class ReleaseCandidateMojo
    extends AbstractMojo
{
    private final static String default_output_uri      = "stdout";
    private final static String default_output_template = "{{ version }}";
    private final static String default_version_format  = "{{ version }}";
    private final static String default_encoding        = "UTF-8";

    protected final Charset charset;

    @Parameter(defaultValue = default_version_format, required = false)
    protected String versionFormat;

    @Parameter(defaultValue = default_encoding, required = false)
    protected String encoding;

    @Parameter(defaultValue = default_output_uri, required = false)
    protected URI outputUri;

    @Parameter(defaultValue = default_output_template, required = false)
    protected String outputTemplate;

    @Component
    protected MavenProject project;

    public ReleaseCandidateMojo() {
        this.charset = Charset.forName(getOrElse(encoding, default_encoding));
    }

    protected void info(String template, Object... values) {
        getLog().info(String.format(template, values));
    }

    protected <T> T with(T value) {
        return value;
    }

    protected Version versionOf(MavenProject project) {
        return new Version(project.getVersion());
    }

    protected <T> T getOrElse(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }
}
