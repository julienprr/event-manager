package com.julienprr.eventmanager.user_service.dto;

import com.julienprr.eventmanager.user_service.model.Role;
import com.julienprr.eventmanager.user_service.model.User;
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
}
