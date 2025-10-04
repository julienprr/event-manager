package com.julienprr.eventmanager.user_service.dto;

import com.julienprr.eventmanager.user_service.model.Role;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private Role role;
    private String avatarUrl;
    private String bio;
    private String city;
    private String country;


}
