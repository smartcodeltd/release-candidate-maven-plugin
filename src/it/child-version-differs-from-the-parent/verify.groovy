new GroovyShell().parse(new File('./src/it/integration_tests.groovy')).with {

    parent = parse(basedir, 'pom.xml')
    child  = parse(basedir, 'child/pom.xml')

    expect parent.version,       "1.0-2", "version of parent POM"
    expect child.parent.version, "1.0-2", "version of parent POM referenced from child POM"
    expect child.version,        "15.7-SNAPSHOT", "version of child POM"
}