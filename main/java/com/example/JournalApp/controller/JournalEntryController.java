package com.example.JournalApp.controller;

import com.example.JournalApp.JournalEntities.Users;
import com.example.JournalApp.JournalEntities.JournalEntry;
import com.example.JournalApp.service.JournalEntryService;
import com.example.JournalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {
    @Autowired
    private JournalEntryService JournalEntrySer;
    @Autowired
    private UserService userSer;

    @PostMapping
    public String createJournalEntry(@RequestBody JournalEntry myEntry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        JournalEntrySer.saveEntry(myEntry, userName);
        return "successfully saved";
    }

    @GetMapping
    public List<JournalEntry> getAllJournalEntriesByUserName(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        Users user = userSer.findByUsername(userName);
        return  user.getJournalEntriesList();
    }

    @GetMapping("/id/{myId}")
    public JournalEntry getById(@PathVariable ObjectId myId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
       Users user =  userSer.findByUsername(userName);
        List<JournalEntry> collect = user.getJournalEntriesList().stream()
                .filter(x -> x.getId().equals(myId)) // Use == instead of equals()
                .collect(Collectors.toList());
        if (!collect.isEmpty()) {
            return JournalEntrySer.findById(myId);
        }
        return null;
    }

    @PutMapping("/id/{myId}")
    public JournalEntry updateJournalEntry(
            @PathVariable ObjectId  myId,
            @RequestBody JournalEntry newEntry)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Users user =  userSer.findByUsername(userName);
        List<JournalEntry> collect = user.getJournalEntriesList().stream()
                .filter(x -> x.getId().equals(myId)) // Use == instead of equals()
                .collect(Collectors.toList());
        if (!collect.isEmpty()) {
            JournalEntry oldEntry = JournalEntrySer.findById(myId);
            if (oldEntry != null) {
                // Update fields only if the new values are valid
                oldEntry.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().isEmpty() ? newEntry.getTitle() : oldEntry.getTitle());
                oldEntry.setContent(newEntry.getContent() != null && !newEntry.getContent().isEmpty() ? newEntry.getContent() : oldEntry.getContent());

                // Update timestamp only if modifying content
                if (newEntry.getContent() != null && !newEntry.getContent().isEmpty()) {
                    oldEntry.setCreatedAt(LocalDateTime.now());
                }

                // Save the updated journal entry
                JournalEntrySer.saveEntry(oldEntry);
            }
            return JournalEntrySer.findById(myId);
        }
        return null;
    }

    @DeleteMapping("id/{myId}")
    public String deleteJournalData(@PathVariable ObjectId myId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        boolean removed =  JournalEntrySer.deleteData(myId,userName);
        if(removed)
        return "successfully deleted";
        else
            return "user not found";
    }
}
