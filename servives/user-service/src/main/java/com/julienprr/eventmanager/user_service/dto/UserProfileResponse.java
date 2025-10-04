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

    public static UserProfileResponse fromEntity(User user) {
        return UserProfileResponse.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .role(user.getRole())
                .avatarUrl(user.getAvatarUrl())
                .bio(user.getBio())
                .city(user.getCity())
                .country(user.getCountry())
                .emailNotificationsEnabled(user.isEmailNotificationsEnabled())
                .smsNotificationsEnabled(user.isSmsNotificationsEnabled())
                .build();
    }
}