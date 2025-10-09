package com.julienprr.eventmanager.user_service.dto.participant;

import com.julienprr.eventmanager.user_service.model.ParticipantStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeParticipantStatusRequest {
    private ParticipantStatus status;
}
