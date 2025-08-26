package com.example.JournalApp.JournalEntities;

import lombok.Data;

@Data
public class ChangePasswordRequestDTO {
    private String oldPassword;
    private String newPassword;
}
