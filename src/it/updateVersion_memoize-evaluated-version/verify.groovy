new GroovyShell().parse(new File('./src/it/integration_tests.groovy')).with {

    parent = parse(basedir, 'pom.xml')
    child  = parse(basedir, 'child/pom.xml')

    expect child.parent.version, parent.version, "version of parent POM referenced from child POM"
}