package com.julienprr.eventmanager.user_service.service;

import com.julienprr.eventmanager.user_service.dto.SignupRequest;
import com.julienprr.eventmanager.user_service.dto.UserResponse;
import com.julienprr.eventmanager.user_service.exception.EmailAlreadyUsedException;
import com.julienprr.eventmanager.user_service.exception.ResourceNotFoundException;
import com.julienprr.eventmanager.user_service.model.User;
import com.julienprr.eventmanager.user_service.model.UserStatus;
import com.julienprr.eventmanager.user_service.repository.UserRepository;
import com.julienprr.eventmanager.user_service.dto.ChangeUserStatusRequest;
import com.julienprr.eventmanager.user_service.dto.UpdateNotificationSettingsRequest;
import com.julienprr.eventmanager.user_service.dto.UpdateUserProfileRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public User createUser(SignupRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyUsedException(request.getEmail());
        }

        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .status(UserStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .emailNotificationsEnabled(false)
                .smsNotificationsEnabled(false)
                .build();

        User savedUSer = userRepository.save(user);
        log.info("User created Successfully");
        return savedUSer;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " not found"));

    }

    @Override
    public User updateUserProfile(Long userId, UpdateUserProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (request.getFirstname() != null) {
            user.setFirstname(request.getFirstname());
        }
        if (request.getLastname() != null) {
            user.setLastname(request.getLastname());
        }
        if (request.getBio() != null) {
            user.setBio(request.getBio());
        }
        if (request.getCity() != null) {
            user.setCity(request.getCity());
        }
        if (request.getCountry() != null) {
            user.setCountry(request.getCountry());
        }
        if (request.getAvatarUrl() != null) {
            user.setAvatarUrl(request.getAvatarUrl());
        }

        user.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    @Override
    public User updateNotificationSettings(Long userId, UpdateNotificationSettingsRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (request.getEmailNotificationsEnabled() != null) {
            user.setEmailNotificationsEnabled(request.getEmailNotificationsEnabled());
        }
        if (request.getSmsNotificationsEnabled() != null) {
            user.setSmsNotificationsEnabled(request.getSmsNotificationsEnabled());
        }

        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Override
    public User changeUserStatus(Long userId, ChangeUserStatusRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setStatus(request.getStatus());
        user.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }


    @Override
    public List<UserResponse> convertToDto(List<User> users) {
        return users.stream().map(user -> modelMapper.map(user, UserResponse.class)).collect(Collectors.toList());
    }

    @Override
    public UserResponse convertToDto(User user) {
        return modelMapper.map(user, UserResponse.class);
    }
}
