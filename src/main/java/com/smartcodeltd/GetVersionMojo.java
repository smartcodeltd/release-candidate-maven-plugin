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
    @Parameter(defaultValue = "${project}", required = true)
    private MavenProject project;

    public void execute()
        throws MojoExecutionException
    {
        getLog().info("Detected project version: " + project.getVersion());
    }
}