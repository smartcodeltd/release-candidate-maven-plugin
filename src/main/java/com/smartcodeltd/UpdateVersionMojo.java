package com.smartcodeltd;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.common.io.Files;
import com.smartcodeltd.domain.Version;
import de.pdark.decentxml.*;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.*;

//import static org.twdata.maven.mojoexecutor.MojoExecutor.*;

@Mojo(name = "updateVersion")
public class UpdateVersionMojo
        extends ReleaseCandidateMojo
{
    // todo: make charset configurable
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
        return version.formattedWith(versionFormat);
    }

    private void update(File pom, String newVersion) throws IOException {
        Document doc = parsed(pom);

        doc.getChild("project/version").setText(newVersion);

        Files.write(doc.toString(), pom, charset);
    }

    private Document parsed(File pom) throws IOException {
        XMLParser parser = new XMLParser();
        XMLSource source = new XMLStringSource(CharStreams.toString(Files.newReader(pom, charset)));

        return parser.parse(source);
    }
}