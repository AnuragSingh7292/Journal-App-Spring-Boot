package com.example.JournalApp.JournalEntities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JournalCountDTO {

    private List<LoggedUserJournalDTO> journals;
    private Counts counts;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Counts {
        private long total;
        private long publicCount;
        private long privateCount;
    }
}

