package com.example.JournalApp.JournalEntities;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "APIS")  // Ensure this matches exactly!
@Data
@NoArgsConstructor
public class ConfigWeatherEntities {
    @Id
    private ObjectId id;
    private String key;
    private String value;
}
