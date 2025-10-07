package com.julienprr.eventmanager.user_service.service;

import com.julienprr.eventmanager.user_service.dto.SignupRequest;
import com.julienprr.eventmanager.user_service.dto.UserResponse;
import com.julienprr.eventmanager.user_service.model.User;
import com.julienprr.eventmanager.user_service.dto.ChangeUserStatusRequest;
import com.julienprr.eventmanager.user_service.dto.UpdateNotificationSettingsRequest;
import com.julienprr.eventmanager.user_service.dto.UpdateUserProfileRequest;

import java.util.List;

public interface IUserService {
    User createUser(SignupRequest request);

    List<User> getAllUsers();

    User getUserById(Long userId);

    User getUserByEmail(String email);

    User updateUserProfile(Long userId, UpdateUserProfileRequest request);

    User updateUserProfileByEmail(String email, UpdateUserProfileRequest request);

    User updateNotificationSettings(Long userId, UpdateNotificationSettingsRequest request);

    User updateNotificationSettingsByEmail(String email, UpdateNotificationSettingsRequest request);

    User changeUserStatus(Long userId, ChangeUserStatusRequest request);

}
