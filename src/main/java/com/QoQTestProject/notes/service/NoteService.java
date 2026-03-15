package com.QoQTestProject.notes.service;

import com.QoQTestProject.notes.dto.request.CreateNoteDto;
import com.QoQTestProject.notes.dto.request.FilterTagsForNotes;
import com.QoQTestProject.notes.dto.response.GetOnlyTitleNotesResponseDto;
import com.QoQTestProject.notes.dto.response.NoteCardResponseDto;
import com.QoQTestProject.notes.dto.response.NoteStatisticResponseDto;
import com.QoQTestProject.notes.exeptions.EntityNotFoundException;
import com.QoQTestProject.notes.persistence.entity.NoteEntity;
import com.QoQTestProject.notes.persistence.enums.Tags;
import com.QoQTestProject.notes.persistence.repository.NoteRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;

    @Transactional(rollbackFor = Exception.class)
    public void createNote(@NonNull CreateNoteDto dto) {
        if(dto.text().isEmpty() || dto.title().isEmpty()) throw new NullPointerException("Text or Title is empty");
        NoteEntity note = NoteEntity.builder()
                .title(dto.title())
                .text(dto.text())
                .tags(dto.tags())
                .build();
        noteRepository.save(note);
    }

    @Transactional(rollbackFor = Exception.class)
    public String getFirstNoteId() {
        List<NoteEntity> noteEntityList = noteRepository.findAll();
        if (noteEntityList.isEmpty()) {
            return "not found any note";
        }
        return noteEntityList.getFirst().getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateNote(@NonNull String id, @NonNull CreateNoteDto dto) {
        if(dto.text().isEmpty() || dto.title().isEmpty()) throw new NullPointerException("Text or Title is empty");
        NoteEntity note = noteRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Note", id));
        note.setTitle(dto.title());
        note.setText(dto.text());
        note.setTags(dto.tags());
        noteRepository.save(note);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteNote(@NonNull String id) {
        NoteEntity note = noteRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Note", id));
        noteRepository.delete(note);
    }

    @Transactional(readOnly = true)
    public NoteCardResponseDto getNoteCard(@NonNull String id) {
        return new NoteCardResponseDto(noteRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Note", id)));
    }

    @Transactional(readOnly = true)
    public NoteStatisticResponseDto getNoteStatistics(@NonNull String id) {
        NoteEntity currentNote = noteRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Note", id));
        String currentText = currentNote.getText();
        Pattern pattern = Pattern.compile("[\\p{L}]+(?:[\\p{P}\\p{S}]*[\\p{L}]+)*");
        Map<String, Long> statistic = pattern.matcher(currentText.toLowerCase())
                .results()
                .map(MatchResult::group)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        return new NoteStatisticResponseDto(statistic);
    }

    @Transactional(readOnly = true)
    public Page<GetOnlyTitleNotesResponseDto> getOnlyTitleNotes(int page, int size, FilterTagsForNotes dto) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<NoteEntity> noteEntityPage;
        Set<Tags> tags = dto.tags();
        if (tags.isEmpty()) {
            noteEntityPage = noteRepository.findAll(pageable);
        } else {
            noteEntityPage = noteRepository.findByTagsIn(tags, pageable);
        }
        return noteEntityPage.map(GetOnlyTitleNotesResponseDto::new);
    }



}
