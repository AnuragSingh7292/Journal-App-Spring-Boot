package com.example.JournalApp.controller;

import com.example.JournalApp.JournalEntities.EditJournalDTO;
import com.example.JournalApp.JournalEntities.Users;
import com.example.JournalApp.JournalEntities.JournalEntry;
import com.example.JournalApp.service.JournalEntryService;
import com.example.JournalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    /* saving the journal of logged user */
    @PostMapping
    public String createJournalEntry(@RequestBody JournalEntry myEntry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return "User not authenticated";
        }
        String userName = authentication.getName();
        JournalEntrySer.saveEntry(myEntry, userName);
        return "Successfully saved";
    }


    /* geting the journal  by  id */
    @GetMapping("/id/{myId}")
    public ResponseEntity<EditJournalDTO> getById(@PathVariable ObjectId myId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Users user = userSer.findByUsername(userName);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        JournalEntry entry = JournalEntrySer.findById(myId);

        if (entry == null) {
            return ResponseEntity.notFound().build();
        }

        if (!entry.getUserId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // ✅ map entity to DTO
        EditJournalDTO dto = new EditJournalDTO(
                entry.getId().toHexString(),
                entry.getTitle(),
                entry.getContent(),
                entry.isPrivate()
        );
        return ResponseEntity.ok(dto);
    }


        /* updating the journal by id */
    @PutMapping("/id/{myId}")
    public ResponseEntity<String> updateJournalEntry(
            @PathVariable ObjectId myId,
            @RequestBody JournalEntry newEntry) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Users user = userSer.findByUsername(userName);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        boolean exists = user.getJournalEntriesList().stream()
                .anyMatch(x -> x.getId().equals(myId));

        if (!exists) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Journal not found");
        }

        JournalEntry oldEntry = JournalEntrySer.findById(myId);
        if (oldEntry != null) {
            // Update title
            if (newEntry.getTitle() != null && !newEntry.getTitle().isEmpty()) {
                oldEntry.setTitle(newEntry.getTitle());
            }

            // Update content
            if (newEntry.getContent() != null && !newEntry.getContent().isEmpty()) {
                oldEntry.setContent(newEntry.getContent());
            }

            // Update privacy
            oldEntry.setPrivate(newEntry.isPrivate());

            JournalEntrySer.saveEntry(oldEntry);

            return ResponseEntity.ok("Journal updated successfully ✅");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Journal not found");
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


    @GetMapping
    public List<JournalEntry> getAllJournalEntriesByUserName(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        Users user = userSer.findByUsername(userName);
        return  user.getJournalEntriesList();
    }

}
