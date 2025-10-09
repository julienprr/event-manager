package com.julienprr.eventmanager.user_service.service;

import com.julienprr.eventmanager.user_service.dto.participant.ParticipantSignupRequest;
import com.julienprr.eventmanager.user_service.exception.EmailAlreadyUsedException;
import com.julienprr.eventmanager.user_service.exception.ResourceNotFoundException;
import com.julienprr.eventmanager.user_service.model.Participant;
import com.julienprr.eventmanager.user_service.model.ParticipantStatus;
import com.julienprr.eventmanager.user_service.repository.ParticipantRepository;
import com.julienprr.eventmanager.user_service.dto.participant.ChangeParticipantStatusRequest;
import com.julienprr.eventmanager.user_service.dto.participant.UpdateNotificationSettingsRequest;
import com.julienprr.eventmanager.user_service.dto.participant.UpdateParticipantProfileRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParticipantService implements IParticipantService {

    private final ParticipantRepository participantRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public Participant createParticipant(ParticipantSignupRequest request) {

        if (participantRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyUsedException(request.getEmail());
        }

        Participant participant = Participant.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .status(ParticipantStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .emailNotificationsEnabled(false)
                .smsNotificationsEnabled(false)
                .build();

        Participant savedParticipant = participantRepository.save(participant);
        log.info("Participant created Successfully");
        return savedParticipant;
    }

    @Override
    public List<Participant> getAllParticipants() {
        return participantRepository.findAll();
    }

    @Override
    public Participant getParticipantById(Long participantId) {
        return participantRepository.findById(participantId)
                .orElseThrow(() -> new ResourceNotFoundException("Participant not found"));
    }

    @Override
    public Participant getParticipantByEmail(String email) {
        return participantRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Participant with email " + email + " not found"));

    }

    @Override
    public Participant updateParticipantProfile(Long participantId, UpdateParticipantProfileRequest request) {
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new ResourceNotFoundException("Participant not found"));

        if (request.getFirstname() != null) {
            participant.setFirstname(request.getFirstname());
        }
        if (request.getLastname() != null) {
            participant.setLastname(request.getLastname());
        }
        if (request.getBio() != null) {
            participant.setBio(request.getBio());
        }
        if (request.getCity() != null) {
            participant.setCity(request.getCity());
        }
        if (request.getCountry() != null) {
            participant.setCountry(request.getCountry());
        }
        if (request.getAvatarUrl() != null) {
            participant.setAvatarUrl(request.getAvatarUrl());
        }

        participant.setUpdatedAt(LocalDateTime.now());

        return participantRepository.save(participant);
    }

    @Override
    public Participant updateParticipantProfileByEmail(String email, UpdateParticipantProfileRequest request) {
        Participant participant = participantRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Participant not found"));

        if (request.getFirstname() != null) {
            participant.setFirstname(request.getFirstname());
        }
        if (request.getLastname() != null) {
            participant.setLastname(request.getLastname());
        }
        if (request.getBio() != null) {
            participant.setBio(request.getBio());
        }
        if (request.getCity() != null) {
            participant.setCity(request.getCity());
        }
        if (request.getCountry() != null) {
            participant.setCountry(request.getCountry());
        }
        if (request.getAvatarUrl() != null) {
            participant.setAvatarUrl(request.getAvatarUrl());
        }

        participant.setUpdatedAt(LocalDateTime.now());

        return participantRepository.save(participant);
    }

    @Override
    public Participant updateNotificationSettings(Long participantId, UpdateNotificationSettingsRequest request) {
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new ResourceNotFoundException("Participant not found"));

        if (request.getEmailNotificationsEnabled() != null) {
            participant.setEmailNotificationsEnabled(request.getEmailNotificationsEnabled());
        }
        if (request.getSmsNotificationsEnabled() != null) {
            participant.setSmsNotificationsEnabled(request.getSmsNotificationsEnabled());
        }

        participant.setUpdatedAt(LocalDateTime.now());
        return participantRepository.save(participant);
    }

    @Override
    public Participant updateNotificationSettingsByEmail(String email, UpdateNotificationSettingsRequest request) {
        Participant participant = participantRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Participant not found"));

        if (request.getEmailNotificationsEnabled() != null) {
            participant.setEmailNotificationsEnabled(request.getEmailNotificationsEnabled());
        }
        if (request.getSmsNotificationsEnabled() != null) {
            participant.setSmsNotificationsEnabled(request.getSmsNotificationsEnabled());
        }

        participant.setUpdatedAt(LocalDateTime.now());
        return participantRepository.save(participant);
    }

    @Override
    public Participant changeParticipantStatus(Long participantId, ChangeParticipantStatusRequest request) {
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new ResourceNotFoundException("Participant not found"));

        participant.setStatus(request.getStatus());
        participant.setUpdatedAt(LocalDateTime.now());

        return participantRepository.save(participant);
    }

}
