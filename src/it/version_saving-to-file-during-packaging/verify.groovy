new GroovyShell().parse(new File('./src/it/integration_tests.groovy')).with {

    contents = read(basedir, 'child/target/classes/build.properties')

    expect contents, "version=1.7-SNAPSHOT", "version exported via 'version' goal"
}