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
        taskDictionary.put("reset my password", "ResetPasswordTask");
        taskDictionary.put("forgot password", "ResetPasswordTask");
        taskDictionary.put("forgot my password", "ResetPasswordTask");
        taskDictionary.put("check order", "CheckOrderStatusTask");
        taskDictionary.put("check my order", "CheckOrderStatusTask");
        taskDictionary.put("check my orders status", "CheckOrderStatusTask");
        taskDictionary.put("track order", "CheckOrderStatusTask");
        taskDictionary.put("track my order", "CheckOrderStatusTask");
        taskDictionary.put("track orders", "CheckOrderStatusTask");
        taskDictionary.put("order status", "CheckOrderStatusTask");
        taskDictionary.put("cancel order", "CancelOrderTask");
        taskDictionary.put("cancel my order", "CancelOrderTask");
        taskDictionary.put("want to order", "MakeOrderTask");
        taskDictionary.put("want to place an order", "MakeOrderTask");
    }

    public void updateTaskDictionary(String key, String task){
        if (key == null || key.isBlank() || task == null || task.isBlank()) {
            logger.warn("Invalid mapping: key='{}', task='{}' – ignoring", key, task);
            return;
        }
        String lowerKey = key.toLowerCase();
        taskDictionary.put(lowerKey, task);
        logger.info("Mapping added/updated: '{}' -> '{}'", key, task);
    }

    public String suggestTask(String utterance) {
        if (utterance == null) {
            logger.info("Received null utterance, returning NoTaskFound");
            return "NoTaskFound";
        }

        // Change to lower case (case-sensitive)
        String lowerUtterance = utterance.toLowerCase();

        // Compare against all keys in taskDictionary
        for (Map.Entry<String, String> entry : taskDictionary.entrySet()) {
            if (lowerUtterance.contains(entry.getKey())) {
                // There is a match
                logger.info("Matched utterance '{}' to task '{}'", utterance, entry.getValue());
                return entry.getValue();
            }
        }
        // There is no match
        logger.info("No task found for utterance '{}'", utterance);
        return "NoTaskFound";
    }
}
