package com.karan.wbtracker.journal;

import com.karan.wbtracker.insights.Emotion; // Assuming Emotion record is in insights package
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

// Lombok annotation to reduce boilerplate code for getters, setters, etc.
import lombok.Data;

@Data // Creates all the getters, setters, required-args-constructor, etc.
@Document(collection = "journal_entries") // Maps this class to a MongoDB collection
public class JournalEntry {

    @Id // Marks this field as the primary key
    private String id;

    private String userId; // Links the entry to a user
    private String text;
    private int mood;
    private int energy;
    private int stress;
    private LocalDateTime createdAt;
    private List<Emotion> emotions; // To store the AI analysis results
    private String dominantEmotion;

    public JournalEntry() {
        this.createdAt = LocalDateTime.now();
    }
}