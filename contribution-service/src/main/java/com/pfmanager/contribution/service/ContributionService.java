package com.pfmanager.contribution.service;

import com.pfmanager.contribution.dto.*;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Core service interface for the Contribution ledger.
 * Defines the business operations exposed by the REST API.
 */
public interface ContributionService {

    /**
     * Creates a new CONTRIBUTION ledger entry.
     * Enforces idempotency via the provided key, validates the participant,
     * rejects inactive participants and future-dated effective dates,
     * and checks per-participant reference uniqueness.
     *
     * @param request        validated request payload
     * @param idempotencyKey client-supplied idempotency key (required)
     * @return the created ledger entry
     */
    ContributionResponse createContribution(CreateContributionRequest request, String idempotencyKey);

    /**
     * Retrieves a single ledger entry by its transaction ID.
     *
     * @param transactionId UUID of the ledger entry
     * @return the ledger entry
     */
    ContributionResponse getContribution(UUID transactionId);

    /**
     * Returns a paginated history of ledger entries for a participant,
     * ordered by recordedAt descending (newest first).
     *
     * @param participantId the participant whose history is requested
     * @param pageable      page and size parameters
     * @return paginated list of ledger entries
     */
    PagedResponse<ContributionResponse> getParticipantHistory(String participantId, Pageable pageable);

    /**
     * Derives and returns the current balance for a participant in INR.
     * Balance is calculated from ledger entries, never stored independently.
     *
     * @param participantId the participant whose balance is requested
     * @return balance response with participant ID, currency, amount, and as-of timestamp
     */
    BalanceResponse getParticipantBalance(String participantId);

    /**
     * Creates a full REVERSAL of an existing CONTRIBUTION entry.
     * Enforces: original must exist, must be a CONTRIBUTION (not a REVERSAL),
     * and must not have been previously reversed.
     *
     * @param transactionId  the transaction ID of the original contribution to reverse
     * @param idempotencyKey client-supplied idempotency key (required)
     * @return the created reversal ledger entry
     */
    ContributionResponse reverseContribution(UUID transactionId, String idempotencyKey);
}
