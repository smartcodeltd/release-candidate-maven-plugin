package com.smartcodeltd;

import com.smartcodeltd.domain.Version;
import com.smartcodeltd.writer.Writer;
import de.pdark.decentxml.Document;
import de.pdark.decentxml.Element;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.maven.plugins.annotations.LifecyclePhase.PACKAGE;

/**
 * Retrieves project version specified in <code>pom.xml</code> and outputs it
 * either to stdout or to a file, depending on <a href="/version-mojo.html">configuration</a>.
 */
@Mojo(name = "version", requiresProject = true, defaultPhase = PACKAGE, aggregator = true)
public class VersionMojo
    extends ReleaseCandidateMojo
{
    private final static String default_output_uri      = "stdout";
    private final static String default_output_template = "{{ version }}";

    /**
     * <p>
     * Defines where to direct the output of <code>release-candidate:version</code>
     * </p>
     *
     * <ul>
     * <li>Setting outputUri to <code>stdout</code> will print output to the console,</li>
     * <li>using <code>file://${project.basedir}/project.properties</code> will direct the output to a <code>project.properties</code> file for example.</li>
     * </ul>
     *
     * <p>
     * <strong>Please note</strong> that you should use an absolute path when specifying the <code>outputUri</code>.
     * You can get hold of your project base directory by using <code>${project.basedir}</code> as per the example above.
     * </p>
     */
    @Parameter(defaultValue = default_output_uri, required = false, property = "outputUri")
    private URI outputUri;

    /**
     * <p>
     * Defines how to structure the output of <code>release-candidate:version</code>
     * </p>
     *
     * <p>
     * If your build server of choice understands text output produced by maven (which is the case if you're using
     * TeamCity for example), you can specify the <code>outputTemplate</code> as:
     * </p>
     *
     * <pre>&lt;outputTemplate&gt;
     *  ##teamcity[setParameter name='env.PROJECT_VERSION' value='{{ version }}']
     *  ##teamcity[message text='Project version: {{ version }}']
     * &lt;/outputTemplate&gt;</pre>
     *
     * <p>
     * If your build server prefers to use env variables defined using property files (Jenkins with EnvInject plugin)
     * you can specify the <code>outputTemplate</code> as:
     * </p>
     *
     * <pre>&lt;outputTemplate&gt;PROJECT_VERSION={{ version }}&lt;/outputTemplate&gt;</pre>
     *
     * <p>
     * <strong>Please note</strong> that when using multi-line templates, leading whitespace characters will be stripped.
     * </p>
     */
    @Parameter(defaultValue = default_output_template, required = false, property = "outputTemplate")
    protected String outputTemplate;

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
        Document doc = parsed(pom);

        return new Version(checkNotNull(firstExisting(
                projectVersion(doc),
                parentVersion(doc)
        )).getText());
    }
}