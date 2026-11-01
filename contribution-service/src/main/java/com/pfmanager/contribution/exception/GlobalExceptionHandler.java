package com.pfmanager.contribution.exception;

import com.pfmanager.contribution.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

/**
 * Centralised exception → HTTP response mapping.
 * All error responses use the {@link ErrorResponse} structure.
 * Stack traces, SQL messages, and internal details are never exposed to callers.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ─── Domain / business exceptions ────────────────────────────────────────

    @ExceptionHandler(ParticipantNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleParticipantNotFound(ParticipantNotFoundException ex) {
        log.warn("Participant not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.builder()
                        .errorCode("PARTICIPANT_NOT_FOUND")
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(ParticipantInactiveException.class)
    public ResponseEntity<ErrorResponse> handleParticipantInactive(ParticipantInactiveException ex) {
        log.warn("Inactive participant: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ErrorResponse.builder()
                        .errorCode("PARTICIPANT_INACTIVE")
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(ContributionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleContributionNotFound(ContributionNotFoundException ex) {
        log.warn("Contribution not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.builder()
                        .errorCode("CONTRIBUTION_NOT_FOUND")
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(IdempotencyConflictException.class)
    public ResponseEntity<ErrorResponse> handleIdempotencyConflict(IdempotencyConflictException ex) {
        log.warn("Idempotency conflict: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorResponse.builder()
                        .errorCode("IDEMPOTENCY_CONFLICT")
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(ReversalNotAllowedException.class)
    public ResponseEntity<ErrorResponse> handleReversalNotAllowed(ReversalNotAllowedException ex) {
        log.warn("Reversal not allowed: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorResponse.builder()
                        .errorCode("REVERSAL_NOT_ALLOWED")
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(DuplicateReferenceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateReference(DuplicateReferenceException ex) {
        log.warn("Duplicate reference: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorResponse.builder()
                        .errorCode("DUPLICATE_REFERENCE")
                        .message(ex.getMessage())
                        .build());
    }

    // ─── Validation exceptions ────────────────────────────────────────────────

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        List<ErrorResponse.FieldError> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> ErrorResponse.FieldError.builder()
                        .field(fe.getField())
                        .message(fe.getDefaultMessage())
                        .build())
                .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .errorCode("VALIDATION_ERROR")
                        .message("Request validation failed")
                        .fieldErrors(fieldErrors)
                        .build());
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponse> handleMissingHeader(MissingRequestHeaderException ex) {
        log.warn("Missing required header: {}", ex.getHeaderName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .errorCode("MISSING_HEADER")
                        .message("Required header is missing: " + ex.getHeaderName())
                        .build());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.warn("Type mismatch for parameter '{}': {}", ex.getName(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .errorCode("INVALID_PARAMETER")
                        .message("Invalid value for parameter: " + ex.getName())
                        .build());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadableBody(HttpMessageNotReadableException ex) {
        log.warn("Malformed request body: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .errorCode("MALFORMED_REQUEST")
                        .message("Request body is malformed or contains an invalid field type")
                        .build());
    }

    // ─── Catch-all ────────────────────────────────────────────────────────────

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex) {
        // Log the full exception internally but never expose it to the caller
        log.error("Unexpected internal error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder()
                        .errorCode("INTERNAL_ERROR")
                        .message("An unexpected error occurred. Please try again later.")
                        .build());
    }
}
