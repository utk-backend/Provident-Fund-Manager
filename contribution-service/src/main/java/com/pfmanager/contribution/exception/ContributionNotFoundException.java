package com.pfmanager.contribution.exception;

import java.util.UUID;

/**
 * Thrown when a contribution entry is not found by its transaction ID.
 * Maps to HTTP 404.
 */
public class ContributionNotFoundException extends RuntimeException {

    public ContributionNotFoundException(UUID transactionId) {
        super("Contribution not found: " + transactionId);
    }
}
