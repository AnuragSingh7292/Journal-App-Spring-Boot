package com.example.JournalApp.controller;

import com.example.JournalApp.JournalEntities.Users;
import com.example.JournalApp.repository.userRepo;
import com.example.JournalApp.service.UserService;
//import com.example.JournalApp.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private userRepo userRepo;



    @PutMapping
    public String updateUser(@RequestBody Users userData)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       String name = authentication.getName();
       Users userName =  userService.findByUsername(name);
       if(userName != null){
           userName.setUsername(userData.getUsername());
           userName.setPassword(userData.getPassword());
           userService.saveNewUser(userName);
       }
       return "User updated successfully";
    }

    @DeleteMapping
    public String deleteUserById()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // get logged-in user's username
        userRepo.deleteByUsername(username);
        return "User deleted successfully";
    }
}
