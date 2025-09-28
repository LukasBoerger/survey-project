package de.survey.demo.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.*;

@Table("surveys")
@Data
public class Survey {
    @Id
    private UUID id;
    private UUID ownerId;
    private String title;
    private String publicId;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    @MappedCollection(idColumn = "survey_id")
    private Set<Question> questions = new LinkedHashSet<>();

    public Survey(UUID id, UUID ownerId, String title, String publicId,
                  OffsetDateTime createdAt, OffsetDateTime updatedAt, Set<Question> questions) {
        this.id = id; this.ownerId = ownerId; this.title = title; this.publicId = publicId;
        this.createdAt = createdAt; this.updatedAt = updatedAt;
        if (questions != null) this.questions = questions;
    }

    public static Survey newSurvey(UUID ownerId, String title, String publicId) {
        var now = OffsetDateTime.now();
        return new Survey(UUID.randomUUID(), ownerId, title, publicId, now, now, new LinkedHashSet<>());
    }

    public void replaceQuestions(List<Question> newQuestions) {
        this.questions.clear();
        int pos = 0;
        for (Question q : newQuestions) {
            this.questions.add(new Question(UUID.randomUUID(), q.getType(), q.getPrompt(), pos++, OffsetDateTime.now(), OffsetDateTime.now(), Collections.emptySet()));
        }
    }

    public void touch() { this.updatedAt = OffsetDateTime.now(); }
}
