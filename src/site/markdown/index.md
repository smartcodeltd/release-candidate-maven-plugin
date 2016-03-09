**Release Candidate** is a [Maven](https://maven.apache.org/) plugin
developed by [smartcode ltd](http://smartcodeltd.co.uk) to make
integrating Maven projects with [Continuous Delivery](https://en.wikipedia.org/wiki/Continuous_delivery)
pipelines a little bit easier.

The plugin allows your build server to **read the current version** of a Maven project and **update it automatically**
as part of the build process to include **any additional metadata**, such as:

* an **identifier of the commit** that triggered the build
* a current **timestamp**
* a **build number**
* and pretty much anything else

This also means that you can:

* use **[any versioning strategy you like](controlling-the-version-number.html)** for your release candidates
(and `SNAPSHOT`s for local development)
* **[control the version](getting-started.html)** of your release candidate and manage it in one place - the `pom` file
* easily **[integrate with your CI server](version-mojo.html)**
(such as [Jenkins](http://jenkins-ci.org/), [TeamCity](https://www.jetbrains.com/teamcity/) and others)
* do all this **without the Maven Release Plugin**

You're welcome ;-)

**Read next**: [Controlling the version number](controlling-the-version-number.html) <small>*(2 minute read)*</small>

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