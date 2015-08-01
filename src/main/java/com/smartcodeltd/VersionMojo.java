package com.smartcodeltd;

import com.smartcodeltd.domain.Version;
import com.smartcodeltd.writer.Writer;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;

import java.io.File;
import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

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
        File pom = project.getFile();

        try {
            Version version = currentVersionFrom(project.getFile());

            info("Detected version: '%s'", version);

            Writer.from(outputUri, charset).
                    write(version.formattedWith(outputTemplate));
        }
        catch (Exception e) {
            throw new MojoExecutionException(String.format("Couldn't read project version from '%s'.", pom.getPath()), e);
        }
    }

    private Version currentVersionFrom(File pom) throws IOException {
        return new Version(checkNotNull(parsed(pom).getChild("project/version")).getText());
    }
}