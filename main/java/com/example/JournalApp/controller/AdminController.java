package com.example.JournalApp.controller;

import com.example.JournalApp.JournalEntities.Users;
import com.example.JournalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private  UserService userService;

    @GetMapping("/all-user")
    public ResponseEntity<?> getAllUsers() {
        List<Users> all = userService.findAll();
        if (!all.isEmpty()) {
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create-admin-user")
    public ResponseEntity<?> createAdminUser(@RequestBody Users user) {
        userService.saveAdminUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/create-admin")
    public ResponseEntity<?> createAdmin(@RequestBody Users user){
        userService.saveAdmin(user);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }
}
