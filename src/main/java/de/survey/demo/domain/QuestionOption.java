package de.survey.demo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;
import java.util.UUID;

@Table("question_options")
public class QuestionOption {

    @Id
    private UUID id;

    // FK question_id kommt über @MappedCollection in Question
    private int ord;         // Reihenfolge
    private String label;    // z. B. 😀 😐 😞
    private String value;    // optional: "happy" | "neutral" | "sad"

    public QuestionOption(UUID id, int ord, String label, String value) {
        this.id = id;
        this.ord = ord;
        this.label = label;
        this.value = value;
    }

    public static QuestionOption of(int ord, String label, String value) {
        return new QuestionOption(UUID.randomUUID(), ord, label, value);
    }

    // getters/setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public int getOrd() { return ord; }
    public void setOrd(int ord) { this.ord = ord; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuestionOption that)) return false;
        return Objects.equals(id, that.id);
    }
    @Override public int hashCode() { return Objects.hash(id); }
}
