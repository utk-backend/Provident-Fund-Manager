package com.pfmanager.contribution.exception;

import java.util.UUID;

/**
 * Thrown when a reversal request cannot be fulfilled due to a business rule violation:
 * - The contribution has already been reversed.
 * - The target entry is itself a reversal (reversal of a reversal is not supported).
 * Maps to HTTP 409.
 */
public class ReversalNotAllowedException extends RuntimeException {

    public ReversalNotAllowedException(UUID transactionId, String reason) {
        super("Reversal not allowed for transaction " + transactionId + ": " + reason);
    }
}
