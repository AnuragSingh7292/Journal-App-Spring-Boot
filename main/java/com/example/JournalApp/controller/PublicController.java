package com.example.JournalApp.controller;

import com.example.JournalApp.JournalEntities.Users;
import com.example.JournalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {
    @Autowired
    private UserService userService;

    @PostMapping("/create-user")
    public String addUser(@RequestBody Users userData)
    {
        userService.saveNewUser(userData);
        return "User added successfully";
    }

    @GetMapping("/health-check")
    public String healthCheck() {
        return "OK";
    }
}
