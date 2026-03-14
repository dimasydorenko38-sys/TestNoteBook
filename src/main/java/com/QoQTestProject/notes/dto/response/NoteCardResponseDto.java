package com.QoQTestProject.notes.dto.response;

import com.QoQTestProject.notes.properties.entity.NoteEntity;

public record NoteCardResponseDto(
        String id,
        String title,
        String text
) {
    public NoteCardResponseDto(NoteEntity byId) {
        this(byId.getId(), byId.getTitle(), byId.getText());
    }
}
