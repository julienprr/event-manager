package com.julienprr.eventmanager.user_service.controller;

import com.julienprr.eventmanager.user_service.dto.participant.*;
import com.julienprr.eventmanager.user_service.model.Participant;
import com.julienprr.eventmanager.user_service.service.IParticipantService;
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
@RequestMapping("/api/participants")
@RequiredArgsConstructor
@Tag(name = "Participants", description = "Operations related to participant management")
public class ParticipantController {

    private final IParticipantService participantService;
    private final ModelMapper modelMapper;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a new participant", description = "Creates a new participant in the system")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Participant successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ParticipantResponse signup(@Valid @RequestBody ParticipantSignupRequest request) {
        Participant participant = participantService.createParticipant(request);
        return modelMapper.map(participant, ParticipantResponse.class);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all participants", description = "Returns a list of all participants. Accessible only by admins.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of participants retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access forbidden for non-admin participants")
    })
    public List<ParticipantResponse> getParticipants() {
        List<Participant> participantList = participantService.getAllParticipants();
        return participantList.stream()
                .map(participant -> modelMapper.map(participant, ParticipantResponse.class))
                .toList();
    }

    @GetMapping("/{participantId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get participant by ID", description = "Returns detailed information about a participant by their ID. Accessible only by admins.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Participant retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access forbidden for non-admin participants"),
            @ApiResponse(responseCode = "404", description = "Participant not found")
    })
    public AdminParticipantResponse getParticipantById(@PathVariable Long participantId) {
        Participant participant = participantService.getParticipantById(participantId);
        return modelMapper.map(participant, AdminParticipantResponse.class);
    }

    @GetMapping("/by-email")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get participant by email", description = "Returns participant information by email. Accessible only by admins.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Participant retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access forbidden for non-admin participants"),
            @ApiResponse(responseCode = "404", description = "Participant not found")
    })
    public ParticipantResponse getParticipantByEmail(@RequestParam String email) {
        Participant participant = participantService.getParticipantByEmail(email);
        return modelMapper.map(participant, ParticipantResponse.class);
    }

    @GetMapping("/me/profile")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get own profile", description = "Returns the currently authenticated participant's profile.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Participant profile retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ParticipantProfileResponse getOwnProfile(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String email = jwt.getClaim("email");
        Participant participant = participantService.getParticipantByEmail(email);
        return modelMapper.map(participant, ParticipantProfileResponse.class);
    }

    @GetMapping("/{participantId}/public")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get public profile of a participant", description = "Returns public profile information of a participant by their ID. Accessible by authenticated participants.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Participant public profile retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Participant not found")
    })
    public ParticipantPublicProfileResponse getParticipantPublicProfile(@PathVariable Long participantId) {
        Participant participant = participantService.getParticipantById(participantId);
        return modelMapper.map(participant, ParticipantPublicProfileResponse.class);
    }


    @PutMapping("/{participantId}/profile")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update participant profile", description = "Updates the profile of a participant by their ID. Accessible only by admins.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Participant profile updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access forbidden for non-admin participants"),
            @ApiResponse(responseCode = "404", description = "Participant not found")
    })
    public AdminParticipantResponse updateParticipantProfile(
            @PathVariable Long participantId,
            @Valid @RequestBody UpdateParticipantProfileRequest request
    ) {
        Participant updatedParticipant = participantService.updateParticipantProfile(participantId, request);
        return modelMapper.map(updatedParticipant, AdminParticipantResponse.class);
    }

    @PutMapping("/me/profile")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Update own profile", description = "Updates the profile of the currently authenticated participant.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Participant profile updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ParticipantProfileResponse updateOwnProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateParticipantProfileRequest request
    ) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String email = jwt.getClaim("email");
        Participant updatedParticipant = participantService.updateParticipantProfileByEmail(email, request);
        return modelMapper.map(updatedParticipant, ParticipantProfileResponse.class);
    }

    @PatchMapping("/{participantId}/notification")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update participant notification settings", description = "Updates notification settings for a participant by their ID. Accessible only by admins.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notification settings updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access forbidden for non-admin participants"),
            @ApiResponse(responseCode = "404", description = "Participant not found")
    })
    public AdminParticipantResponse updateNotificationSettings(
            @PathVariable Long participantId,
            @Valid @RequestBody UpdateNotificationSettingsRequest request
    ) {
        Participant updatedParticipant = participantService.updateNotificationSettings(participantId, request);
        return modelMapper.map(updatedParticipant, AdminParticipantResponse.class);
    }

    @PatchMapping("/me/notification")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Update own notification settings", description = "Updates notification settings for the currently authenticated participant.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notification settings updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ParticipantProfileResponse updateOwnNotificationSettings(
            Authentication authentication,
            @Valid @RequestBody UpdateNotificationSettingsRequest request
    ) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String email = jwt.getClaim("email");
        Participant updatedParticipant = participantService.updateNotificationSettingsByEmail(email, request);
        return modelMapper.map(updatedParticipant, ParticipantProfileResponse.class);
    }

    @PatchMapping("/{participantId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Change participant status", description = "Changes the status of a participant by their ID. Accessible only by admins.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Participant status changed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access forbidden for non-admin participants"),
            @ApiResponse(responseCode = "404", description = "Participant not found")
    })
    public AdminParticipantResponse changeParticipantStatus(
            @PathVariable Long participantId,
            @Valid @RequestBody ChangeParticipantStatusRequest request
    ) {
        Participant updatedParticipant = participantService.changeParticipantStatus(participantId, request);
        return modelMapper.map(updatedParticipant, AdminParticipantResponse.class);
    }
}
