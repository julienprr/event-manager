package com.julienprr.eventmanager.user_service.dto;


import com.julienprr.eventmanager.user_service.model.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPublicProfileResponse {

    private String firstname;
    private String lastname;
    private Role role;

    // Profile info
    private String avatarUrl;
    private String bio;
    private String city;
    private String country;
}