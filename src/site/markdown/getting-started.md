## Getting started

The goal of this tutorial is to configure our project so that executing the following commands on the CI server:

* `mvn release-candidate:updateVersion` - sets the `pom.xml` project version according to the format we specify
* `mvn clean package` - produces an artifact of our project and outputs its version in a format the CI server understands

### Target version format

For the purpose of this tutorial let's assume that we're working on version `1.0.0` of an `awesome-app` project,
and we'd like the target version format to include:

* the API version - `1.0.0`
* a build timestamp - `20150801`
* and a git hash - `1b4d455`

which means that the version format looks like this:

```
{{ api_version }}-{{ timestamp('YYYYMMdd') }}.${revision}
```

and an example version would be:

```
1.0.0-20150801.1b4d455
```

**Note**: Maven compares the qualifiers of two versions with an identical api version part using [string comparison](https://docs.oracle.com/middleware/1212/core/MAVEN/maven_version.htm#MAVEN8855)
and for this reason when using both a timestamp and a git hash in the qualifier it makes sense to have the timestamp occur first.

## POM configuration

```maven pom
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <groupId>com.example</groupId>
    <artifactId>awesome-app</artifactId>

    <version>1.0.0-SNAPSHOT</version> <!-- (1) -->

    <properties>
      <revision>0000000</revision>    <!-- (2) -->

        [...]
    </properties>

    <build>
      <plugins>                       <!-- (3) -->
        <plugin>
          <groupId>@GROUP_ID</groupId>
          <artifactId>@ARTIFACT_ID</artifactId>
          <version>@VERSION</version>
          <executions>                <!-- (4) -->
            [ todo: configure the plugin ]
          </executions>
        </plugin>
      </plugins>
    </build>

    [...]
</project>
```

As you can see, we've done several things in the pom:

1. the project version is set to `1.0.0-SNAPSHOT`
2. the `${revision}` parameter we'll use in the final version number has a default value of `0000000`
(this value will be replaced by an actual git hash injected during the build process)
3. `@ARTIFACT_ID` is added to `build/plugins` and ready to be configured (4)

**Note**: You can find more on configuring Maven build plugins in [the official documentation](https://maven.apache.org/guides/mini/guide-configuring-plugins.html#Configuring_Build_Plugins).

### Release Candidate configuration

Due to a limitation in Maven, `release-candidate` goals need to be run in two stages:

* first, to update the `pom.xml` file
* second, to build the project artifact and let the CI server know the new version number

Because of that, two `<execution />` sections need to be present.

### First execution

In order to configure the first execution, add the following section under `<executions>`:

```maven pom
<execution>
  <id>default-cli</id>
  <goals>
    <goal>updateVersion</goal>
  </goals>
  <configuration>
    <releaseVersionFormat>{{ api_version }}-{{ timestamp('YYYYMMddHHmm') }}.${revision}</releaseVersionFormat>
  </configuration>
</execution>
```

**Note**: Setting the execution id to `default-cli` and the goal to `updateVersion` means that the configuration
will be applied when `mvn release-candidate:updateVersion` command is executed on the CI server.
([Learn more](https://maven.apache.org/guides/mini/guide-default-execution-ids.html#Default_executionIds_for_Implied_Executions)).

To learn more about the [`{{ tokens }}`]((/updateVersion-mojo.html#releaseVersionFormat))
used to configure the `releaseVersionFormat`, consult the
[documentation of the `updateVersion` goal](/updateVersion-mojo.html#releaseVersionFormat).

### Second execution

To configure the second execution, add the below section:

```maven pom
<execution>
  <goals>
    <goal>version</goal>
  </goals>
  <configuration>
    <outputTemplate>PROJECT_VERSION={{ version }}</outputTemplate>
  </configuration>
</execution>
```

Specifying the `outputTemplate` as above makes the plugin produce the following output:

```text
[INFO] Scanning for projects...
[...]
[INFO] --- @ARTIFACT_ID:@VERSION:version (default) @ release-candidate-maven-plugin ---
[INFO] Detected version: '1.0.0-SNAPSHOT'
PROJECT_VERSION=1.0.0-SNAPSHOT
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[...]
```

A deterministic output format makes it easy for the CI server to parse the output and
set the [build description](https://wiki.jenkins-ci.org/display/JENKINS/Description+Setter+Plugin) for example.

The [`version` goal](/version-mojo.html) is tied to the `package` phase of
the [Maven build lifecycle](https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html)
by default, which means that whenever you call `mvn package`, the `version` goal will get invoked as well.

**Note** that the `outputTemplate` parameter can be a multi-line string,
and that it also understands [`{{ tokens }}`](updateVersion-mojo.html#releaseVersionFormat).

## Summary

So far our plugin configuration looks like this:

```maven pom
<plugin>
    <groupId>@GROUP_ID</groupId>
    <artifactId>@ARTIFACT_ID</artifactId>
    <version>@VERSION</version>
    <executions>
      <execution>
        <id>default-cli</id>
        <goals>
          <goal>updateVersion</goal>
        </goals>
        <configuration>
          <releaseVersionFormat>{{ api_version }}-{{ timestamp('YYYYMMddHHmm') }}.${revision}</releaseVersionFormat>
        </configuration>
      </execution>
      <execution>
        <goals>
          <goal>version</goal>
        </goals>
        <configuration>
          <outputTemplate>PROJECT_VERSION={{ version }}</outputTemplate>
        </configuration>
      </execution>
    </executions>
</plugin>
```

and allows the following commands to be executed on the CI server:

```
mvn release-candidate:updateVersion -Drevision=`git rev-parse --short HEAD`
```

which updates the `pom.xml` project version, and

```
mvn clean package
```

which now also produces output the CI server can parse in order to set the build description.

### Optional: Enforcer configuration

As you've noticed, the `revision` parameter is [injected by the CI server](getting-started.html#Summary)
when the `updateVersion` goal is called.

If you want to make sure that the parameters passed to Maven are valid - use the [Maven Enforcer Plugin](http://maven.apache.org/enforcer/maven-enforcer-plugin/).

In the case of our example project where the injected parameter is a git hash, an appropriate enforcer rule might looks as follows:

```maven pom
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-enforcer-plugin</artifactId>
  <version>1.4</version>
  <executions>
    <execution>
      <id>default-cli</id>
      <goals>
        <goal>enforce</goal>
      </goals>
      <configuration>
        <rules>
          <requireProperty>
            <property>revision</property>
            <message>Please specify the `revision` property</message>
            <regex>[a-f0-9]{7}</regex>
            <regexMessage>revision should be a 7 character long identifier, generated by running `git rev-parse --short HEAD`</regexMessage>
          </requireProperty>
        </rules>
        <fail>true</fail>
      </configuration>
    </execution>
  </executions>
</plugin>
```

To make sure that the enforcer rule is called, add the `validate` goal when invoking `updateVersion`:

```
mvn validate release-candidate:updateVersion -Drevision=`git rev-parse --short HEAD`
```

**Next**: find out more about the [`updateVersion`](updateVersion-mojo.html) and [`version`](version-mojo.html) goals.

---

### Your feedback matters!

Do you find Release Candidate useful? ⇒ [Give it a star](https://github.com/smartcodeltd/release-candidate-maven-plugin/stargazers) on github! &#9733;

Found a bug? ⇒ Raise [an issue](https://github.com/smartcodeltd/release-candidate-maven-plugin/issues) or submit a pull request

Have feedback, interested in the project news? ⇒ Get in touch and follow me on twitter: [@JanMolak](https://twitter.com/JanMolak)