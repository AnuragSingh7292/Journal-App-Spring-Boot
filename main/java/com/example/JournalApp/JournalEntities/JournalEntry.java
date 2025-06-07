package com.example.JournalApp.JournalEntities;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "Journal_entries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JournalEntry {
    @Id
    private ObjectId id;  // Changed to ObjectId for MongoDB consistency

    @NonNull
    private String title; // Changed from "name" to "title" (more journal-related)

    @NonNull
    private String content; // Content of the journal entry

    private LocalDateTime createdAt = LocalDateTime.now(); // Default to current timestamp
}
