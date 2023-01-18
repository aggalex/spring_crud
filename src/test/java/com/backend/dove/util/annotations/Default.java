package com.backend.dove.util.annotations;

import java.util.Objects;

public class Default {
    public static final String STRING = "";
    public static final long LONG = -1;

    public static String get(String value, String defaultValue) {
        return Objects.equals(value, STRING)? defaultValue: value;
    }

    public static long get(long value, long defaultValue) {
        return value == LONG? defaultValue: value;
    }
}
