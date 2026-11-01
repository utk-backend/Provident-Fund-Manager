package com.pfmanager.contribution.repository;

import com.pfmanager.contribution.entity.ContributionEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContributionEntryRepository extends JpaRepository<ContributionEntry, UUID> {

    /**
     * Returns all ledger entries for a participant ordered by recordedAt descending
     * (newest first), with pagination support.
     */
    Page<ContributionEntry> findByParticipant_ParticipantIdOrderByRecordedAtDesc(
            String participantId, Pageable pageable);

    /**
     * Checks whether a REVERSAL entry already exists that references the given
     * original transaction ID. Used to enforce the "at most one reversal" rule.
     */
    boolean existsByReversesTransactionId(UUID reversesTransactionId);

    /**
     * Checks whether any entry for a given participant already uses the provided
     * reference string. Used to enforce per-participant reference uniqueness.
     */
    boolean existsByParticipant_ParticipantIdAndReference(String participantId, String reference);

    /**
     * Derives the balance for a participant by summing CONTRIBUTION amounts and
     * subtracting REVERSAL amounts. Returns null (mapped to zero in the service)
     * if the participant has no entries.
     */
    @Query("""
            SELECT
                COALESCE(SUM(CASE WHEN e.entryType = 'CONTRIBUTION' THEN e.amount ELSE -e.amount END), 0)
            FROM ContributionEntry e
            WHERE e.participant.participantId = :participantId
              AND e.currency = :currency
            """)
    BigDecimal deriveBalance(@Param("participantId") String participantId,
                             @Param("currency") String currency);

    /**
     * Finds the REVERSAL entry that references the given original transaction ID,
     * if one exists.
     */
    Optional<ContributionEntry> findByReversesTransactionId(UUID reversesTransactionId);
}
