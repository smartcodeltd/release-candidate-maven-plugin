[![Build Status](https://smartcode-opensource.ci.cloudbees.com/buildStatus/icon?job=release-candidate-maven-plugin)](https://smartcode-opensource.ci.cloudbees.com/job/release-candidate-maven-plugin/)
[![Download](https://api.bintray.com/packages/jan-molak/maven/release-candidate-maven-plugin/images/download.svg) ](https://bintray.com/jan-molak/maven/release-candidate-maven-plugin/_latestVersion)

**Release Candidate** is a [Maven](https://maven.apache.org/) plugin developed by [smartcode ltd](http://smartcodeltd.co.uk) to make
integrating Maven projects with [Continuous Delivery](https://en.wikipedia.org/wiki/Continuous_delivery)
pipelines a little bit easier.

The plugin allows your build server to read the current version of a Maven project and update it automatically
as part of the build process to include any additional metadata,
such as an identifier of the commit that triggered the build, current timestamp, build number, etc.

Release Candidate is designed to work with any popular <abbr title="Continuous Integration">CI</abbr> server,
such as
[Jenkins](http://jenkins-ci.org/),
[TeamCity](https://www.jetbrains.com/teamcity/)
and others.

## Every build is a Release Candidate

The first principle of Continuous Delivery is that every change introduced to the codebase should lead to
a potential release.

Applying an automated build, deployment and test processes to this code change validate whether or not it can be released.

### Unique version number for every build

If every introduced code change forms a new release candidate, each one of those should be identified
with a *unique version number*.
This way the successful release candidates can be released and the failed ones discarded.

In order to achieve this versioning strategy while still honouring Maven's recommended
[version format](http://books.sonatype.com/mvnref-book/reference/pom-relationships-sect-pom-syntax.html),
I&nbsp;propose for the developers to be responsible for defining the API version and make the build server responsible
for the qualifier part:

```xml
<api_version>-<qualifier>
```

### Developers define the API version

I strongly recommend using [Semantic Versioning](http://semver.org/) rules when versioning the API.

According to those, the `<api_version>` part of the version number consists of:

```xml
<major_version>.<minor_version>.<patch_version>
```

You increment:

* `<major_version>` when you make incompatible API changes
* `<minor_version>` when you add functionality in a backwards-compatible manner
* `<patch_version>` when you make backwards-compatible bug fixes

In the Maven world this would translate to setting your `pom.xml` version to something like `1.0.0`.
Of course if you did that, Maven would treat this version number as a *release* rather than *work in progress*,
making local development inconvenient.

To signify to Maven that the project is under active development, the `SNAPSHOT` qualifier needs to be added:

```xml
<major>.<minor>.<patch>-SNAPSHOT
```

This way the version defined in the `pom.xml` file becomes:

```maven pom
<version>1.0.0-SNAPSHOT</version>
```

### Build server defines the qualifier

The purpose of giving a release candidate a meaningful qualifier is to be able to link a version of released software
back to the matching revision of the codebase in the <abbr title="Version Control System">VCS</abbr>.

It's much easier to reproduce bugs found in a particular version or deterministically roll-back
a problematic release to a working state if this link is explicitly stated in the version number itself.

The Release Candidate plugin uses a combination of a timestamp and a short git hash, so that its version number looks like this:

```
@VERSION
```

The reason for using the git hash is obvious - to link the build back to the VCS revision.
The timestamp is used as in certain scenarios (such as debugging the CI server configuration for example)
there may be several builds produced from the same source code revision.

**Next**: [Brief overview of the plugin](overview.html)

---

### Your feedback matters!

Do you find Release Candidate useful?

⇒ [Give it a star](https://github.com/smartcodeltd/release-candidate-maven-plugin/stargazers) on github! &#9733;

Found a bug?

⇒ Raise [an issue](https://github.com/smartcodeltd/release-candidate-maven-plugin/issues) or submit a pull request

Have feedback, interested in project news?

⇒ Get in touch on twitter: [@JanMolak](https://twitter.com/JanMolak)

<div class="download">
    <a
     href="https://bintray.com/jan-molak/maven/release-candidate-maven-plugin/_latestVersion"
     class="center-block btn btn-primary btn-lg btn-block"
     role="button">
    Download @VERSION
    </a>
</div>

---