# Integration tests

Release Candidate uses the [Maven Invoker Plugin](https://maven.apache.org/plugins/maven-invoker-plugin/) to execute
integration tests you see in this directory.

Each directory is prefixed with the name of the Mojo the integration test inside it exercises, ie. `updateVersion_*`
verify the [UpdateVersion mojo](../../src/main/java/com/smartcodeltd/UpdateVersionMojo.java) and `version_*`
verify the [Version mojo](../../src/main/java/com/smartcodeltd/VersionMojo.java).