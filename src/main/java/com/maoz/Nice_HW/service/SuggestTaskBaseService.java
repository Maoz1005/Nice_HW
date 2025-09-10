package com.maoz.Nice_HW.service;

import com.maoz.Nice_HW.config.Constants;
import com.maoz.Nice_HW.config.TaskDictionary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

/**
 * Service implementation of SuggestTaskInterface that uses a dictionary-based approach.
 *
 * The dictionary is loaded from an external JSON file and contains mappings between a task
 * and its possible synonyms. When a new utterance arrives, this service attempts to match it
 * against the synonyms in the dictionary and return the corresponding task.
 */
@Service
public class SuggestTaskBaseService implements SuggestTaskInterface {

    private static final Logger logger = LoggerFactory.getLogger(SuggestTaskBaseService.class);
    private final TaskDictionary taskDictionary; // Central dictionary of task to synonyms, shared across services

    /**
     * Constructor with dependency injection of the shared task dictionary.
     *
     * @param taskDictionary a taskDictionary object
     */
    public SuggestTaskBaseService(TaskDictionary taskDictionary) {
        this.taskDictionary = taskDictionary;
    }

    /**
     * Suggest a task given an utterance by checking for matches in the dictionary.
     *
     * @param utterance The user input (free text)
     * @return The task name if a match is found, or "NoTaskFound" otherwise
     */
    @Override
    public String suggestTask(String utterance) {
        if (utterance == null || utterance.isBlank()) {
            return Constants.NO_TASK_FOUND;
        }

        String lowerUtterance = utterance.toLowerCase();

        // Check each task and its synonyms for a match
        for (Map.Entry<String, List<String>> entry : taskDictionary.getDictionary().entrySet()) {
            for (String synonym : entry.getValue()) {
                if (lowerUtterance.contains(synonym.toLowerCase())) {
                    logger.info("Matched '{}' to task '{}'", utterance, entry.getKey());
                    return entry.getKey();
                }
            }
        }
        logger.info("No match for '{}'", utterance);
        return Constants.NO_TASK_FOUND;
    }

    public void updateDictionary(String[] symAndTask){
        taskDictionary.updateDictionary(symAndTask[0], symAndTask[1]);
    }
}
