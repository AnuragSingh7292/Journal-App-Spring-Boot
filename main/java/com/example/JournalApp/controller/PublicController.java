package com.example.JournalApp.controller;

import com.example.JournalApp.JournalEntities.Users;
import com.example.JournalApp.service.UserService;
import com.mongodb.DuplicateKeyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/public")
public class PublicController {
    @Autowired
    private UserService userService;

    @PostMapping("/create-user")
    public ResponseEntity<?> addUser(@RequestBody Users userData) {
        try {
            userService.saveNewUser(userData);
            return ResponseEntity.ok().body(Map.of("message", "User added successfully"));
        } catch (org.springframework.dao.DuplicateKeyException ex) {
            String message = ex.getMessage();
            if (message.contains("username")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "username already exists"));
            } else if (message.contains("email")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "email already exists"));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Duplicate entry"));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Unexpected error occurred"));
        }
    }


    @GetMapping("/health-check")
    public String healthCheck() {
        return "OK";
    }
}
