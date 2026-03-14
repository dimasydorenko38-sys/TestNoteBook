package com.QoQTestProject.notes.properties.repository;

import com.QoQTestProject.notes.properties.entity.NoteEntity;
import com.QoQTestProject.notes.properties.enums.Tags;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;


@Repository
public interface NoteRepository extends MongoRepository<NoteEntity, String> {

    Page<NoteEntity> findByTagsIn(Set<Tags> tagsSet , @NonNull Pageable pageable);
}
