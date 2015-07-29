package com.smartcodeltd;

import com.smartcodeltd.domain.Version;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

abstract public class ReleaseCandidateMojo
    extends AbstractMojo
{
    private final static String default_version_format = "{{ version }}";

    @Parameter(defaultValue = default_version_format, required = false)
    protected String versionFormat;

    @Component
    protected MavenProject project;

    protected void info(String template, Object... values) {
        getLog().info(String.format(template, values));
    }

    protected <T> T with(T value) {
        return value;
    }

    protected Version versionOf(MavenProject project) {
        return new Version(project.getVersion());
    }
}
