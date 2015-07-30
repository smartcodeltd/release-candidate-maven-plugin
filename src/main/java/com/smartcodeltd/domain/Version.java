package com.smartcodeltd.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Version {
    private final String version;
    private final SystemTime time;

    private static final Pattern leading_whitespace = Pattern.compile("^\\s+", Pattern.MULTILINE);
    private static final Pattern timestamp          = Pattern.compile("\\{\\{ timestamp\\('(.*?)'\\) \\}\\}");

    public Version(String projectVersion) {
        this.version = projectVersion;
        this.time    = new SystemTime();
    }
    public Version(String projectVersion, SystemTime time) {
        this.version = projectVersion;
        this.time    = time;
    }

    /**
     * Formats the version as per the template provided.
     *
     * For example, assume pom version '1.2.5-beta-2-SNAPSHOT'.
     *
     * If a template contains one of the following tokens, they will be replaced as follows:
     * <pre>
     *   "{{ version }}"                   => "1.2.5-beta-2-SNAPSHOT"
     *   "{{ api_version }}"               => "1.2.5"
     *   "{{ qualified_api_version }}"     => "1.2.5-beta-2"
     *   "{{ timestamp('YYYYMMddHHmm') }}" => "201507302354"
     * </pre>
     *
     * This allows you to do more interesting things. If you specified a following template:
     * <pre>
     *   "{{ qualified_api_version }}-{{ timestamp('YYYYMMddHHmm') }}"
     * </pre>
     * you'd get:
     * <pre>
     *   "1.2.5-beta-2-201507302354"
     * </pre>
     *
     * @param template A template to format the version with
     * @return project version with tokens substituted by calculated values
     */
    public String formattedWith(String template) {
        return parseTimestamp(trimmed(template)
                .replace("{{ version }}", original(version))
                .replace("{{ api_version }}", api(version))
                .replace("{{ qualified_api_version }}", qualifiedApi(version)));
    }

    @Override
    public String toString() {
        return version;
    }

    // --

    private String trimmed(String template) {
        return leading_whitespace.matcher(template).replaceAll("");
    }

    private String parseTimestamp(String template) {
        Matcher matcher = timestamp.matcher(template);

        return matcher.find()
            ? matcher.replaceAll(time.as(matcher.group(1)))
            : template;
    }

    private String original(String version) {
        return version;
    }

    private String api(String version) {
        int firstHyphenPosition = version.indexOf("-");

        return firstHyphenPosition > 0 ?
                version.substring(0, firstHyphenPosition) :
                version;
    }

    private String qualifiedApi(String version) {
        return version.replaceAll("-SNAPSHOT", "");
    }
}