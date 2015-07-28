package com.smartcodeltd;

import com.smartcodeltd.domain.Version;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

//import static org.twdata.maven.mojoexecutor.MojoExecutor.*;

@Mojo(name = "updateVersion")
public class UpdateVersionMojo
        extends AbstractMojo
{
    private final static String default_version_format = "{{ version }}";

    @Parameter(defaultValue = default_version_format, required = false)
    private String versionFormat;

    @Component
    private MavenProject mavenProject;

    @Component
    private MavenSession mavenSession;

    @Component
    private BuildPluginManager pluginManager;

    public void execute()
            throws MojoExecutionException
    {
        Version version = new Version(mavenProject.getVersion());

        getLog().info(version.formattedWith(versionFormat));



//        executeMojo(
//            plugin(
//                groupId("org.codehaus.mojo"),
//                artifactId("versions-maven-plugin"),
//                version("2.2")
//            ),
//            goal("set"),
//            configuration(
//                element(name("newVersion"), version.formattedWith(versionFormat))
//            ),
//            executionEnvironment(
//                mavenProject,
//                mavenSession,
//                pluginManager
//            )
//        );
    }
}