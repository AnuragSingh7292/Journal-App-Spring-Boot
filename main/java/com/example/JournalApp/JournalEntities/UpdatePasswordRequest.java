package com.example.JournalApp.JournalEntities;

// ✅ DTO for update-password API
public class UpdatePasswordRequest {
    private String userIdentifier;
    private String newPassword;
    private String confirmPassword;

    public String getUserIdentifier() { return userIdentifier; }
    public void setUserIdentifier(String userIdentifier) { this.userIdentifier = userIdentifier; }

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
}