package com.julienprr.eventmanager.user_service.controller;

import com.julienprr.eventmanager.user_service.dto.*;
import com.julienprr.eventmanager.user_service.model.User;
import com.julienprr.eventmanager.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
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
        return modelMapper.map(user, UserResponse.class);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        List<User> userList = userService.getAllUsers();
        return userList.stream()
                .map(user -> modelMapper.map(user, UserResponse.class))
                .toList();
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public AdminUserResponse getUserById(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return modelMapper.map(user, AdminUserResponse.class);
    }

    @GetMapping("/by-email")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getUserByEmail(@RequestParam String email) {
        User user = userService.getUserByEmail(email);
        return modelMapper.map(user, UserResponse.class);
    }

    @GetMapping("/me/profile")
    @PreAuthorize("isAuthenticated()")
    public UserProfileResponse getOwnProfile(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String email = jwt.getClaim("email");
        User user = userService.getUserByEmail(email);
        return modelMapper.map(user, UserProfileResponse.class);
    }

    @GetMapping("/{userId}/public")
    @PreAuthorize("isAuthenticated()")
    public UserPublicProfileResponse getUserPublicProfile(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return modelMapper.map(user, UserPublicProfileResponse.class);
    }


    @PutMapping("/{userId}/profile")
    @PreAuthorize("hasRole('ADMIN')")
    public AdminUserResponse updateUserProfile(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserProfileRequest request
    ) {
        User updatedUser = userService.updateUserProfile(userId, request);
        return modelMapper.map(updatedUser, AdminUserResponse.class);
    }

    @PutMapping("/me/profile")
    @PreAuthorize("isAuthenticated()")
    public UserProfileResponse updateOwnProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateUserProfileRequest request
    ) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String email = jwt.getClaim("email");
        User updatedUser = userService.updateUserProfileByEmail(email, request);
        return modelMapper.map(updatedUser, UserProfileResponse.class);
    }

    @PatchMapping("/{userId}/notification")
    @PreAuthorize("hasRole('ADMIN')")
    public AdminUserResponse updateNotificationSettings(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateNotificationSettingsRequest request
    ) {
        User updatedUser = userService.updateNotificationSettings(userId, request);
        return modelMapper.map(updatedUser, AdminUserResponse.class);
    }

    @PatchMapping("/me/notification")
    @PreAuthorize("isAuthenticated()")
    public UserProfileResponse updateOwnNotificationSettings(
            Authentication authentication,
            @Valid @RequestBody UpdateNotificationSettingsRequest request
    ) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String email = jwt.getClaim("email");
        User updatedUser = userService.updateNotificationSettingsByEmail(email, request);
        return modelMapper.map(updatedUser, UserProfileResponse.class);
    }

    @PatchMapping("/{userId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public AdminUserResponse changeUserStatus(
            @PathVariable Long userId,
            @Valid @RequestBody ChangeUserStatusRequest request
    ) {
        User updatedUser = userService.changeUserStatus(userId, request);
        return modelMapper.map(updatedUser, AdminUserResponse.class);
    }
}
