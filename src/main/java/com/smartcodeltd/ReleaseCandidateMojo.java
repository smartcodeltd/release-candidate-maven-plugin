package com.smartcodeltd;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.io.CharStreams;
import com.google.common.io.Files;
import com.smartcodeltd.domain.Version;
import de.pdark.decentxml.*;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import static java.util.Arrays.asList;

abstract public class ReleaseCandidateMojo
    extends AbstractMojo
{
    private final static String default_encoding        = "UTF-8";

    protected final Charset charset;

    /**
     * Encoding used when reading from and writing to files on disk.
     */
    @Parameter(defaultValue = default_encoding, required = false)
    protected String encoding;

    @Parameter(defaultValue = "${project}", readonly = true )
    protected MavenProject project;

    public ReleaseCandidateMojo() {
        this.charset = Charset.forName(getOrElse(encoding, default_encoding));
    }

    protected void info(String template, Object... values) {
        getLog().info(String.format(template, values));
    }

    protected <T> T with(T value) {
        return value;
    }

    protected Version versionOf(MavenProject project) {
        return new Version(project.getVersion());
    }

    protected MavenProject root(MavenProject project) {
        return project.hasParent() && !isOrganizationPom(project.getParent())
                ? root(project.getParent())
                : project;
    }

    private boolean isOrganizationPom(MavenProject parent) {
        return parent.getPackaging().equals("pom") && parent.getModules().isEmpty();
    }

    protected <T> T getOrElse(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    protected Document parsed(File pom) throws IOException {
        XMLParser parser = new XMLParser();
        XMLSource source = new XMLStringSource(CharStreams.toString(Files.newReader(pom, charset)));

        return parser.parse(source);
    }

    protected Element firstExisting(Element... elements) {
        return Iterables.find(asList(elements), Predicates.notNull(), new Element("dummy"));
    }

    protected Element projectVersion(Document doc) {
        return doc.getChild("project/version");
    }

    protected Element parentVersion(Document doc) {
        return doc.getChild("project/parent/version");
    }
}