package com.smartcodeltd;

import com.smartcodeltd.domain.Version;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.net.URI;
import java.nio.charset.Charset;

abstract public class ReleaseCandidateMojo
    extends AbstractMojo
{
    private final static String default_output_uri      = "stdout";
    private final static String default_output_template = "{{ version }}";
    private final static String default_version_format  = "{{ version }}";
    private final static String default_encoding        = "UTF-8";

    protected final Charset charset;

    /**
     * Describes how the pom.xml project version should be translated to a new version number.
     *
     * Let's say that the project version is set to `1.2.0-beta-SNAPSHOT`, specifying `versionFormat` as per below examples
     * will result in following outcomes:
     *
     * Format                       Outcome                 Comment
     * --------------------------------------------------------------------------------------------------------------------------------
     * {{ version }}                1.2.0-beta-SNAPSHOT     Leave the version as it is
     * {{ api_version }}            1.2.0                   Use only the API version
     * {{ qualified_api_version }}  1.2.0-beta              API version plus the qualifier
     * {{ timestamp('YYYYMMDD') }}  20150801                Current time (JodaTime-compatible timestamp format can be used as argument)
     *
     * Above tokens can also be used together, so specifying
     *   versionFormat: "{{ api_version }}.{{ timestamp('YYYYMMDD') }}"
     *   yields:        1.2.0.20150801
     *
     *   versionFormat: "{{ api_version }}-builton.{{ timestamp('YYYYMMDD') }}"
     *   yields:        1.2.0-builton.20150801
     *
     * Standard maven tokens can be used as well, so if you provide, say a `build_number` parameter when you build your project:
     *   mvn clean package -Dbuild_number=176
     *
     * and given that you set `versionFormat` to:
     *   "{{ qualified_api_version }}-build.${build_number}"
     *
     * the resulting version number will be:
     *   1.2.0-build.176
     */
    @Parameter(defaultValue = default_version_format, required = false)
    protected String versionFormat;

    /**
     * Encoding used when reading and writing to files on disk.
     */
    @Parameter(defaultValue = default_encoding, required = false)
    protected String encoding;

    /**
     * Defines where to direct the output of `release-candidate:version`
     *
     * Setting outputUri to `stdout` will print output to the console,
     * using `file://${project.basedir}/project.properties` will direct the output to `project.properties` file.
     *
     * Please note that you should use an absolute path when specifying the outputUri.
     * You can get hold of your project base directory by using `${project.basedir}` as per the example above.
     */
    @Parameter(defaultValue = default_output_uri, required = false)
    protected URI outputUri;

    /**
     * Defines how to structure the output of `release-candidate:version`
     *
     * If your build server of choice understands text output produced by maven (which is the case if you're using
     * TeamCity), you can specify the `outputTemplate` as:
     *
     * <outputTemplate>
     *  ##teamcity[setParameter name='env.PROJECT_VERSION' value='{{ version }}']
     *  ##teamcity[message text='Project version: {{ version }}']
     * </outputTemplate>
     *
     * If your build server prefers to use env variables defined using property files (Jenkins with EnvInject plugin)
     * you can specify the `outputTemplate as:
     *
     * <outputTemplate>PROJECT_VERSION={{ version }}</outputTemplate>
     *
     * Please note that when using multi-line templates leading whitespace characters will be stripped.
     */
    @Parameter(defaultValue = default_output_template, required = false)
    protected String outputTemplate;

    @Component
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

    protected <T> T getOrElse(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }
}
