## Every build is a Release Candidate

The first principle of Continuous Delivery is that every change introduced to the codebase should lead to
a potential release.

Applying an automated build, deployment and test processes to this code change validates whether or not it can be released.

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

So far - pretty standard.

### Build server defines the qualifier

The purpose of giving a release candidate a meaningful qualifier is to be able to link a version of released software
back to the matching revision of the codebase in the <abbr title="Version Control System">VCS</abbr>.

It's much easier to reproduce bugs found in a particular version or deterministically roll-back
a problematic release to a working state if this link is explicitly stated in the version number itself.

For example, the Release Candidate plugin itself uses a combination of a timestamp and a short git hash, so that its version number looks like this:

```
@VERSION
```

**Read next**: [Command line usage](command-line-usage.html) <small>*(1 minute read)*</small>