package com.julienprr.eventmanager.user_service.dto;

import com.julienprr.eventmanager.user_service.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class SignupRequest {

    @NotBlank
    private String firstname;

    @NotBlank
    private String lastname;

    @Email
    private String email;

    @Size(min = 8)
    private String password;

    private Role role;
}
