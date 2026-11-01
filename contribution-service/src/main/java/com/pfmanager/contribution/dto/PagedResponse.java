package com.pfmanager.contribution.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

/**
 * Generic paginated wrapper returned by list endpoints.
 * Used by GET /api/v1/participants/{participantId}/contributions.
 */
@Builder
@Schema(description = "Paginated result set")
public record PagedResponse<T>(

        @Schema(description = "Items on the current page")
        List<T> content,

        @Schema(description = "Requested page number (0-based)", example = "0")
        int page,

        @Schema(description = "Requested page size", example = "20")
        int size,

        @Schema(description = "Total number of matching items")
        long totalElements,

        @Schema(description = "Total number of pages")
        int totalPages
) {}
