package com.smartcodeltd;

import com.smartcodeltd.domain.Version;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name = "version", requiresProject = true)
public class VersionMojo
    extends ReleaseCandidateMojo
{
    // todo: inject output based on params
    @Parameter(required = false)
    private Output output = new Output();

    @Override
    public void execute()
        throws MojoExecutionException
    {
        Version version = versionOf(project);

        info("Detected version: '%s'", versionOf(project));

        output.write(version);

        output.close();
    }
}