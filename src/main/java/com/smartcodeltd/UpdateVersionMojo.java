package com.smartcodeltd;

import com.google.common.io.Files;
import com.smartcodeltd.domain.Version;
import de.pdark.decentxml.Document;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;

import java.io.File;
import java.io.IOException;

/**
 * Sets the pom.xml project version as per the configured `releaseVersionFormat` template.
 */
@Mojo(name = "updateVersion")
public class UpdateVersionMojo
        extends ReleaseCandidateMojo
{
    public void execute()
            throws MojoExecutionException
    {
        File   pom        = project.getFile();
        String newVersion = evaluated(versionOf(project));

        info("Setting version to: '%s'", newVersion);

        try {
            update(pom, with(newVersion));
        }
        catch (IOException e) {
            throw new MojoExecutionException(String.format("Couldn't write to pom file '%s'", pom.getPath()), e);
        }
    }

    // --

    private String evaluated(Version version) {
        return version.formattedWith(releaseVersionFormat);
    }

    private void update(File pom, String newVersion) throws IOException {
        Document doc = parsed(pom);

        doc.getChild("project/version").setText(newVersion);

        Files.write(doc.toString(), pom, charset);
    }
}