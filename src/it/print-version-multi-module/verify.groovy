new GroovyShell().parse(new File('./src/it/integration_tests.groovy')).with {

    exportedVersion = parseVersionFile("./target/it/print-version-multi-module", 'maven.version')

    expect exportedVersion, "1.0.0-SNAPSHOT", "version exported via 'version' goal"
}