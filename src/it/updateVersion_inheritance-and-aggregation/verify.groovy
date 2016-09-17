new GroovyShell().parse(new File('./src/it/integration_tests.groovy')).with {

    parent       = parse(basedir, 'pom.xml')
    first_child  = parse(basedir, 'project-parent/pom.xml')
    second_child = parse(basedir, 'project-child/pom.xml')

    expect parent.version,              "1.0-2", "version of parent POM"
    expect first_child.parent.version,  "1.0-2", "version of parent POM referenced from project parent POM"
    expect second_child.parent.version, "1.0-2", "version of parent POM referenced from child POM"
}
