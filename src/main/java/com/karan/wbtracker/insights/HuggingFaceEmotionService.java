package com.karan.wbtracker.insights;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
import java.util.List;

@Service
public class HuggingFaceEmotionService implements EmotionAnalysisService {

    @Value("${huggingface.api.url}")
    private String apiUrl;

    @Value("${huggingface.api.token}")
    private String apiToken;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public List<Emotion> analyzeEmotion(String text) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        String requestJson = "{\"inputs\": \"" + text.replace("\"", "\\\"") + "\"}";

        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        try {
            Emotion[][] response = restTemplate.postForObject(apiUrl, entity, Emotion[][].class);

            if (response != null && response.length > 0) {
                return List.of(response[0]);
            }
        } catch (Exception e) {
            // --- UPDATE THIS BLOCK ---
            System.err.println("Error calling Hugging Face API. Full error details:");
            e.printStackTrace(); // This will print the full error stack trace
        }

        return Collections.emptyList();
    }
}