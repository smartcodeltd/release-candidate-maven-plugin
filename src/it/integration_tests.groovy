def parse (dir, file) {
    new XmlSlurper().parse(new File(dir, file))
}

def read (dir, file) {
    new File(dir, file).text
}

def expect (actual, expected, name) {
    assert actual == expected : "Expected the $name to equal '$expected', not '$actual'"
}