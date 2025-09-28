package de.survey.demo.service;

import de.survey.demo.exceptions.EmailExistsException;
import de.survey.demo.repository.UserRepository;
import de.survey.demo.requests.RegisterRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

class RegistrationServiceTest {

    @Test
    void registersNewEmail() {
        UserRepository repo = mock(UserRepository.class);
        PasswordEncoder enc = mock(PasswordEncoder.class);
        when(repo.existsByEmail("a@b.c")).thenReturn(false);
        when(enc.encode("Passw0rd!")).thenReturn("{argon2}hash");

        RegistrationService svc = new RegistrationService(repo, enc);
        svc.register(new RegisterRequest("A@B.c", "Passw0rd!"));

        verify(repo).save(argThat(u ->
                u.getEmail().equals("a@b.c") && u.getPasswordHash().startsWith("{argon2}")
        ));
    }

    @Test
    void failsOnExistingEmail() {
        UserRepository repo = mock(UserRepository.class);
        PasswordEncoder enc = mock(PasswordEncoder.class);
        when(repo.existsByEmail("a@b.c")).thenReturn(true);

        RegistrationService svc = new RegistrationService(repo, enc);
        assertThrows(EmailExistsException.class,
                () -> svc.register(new RegisterRequest("a@b.c", "Passw0rd!")));
    }
}