new GroovyShell().parse(new File('./src/it/integration_tests.groovy')).with {

    parent       = parse(basedir, 'pom.xml')
    first_child  = parse(basedir, 'first/pom.xml')
    second_child = parse(basedir, 'second/pom.xml')

    expect parent.version,              "1.0-2", "version of parent POM"
    expect first_child.parent.version,  "1.0-2", "version of parent POM referenced from first child POM"
    expect second_child.parent.version, "1.0-2", "version of parent POM referenced from second child POM"
}