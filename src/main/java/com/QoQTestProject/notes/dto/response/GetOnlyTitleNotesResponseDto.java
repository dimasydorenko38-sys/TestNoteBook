package com.QoQTestProject.notes.dto.response;

import com.QoQTestProject.notes.properties.entity.NoteEntity;

import java.time.LocalDateTime;

public record GetOnlyTitleNotesResponseDto(
        String id,
        LocalDateTime createdDate,
        String title
        ) {

    public GetOnlyTitleNotesResponseDto(NoteEntity note) {
        this(note.getId(), note.getCreatedDate(), note.getTitle());
    }
}
