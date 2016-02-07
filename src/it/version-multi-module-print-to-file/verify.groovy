new GroovyShell().parse(new File('./src/it/integration_tests.groovy')).with {

    exported_version = read(basedir, 'project.version')

    expect exported_version, "1.0-SNAPSHOT", "version exported via 'version' goal"
}