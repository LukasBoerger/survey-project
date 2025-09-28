package de.survey.demo.requests;

import jakarta.validation.constraints.*;

public record RegisterRequest(
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8, max = 100) String password
) {}