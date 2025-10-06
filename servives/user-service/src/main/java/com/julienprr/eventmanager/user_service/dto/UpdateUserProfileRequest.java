package com.julienprr.eventmanager.user_service.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserProfileRequest {

    @Size(max = 50)
    private String firstname;

    @Size(max = 50)
    private String lastname;

    @Size(max = 255)
    private String bio;

    @Size(max = 100)
    private String city;

    @Size(max = 100)
    private String country;

    @Size(max = 255)
    private String avatarUrl;
}