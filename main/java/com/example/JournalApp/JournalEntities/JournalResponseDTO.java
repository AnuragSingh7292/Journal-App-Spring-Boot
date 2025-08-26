package com.example.JournalApp.JournalEntities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class JournalResponseDTO {
        private String id;
        private String title;
        private String content;
        private String createdAt;

        private boolean isPrivate;
        private String username;
        private String avatarUrl;
    }
