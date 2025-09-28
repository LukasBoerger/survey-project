package de.survey.demo.exceptions.handler;

import de.survey.demo.exceptions.EmailExistsException;
import de.survey.demo.exceptions.ValidationException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(EmailExistsException.class)
    public ResponseEntity<Map<String,Object>> handleEmailExists() {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error","email_exists"));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String,Object>> handleValidation(ValidationException ex) {
        return ResponseEntity.badRequest()
                .body(Map.of("error","validation_failed","details", Map.of(ex.field(),"invalid")));
    }

    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handleBeanValidation(org.springframework.web.bind.MethodArgumentNotValidException ex) {
        var details = ex.getBindingResult().getFieldErrors().stream()
                .collect(java.util.stream.Collectors.toMap(
                        fe -> fe.getField(), fe -> "invalid", (a,b) -> a
                ));
        return ResponseEntity.badRequest()
                .body(Map.of("error","validation_failed","details", details));
    }
}