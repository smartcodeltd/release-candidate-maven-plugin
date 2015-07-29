package com.smartcodeltd;

import com.smartcodeltd.domain.Version;
import com.smartcodeltd.writer.Writer;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "version", requiresProject = true)
public class VersionMojo
    extends ReleaseCandidateMojo
{
    @Override
    public void execute()
        throws MojoExecutionException
    {
        Version version = versionOf(project);

        info("Detected version: '%s'", versionOf(project));

        Writer.from(outputUri, charset).
            write(version.formattedWith(outputTemplate));
    }
}