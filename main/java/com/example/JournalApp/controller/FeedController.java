package com.example.JournalApp.controller;

import com.example.JournalApp.JournalEntities.JournalEntry;
import com.example.JournalApp.JournalEntities.JournalResponseDTO;
import com.example.JournalApp.JournalEntities.Users;
import com.example.JournalApp.repository.userRepo;
import com.example.JournalApp.service.JournalEntryService;
import com.example.JournalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class FeedController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @Autowired
    private userRepo userRepo;


    /**
     * ✅ 1. Fetch public journals (paginated)
     * Example: /api/journals/public?page=0&size=20
     */
    @GetMapping("/journals/public")
    public Map<String, Object> getPublicJournals(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "1") int sortOrder) {

        Sort sort = (sortOrder == 1) ?
                Sort.by(Sort.Direction.DESC, "createdAt") :
                Sort.by(Sort.Direction.ASC, "createdAt");

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<JournalEntry> paginated = journalEntryService.findAllPublicEntries(pageable);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");

        List<JournalResponseDTO> dtoList = paginated.getContent().stream()
                // Only include journals that are public
                .filter(entry -> !entry.isPrivate())
                .map(entry -> {
                    String username = "Unknown";
                    String avatarUrl = "/Journal/images/avatar.jpg"; // default

                    if (entry.getUserId() != null) {
                        Users user = userRepo.findById(entry.getUserId()).orElse(null);
                        if (user != null) {
                            username = user.getUsername();
                            if (user.getAvatarUrl() != null && !user.getAvatarUrl().isEmpty()) {
                                avatarUrl = user.getAvatarUrl();
                            }
                        }
                    }
                    String formattedDate = entry.getCreatedAt().format(formatter);

                    return new JournalResponseDTO(
                            entry.getId().toString(),
                            entry.getTitle(),
                            entry.getContent(),
                            formattedDate,
                            entry.isPrivate(),
                            username,
                            avatarUrl
                    );
                })
                .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("content", dtoList);
        response.put("totalPages", paginated.getTotalPages());

        return response;
    }



    /**
     * ✅ 2. Fetch public journals of a specific user (paginated)
     * Example: /api/journals/user/khushi?page=0&size=10
     */
    @GetMapping("/journals/public/by-user")
    public Map<String, Object> getPublicJournalsByUser(@RequestParam String username,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size,
                                                       @RequestParam(defaultValue ="1") int sortOrder) {
        // 1️⃣ Get user by username
        Users user = userService.findByUsername(username);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        // 2️⃣ Get avatar (default if null/empty)
        String avatarUrl = (user.getAvatarUrl() != null && !user.getAvatarUrl().isEmpty())
                ? user.getAvatarUrl()
                : "/Journal/images/avatar.jpg"; // default avatar

// 3️⃣ Filter only public journals from the user
        List<JournalEntry> publicJournals = user.getJournalEntriesList().stream()
                .filter(j -> !j.isPrivate()) // only public
                .sorted((j1, j2) -> {
                    if (sortOrder == 1) { // Newest first
                        return j2.getCreatedAt().compareTo(j1.getCreatedAt());
                    } else { // Oldest first
                        return j1.getCreatedAt().compareTo(j2.getCreatedAt());
                    }
                })
                .toList();

        // 4️⃣ Pagination logic
        int start = page * size;
        int end = Math.min(start + size, publicJournals.size());
        List<JournalEntry> paginatedList = (start < end) ? publicJournals.subList(start, end) : List.of();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");

        // 5️⃣ Map to DTO
        List<JournalResponseDTO> dtoList = paginatedList.stream()
                .map(entry -> new JournalResponseDTO(
                        entry.getId().toString(),
                        entry.getTitle(),
                        entry.getContent(),
                        entry.getCreatedAt().format(formatter),
                        entry.isPrivate(),
                        user.getUsername(),
                        avatarUrl
                ))
                .toList();

        // 6️⃣ Prepare response
        Map<String, Object> response = new HashMap<>();
        response.put("content", dtoList);
        response.put("totalPages", (int) Math.ceil((double) publicJournals.size() / size));

        return response;
    }



    // get journal  by  id
    @GetMapping("/journals/id/{id}")
    public ResponseEntity<JournalResponseDTO> getJournalById(@PathVariable ObjectId id) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");

        JournalEntry entry = journalEntryService.findById(id);
                if(entry == null) new ResponseStatusException(HttpStatus.NOT_FOUND, "Journal not found");

        String username = "Unknown";
        String avatarUrl = "/Journal/images/avatar.jpg"; // default

        if (entry.getUserId() != null) {
            Users user = userRepo.findById(entry.getUserId()).orElse(null);
            if (user != null) {
                username = user.getUsername();
                if (user.getAvatarUrl() != null && !user.getAvatarUrl().isEmpty()) {
                    avatarUrl = user.getAvatarUrl();
                }
            }
        }

        String formattedDate = entry.getCreatedAt().format(formatter);

        JournalResponseDTO dto = new JournalResponseDTO(
                entry.getId().toString(),
                entry.getTitle(),
                entry.getContent(),
                formattedDate,
                entry.isPrivate(),
                username,
                avatarUrl
        );
        return ResponseEntity.ok(dto);
    }



    /**
     * ✅ 4. Get public profile details of a user
     * Example: /api/user/khushi
     */
    @GetMapping("/user/{username}")
    public Users getPublicProfile(@PathVariable String username) {
        Users user = userService.findByUsername(username);
        if (user != null) {
            // Hide sensitive info like password
            user.setPassword(null);
            user.setJournalEntriesList(null); // We only show basic info
        }
        return user;
    }
}
