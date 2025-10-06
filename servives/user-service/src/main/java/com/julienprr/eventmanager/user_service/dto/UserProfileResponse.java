package com.julienprr.eventmanager.user_service.dto;


import com.julienprr.eventmanager.user_service.model.Role;
import com.julienprr.eventmanager.user_service.model.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileResponse {

    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private Role role;

    // Profile info
    private String avatarUrl;
    private String bio;
    private String city;
    private String country;

    // Notification preferences
    private boolean emailNotificationsEnabled;
    private boolean smsNotificationsEnabled;
}