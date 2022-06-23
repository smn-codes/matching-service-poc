package com.hicounselor.matching.core.utils;

import java.util.Objects;

public class PropertyUtils {

    public PropertyUtils() {
        //ignore
    }

    public static String getProperty(final String name, final String defaultValue) {
        if (Objects.isNull(name)) {
            return null;
        }
        String value = System.getenv(name);
        if (Objects.nonNull(value)) {
            return value;
        }
        String property = name.toLowerCase().replaceAll("_", ".");
        return System.getProperty(property, defaultValue);
    }


}
