def parse(dir, file) {
    new XmlSlurper().parse(new File(dir, file))
}

def expect(actual, expected, name) {
    assert actual == expected : "Expected the $name to equal '$expected', not '$actual'"
}