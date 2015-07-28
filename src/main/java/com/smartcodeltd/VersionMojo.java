package com.smartcodeltd;

import com.smartcodeltd.domain.Version;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import javax.inject.Inject;

@Mojo(name = "version", requiresProject = true)
public class VersionMojo
    extends AbstractMojo
{
    private final static String default_version_format = "{{ version }}";

    @Parameter(defaultValue = default_version_format, required = true)
    private String versionFormat;

    // todo: inject output based on params
    @Parameter(required = false)
    private Output output = new Output();

    @Component
    private MavenProject project;

    @Inject
    public VersionMojo(String versionFormat) {
        this.versionFormat = getOrElse(versionFormat, default_version_format);
    }

    @Override
    public void execute()
        throws MojoExecutionException
    {
        Version version = versionOf(project);

        getLog().info(userFriendly(version));

        output.write(version);

        output.close();
    }

    // --

    private <T> T getOrElse(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    private Version versionOf(MavenProject project) {
        return new Version(project.getVersion());
    }

    private String userFriendly(Version version) {
        return version.formattedWith("Detected version: '{{ version }}'");
    }
}