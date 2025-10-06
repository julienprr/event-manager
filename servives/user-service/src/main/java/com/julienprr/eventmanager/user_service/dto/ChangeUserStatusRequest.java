package com.julienprr.eventmanager.user_service.dto;

import com.julienprr.eventmanager.user_service.model.UserStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeUserStatusRequest {
    private UserStatus status;
}
