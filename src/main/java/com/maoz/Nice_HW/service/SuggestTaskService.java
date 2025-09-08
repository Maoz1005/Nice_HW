package com.maoz.Nice_HW.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SuggestTaskService {
    private static final Logger logger = LoggerFactory.getLogger(SuggestTaskService.class);
    private final Map<String, String> taskDictionary = new HashMap<>();

    public SuggestTaskService() {
        taskDictionary.put("reset password", "ResetPasswordTask");
        taskDictionary.put("forgot password", "ResetPasswordTask");
        taskDictionary.put("check order", "CheckOrderStatusTask");
        taskDictionary.put("track order", "CheckOrderStatusTask");
    }

    public void updateTaskDictionary(String key, String task){
        if (key == null || key.isBlank() || task == null || task.isBlank()) {
            logger.warn("Invalid mapping: key='{}', task='{}' – ignoring", key, task);
            return;
        }

        taskDictionary.put(key, task);
        logger.info("Mapping added/updated: '{}' -> '{}'", key, task);
    }

    public String suggestTask(String utterance) {
        if (utterance == null) {
            logger.info("Received null utterance, returning NoTaskFound");
            return "NoTaskFound";
        }

        // המרה ל-lowercase כדי שהבדיקה תהיה case-insensitive
        String lowerUtterance = utterance.toLowerCase();

        // בדיקה מול כל הערכים במילון
        for (Map.Entry<String, String> entry : taskDictionary.entrySet()) {
            if (lowerUtterance.contains(entry.getKey())) {
                // התאמה נמצאה → לוג בקונסול
                logger.info("Matched utterance '{}' to task '{}'", utterance, entry.getValue());
                return entry.getValue();
            }
        }

        // אם לא נמצאה התאמה → לוג בקונסול
        logger.info("No task found for utterance '{}'", utterance);
        return "NoTaskFound";
    }
}
