package com.QoQTestProject.notes;

import com.QoQTestProject.notes.dto.request.CreateNoteDto;
import com.QoQTestProject.notes.dto.request.FilterTagsForNotes;
import com.QoQTestProject.notes.dto.response.GetOnlyTitleNotesResponseDto;
import com.QoQTestProject.notes.dto.response.NoteCardResponseDto;
import com.QoQTestProject.notes.dto.response.NoteStatisticResponseDto;
import com.QoQTestProject.notes.exeptions.EntityNotFoundException;
import com.QoQTestProject.notes.persistence.entity.NoteEntity;
import com.QoQTestProject.notes.persistence.enums.Tags;
import com.QoQTestProject.notes.persistence.repository.NoteRepository;
import com.QoQTestProject.notes.service.NoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class NotesApplicationTests {

    @Autowired
    private NoteService noteService;

    @Autowired
    private NoteRepository noteRepository;

    @Test
    void contextLoads() {
        assertThat(noteService).isNotNull();
        assertThat(noteRepository).isNotNull();
    }

    @BeforeEach
    void cleanUp() {
        noteRepository.deleteAll();
    }

    @Test
    void createNote() {
        CreateNoteDto dto = new CreateNoteDto("TestNote", "TestNoteTestNoteTestNote", Set.of(Tags.BUSINESS, Tags.IMPORTANT));
        noteService.createNote(dto);
        List<NoteEntity> allNotes = noteRepository.findAll();
        assertThat(allNotes).hasSize(1);
        assertThat(allNotes.get(0).getTitle()).isEqualTo("TestNote");
        assertThat(allNotes.get(0).getId()).isNotNull();
    }

    @Test
    void updateNote() {
        NoteEntity noteEntity = new NoteEntity("123", LocalDateTime.now(), "TestNote", "TestNoteTestNoteTestNote", Set.of(Tags.BUSINESS, Tags.IMPORTANT));
        noteRepository.save(noteEntity);
        CreateNoteDto dto = new CreateNoteDto("TestUpdate", "TestUpdate,TestUpdate", Set.of(Tags.BUSINESS));
        noteService.updateNote(noteEntity.getId(), dto);
        NoteEntity updatedNote = noteRepository.findById(noteEntity.getId()).orElseThrow();
        assertThat(updatedNote.getTitle()).isEqualTo("TestUpdate");
        assertThat(updatedNote.getId()).isEqualTo(noteEntity.getId());
        assertThat(updatedNote.getText()).isEqualTo("TestUpdate,TestUpdate");
        assertThat(updatedNote.getTags()).contains(Tags.BUSINESS);
        assertThat(updatedNote.getTags()).doesNotContain(Tags.IMPORTANT);
    }

    @Test
    @DisplayName("Title can not be null")
    void updateNote_WhenTitleIsEmpty() {
        NoteEntity saved = noteRepository.save(NoteEntity.builder().title("Title").text("Text").build());
        CreateNoteDto invalidDto = new CreateNoteDto("", "New Text", Set.of());
        assertThrows(NullPointerException.class, () ->
                noteService.updateNote(saved.getId(), invalidDto)
        );
    }

    @Test
    @DisplayName("Text can not be null")
    void updateNote_WhenTextIsEmpty() {
        NoteEntity saved = noteRepository.save(NoteEntity.builder().title("Title").text("Text").build());
        CreateNoteDto invalidDto = new CreateNoteDto("Title", null, Set.of());
        assertThrows(NullPointerException.class, () ->
                noteService.updateNote(saved.getId(), invalidDto)
        );
    }

    @Test
    @DisplayName("non-existent-id")
    void updateNote_WhenIdNotFound() {
        CreateNoteDto dto = new CreateNoteDto("Title", "Text", Set.of());
        assertThrows(EntityNotFoundException.class, () ->
                noteService.updateNote("non-existent-id", dto)
        );
    }

    @Test
    @DisplayName("Check globalHandler: Not found Entity")
    void entityNotFound() {
        assertThrows(EntityNotFoundException.class, () ->
                noteRepository.findById("non-existent-id").orElseThrow(() -> new EntityNotFoundException("name", "id"))
        );
    }

    @Test
    @DisplayName("Delete")
    void deleteNote_Success() {
        NoteEntity savedNote = noteRepository.save(NoteEntity.builder()
                .title("To be deleted")
                .text("Some text")
                .build());
        String id = savedNote.getId();
        noteService.deleteNote(id);
        Optional<NoteEntity> deletedNote = noteRepository.findById(id);
        assertThat(deletedNote).isEmpty();
    }

    @Test
    @DisplayName("getNoteCard")
    void getNoteCard_Success() {
        NoteEntity entity = NoteEntity.builder()
                .title("Spring Security")
                .text("Learn JWT tokens")
                .tags(Set.of(Tags.IMPORTANT, Tags.BUSINESS))
                .createdDate(LocalDateTime.now())
                .build();
        NoteEntity saved = noteRepository.save(entity);
        NoteCardResponseDto result = noteService.getNoteCard(saved.getId());
        assertThat(result.title()).isEqualTo("Spring Security");
        assertThat(result.text()).isEqualTo("Learn JWT tokens");
    }

    @Test
    @DisplayName("getNoteCard(fakeID)")
    void getNoteCard_NotFound() {
        String fakeId = "random_id_123";
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                noteService.getNoteCard(fakeId)
        );
        assertThat(exception.getMessage()).contains("Note").contains(fakeId);
    }

    @Test
    @DisplayName("getNoteCard(null)")
    void getNoteCard_NullId() {
        assertThrows(NullPointerException.class, () -> noteService.getNoteCard(null));
    }


    @ParameterizedTest
    @CsvSource({
            "'Java, Java, Java', 1, java, 3",
            "'Привіт, світ! Привіт', 2, привіт, 2",
            "'Spring-boot is cool', 3, spring-boot, 1",
            "'m&m`s, fire, m&m`s', 2, m&m`s, 2",
            "'м`яч, м`яч, поле', 2, м`яч, 2",
            "'%2#$***`м`яч, м`яч, поле', 2, м`яч, 2"
    })
    @DisplayName("Pattern for statistic is correct")
    void getNoteStatistics_WordCountLogic(String input, int uniqueWords, String targetWord, long expectedCount) {
        NoteEntity entity = noteRepository.save(NoteEntity.builder()
                .title("Title")
                .text(input)
                .build());
        NoteStatisticResponseDto result = noteService.getNoteStatistics(entity.getId());
        assertThat(result.statistic())
                .as("count uniqueWords: " + input)
                .hasSize(uniqueWords);
        assertThat(result.statistic())
                .as("target '" + targetWord + "' count: " + expectedCount)
                .containsEntry(targetWord.toLowerCase(), expectedCount);
    }


    @ParameterizedTest
    @CsvSource({
            "1223, ``~~, ;: 4342",
            "' '",
            "''"
    })
    @DisplayName("statistic is empty")
    void getNoteStatistics(String string) {
        NoteEntity entity = noteRepository.save(NoteEntity.builder()
                .title("Title")
                .text(string)
                .build());
        var result = noteService.getNoteStatistics(entity.getId());
        assertThat(result.statistic())
                .as("not empty statistic")
                .isEmpty();
    }

    @Test
    @DisplayName("Get notes if filter is empty")
    void getOnlyTitleNotes_AllNotes() {
        noteRepository.save(new NoteEntity(null, LocalDateTime.now(), "Title 1", "Text", Set.of(Tags.BUSINESS)));
        noteRepository.save(new NoteEntity(null, LocalDateTime.now().minusDays(1), "Title 2", "Text", Set.of(Tags.PERSONAL)));
        FilterTagsForNotes filter = new FilterTagsForNotes(Set.of());
        Page<GetOnlyTitleNotesResponseDto> result = noteService.getOnlyTitleNotes(0, 10, filter);
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(2);
    }

    @Test
    @DisplayName("Get notes if filter is BUSINESS")
    void getOnlyTitleNotes_WithFilter() {
        noteRepository.save(new NoteEntity(null, LocalDateTime.now(), "Business Note", "Text", Set.of(Tags.BUSINESS)));
        noteRepository.save(new NoteEntity(null, LocalDateTime.now(), "Personal Note", "Text", Set.of(Tags.PERSONAL)));
        FilterTagsForNotes filter = new FilterTagsForNotes(Set.of(Tags.BUSINESS));
        Page<GetOnlyTitleNotesResponseDto> result = noteService.getOnlyTitleNotes(0, 10, filter);
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).title()).isEqualTo("Business Note");
    }


    @Test
    @DisplayName("Get sorted notes")
    void getOnlyTitleNotes_PaginationAndSorting() {
        LocalDateTime now = LocalDateTime.now();
        noteRepository.save(new NoteEntity(null, now.minusHours(5), "Old Note", "Text", Set.of()));
        noteRepository.save(new NoteEntity(null, now, "New Note", "Text", Set.of()));
        FilterTagsForNotes filter = new FilterTagsForNotes(Set.of());
        Page<GetOnlyTitleNotesResponseDto> result = noteService.getOnlyTitleNotes(0, 1, filter);
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).title()).isEqualTo("New Note");
        assertThat(result.getTotalPages()).isEqualTo(2);
    }

}

