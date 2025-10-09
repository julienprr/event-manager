package com.julienprr.eventmanager.user_service.repository;



import com.julienprr.eventmanager.user_service.model.Participant;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    Optional<Participant> findByEmail(String email);

    boolean existsByEmail(@Email String email);
}
