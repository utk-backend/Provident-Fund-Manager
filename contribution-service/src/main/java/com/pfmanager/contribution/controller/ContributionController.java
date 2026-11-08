package com.pfmanager.contribution.controller;

import com.pfmanager.contribution.dto.*;
import com.pfmanager.contribution.service.ContributionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Contributions", description = "Contribution ledger operations")
public class ContributionController {

    private static final String IDEMPOTENCY_KEY_HEADER = "Idempotency-Key";

    private final ContributionService contributionService;

    // ─── Create Contribution ──────────────────────────────────────────────────

    @Operation(
            summary = "Create a contribution",
            description = "Creates a new CONTRIBUTION ledger entry. Idempotency-Key header is required."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Contribution created",
                    content = @Content(schema = @Schema(implementation = ContributionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Missing or malformed request",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Participant not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Idempotency conflict or duplicate reference",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "Validation error (inactive participant, future date, etc.)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/contributions")
    public ResponseEntity<ContributionResponse> createContribution(
            @RequestHeader(IDEMPOTENCY_KEY_HEADER) String idempotencyKey,
            @Valid @RequestBody CreateContributionRequest request) {

        log.info("Creating contribution for participant={} idempotencyKey={}", request.participantId(), idempotencyKey);
        ContributionResponse response = contributionService.createContribution(request, idempotencyKey);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ─── Get Contribution ─────────────────────────────────────────────────────

    @Operation(summary = "Get a contribution by transaction ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contribution found",
                    content = @Content(schema = @Schema(implementation = ContributionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Malformed transaction ID",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Contribution not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/contributions/{transactionId}")
    public ResponseEntity<ContributionResponse> getContribution(
            @Parameter(description = "UUID of the ledger entry")
            @PathVariable UUID transactionId) {

        log.info("Fetching contribution transactionId={}", transactionId);
        return ResponseEntity.ok(contributionService.getContribution(transactionId));
    }

    // ─── Get Participant History ──────────────────────────────────────────────

    @Operation(
            summary = "List participant contribution history",
            description = "Returns paginated ledger entries for a participant, ordered newest first."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "History returned"),
            @ApiResponse(responseCode = "400", description = "Invalid paging parameters",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Participant not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/participants/{participantId}/contributions")
    public ResponseEntity<PagedResponse<ContributionResponse>> getParticipantHistory(
            @Parameter(description = "Stable participant identifier")
            @PathVariable String participantId,

            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") @Min(0) int page,

            @Parameter(description = "Page size (max 100)", example = "20")
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {

        log.info("Fetching history for participant={} page={} size={}", participantId, page, size);
        return ResponseEntity.ok(
                contributionService.getParticipantHistory(participantId, PageRequest.of(page, size)));
    }

    // ─── Get Participant Balance ──────────────────────────────────────────────

    @Operation(
            summary = "Get derived balance for a participant",
            description = "Calculates balance by summing CONTRIBUTION amounts and subtracting REVERSAL amounts."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Balance returned",
                    content = @Content(schema = @Schema(implementation = BalanceResponse.class))),
            @ApiResponse(responseCode = "404", description = "Participant not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/participants/{participantId}/balance")
    public ResponseEntity<BalanceResponse> getParticipantBalance(
            @Parameter(description = "Stable participant identifier")
            @PathVariable String participantId) {

        log.info("Fetching balance for participant={}", participantId);
        return ResponseEntity.ok(contributionService.getParticipantBalance(participantId));
    }

    // ─── Reverse Contribution ─────────────────────────────────────────────────

    @Operation(
            summary = "Reverse a contribution",
            description = "Creates a full REVERSAL of an existing CONTRIBUTION. " +
                    "Each contribution can be reversed at most once. Reversal of reversals is not supported."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reversal created",
                    content = @Content(schema = @Schema(implementation = ContributionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Missing or malformed request",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Original contribution not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Already reversed or reversal of reversal attempted",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/contributions/{transactionId}/reversals")
    public ResponseEntity<ContributionResponse> reverseContribution(
            @Parameter(description = "UUID of the original contribution to reverse")
            @PathVariable UUID transactionId,
            @RequestHeader(IDEMPOTENCY_KEY_HEADER) String idempotencyKey) {

        log.info("Reversing contribution transactionId={} idempotencyKey={}", transactionId, idempotencyKey);
        ContributionResponse response = contributionService.reverseContribution(transactionId, idempotencyKey);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
