package com.smartcodeltd.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class EqualsIgnoringOSSpecificLineSeparators extends TypeSafeMatcher<String> {

    public static TypeSafeMatcher<String> equalsIgnoringOSSpecificLineSeparators(String expected) {
        return new EqualsIgnoringOSSpecificLineSeparators(expected);
    }

    private static final String OS_SPECIFIC_LINE_BREAK = System.getProperty("line.separator");

    private final String expected;

    public EqualsIgnoringOSSpecificLineSeparators(String expected) {
        this.expected = standardisedToCurrentOS(expected);
    }

    @Override
    protected boolean matchesSafely(String actual) {
        return expected.equals(standardisedToCurrentOS(actual));
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("a string equal ")
                .appendValue(expected)
                .appendText(" (ignoring OS-specific line separators)");
    }

    private String standardisedToCurrentOS(String textWithLineBreaks) {
        return textWithLineBreaks.replaceAll("\\r\\n?|\\n", OS_SPECIFIC_LINE_BREAK);
    }
}
