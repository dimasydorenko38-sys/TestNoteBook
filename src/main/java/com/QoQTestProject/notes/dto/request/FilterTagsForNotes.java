package com.QoQTestProject.notes.dto.request;

import java.util.HashSet;
import java.util.Set;

public record FilterTagsForNotes(
        Set<String> tags
) {
    public FilterTagsForNotes {
        if (tags == null) {
            tags = new HashSet<>();
        }}
}
