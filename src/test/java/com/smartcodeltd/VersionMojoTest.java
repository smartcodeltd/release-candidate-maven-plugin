package com.smartcodeltd;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.*;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class VersionMojoTest {
    
    @Rule public final MojoRule mojos = new MojoRule();
    @Rule public final TestProjectResources resources = new TestProjectResources("src/test/resources/projects", "target/projects");

    private Log log;

    private VersionMojo releaseCandidateVersion;

    private final ByteArrayOutputStream stdOutContent = new ByteArrayOutputStream();
    
    @Before
    public void each_test() {
        log = mock(Log.class);

        System.setOut(new PrintStream(stdOutContent));
    }

    @After
    public void clean_up() {
        System.setOut(null);
    }

    @Test
    public void logs_detected_version_in_a_user_friendly_format() throws Exception {
        releaseCandidateVersion = forProject("out-of-the-box");

        releaseCandidateVersion.execute();

        verify(log).info("Detected version: '1.7.2-SNAPSHOT'");
        verifyNoMoreInteractions(log);
    }

    @Test
    public void tells_full_version_by_defualt() throws Exception {
        releaseCandidateVersion = forProject("out-of-the-box");

        releaseCandidateVersion.execute();

        assertThat(stdOutContent.toString(), is("1.7.2-SNAPSHOT\n"));
    }


    @Test
    public void writes_templated_version_to_stdout() throws Exception {
        releaseCandidateVersion = forProject("teamcity-integration");

        releaseCandidateVersion.execute();

        assertThat(stdOutContent.toString(), is(
            "##teamcity[setParameter name='env.PROJECT_VERSION' value='1.7.2-teamcity-SNAPSHOT']\n" +
            "##teamcity[message text='Project version: 1.7.2-teamcity-SNAPSHOT']\n" +
            "##teamcity[message text='Api version: 1.7.2']\n" +
            "##teamcity[message text='Qualified api version: 1.7.2-teamcity']\n"
        ));
    }

    @Test
    public void writes_templated_version_to_file() throws Exception {
        releaseCandidateVersion = forProject("jenkins-integration");

        releaseCandidateVersion.execute();

        assertThat(contentOf(fileIn("jenkins-integration", "project.properties")), is(
            "PROJECT_VERSION=1.7.2-jenkins-SNAPSHOT\n" +
            "API_VERSION=1.7.2\n" +
            "QUALIFIED_API_VERSION=1.7.2-jenkins"
        ));
    }

    @Test
    public void trims_leading_whitespace_in_templates() throws Exception {
        releaseCandidateVersion = forProject("out-of-the-box");

        givenOutputWriter("stdout", withTemplate(
        "    #teamcity[setParameter name='env.PROJECT_VERSION' value='{{ version }}']\n" +
        "    #teamcity[message text='Project version: {{ version }}']"
        ));

        releaseCandidateVersion.execute();

        assertThat(stdOutContent.toString(), is(
            "##teamcity[setParameter name='env.PROJECT_VERSION' value='1.7.2-SNAPSHOT']\n" +
            "##teamcity[message text='Project version: 1.7.2-SNAPSHOT']\n"
        ));
    }

    // --

    private void givenOutputWriter(String uri, String outputTemplate) throws IllegalAccessException {
        given(releaseCandidateVersion, "output", new Output(uri, outputTemplate, "UTF-8"));
    }

    private void given(Mojo mojo, String field, Object value) throws IllegalAccessException {
        mojos.setVariableValueToObject(mojo, field, value);
    }

    private String withTemplate(String template) {
        return template;
    }

    private VersionMojo forProject(String projectName) throws Exception {
        File pom = testPomFor(projectName);

        VersionMojo mojo = (VersionMojo) mojos.lookupConfiguredMojo(projectFrom(pom), "version");
        assertThat(mojo, not(nullValue()));

        mojo.setLog(log);

        return mojo;
    }

    private MavenProject projectFrom(File pom) throws IOException, XmlPullParserException {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model = reader.read(new FileReader(pom));

        return new MavenProject(model);
    }

    private File testPomFor(String projectName) throws IOException {
        File projectDirectory = resources.baseDirectoryFor(projectName);
        File pom = new File(projectDirectory, "pom.xml");

        assertThat(pom, not(nullValue()));
        assertThat(pom.exists(), is(true));

        // fixme: hack as maven doesn't expand ${project.basedir} when pom.xml is loaded, am I missing something?
        FileUtils.fileWrite(pom, FileUtils.fileRead(pom).replace(
                "${project.basedir}",
                resources.baseDirectoryOf(projectName).getAbsolutePath()
        ));

        return pom;
    }

    private File fileIn(String projectName, String fileName) throws IOException {
        return new File(resources.baseDirectoryOf(projectName), fileName);
    }

    private String contentOf(File file) throws IOException {
        return FileUtils.fileRead(file);
    }
}