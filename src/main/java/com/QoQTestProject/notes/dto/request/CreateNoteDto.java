package com.QoQTestProject.notes.dto.request;

import com.QoQTestProject.notes.persistence.enums.Tags;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;

public record CreateNoteDto(
        @NotBlank(message = "Title can not be empty")
        String title,
        @NotBlank(message = "Text can not be empty")
        String text,
        Set<Tags> tags
) {
    public CreateNoteDto {
        if (tags == null) {
            tags = new HashSet<>();
        }
    }
}
