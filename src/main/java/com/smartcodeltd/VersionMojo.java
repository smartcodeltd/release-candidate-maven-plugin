package com.smartcodeltd;

import com.smartcodeltd.domain.Version;
import com.smartcodeltd.writer.Writer;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * Retrieves project version specified in pom.xml and outputs it
 * either to stdout or to a file, depending on configuration.
 *
 * The format of the output (`outputTemplate`) is configurable
 * to allow for easy integration with popular Continuous Integration servers.
 */
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