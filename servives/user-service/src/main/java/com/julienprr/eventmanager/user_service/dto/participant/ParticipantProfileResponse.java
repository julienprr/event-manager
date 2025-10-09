package com.julienprr.eventmanager.user_service.dto.participant;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipantProfileResponse {

    private String firstname;
    private String lastname;
    private String email;

    // Profile info
    private String avatarUrl;
    private String bio;
    private String city;
    private String country;

    // Notification preferences
    private boolean emailNotificationsEnabled;
    private boolean smsNotificationsEnabled;
}