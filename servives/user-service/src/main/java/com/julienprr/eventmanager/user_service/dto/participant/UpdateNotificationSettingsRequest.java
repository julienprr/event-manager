package com.julienprr.eventmanager.user_service.dto.participant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateNotificationSettingsRequest {
    private Boolean emailNotificationsEnabled;
    private Boolean smsNotificationsEnabled;
}
