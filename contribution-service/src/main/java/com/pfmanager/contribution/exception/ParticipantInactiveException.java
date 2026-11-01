package com.pfmanager.contribution.exception;

/**
 * Thrown when a contribution is attempted for a participant with INACTIVE status.
 * Maps to HTTP 422.
 */
public class ParticipantInactiveException extends RuntimeException {

    public ParticipantInactiveException(String participantId) {
        super("Participant is inactive and cannot receive contributions: " + participantId);
    }
}
