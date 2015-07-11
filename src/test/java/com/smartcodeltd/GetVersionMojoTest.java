package com.smartcodeltd;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.plugin.testing.resources.TestResources;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class GetVersionMojoTest {
    
    @Rule
    public final MojoRule rule = new MojoRule();
    
    @Rule
    public final TestResources resources = new TestResources("src/test/resources", "target/projects");
    
    private Log log;

    private GetVersionMojo getVersion;
    
    @Before
    public void each_test() {
        log = mock(Log.class);
    }

    @Test
    public void outputs_detected_version() throws Exception {
        getVersion = mojoFor("sample-project");

        getVersion.setLog(log);

        getVersion.execute();

        verify(log).info("Detected project version: 1.7.2-SNAPSHOT");
    }

    private GetVersionMojo mojoFor(String projectName) throws Exception {
        File pom = testPomFor(projectName);

        GetVersionMojo mojo = (GetVersionMojo) rule.lookupMojo("getVersion", pom);
        assertThat(mojo, not(nullValue()));

        rule.setVariableValueToObject(mojo, "project", projectFrom(pom));

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
}