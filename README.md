# Release Candidate (Maven Plugin)

[![Build Status](https://smartcode-opensource.ci.cloudbees.com/buildStatus/icon?job=release-candidate-maven-plugin)](https://smartcode-opensource.ci.cloudbees.com/job/release-candidate-maven-plugin/)
[![Download](https://api.bintray.com/packages/jan-molak/maven/release-candidate-maven-plugin/images/download.svg) ](https://bintray.com/jan-molak/maven/release-candidate-maven-plugin/_latestVersion)

**Release Candidate** is a [Maven](https://maven.apache.org/) plugin that makes integrating Maven projects with [Continuous Delivery](https://en.wikipedia.org/wiki/Continuous_delivery) pipelines a little bit easier. It allows your build server to read the current version of a Maven project and update it automatically as part of the build process. 

The new version number can include any additional metadata, such as an identifier of the commit that triggered the build, current timestamp, build number, etc.

The plugin is designed to work with any popular CI server, such as [Jenkins](http://jenkins-ci.org/), [TeamCity](https://www.jetbrains.com/teamcity/) and others.

## Your feedback matters!

Do you find Release Candidate useful? Give it a star! &#9733;

Found a bug? Raise [an issue](https://github.com/smartcodeltd/release-candidate-maven-plugin/issues) or submit a pull request

Have feedback? Let me know on twitter: [@JanMolak](https://twitter.com/JanMolak)

## Getting Started

Add the plugin to your `pom.xml` `build/plugins` section:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.example</groupId>
    <artifactId>my-awesome-project</artifactId>
    <version>1.0.0-SNAPSHOT</version>

    <packaging>jar</packaging>

    <properties>
        <encoding>UTF-8</encoding>
    </properties>

    <build>
        <plugins>
            <plugin>
                <groupId>com.smartcodeltd</groupId>
                <artifactId>release-candidate-maven-plugin</artifactId>
                <version><!-- plugin version --></version>
                
                <!-- configuration is optional, here are the defaults: -->
                <!--
                <configuration>
                    <releaseVersionFormat>{{ version }}</releaseVersionFormat>
                    <outputUri>stdout</outputUri>
                    <outputTemplate>{{ version }}</outputTemplate>
                    <encoding>UTF-8</encoding>
                </configuration>
                -->
            </plugin>
        </plugins>
    </build>
</project>
```

## Available Maven Goals

Once you've added the plugin to your `pom.xml` file, you can execute one of the below Maven goals:

## `mvn release-candidate:updateVersion`

This goal updates the `pom.xml` project version as per the configured `releaseVersionFormat`.

You might be pleased to note that the original structure of the pom.xml file is preserved when the file is updated (including the comments, whitespace, formatting, etc.), and [the only thing that changes](src/test/java/com/smartcodeltd/UpdateVersionMojoTest.java#L90) is the version number.

### Configuration Options

#### releaseVersionFormat (default value: `{{ version }}`)

Determines the structure of the final version of your release candidate. This configuration option [understands tokens](README.md#user-content-tokens).

To give you an idea of what can be achieved, assume the below example:
* `pom.xml` project version set to `1.0.0-SNAPSHOT`
* `releaseVersionFormat` defined as `{{ api_version }}-build.${build_number}.sha.${git_commit_hash}` 

when the build is executed on your CI server and the server injects both the `build_number` and `git_commit_hash` parameters as follows:
```
mvn release-candidate:updateVersion -Dbuild_number=42 -Dgit_commit_hash=1b4d455
```
you'll get a project version set to:
```
1.0.0-build.42.sha.b4d455f
```

#### encoding (default value: `UTF-8`)

Determines what encoding should be used when the `pom.xml` file is written back to disk.

## `mvn release-candidate:version`

This goal prints the current `pom.xml` project version, templated as per the `outputTemplate` either to standard output (`outputUri=stdout`) or a file (`outputUri=file://${project.basedir}/project.properties` for example).

### Configuration Options

#### outputUri (default value: `stdout`)

Defines where the output should end up. It can be defined either as:
* `stdout`, which will result in the plugin printing to [standard output](https://en.wikipedia.org/wiki/Standard_streams#Standard_output_.28stdout.29)
* `file://<absolute path to the output file>`, which will make the plugin write its output to a file. 
 
Please note that when writing to a file, the path specified needs to be **absolute**. 

If you want to save the file in the root directory of your project you can define: `outputUri=file://${project.basedir}/project.properties`

By the way, `${project.basedir}` is a [Maven variable](https://maven.apache.org/guides/introduction/introduction-to-the-pom.html)

#### outputTemplate (default value: `{{ version }}`)

Defines how the output of the `mvn release-candidate:version` goal should be structured so that it can be understood by your CI server.

This configuration option [understands tokens](README.md#user-content-tokens) and can be defined as a multi-line string.

For example, configuring plugin as per the below example:

```xml
<outputUri>file://${project.basedir}/project.properties</outputUri>
<outputTemplate>
  PROJECT_VERSION={{ version }}
  BUILT_AT={{ timestamp('YYYY-MM-dd HH:mm:ss') }}
</outputTemplate>
```

makes it produce a `project.properties` file in the project root directory. 
This file can then be read by your CI server to set the build description for example, or notify the development team about a successful build, etc.

#### encoding (default value: `UTF-8`)

Determines what encoding should be used when the output is produced.

## Recognised tokens

There are several tokens in the form of `{{ token_name }}` that the plugin can recognise.
Tokens can be used to define both [releaseVersionFormat](README.md#user-content-releaseversionformat-default-value--version-) and the [outputTemplate](README.md#user-content-outputtemplate-default-value--version-) configuration options.

Whenever you use a token as part of your configuration option definition, they will be expanded as per the below examples (assume the project version is defined as `1.2-beta-2-SNAPSHOT`):

Token                          | Result                | Comment
------------------------------ | --------------------- | --------------------------------------------------------------------
`{{ version }}`                | `1.2-beta-2-SNAPSHOT` | prints the full version as is
`{{ qualified_api_version }}`  | `1.2-beta-2`          | prints the [qualified version](https://docs.oracle.com/middleware/1212/core/MAVEN/maven_version.htm#MAVEN400)
`{{ api_version }}`            | `1.2`                 | prints only the API version
`timestamp('<format>')`        | current timestamp     | `<format>` should be [JodaTime-compatible](http://joda-time.sourceforge.net/apidocs/org/joda/time/format/DateTimeFormat.html)

Above tokens can be used together, for example release-candidate-maven-plugin defines [its own version as](c950bd4f9087ac8ff2111c45587cae556367d865/pom.xml#L210):
```
{{ api_version }}-{{ timestamp('YYYYMMddHHmm') }}.${git_hash}
```
which produces a version number such as this one:
```
1.0.0-201508081934.1b4d455
```

Those and other examples are [documented in the unit tests](src/test/java/com/smartcodeltd/domain/).