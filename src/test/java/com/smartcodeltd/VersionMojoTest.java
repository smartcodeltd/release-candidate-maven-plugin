package com.smartcodeltd;

import com.smartcodeltd.sugar.ConfigEntry;
import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.logging.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.URI;

import static com.smartcodeltd.matchers.EqualsIgnoringOSSpecificLineSeparators.equalsIgnoringOSSpecificLineSeparators;
import static com.smartcodeltd.sugar.ConfigEntry.configured;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class VersionMojoTest {

    @Rule public final TestProjectResources resource = new TestProjectResources("src/test/resources/projects", "target/projects");
    @Rule public final Mojos mojo = new Mojos("version", resource);

    private Log log;

    private Mojo releaseCandidateVersion;

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
        releaseCandidateVersion = mojo.forProject("out-of-the-box");
        releaseCandidateVersion.setLog(log);

        releaseCandidateVersion.execute();

        verify(log).info("Detected version: '1.7.2-SNAPSHOT'");
        verifyNoMoreInteractions(log);
    }

    @Test
    public void tells_full_version_by_default() throws Exception {
        releaseCandidateVersion = mojo.forProject("out-of-the-box");
        releaseCandidateVersion.setLog(log);

        releaseCandidateVersion.execute();

        assertThat(stdOutContent.toString(), equalsIgnoringOSSpecificLineSeparators("1.7.2-SNAPSHOT\n"));
    }


    @Test
    public void writes_templated_version_to_stdout() throws Exception {
        releaseCandidateVersion = mojo.forProject("teamcity-integration");
        releaseCandidateVersion.setLog(log);

        releaseCandidateVersion.execute();

        assertThat(stdOutContent.toString(), equalsIgnoringOSSpecificLineSeparators(
            "##teamcity[setParameter name='env.PROJECT_VERSION' value='1.7.2-teamcity-SNAPSHOT']\n" +
            "##teamcity[message text='Project version: 1.7.2-teamcity-SNAPSHOT']\n" +
            "##teamcity[message text='Api version: 1.7.2']\n" +
            "##teamcity[message text='Qualified api version: 1.7.2-teamcity']\n"
        ));
    }

    @Test
    public void writes_templated_version_to_file() throws Exception {
        releaseCandidateVersion = mojo.forProject("jenkins-integration");
        releaseCandidateVersion.setLog(log);

        releaseCandidateVersion.execute();

        assertThat(resource.contentOf("jenkins-integration", "project.properties"), is(
            "PROJECT_VERSION=1.7.2-jenkins-SNAPSHOT\n" +
            "API_VERSION=1.7.2\n" +
            "QUALIFIED_API_VERSION=1.7.2-jenkins"
        ));
    }

    @Test
    public void trims_leading_whitespace_in_templates() throws Exception {
        releaseCandidateVersion = mojo.forProject("out-of-the-box");
        releaseCandidateVersion.setLog(log);

        given(
            configured("outputUri", URI.create("stdout")),
            configured("outputTemplate",
                    "    ##teamcity[setParameter name='env.PROJECT_VERSION' value='{{ version }}']\n" +
                    "    ##teamcity[message text='Project version: {{ version }}']"
            ),
            configured("encoding", "UTF-8")
        );

        releaseCandidateVersion.execute();

        assertThat(stdOutContent.toString(), equalsIgnoringOSSpecificLineSeparators(
            "##teamcity[setParameter name='env.PROJECT_VERSION' value='1.7.2-SNAPSHOT']\n" +
            "##teamcity[message text='Project version: 1.7.2-SNAPSHOT']\n"
        ));
    }

    // --

    private void given(ConfigEntry<?>... entries) throws IllegalAccessException {
        for (ConfigEntry<?> entry : entries) {
            mojo.given(releaseCandidateVersion, entry.name, entry.value);
        }
    }
}