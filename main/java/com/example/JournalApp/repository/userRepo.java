package com.example.JournalApp.repository;

import com.example.JournalApp.JournalEntities.Users;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface userRepo extends MongoRepository<Users, ObjectId>
{
    Users findByUsername(String username);

    void deleteByUsername(String name);
}
