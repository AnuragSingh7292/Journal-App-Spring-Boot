package com.example.JournalApp.JournalEntities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data   // getter setter and  construct
@NoArgsConstructor
@AllArgsConstructor
public class LoggedUserJournalDTO {
    private String id;
    private String title;
    private String content;
    @JsonProperty("isPrivate")
    private boolean isPrivate;
    private String createdAt;
}
