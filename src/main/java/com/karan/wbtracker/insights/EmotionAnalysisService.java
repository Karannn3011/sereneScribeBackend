package com.karan.wbtracker.insights;

import java.util.List;

public interface EmotionAnalysisService {
    List<Emotion> analyzeEmotion(String text);
}