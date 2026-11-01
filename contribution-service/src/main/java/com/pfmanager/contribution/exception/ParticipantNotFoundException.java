package com.pfmanager.contribution.exception;

/**
 * Thrown when a participant ID is not found in the participants table.
 * Maps to HTTP 404.
 */
public class ParticipantNotFoundException extends RuntimeException {

    public ParticipantNotFoundException(String participantId) {
        super("Participant not found: " + participantId);
    }
}
