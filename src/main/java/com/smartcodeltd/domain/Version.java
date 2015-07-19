package com.smartcodeltd.domain;

import java.util.regex.Pattern;

public class Version {
    private final String version;

    private static final Pattern leading_whitespace = Pattern.compile("^\\s+", Pattern.MULTILINE);

    public Version(String projectVersion) {
        this.version = projectVersion;
    }


    public String formattedWith(String template) {
        return leading_whitespace.matcher(template).replaceAll("")
                .replace("{{ version }}",               original(version))
                .replace("{{ api_version }}",           api(version))
                .replace("{{ qualified_api_version }}", qualifiedApi(version));
    }

    // --

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