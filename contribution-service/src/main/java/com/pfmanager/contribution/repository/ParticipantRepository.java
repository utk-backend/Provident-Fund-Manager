package com.pfmanager.contribution.repository;

import com.pfmanager.contribution.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, String> {
    // Lookup by participantId (PK) is provided by JpaRepository#findById
    Optional<Participant> findByParticipantId(String participantId);
}
