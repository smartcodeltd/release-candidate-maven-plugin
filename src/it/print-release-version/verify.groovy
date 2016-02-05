new GroovyShell().parse(new File('./src/it/integration_tests.groovy')).with {

    parent       = parse(basedir, 'pom.xml')
    first_child  = parse(basedir, 'first/pom.xml')
    second_child = parse(basedir, 'second/pom.xml')
}