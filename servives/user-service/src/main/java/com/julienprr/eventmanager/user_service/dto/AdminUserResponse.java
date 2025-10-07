package com.julienprr.eventmanager.user_service.dto;

import com.julienprr.eventmanager.user_service.model.Role;
import com.julienprr.eventmanager.user_service.model.UserStatus;

import java.time.LocalDateTime;

public class AdminUserResponse {
    private String firstname;
    private String lastname;
    private String email;
    private Role role;

    private String avatarUrl;
    private String bio;
    private String city;
    private String country;

    private boolean emailNotificationsEnabled;
    private boolean smsNotificationsEnabled;

    private UserStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLoginAt;
}
