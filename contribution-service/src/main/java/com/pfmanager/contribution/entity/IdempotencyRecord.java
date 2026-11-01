package com.pfmanager.contribution.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

/**
 * Persisted record of an idempotency key used in a POST operation.
 * <p>
 * Stores the key, a SHA-256 hash of the canonical request body, and the
 * resulting transaction ID. A key reused with a different request body
 * (different hash) must be rejected with 409.
 * <p>
 * Uniqueness is enforced at the database level to handle concurrent calls.
 */
@Entity
@Table(name = "idempotency_records")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdempotencyRecord {

    /**
     * Client-supplied idempotency key (from the Idempotency-Key request header).
     * Acts as the primary key — database uniqueness prevents concurrent duplicates.
     */
    @Id
    @Column(name = "idempotency_key", nullable = false)
    private String idempotencyKey;

    /**
     * SHA-256 hash of the canonical (sorted, normalized) request body.
     * Used to detect key reuse with a different payload.
     */
    @Column(name = "request_hash", nullable = false, length = 64)
    private String requestHash;

    /** The transaction ID that was created for this idempotency key. */
    @Column(name = "transaction_id", nullable = false)
    private UUID transactionId;

    /** UTC timestamp when this record was first created. */
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
