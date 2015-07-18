package com.smartcodeltd;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name = "getVersion")
public class GetVersionMojo
    extends AbstractMojo
{
    private final static String default_project = "${project}";
    private final static String default_version_format = "{{ version }}";

    @Parameter(defaultValue = default_project, required = true)
    private MavenProject project;

    @Parameter(defaultValue = default_version_format, required = true)
    private String versionFormat;

    @Parameter(required = false)
    private Output output;

    public void execute()
        throws MojoExecutionException
    {
        final String version = project.getVersion();

        getLog().info(userFriendly(version));

        output.write(version);

        output.close();
    }

    // --

    private String userFriendly(String version) {
        return String.format("Detected version: '%s'", version);
    }
}