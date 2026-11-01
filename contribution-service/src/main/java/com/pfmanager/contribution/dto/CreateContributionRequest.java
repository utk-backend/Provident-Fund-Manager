package com.pfmanager.contribution.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Request body for POST /api/v1/contributions.
 * The caller must also supply an Idempotency-Key header.
 */
@Builder
@Schema(description = "Request to create a new contribution ledger entry")
public record CreateContributionRequest(

        @NotBlank(message = "participantId is required")
        @Size(max = 50, message = "participantId must not exceed 50 characters")
        @Schema(description = "Stable participant identifier", example = "PF-10001")
        String participantId,

        @NotNull(message = "amount is required")
        @DecimalMin(value = "0.01", message = "amount must be greater than zero")
        @Digits(integer = 17, fraction = 2, message = "amount must have at most 17 integer digits and 2 decimal places")
        @Schema(description = "Positive contribution amount (INR, max 2 decimal places)", example = "2500.00")
        BigDecimal amount,

        @NotBlank(message = "currency is required")
        @Pattern(regexp = "INR", message = "Only INR is supported in Phase 1")
        @Schema(description = "ISO 4217 currency code — INR only", example = "INR")
        String currency,

        @NotNull(message = "effectiveDate is required")
        @Schema(description = "Business effective date (ISO date, not in the future)", example = "2026-10-29")
        LocalDate effectiveDate,

        @Size(max = 100, message = "reference must not exceed 100 characters")
        @Schema(description = "Optional external/business reference", example = "PAYROLL-OCT-2026-10001")
        String reference,

        @Size(max = 500, message = "note must not exceed 500 characters")
        @Schema(description = "Optional short description", example = "October contribution")
        String note,

        @Schema(description = "Optional caller or system identity")
        String createdBy
) {}
