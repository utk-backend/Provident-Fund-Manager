package com.pfmanager.contribution.exception;

/**
 * Thrown when a contribution reference already exists for the same participant.
 * Maps to HTTP 409.
 */
public class DuplicateReferenceException extends RuntimeException {

    public DuplicateReferenceException(String participantId, String reference) {
        super("Reference '" + reference + "' already exists for participant: " + participantId);
    }
}
