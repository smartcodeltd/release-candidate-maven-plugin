package com.smartcodeltd;

import com.smartcodeltd.domain.Version;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name = "version")
public class VersionMojo
    extends AbstractMojo
{
    private final static String default_project = "${project}";
    private final static String default_version_format = "{{ version }}";

    @Parameter(defaultValue = default_project, required = true)
    private MavenProject project;

    @Parameter(defaultValue = default_version_format, required = true)
    private String versionFormat;

    @Parameter(required = false)
    private Output output = new Output();

    public void execute()
        throws MojoExecutionException
    {
        Version version = versionOf(project);

        getLog().info(userFriendly(project.getVersion()));

        output.write(version);

        output.close();
    }

    // --

    private Version versionOf(MavenProject project) {
        return new Version(project.getVersion());
    }

    private String userFriendly(String version) {
        return String.format("Detected version: '%s'", version);
    }
}