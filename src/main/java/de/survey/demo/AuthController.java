package de.survey.demo;

import de.survey.demo.requests.RegisterRequest;
import de.survey.demo.responses.RegisterResponse;
import de.survey.demo.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegistrationService registrationService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Validated @RequestBody RegisterRequest req) {
        registrationService.register(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new RegisterResponse("registered", "/login"));
    }
}