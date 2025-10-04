package com.julienprr.eventmanager.user_service.service;

import com.julienprr.eventmanager.user_service.dto.SignupRequest;
import com.julienprr.eventmanager.user_service.dto.UserResponse;
import com.julienprr.eventmanager.user_service.model.User;
import com.julienprr.eventmanager.user_service.model.UserStatus;
import com.julienprr.eventmanager.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(user -> new UserResponse(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getRole(),
                user.getAvatarUrl(),
                user.getBio(),
                user.getCity(),
                user.getCountry()
        )).toList();
    }

    public UserResponse createUser(SignupRequest request) {
        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(request.getPassword()) // TODO: à sécuriser plus tard (BCrypt)
                .role(request.getRole())
                .status(UserStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .emailNotificationsEnabled(false)
                .smsNotificationsEnabled(false)
                .build();

        userRepository.save(user);

        log.info("User created Successfully");

        return UserResponse.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .role(user.getRole())
                .avatarUrl(user.getAvatarUrl())
                .bio(user.getBio())
                .city(user.getCity())
                .country(user.getCountry())
                .build();
    }
}
