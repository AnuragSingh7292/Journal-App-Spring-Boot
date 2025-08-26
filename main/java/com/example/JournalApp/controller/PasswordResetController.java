package com.example.JournalApp.controller;

import com.example.JournalApp.JournalEntities.OtpStore;
import com.example.JournalApp.JournalEntities.Users;
import com.example.JournalApp.repository.userRepo;
import com.example.JournalApp.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import java.util.Random;

@RestController
    @RequestMapping("/api/PasswordReset")
    public class PasswordResetController {

        @Autowired
        private userRepo userRepository;

        @Autowired
        private EmailService emailService;

        @Autowired
        private OtpStore otpStore; // In-memory OTP storage

        private final Random random = new Random();

        // ---------------- API 1: Send OTP ----------------
        @PostMapping("/send-otp")
        public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> request) {
            String userIdentifier = request.get("userIdentifier");


            if (userIdentifier == null || userIdentifier.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "Email or username is required"
                ));
            }

                // Check if input contains '@', then treat it as email, else username
            Users user;
            if (userIdentifier.contains("@") && userIdentifier.contains(".")) {
                user = userRepository.findByEmail(userIdentifier);
            } else {
                user = userRepository.findByUsername(userIdentifier);
            }

            if (user == null) {
                return ResponseEntity.status(404).body(Map.of(
                        "success", false,
                        "message", "User not found!"
                ));
            }


            // Generate 6-digit OTP
            String otp = String.format("%06d", random.nextInt(999999));
            long expiryDuration = 60 * 1000; // 1 minute in milliseconds
            otpStore.saveOtp(userIdentifier, otp, expiryDuration);

            // Send OTP to user email
            try {

                String to = user.getEmail();
                String subject = "🔐 Your OTP Code for JournalApp";

                String htmlBody = "<!DOCTYPE html>" +
                        "<html>" +
                        "<head>" +
                        "<style>" +
                        "body { font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px; }" +
                        ".container { background-color: #ffffff; padding: 20px; border-radius: 8px; " +
                        "box-shadow: 0 2px 5px rgba(0,0,0,0.1); max-width: 500px; margin: auto; }" +
                        ".otp { font-size: 24px; font-weight: bold; color: #2c3e50; }" +
                        ".footer { margin-top: 20px; font-size: 12px; color: #777; }" +
                        "</style>" +
                        "</head>" +
                        "<body>" +
                        "<div class='container'>" +
                        "   <h2 style='color:#2c3e50;'>JournalApp Security Verification</h2>" +
                        "   <p>Hello,</p>" +
                        "   <p>We received a request to verify your identity. Please use the OTP below:</p>" +
                        "   <p class='otp'>" + otp + "</p>" +
                        "   <p>This OTP will expire in <b>1 minute</b>. Do not share it with anyone for your security.</p>" +
                        "   <hr>" +
                        "   <p class='footer'>If you did not request this, please ignore this email.<br>" +
                        "   Need help? Contact <a href='mailto:journal.mindscribe@gmail.com'>journal.mindscribe@gmail.com</a></p>" +
                        "</div>" +
                        "</body>" +
                        "</html>";

                emailService.sendHtmlEmail(to,subject, htmlBody);

            } catch (Exception e) {
                return ResponseEntity.ok(Map.of(
                        "success", false,
                        "message", "Failed to send email: " + e.getMessage()
                ));
            }

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "OTP sent successfully!",
                    "expiry", 60
            ));
        }



    // ---------------- API 2: Verify OTP ----------------
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request) {
        String userIdentifier = request.get("userIdentifier");
        String otpInput = request.get("otp");

        if (userIdentifier == null || userIdentifier.isEmpty() || otpInput == null || otpInput.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "User identifier and OTP are required"
            ));
        }

        // Fetch stored OTP
        OtpStore.OtpEntry stored = otpStore.getOtpEntry(userIdentifier);
        if (stored == null) {
            return ResponseEntity.ok(Map.of(
                    "success", false,
                    "message", "No OTP found. Please request a new one."
            ));
        }

        String storedOtp = stored.getOtp();
        long expiryTime = stored.getExpiryTime();
        long currentTime = System.currentTimeMillis();

        if (currentTime > expiryTime) {
            otpStore.removeOtp(userIdentifier);
            return ResponseEntity.ok(Map.of(
                    "success", false,
                    "message", "OTP expired. Please request a new one."
            ));
        }


        if (!otpInput.equals(storedOtp)) {
            return ResponseEntity.ok(Map.of(
                    "success", false,
                    "message", "Invalid OTP. Please try again."
            ));
        }

        // OTP verified successfully, remove it   here  i not removing this to  keep  used in while  change the  pass then again verify  the user identifier
//        otpStore.removeOtp(userIdentifier);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "OTP verified successfully!"
        ));
    }

    }
