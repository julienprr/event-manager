package com.julienprr.eventmanager.user_service.controller;

import com.julienprr.eventmanager.user_service.dto.*;
import com.julienprr.eventmanager.user_service.model.User;
import com.julienprr.eventmanager.user_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Users", description = "Operations related to user management")
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a new user", description = "Creates a new user in the system")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public UserResponse signup(@Valid @RequestBody SignupRequest request) {
        User user = userService.createUser(request);
        return modelMapper.map(user, UserResponse.class);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all users", description = "Returns a list of all users. Accessible only by admins.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of users retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access forbidden for non-admin users")
    })
    public List<UserResponse> getUsers() {
        List<User> userList = userService.getAllUsers();
        return userList.stream()
                .map(user -> modelMapper.map(user, UserResponse.class))
                .toList();
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get user by ID", description = "Returns detailed information about a user by their ID. Accessible only by admins.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access forbidden for non-admin users"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public AdminUserResponse getUserById(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return modelMapper.map(user, AdminUserResponse.class);
    }

    @GetMapping("/by-email")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get user by email", description = "Returns user information by email. Accessible only by admins.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access forbidden for non-admin users"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public UserResponse getUserByEmail(@RequestParam String email) {
        User user = userService.getUserByEmail(email);
        return modelMapper.map(user, UserResponse.class);
    }

    @GetMapping("/me/profile")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get own profile", description = "Returns the currently authenticated user's profile.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User profile retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public UserProfileResponse getOwnProfile(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String email = jwt.getClaim("email");
        User user = userService.getUserByEmail(email);
        return modelMapper.map(user, UserProfileResponse.class);
    }

    @GetMapping("/{userId}/public")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get public profile of a user", description = "Returns public profile information of a user by their ID. Accessible by authenticated users.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User public profile retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public UserPublicProfileResponse getUserPublicProfile(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return modelMapper.map(user, UserPublicProfileResponse.class);
    }


    @PutMapping("/{userId}/profile")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update user profile", description = "Updates the profile of a user by their ID. Accessible only by admins.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User profile updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access forbidden for non-admin users"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public AdminUserResponse updateUserProfile(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserProfileRequest request
    ) {
        User updatedUser = userService.updateUserProfile(userId, request);
        return modelMapper.map(updatedUser, AdminUserResponse.class);
    }

    @PutMapping("/me/profile")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Update own profile", description = "Updates the profile of the currently authenticated user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User profile updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
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
    @Operation(summary = "Update user notification settings", description = "Updates notification settings for a user by their ID. Accessible only by admins.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notification settings updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access forbidden for non-admin users"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public AdminUserResponse updateNotificationSettings(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateNotificationSettingsRequest request
    ) {
        User updatedUser = userService.updateNotificationSettings(userId, request);
        return modelMapper.map(updatedUser, AdminUserResponse.class);
    }

    @PatchMapping("/me/notification")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Update own notification settings", description = "Updates notification settings for the currently authenticated user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notification settings updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
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
    @Operation(summary = "Change user status", description = "Changes the status of a user by their ID. Accessible only by admins.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User status changed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access forbidden for non-admin users"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public AdminUserResponse changeUserStatus(
            @PathVariable Long userId,
            @Valid @RequestBody ChangeUserStatusRequest request
    ) {
        User updatedUser = userService.changeUserStatus(userId, request);
        return modelMapper.map(updatedUser, AdminUserResponse.class);
    }
}
