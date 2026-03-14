package com.QoQTestProject.notes.properties.enums;

import java.util.Arrays;

public enum Tags {
    BUSINESS,
    PERSONAL,
    IMPORTANT;

    public static Tags fromString(String str) {
        if (str == null) return null;
        return Arrays.stream(Tags.values())
                .filter(tag -> tag.name().equalsIgnoreCase(str.trim()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No tags found with name " + str));
    }
}
