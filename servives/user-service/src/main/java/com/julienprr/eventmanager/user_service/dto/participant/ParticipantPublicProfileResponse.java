package com.julienprr.eventmanager.user_service.dto.participant;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipantPublicProfileResponse {

    private String firstname;
    private String lastname;
    private String avatarUrl;
    private String bio;
    private String city;
    private String country;
}