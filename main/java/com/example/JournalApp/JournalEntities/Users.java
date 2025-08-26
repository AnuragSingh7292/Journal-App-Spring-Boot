    package com.example.JournalApp.JournalEntities;

    import lombok.*;
    import org.bson.types.ObjectId;
    import org.springframework.data.annotation.Id;
    import org.springframework.data.mongodb.core.index.Indexed;
    import org.springframework.data.mongodb.core.mapping.DBRef;
    import org.springframework.data.mongodb.core.mapping.Document;

    import java.time.LocalDateTime;
    import java.util.ArrayList;
    import java.util.List;

    @Document(collection = "JournalUser")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class Users {
        @Id
        private ObjectId id;

        @Indexed(unique = true)
        @NonNull
        private String username;

        @Indexed(unique = true, sparse = true)
        @NonNull
        private String email;

        @NonNull
        private String password;

        private String avatarUrl;

        private LocalDateTime joinDate = LocalDateTime.now(); // Default to current timestamp

        @DBRef
        private List<JournalEntry> journalEntriesList = new ArrayList<>(); // Changed field name for clarity

        private List<String> roles; // Roles like ["USER", "ADMIN"]
    }
