package com.example.JournalApp.controller;

import com.example.JournalApp.JournalEntities.ChangePasswordRequestDTO;
import com.example.JournalApp.JournalEntities.OtpStore;
import com.example.JournalApp.JournalEntities.UpdatePasswordRequest;
import com.example.JournalApp.JournalEntities.Users;
import com.example.JournalApp.repository.userRepo;
import org.apache.tomcat.util.log.SystemLogHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/profileController/api")
public class ProfileController {

    @Autowired
    private userRepo userRepository;
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private OtpStore otpStore; // In-memory OTP storage

    // ✅ 1. changing password of logged user (via change Password flow,  old password required)
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequestDTO request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Users user = userRepository.findByUsername(username);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        // Verify old password
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Old password is incorrect!");
        }

        // Save new password with BCrypt
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("Password changed successfully!");
    }



    // ✅ 2. Update password (via Forgot Password flow, no old password required)
    @PostMapping("/forget-password")
    public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordRequest request) {
        // here request contains: userIdentifier, newPassword, confirmPassword

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("{\"success\": false, \"message\": \"Passwords do not match!\"}");
        }
        // Fetch stored OTP
        OtpStore.OtpEntry stored = otpStore.getOtpEntry(request.getUserIdentifier());
        if (stored == null) {
            return ResponseEntity.ok(Map.of(
                    "success", false,
                    "message", "User Not Found. Please request a new one."
            ));
        }
        String storedUser= stored.getUserIdentification();
        System.out.println(storedUser + " "+request.getUserIdentifier());
        if(storedUser != null && storedUser.equals(request.getUserIdentifier()) ) {
            Users user;
            if (storedUser.contains("@") && storedUser.contains(".")) {
                user = userRepository.findByEmail(request.getUserIdentifier());
            } else {
                user = userRepository.findByUsername(request.getUserIdentifier());
            }

            if (user == null) {
                return ResponseEntity.badRequest().body("{\"success\": false, \"message\": \"User not found!\"}");
            }
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);
        }else {
                return ResponseEntity.ok(Map.of(
                        "success", false,
                        "message", "User Not Match. Please request a new one."
                ));
            }

        // OTP verified successfully, remove it
        otpStore.removeOtp(request.getUserIdentifier());
        return ResponseEntity.ok("{\"success\": true, \"message\": \"Password updated successfully!\"}");
    }


}
