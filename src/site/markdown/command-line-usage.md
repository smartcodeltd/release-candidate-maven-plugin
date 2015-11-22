## Command line usage

To include the Release Candidate plugin in your Maven project, add the following declaration
to the `project/build/plugins` section of the `pom.xml` file:

```maven pom
<plugin>
  <groupId>@GROUP_ID</groupId>
  <artifactId>@ARTIFACT_ID</artifactId>
  <version>@VERSION</version>
</plugin>
```

You can find more on configuring Maven build plugins in [the official documentation](https://maven.apache.org/guides/mini/guide-configuring-plugins.html#Configuring_Build_Plugins).

### Usage

Adding the plugin to your `pom.xml` file enables following goals:

```
$> mvn release-candidate:updateVersion
```
which updates the `pom.xml` version as per the configuration and arguments injected by your CI server
[(learn more)](updateVersion-mojo.html),

```
$> mvn release-candidate:version
```
which prints the current version of your project to either standard output or a file so that it can be parsed
and understood by the CI server [(learn more)](version-mojo.html).

Both goals can also be executed from the command line directly:

```
$> mvn com.smartcodeltd:release-candidate-maven-plugin:LATEST:version
$> mvn com.smartcodeltd:release-candidate-maven-plugin:LATEST:updateVersion
```

**Read next**: [Configure the plugin to your needs](getting-started.html) <small>*(4 minute read)*</small>