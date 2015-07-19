package com.smartcodeltd;

import org.apache.maven.plugin.testing.resources.TestResources;
import org.codehaus.plexus.util.FileUtils;
import org.junit.runner.Description;

import java.io.File;
import java.io.IOException;

class TestProjectResources extends TestResources {
    private final String projectsDir;
    private final String workDir;

    private String testName;

    public TestProjectResources()
    {
        this( "src/test/projects", "target/test-projects" );
    }

    public TestProjectResources(String projectsDir, String workDir) {
        super(projectsDir, workDir);

        this.projectsDir = projectsDir;
        this.workDir     = workDir;
    }

    @Override
    protected void starting(Description description) {
        super.starting(description);

        String methodName = description.getMethodName();
        if ( methodName != null ) {
            methodName = methodName.replace( '/', '_' ).replace( '\\', '_' );
        }

        testName = description.getTestClass().getSimpleName() + "_" + methodName;
    }

    public File baseDirectoryFor(String project) throws IOException {
        return super.getBasedir(project);
    }

    public File baseDirectoryOf(String project) throws IOException {
        return new File(workDir, testName + "_" + project ).getCanonicalFile();
    }

    public File fileIn(String projectName, String fileName) throws IOException {
        return new File(baseDirectoryOf(projectName), fileName);
    }

    public String contentOf(String projectName, String fileName) throws IOException {
        return FileUtils.fileRead(fileIn(projectName, fileName));
    }
}
