package com.QoQTestProject.notes.dto.request;

import com.QoQTestProject.notes.persistence.enums.Tags;

import java.util.HashSet;
import java.util.Set;

public record FilterTagsForNotes(
        Set<Tags> tags
) {
    public FilterTagsForNotes {
        if (tags == null) {
            tags = new HashSet<>();
        }}
}
