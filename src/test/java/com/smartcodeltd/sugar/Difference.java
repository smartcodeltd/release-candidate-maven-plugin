package com.smartcodeltd.sugar;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Objects;

public class Difference {
    public final String left;
    public final String right;

    public Difference(String left, String right) {
        this.left = left;
        this.right = right;
    }

    public static Difference difference(String left, String right) {
        return new Difference(left, right);
    }

    public static List<Difference> differenceOf(String originalContent, String newContent) {
        List<Difference> differences = new ArrayList<Difference>();

        String[] originalContentLines = originalContent.split(System.getProperty("line.separator", "\\n"));
        String[] newContentLines      = newContent.split(System.getProperty("line.separator", "\\n"));

        assertThat("Number of lines differs", originalContentLines.length, is(newContentLines.length));

        for (int i = 0; i < originalContentLines.length; i++) {
            String left  = originalContentLines[i];
            String right = newContentLines[i];

            if (! left.equals(right)) {
                differences.add(difference(left, right));
            }
        }

        return differences;
    }

    @Override
    public String toString() {
        return String.format("%s\n%s", left, right);
    }

    @Override
    public int hashCode(){
        return Objects.hashCode(left, right);
    }

    @Override
    public boolean equals(final Object obj){
        if(obj instanceof Difference){
            final Difference other = (Difference) obj;
            return Objects.equal(left, other.left) &&
                   Objects.equal(right, other.right);
        } else{
            return false;
        }
    }
}