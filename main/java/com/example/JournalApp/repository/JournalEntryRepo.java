package com.example.JournalApp.repository;

import com.example.JournalApp.JournalEntities.JournalEntry;
import com.example.JournalApp.JournalEntities.Users;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface JournalEntryRepo extends MongoRepository<JournalEntry, ObjectId> {
    // Only public journals, with pagination & sorting
    Page<JournalEntry> findByIsPrivateFalse(Pageable pageable);

    // ✅ New: Only public journals of a specific user
//    Page<JournalEntry> findByUserAndIsPrivateFalse(Users user, Pageable pageable);

    /**
     * Search journal entries by title using regex, case-insensitive
     */
    @Query("{ 'title': { $regex: ?0, $options: 'i' }, 'isPrivate': false }")
    List<JournalEntry> findPublicByTitleRegex(String regex);

}
