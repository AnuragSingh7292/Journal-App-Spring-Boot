package com.example.JournalApp.JournalEntities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditJournalDTO {
    private String id;       // journal id
    private String title;
    private String content;
    @JsonProperty("isPrivate")
    private boolean isPrivate; // or privacy flag
}

