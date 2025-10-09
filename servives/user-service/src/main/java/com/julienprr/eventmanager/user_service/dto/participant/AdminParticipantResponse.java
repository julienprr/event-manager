package com.julienprr.eventmanager.user_service.dto.participant;

import com.julienprr.eventmanager.user_service.model.ParticipantStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminParticipantResponse {
    private String firstname;
    private String lastname;
    private String email;

    private String avatarUrl;
    private String bio;
    private String city;
    private String country;

    private boolean emailNotificationsEnabled;
    private boolean smsNotificationsEnabled;

    private ParticipantStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLoginAt;
}
