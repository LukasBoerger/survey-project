package de.survey.demo.service;

import de.survey.demo.entities.UserEntity;
import de.survey.demo.exceptions.EmailExistsException;
import de.survey.demo.repository.UserRepository;
import de.survey.demo.requests.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(RegisterRequest req) {
        String email = normalize(req.email());
        if (users.existsByEmail(email)) throw new EmailExistsException();

        var user = new UserEntity();
        user.setId(UUID.randomUUID());
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(req.password()));
        users.save(user);
    }

    private String normalize(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }
}
