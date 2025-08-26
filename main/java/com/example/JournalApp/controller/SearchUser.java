package com.example.JournalApp.controller;

import com.example.JournalApp.JournalEntities.Users;
import com.example.JournalApp.service.JournalEntryService;
import com.example.JournalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class SearchUser {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    /* suggestion all  user name  while  any other  user is typing user  name to search user journal */
    @GetMapping("/search-usernames")
    public List<Map<String, String>> searchUsernames(@RequestParam String query) {
        if (query == null || query.trim().isEmpty()) {
            return List.of();
        }

        String regex = ".*" + query.trim() + ".*";
        String defaultAvatar = "/Journal/images/avatar.jpg"; // Path to default avatar in static folder

        return userService.findUserName(regex)
                .stream()
                .map(user -> {
                    Map<String, String> userMap = new HashMap<>();
                    userMap.put("username", user.getUsername());
                    userMap.put("avatarUrl",
                            (user.getAvatarUrl() == null || user.getAvatarUrl().isBlank())
                                    ? defaultAvatar
                                    : user.getAvatarUrl());
                    return userMap;
                })
                .toList();
    }


    /**
     * ✅ Smart search journals by title (case-insensitive, regex)
     * Example: /Journal/user/search-title?query=vacation
     */
    @GetMapping("/search-title")
    public List<Map<String, String>> searchJournalsByTitle(@RequestParam String query) {
        if (query == null || query.trim().isEmpty()) {
            return List.of();
        }
        String regex = ".*" + query.trim() + ".*";
        return journalEntryService.findPublicByTitleRegex(regex)
                .stream()
                .map(journal -> Map.of(
                        "id", journal.getId().toString(),
                        "title", journal.getTitle()
                ))
                .collect(Collectors.toList());
    }


}
