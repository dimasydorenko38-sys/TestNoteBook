package com.QoQTestProject.notes.persistence.entity;

import com.QoQTestProject.notes.persistence.enums.Tags;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Document(collection = "notes")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoteEntity {

    @Id
    private String id;
    @CreatedDate
    private LocalDateTime createdDate;
    @NonNull private String title;
    @NonNull private String text;
    private Set<Tags> tags;

}
