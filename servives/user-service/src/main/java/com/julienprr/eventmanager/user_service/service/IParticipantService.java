package com.julienprr.eventmanager.user_service.service;

import com.julienprr.eventmanager.user_service.dto.participant.ParticipantSignupRequest;
import com.julienprr.eventmanager.user_service.model.Participant;
import com.julienprr.eventmanager.user_service.dto.participant.ChangeParticipantStatusRequest;
import com.julienprr.eventmanager.user_service.dto.participant.UpdateNotificationSettingsRequest;
import com.julienprr.eventmanager.user_service.dto.participant.UpdateParticipantProfileRequest;

import java.util.List;

public interface IParticipantService {


    Participant createParticipant(ParticipantSignupRequest request);

    List<Participant> getAllParticipants();

    Participant getParticipantById(Long participantId);

    Participant getParticipantByEmail(String email);

    Participant updateParticipantProfile(Long participantId, UpdateParticipantProfileRequest request);

    Participant updateParticipantProfileByEmail(String email, UpdateParticipantProfileRequest request);

    Participant updateNotificationSettings(Long participantId, UpdateNotificationSettingsRequest request);

    Participant updateNotificationSettingsByEmail(String email, UpdateNotificationSettingsRequest request);

    Participant changeParticipantStatus(Long participantId, ChangeParticipantStatusRequest request);
}
