package com.example.JournalApp.repository;

import com.example.JournalApp.JournalEntities.ConfigWeatherEntities;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface configWeatherRepo extends MongoRepository<ConfigWeatherEntities, ObjectId> {

}
