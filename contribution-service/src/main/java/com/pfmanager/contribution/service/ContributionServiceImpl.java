package com.pfmanager.contribution.service;

import com.pfmanager.contribution.dto.*;
import com.pfmanager.contribution.repository.ContributionEntryRepository;
import com.pfmanager.contribution.repository.IdempotencyRecordRepository;
import com.pfmanager.contribution.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContributionServiceImpl implements ContributionService {

    private final ContributionEntryRepository contributionEntryRepository;
    private final ParticipantRepository participantRepository;
    private final IdempotencyRecordRepository idempotencyRecordRepository;

    @Override
    @Transactional
    public ContributionResponse createContribution(CreateContributionRequest request, String idempotencyKey) {
        // TODO: Implement
        // 1. Validate idempotency key — check if already processed (same hash → return cached; different hash → 409)
        // 2. Resolve and validate participant (not found → 404; inactive → 422)
        // 3. Validate effective date is not in the future
        // 4. Validate amount scale (no more than 2 decimal places)
        // 5. Validate reference uniqueness per participant (if reference provided)
        // 6. Build and save ContributionEntry
        // 7. Save IdempotencyRecord atomically
        // 8. Map to ContributionResponse and return
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    @Transactional(readOnly = true)
    public ContributionResponse getContribution(UUID transactionId) {
        // TODO: Implement
        // 1. Find by transactionId (not found → 404)
        // 2. Map to ContributionResponse and return
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<ContributionResponse> getParticipantHistory(String participantId, Pageable pageable) {
        // TODO: Implement
        // 1. Verify participant exists (not found → 404)
        // 2. Query paginated history ordered by recordedAt desc
        // 3. Map Page<ContributionEntry> to PagedResponse<ContributionResponse>
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    @Transactional(readOnly = true)
    public BalanceResponse getParticipantBalance(String participantId) {
        // TODO: Implement
        // 1. Verify participant exists (not found → 404)
        // 2. Derive balance via repository query (CONTRIBUTION amounts minus REVERSAL amounts)
        // 3. Return BalanceResponse with participantId, currency "INR", balance, and asOf = now()
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    @Transactional
    public ContributionResponse reverseContribution(UUID transactionId, String idempotencyKey) {
        // TODO: Implement
        // 1. Validate idempotency key
        // 2. Find original contribution (not found → 404)
        // 3. Verify original is a CONTRIBUTION, not a REVERSAL (reversal of reversal → 409)
        // 4. Verify no reversal already exists for this transaction (already reversed → 409)
        // 5. Build and save REVERSAL ContributionEntry referencing original transactionId
        // 6. Save IdempotencyRecord atomically
        // 7. Map to ContributionResponse and return
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
