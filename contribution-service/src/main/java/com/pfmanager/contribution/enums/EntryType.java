package com.pfmanager.contribution.enums;

/**
 * Represents the type of a contribution ledger entry.
 * <p>
 * CONTRIBUTION — increases the participant's balance.
 * REVERSAL     — decreases the participant's balance by the original contribution amount.
 */
public enum EntryType {
    CONTRIBUTION,
    REVERSAL
}
