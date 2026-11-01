package com.pfmanager.contribution.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Response body for GET /api/v1/participants/{participantId}/balance.
 */
@Builder
@Schema(description = "Derived balance for a participant, calculated from ledger entries")
public record BalanceResponse(

        @Schema(description = "Participant identifier", example = "PF-10001")
        String participantId,

        @Schema(description = "ISO 4217 currency code", example = "INR")
        String currency,

        @Schema(description = "Derived balance (sum of contributions minus reversals)", example = "5000.00")
        BigDecimal balance,

        @Schema(description = "UTC timestamp at which the balance was calculated")
        Instant asOf
) {}
