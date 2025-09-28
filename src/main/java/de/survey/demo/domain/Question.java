package de.survey.demo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Table("questions")
public class Question {

    @Id
    private UUID id;

    // FK survey_id wird über @MappedCollection in Survey gesetzt (kein Feld nötig)
    private QuestionType type;          // STAR | EMOJI | TEXT
    private String prompt;              // entspricht DB: prompt (text)
    private int position;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    @MappedCollection(idColumn = "question_id")
    private Set<QuestionOption> options = new LinkedHashSet<>(); // nur bei EMOJI relevant

    // --- ctor(s) ---
    public Question(UUID id,
                    QuestionType type,
                    String prompt,
                    int position,
                    OffsetDateTime createdAt,
                    OffsetDateTime updatedAt,
                    Set<QuestionOption> options) {
        this.id = id;
        this.type = type;
        this.prompt = prompt;
        this.position = position;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        if (options != null) this.options = options;
    }

    public static Question newQuestion(QuestionType type, String prompt, int position) {
        var now = OffsetDateTime.now();
        return new Question(UUID.randomUUID(), type, prompt, position, now, now, new LinkedHashSet<>());
    }

    // --- convenience ---
    public void replaceOptions(Set<QuestionOption> newOptions) {
        this.options.clear();
        if (newOptions != null) this.options.addAll(newOptions);
        this.touch();
    }

    public void touch() {
        this.updatedAt = OffsetDateTime.now();
    }

    // --- getters/setters ---
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public QuestionType getType() { return type; }
    public void setType(QuestionType type) { this.type = type; }

    public String getPrompt() { return prompt; }
    public void setPrompt(String prompt) { this.prompt = prompt; }

    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Set<QuestionOption> getOptions() { return options; }
    public void setOptions(Set<QuestionOption> options) { this.options = options; }

    // --- equals/hashCode nur über id ---
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Question that)) return false;
        return Objects.equals(id, that.id);
    }
    @Override public int hashCode() { return Objects.hash(id); }
}
