package com.julienprr.eventmanager.user_service.dto.participant;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ParticipantResponse {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
}
