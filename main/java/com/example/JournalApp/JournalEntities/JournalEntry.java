package com.example.JournalApp.JournalEntities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "Journal_entries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JournalEntry {
    @Id
    private ObjectId id;  // Changed to ObjectId for MongoDB consistency

    private ObjectId userId; // only store the user's ObjectId


    @NonNull
    private String title; // Changed from "name" to "title" (more journal-related)

    @NonNull
    private String content; // Content of the journal entry

    @Field("isPrivate") // Optional, MongoDB name override
    @JsonProperty("isPrivate")
    private boolean isPrivate;


    private LocalDateTime createdAt = LocalDateTime.now(); // Default to current timestamp
}
