package com.smartcodeltd;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.plugin.testing.resources.TestResources;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.Suite;

import java.io.*;
import java.net.URI;
import java.util.Scanner;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class GetVersionMojoTest {
    
    @Rule
    public final MojoRule mojos = new MojoRule();
    
    @Rule
    public final TestResources resources = new TestResources("src/test/resources", "target/projects");
    
    private Log log;

    private GetVersionMojo getVersion;

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
        getVersion = mojoFor("sample-project");

        getVersion.execute();

        verify(log).info("Detected version: '1.7.2-SNAPSHOT'");
        verifyNoMoreInteractions(log);
    }

    @Test
    public void writes_templated_version_using_a_stdout_writer() throws Exception {
        getVersion = mojoFor("sample-project");

        givenOutputWriter("stdout", withTemplate("VERSION={{ version }}"));

        getVersion.execute();

        assertThat(stdOutContent.toString(), is("VERSION=1.7.2-SNAPSHOT\n"));
    }

    @Test
    public void writes_version_using_a_multiline_template_trimming_the_leading_whitespace() throws Exception {
        getVersion = mojoFor("sample-project");

        givenOutputWriter("stdout", withTemplate(
        "       ##teamcity[setParameter name='env.PROJECT_VERSION' value='{{ version }}']\n" +
        "       ##teamcity[message text='Project version: {{ version }}']"
        ));

        getVersion.execute();

        assertThat(stdOutContent.toString(), is(
                "##teamcity[setParameter name='env.PROJECT_VERSION' value='1.7.2-SNAPSHOT']\n" +
                "##teamcity[message text='Project version: 1.7.2-SNAPSHOT']\n"
        ));
    }

    @Test
    public void writes_templated_version_using_a_file_writer() throws Exception {
        getVersion = mojoFor("sample-project");

        givenOutputWriter(
                uriOfFileInProjectDirectoryOf("sample-project", "project.propeties"),
                withTemplate("VERSION={{ version }}")
        );

        getVersion.execute();

        // todo: assert on the contents
//        assertThat(contentOf(uriOfFileInProjectDirectoryOf("sample-project", "project.propeties")), is("whoop!"));
    }


    // --

    private void givenOutputWriter(String uri, String outputTemplate) throws IllegalAccessException {
        given(getVersion, "output", new Output(uri, outputTemplate, "UTF-8"));
    }

    private void given(Mojo mojo, String field, Object value) throws IllegalAccessException {
        mojos.setVariableValueToObject(mojo, field, value);
    }

    private String withTemplate(String template) {
        return template;
    }

    private GetVersionMojo mojoFor(String projectName) throws Exception {
        File pom = testPomFor(projectName);

        GetVersionMojo mojo = (GetVersionMojo) mojos.lookupConfiguredMojo(projectFrom(pom), "getVersion");
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
        File projectDirectory = resources.getBasedir("projects/" + projectName);
        File pom = new File(projectDirectory, "pom.xml");

        assertThat(pom, not(nullValue()));
        assertThat(pom.exists(), is(true));

        return pom;
    }

    private String uriOfFileInProjectDirectoryOf(String projectName, String fileName) throws IOException {
        return new File(resources.getBasedir("projects/" + projectName), fileName).toURI().toString();
    }

    private String contentOf(String fileURI) throws FileNotFoundException {
//        FileUtils.fileRead(new File(basedir, expectedPath))

        return new Scanner(new File(URI.create(fileURI))).useDelimiter("\\Z").next();
    }
}