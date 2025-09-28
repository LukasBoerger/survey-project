package de.survey.demo.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users",
        indexes = {@Index(name="uk_users_email", columnList = "email", unique = true)})
@Data
public class UserEntity {
    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    private String displayName;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private boolean emailVerified = false;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();
}