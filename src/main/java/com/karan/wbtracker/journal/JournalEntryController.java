package com.karan.wbtracker.journal;

import com.karan.wbtracker.user.User;
import com.karan.wbtracker.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/entries")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserRepository userRepository;

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @GetMapping
    public ResponseEntity<Page<JournalEntry>> getAllEntriesForUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        User user = getAuthenticatedUser();
        Pageable pageable = PageRequest.of(page, size);
        Page<JournalEntry> entries = journalEntryService.getEntriesForUser(user.getId(), pageable);
        return ResponseEntity.ok(entries);
    }

    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry newEntry) {
        User user = getAuthenticatedUser();
        JournalEntry createdEntry = journalEntryService.createEntry(newEntry, user);
        return new ResponseEntity<>(createdEntry, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEntry(@PathVariable String id) {
        User user = getAuthenticatedUser();
        journalEntryService.deleteEntry(id, user.getId());
        return ResponseEntity.ok().build();
    }
}