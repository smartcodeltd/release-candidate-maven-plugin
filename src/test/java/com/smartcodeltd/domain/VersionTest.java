package com.smartcodeltd.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class VersionTest {
    private Version version;

    @Parameterized.Parameter(value = 0)
    public String example;

    @Parameterized.Parameter(value = 1)
    public String projectVersion;

    @Parameterized.Parameter(value = 2)
    public String template;

    @Parameterized.Parameter(value = 3)
    public String expectedResult;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        // https://docs.oracle.com/middleware/1212/core/MAVEN/maven_version.htm#MAVEN400

        /*
            M - Major
            m - minor
            p - patch
            q - qualifier
         */

        return Arrays.asList(new Object[][] {
                // example name         project.version        template          expected result
                { "M.m.p-SNAPSHOT",     "1.2.1-SNAPSHOT",      "{{ version }}" , "1.2.1-SNAPSHOT"      },
                { "M.m-SNAPSHOT",       "1.2-SNAPSHOT",        "{{ version }}" , "1.2-SNAPSHOT"        },
                { "M.m.p",              "1.2.1",               "{{ version }}" , "1.2.1"               },
                { "m.p",                "2.1",                 "{{ version }}" , "2.1"                 },
                { "M.m.p-q-SNAPSHOT",   "1.2.1-12-SNAPSHOT",   "{{ version }}" , "1.2.1-12-SNAPSHOT"   },
                { "M.m.p-q",            "1.2.1-client-name",   "{{ version }}" , "1.2.1-client-name"   },
                { "M.m.p-q-q-SNAPSHOT", "1.2-beta-2-SNAPSHOT", "{{ version }}" , "1.2-beta-2-SNAPSHOT" },
                { "m.p-q-q",            "1.2-beta-2",          "{{ version }}" , "1.2-beta-2"          },

                { "M.m.p-SNAPSHOT",     "1.2.1-SNAPSHOT",      "{{ api_version }}" , "1.2.1" },
                { "M.m-SNAPSHOT",       "1.2-SNAPSHOT",        "{{ api_version }}" , "1.2"   },
                { "M.m.p",              "1.2.1",               "{{ api_version }}" , "1.2.1" },
                { "m.p",                "2.1",                 "{{ api_version }}" , "2.1"   },
                { "M.m.p-q-SNAPSHOT",   "1.2.1-12-SNAPSHOT",   "{{ api_version }}" , "1.2.1" },
                { "M.m.p-q",            "1.2.1-client-name",   "{{ api_version }}" , "1.2.1" },
                { "M.m.p-q-q-SNAPSHOT", "1.2-beta-2-SNAPSHOT", "{{ api_version }}" , "1.2"   },
                { "m.p-q-q",            "1.2-beta-2",          "{{ api_version }}" , "1.2"   },

                { "M.m.p-SNAPSHOT",     "1.2.1-SNAPSHOT",      "{{ qualified_api_version }}" , "1.2.1"             },
                { "M.m-SNAPSHOT",       "1.2-SNAPSHOT",        "{{ qualified_api_version }}" , "1.2"               },
                { "M.m.p",              "1.2.1",               "{{ qualified_api_version }}" , "1.2.1"             },
                { "m.p",                "2.1",                 "{{ qualified_api_version }}" , "2.1"               },
                { "M.m.p-q-SNAPSHOT",   "1.2.1-12-SNAPSHOT",   "{{ qualified_api_version }}" , "1.2.1-12"          },
                { "M.m.p-q",            "1.2.1-client-name",   "{{ qualified_api_version }}" , "1.2.1-client-name" },
                { "M.m.p-q-q-SNAPSHOT", "1.2-beta-2-SNAPSHOT", "{{ qualified_api_version }}" , "1.2-beta-2"        },
                { "m.p-q-q",            "1.2-beta-2",          "{{ qualified_api_version }}" , "1.2-beta-2"        },
        });
    }

    @Test
    public void allows_the_version_to_be_formatted() {
        version = new Version(projectVersion);

        assertThat(example, version.formattedWith(template), is(expectedResult));
    }

    @Test
    public void outputs_the_original_version_when_converted_to_string() {
        version = new Version(projectVersion);

        assertThat(example, version.toString(), is(projectVersion));
    }
}