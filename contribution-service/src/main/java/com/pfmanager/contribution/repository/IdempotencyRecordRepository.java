package com.pfmanager.contribution.repository;

import com.pfmanager.contribution.entity.IdempotencyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IdempotencyRecordRepository extends JpaRepository<IdempotencyRecord, String> {

    /**
     * Finds an existing idempotency record by its key.
     * Used to detect replayed requests and key reuse with a different payload.
     */
    Optional<IdempotencyRecord> findByIdempotencyKey(String idempotencyKey);
}
