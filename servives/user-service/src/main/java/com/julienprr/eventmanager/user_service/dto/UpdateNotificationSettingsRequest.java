package com.julienprr.eventmanager.user_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateNotificationSettingsRequest {
    private Boolean emailNotificationsEnabled;
    private Boolean smsNotificationsEnabled;
}
