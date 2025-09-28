package de.survey.demo.exceptions;

public class ValidationException extends RuntimeException {
    private final String field;
    public ValidationException(String field) { this.field = field; }
    public String field() { return field; }
}