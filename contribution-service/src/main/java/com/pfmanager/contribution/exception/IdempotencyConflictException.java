package com.pfmanager.contribution.exception;

/**
 * Thrown when an Idempotency-Key is reused with a different request payload.
 * Maps to HTTP 409.
 */
public class IdempotencyConflictException extends RuntimeException {

    public IdempotencyConflictException(String idempotencyKey) {
        super("Idempotency key has already been used with a different request payload: " + idempotencyKey);
    }
}
