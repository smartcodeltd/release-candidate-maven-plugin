package com.smartcodeltd;

import com.smartcodeltd.sugar.Property;
import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.logging.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static com.smartcodeltd.sugar.Difference.difference;
import static com.smartcodeltd.sugar.Difference.differenceOf;
import static com.smartcodeltd.sugar.Property.property;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class UpdateVersionMojoTest {
    
    @Rule public final TestProjectResources resource = new TestProjectResources("src/test/resources/projects", "target/projects");
    @Rule public final Mojos mojo = new Mojos("updateVersion", resource);

    private Log log;

    private Mojo releaseCandidateUpdateVersion;

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
    public void keeps_pom_xml_unchanged_if_no_config_specified() throws Exception {
        releaseCandidateUpdateVersion = mojo.forProject("out-of-the-box");

        String originalContent = resource.contentOf("out-of-the-box", "pom.xml");

        releaseCandidateUpdateVersion.execute();

        assertThat(resource.contentOf("out-of-the-box", "pom.xml"), is(originalContent));
    }

    @Test
    public void updates_pom_xml_version_as_per_the_version_format() throws Exception {
        releaseCandidateUpdateVersion = mojo.forProject("updating-the-version", with(
            property("build_number", "2"),
            property("git_commit", "16f0eb28")
        ));

        releaseCandidateUpdateVersion.execute();

        assertThat(
            mojo.mavenProjectFor("updating-the-version").getVersion(),
            is("1.7.2-build.2.sha.16f0eb28")
        );
    }

    @Test
    public void notifies_the_user_of_new_version() throws Exception {
        releaseCandidateUpdateVersion = mojo.forProject("updating-the-version", with(
                property("build_number", "2"),
                property("git_commit", "16f0eb28")
        ));

        releaseCandidateUpdateVersion.setLog(log);

        releaseCandidateUpdateVersion.execute();

        verify(log).info("Setting version to: '1.7.2-build.2.sha.16f0eb28'");
        verifyNoMoreInteractions(log);
    }

    @Test
    public void makes_the_version_the_only_difference_between_updated_and_original_pom() throws Exception {
        releaseCandidateUpdateVersion = mojo.forProject("updating-the-version", with(
                property("build_number", "2"),
                property("git_commit", "16f0eb28")
        ));

        String originalContent = resource.contentOf("updating-the-version", "pom.xml");

        releaseCandidateUpdateVersion.execute();

        String newContent = resource.contentOf("updating-the-version", "pom.xml");

        assertThat(differenceOf(originalContent, newContent), hasSize(1));
        assertThat(differenceOf(originalContent, newContent), hasItem(difference(
                "    <version>1.7.2-SNAPSHOT</version>",
                "    <version>1.7.2-build.2.sha.16f0eb28</version>"
        )));
    }

    @Test
    public void complains_if_invalid_version_tokens_specified() throws Exception {
        // todo: implement
    }

    // --

    private List<Property> with(Property... properties) {
        return Arrays.asList(properties);
    }
}