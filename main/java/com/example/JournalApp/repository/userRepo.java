package com.example.JournalApp.repository;

import com.example.JournalApp.JournalEntities.Users;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface userRepo extends MongoRepository<Users, ObjectId> {

    Users findByUsername(String username);
    Users findByEmail(String email);
    void deleteByUsername(String name);

    // ✅ New method to find user by journal entry ID
    @Query("{ 'journalEntriesList._id': ?0 }")
    Users findByJournalEntriesListContains(ObjectId journalId);

    // Search by username containing query (case-insensitive)
    @Query(value = "{ 'username': { $regex: ?0, $options: 'i' } }", fields = "{ 'username': 1, '_id': 0 }")
    List<Users> findByUsernameRegex(String regex);
}
