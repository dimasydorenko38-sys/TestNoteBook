package com.QoQTestProject.notes.controllers;

import com.QoQTestProject.notes.dto.request.CreateNoteDto;
import com.QoQTestProject.notes.dto.request.FilterTagsForNotes;
import com.QoQTestProject.notes.dto.response.GetOnlyTitleNotesResponseDto;
import com.QoQTestProject.notes.dto.response.NoteCardResponseDto;
import com.QoQTestProject.notes.dto.response.NoteStatisticResponseDto;
import com.QoQTestProject.notes.service.NoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/note")
public class NoteController {

    private final NoteService noteService;

    @GetMapping("/getFirstId")
    public ResponseEntity<String> getFeritId() {
        return ResponseEntity.status(HttpStatus.OK).body(noteService.getFirstNoteId());
    }

    @PostMapping()
    public ResponseEntity<String> createNote(@Valid @RequestBody CreateNoteDto dto) {
        noteService.createNote(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Note created");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateNote(@PathVariable String id,@Valid @RequestBody CreateNoteDto dto) {
        noteService.updateNote(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body("Note '" + dto.title() + "' updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNote(@PathVariable String id) {
        noteService.deleteNote(id);
        return ResponseEntity.status(HttpStatus.OK).body("Note deleted");
    }

    @GetMapping("/search")
    public ResponseEntity<Page<GetOnlyTitleNotesResponseDto>> getOnlyTitleNotes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestBody FilterTagsForNotes dto) {
        return ResponseEntity.status(HttpStatus.OK).body(noteService.getOnlyTitleNotes(page, size, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteCardResponseDto> getNoteCard(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(noteService.getNoteCard(id));
    }

    @GetMapping("/statistic/{id}")
    public ResponseEntity<NoteStatisticResponseDto> getNoteStatistics(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(noteService.getNoteStatistics(id));
    }
}
