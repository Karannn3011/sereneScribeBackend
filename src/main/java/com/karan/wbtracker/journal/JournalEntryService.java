package com.karan.wbtracker.journal;

import com.karan.wbtracker.insights.Emotion;
import com.karan.wbtracker.insights.EmotionAnalysisService;
import com.karan.wbtracker.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private EmotionAnalysisService emotionAnalysisService;

    public Page<JournalEntry> getEntriesForUser(String userId, Pageable pageable) {
        return journalEntryRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    public JournalEntry createEntry(JournalEntry newEntry, User user) {
        newEntry.setUserId(user.getId());
        newEntry.setDominantEmotion("pending..."); // <-- Set a default status

        // 2. Save the entry immediately to the database
        JournalEntry savedEntry = journalEntryRepository.save(newEntry);

        // 3. Call the asynchronous method to process emotions in the background
        // The code will not wait for this method to finish
        analyzeAndSaveEmotions(savedEntry);

        // 4. Return the instantly saved entry to the user
        return savedEntry;
    }

    public void deleteEntry(String entryId, String userId) {
        JournalEntry entry = journalEntryRepository.findById(entryId)
                .orElseThrow(() -> new RuntimeException("Entry not found"));

        // Security check: Ensure the user owns the entry they are trying to delete
        if (!Objects.equals(entry.getUserId(), userId)) {
            throw new AccessDeniedException("User does not have permission to delete this entry");
        }

        journalEntryRepository.delete(entry);
    }

    @Async // <-- This makes the method run in a separate background thread
    public void analyzeAndSaveEmotions(JournalEntry entry) {
        System.out.println("Starting emotion analysis for entry ID: " + entry.getId());

        // 1. Call the emotion analysis service
        List<Emotion> emotions = emotionAnalysisService.analyzeEmotion(entry.getText());

        // 2. Set the results on the entry object
        entry.setEmotions(emotions);
        if (emotions != null && !emotions.isEmpty()) {
            Emotion dominantEmotion = Collections.max(emotions, Comparator.comparing(Emotion::score));
            entry.setDominantEmotion(dominantEmotion.label());
        } else {
            entry.setDominantEmotion("analysis_failed"); // Or handle as you see fit
        }

        // 3. Save the updated entry back to the database
        journalEntryRepository.save(entry);
        System.out.println("Finished emotion analysis for entry ID: " + entry.getId());
    }
}