package com.smartcodeltd.domain;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class VersionWithTimestampTest {
    @Test
    public void allows_for_timestamp_to_be_used_as_well() {
        SystemTime now = new SystemTime("2015-07-30T23:54:00Z");

        Version version = new Version("1.2.5-SNAPSHOT", now);
        String  result  = version.formattedWith("{{ api_version }}-{{ timestamp('YYYYMMddHHmm') }}");

        assertThat(result, is("1.2.5-201507302354"));
    }
}