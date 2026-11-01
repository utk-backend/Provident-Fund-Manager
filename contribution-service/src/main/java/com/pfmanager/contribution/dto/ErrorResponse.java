package com.pfmanager.contribution.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

/**
 * Consistent error response body returned for all 4xx and 5xx responses.
 * Never exposes stack traces, SQL messages, credentials, or internal details.
 */
@Builder
@Schema(description = "Error response body")
public record ErrorResponse(

        @Schema(description = "Machine-readable error code", example = "VALIDATION_ERROR")
        String errorCode,

        @Schema(description = "Human-readable error message", example = "Request validation failed")
        String message,

        @Schema(description = "Per-field validation errors, present on 400/422 responses")
        List<FieldError> fieldErrors
) {

    /**
     * A single field-level validation error.
     */
    @Builder
    @Schema(description = "Validation error for a specific request field")
    public record FieldError(

            @Schema(description = "Name of the field that failed validation", example = "amount")
            String field,

            @Schema(description = "Reason the field was rejected", example = "must be greater than zero")
            String message
    ) {}
}
