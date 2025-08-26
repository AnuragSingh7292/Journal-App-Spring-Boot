package com.example.JournalApp.controller;

import com.example.JournalApp.JournalEntities.*;
import com.example.JournalApp.repository.userRepo;
import com.example.JournalApp.service.UserService;
//import com.example.JournalApp.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private userRepo userRepo;

    /**
     * ✅ Get logged-in user details
     * Example: /user/me
     */
    @GetMapping("/me")
    public UserResponseDTO getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Users user = userService.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        // Consistent avatar URL handling
        String avatarUrl = user.getAvatarUrl();
        if (avatarUrl == null || avatarUrl.isEmpty()) {
            avatarUrl = "/Journal/images/avatar.jpg";
        } else if (!avatarUrl.startsWith("/")) {
            avatarUrl = "/" + avatarUrl;
        }

        return new UserResponseDTO(user.getUsername(), user.getEmail(), avatarUrl);
    }


    @GetMapping("/journals/me")
    public JournalCountDTO getAllJournalsForLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            throw new RuntimeException("User not authenticated");
        }

        String username = authentication.getName();
        Users user = userService.findByUsername(username);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");

        // journals
        List<LoggedUserJournalDTO> journalDTOs = user.getJournalEntriesList()
                .stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .map(entry -> new LoggedUserJournalDTO(
                        entry.getId().toString(),
                        entry.getTitle(),
                        entry.getContent(),
                        entry.isPrivate(),
                        entry.getCreatedAt().format(formatter)
                ))
                .collect(Collectors.toList());

        // counts
        long total = journalDTOs.size();
        long publicCount = journalDTOs.stream().filter(j -> !j.isPrivate()).count();
        long privateCount = journalDTOs.stream().filter(LoggedUserJournalDTO::isPrivate).count();

        JournalCountDTO.Counts counts = new JournalCountDTO.Counts(total, publicCount, privateCount);

        return new JournalCountDTO(journalDTOs, counts);
    }


    @PutMapping
    public String updateUser(@RequestBody Users userData)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       String name = authentication.getName();
       Users userName =  userService.findByUsername(name);
       if(userName != null){
           userName.setUsername(userData.getUsername());
           userName.setPassword(userData.getPassword());
           userName.setAvatarUrl(userData.getAvatarUrl());
           userName.setEmail(userData.getEmail());
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
