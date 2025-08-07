package com.karan.wbtracker.journal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface JournalEntryRepository extends MongoRepository<JournalEntry, String> {

    // Custom method to find all entries for a specific user, sorted by date
    Page<JournalEntry> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);
}