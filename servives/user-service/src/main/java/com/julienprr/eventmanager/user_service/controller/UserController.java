package com.julienprr.eventmanager.user_service.controller;

import com.julienprr.eventmanager.user_service.dto.SignupRequest;
import com.julienprr.eventmanager.user_service.dto.UserProfileResponse;
import com.julienprr.eventmanager.user_service.dto.UserResponse;
import com.julienprr.eventmanager.user_service.model.User;
import com.julienprr.eventmanager.user_service.dto.ChangeUserStatusRequest;
import com.julienprr.eventmanager.user_service.dto.UpdateNotificationSettingsRequest;
import com.julienprr.eventmanager.user_service.dto.UpdateUserProfileRequest;
import com.julienprr.eventmanager.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse signup(@Valid @RequestBody SignupRequest request) {
        User user = userService.createUser(request);
        return userService.convertToDto(user);
    }

    @GetMapping("/all")
    public List<UserResponse> getUsers() {
        List<User> userList = userService.getAllUsers();
        return userService.convertToDto(userList);
    }

    @GetMapping("/{userId}")
    public UserResponse getUserById(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return userService.convertToDto(user);
    }

    @GetMapping("/by-email")
    public UserResponse getUserByEmail(@RequestParam String email) {
        User user = userService.getUserByEmail(email);
        return userService.convertToDto(user);
    }

    @PutMapping("/{userId}/profile")
    public UserProfileResponse updateUserProfile(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserProfileRequest request
    ) {
        User updatedUser = userService.updateUserProfile(userId, request);
        return modelMapper.map(updatedUser, UserProfileResponse.class);
    }

    @PatchMapping("/{userId}/notification")
    public UserProfileResponse updateNotificationSettings(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateNotificationSettingsRequest request
    ) {
        User updatedUser = userService.updateNotificationSettings(userId, request);
        return modelMapper.map(updatedUser, UserProfileResponse.class);
    }

    @PatchMapping("/{userId}/status")
    public UserProfileResponse changeUserStatus(
            @PathVariable Long userId,
            @Valid @RequestBody ChangeUserStatusRequest request
    ) {
        User updatedUser = userService.changeUserStatus(userId, request);
        return modelMapper.map(updatedUser, UserProfileResponse.class);
    }
}
