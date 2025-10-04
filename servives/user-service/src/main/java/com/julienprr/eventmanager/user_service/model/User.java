package com.julienprr.eventmanager.user_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- Identité ---
    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;   //  hashé avec BCrypt

    // --- Profil ---
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String avatarUrl;       // optionnel
    private String bio;             // optionnel
    private String city;            // optionnel
    private String country;         // optionnel

    // --- Statut ---
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;      // ACTIVE, SUSPENDED, DELETED

    // --- Audit ---
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLoginAt;

    // --- Notifications ---
    private boolean emailNotificationsEnabled;
    private boolean smsNotificationsEnabled;
}
