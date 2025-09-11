package com.maoz.Nice_HW.suggestTaskUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * TaskDictionary is a shared component that loads and provides access to the mapping of tasks to list of synonyms.
 * The dictionary is loaded from an external JSON file (resources/tasksDictionary.json).
 */
@Component
public class TaskDictionary {
    private static final Logger logger = LoggerFactory.getLogger(TaskDictionary.class);
    // Map of TaskName to List of Synonyms
    private Map<String, List<String>> dictionary = new HashMap<>();
    // Used for parsing JSON into a Java Map
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Constructor: automatically loads the dictionary from tasksDictionary.json when the component is created
     */
    public TaskDictionary() {
        loadDictionary();
    }

    /**
     * Loads the dictionary from resources/tasksDictionary.json.
     * If the file is not found or parsing fails, an error is logged.
     */
    public void loadDictionary() {
        try (InputStream input = getClass().getResourceAsStream("/tasksDictionary.json")) {
            if (input == null) {
                throw new IllegalStateException("tasksDictionary.json not found in resources");
            }

            Map<String, List<String>> newDict =
                    objectMapper.readValue(input, new TypeReference<Map<String, List<String>>>() {});
            Map<String, List<String>> mutableDict = new HashMap<>();
            newDict.forEach((task, synonyms) -> mutableDict.put(task, new ArrayList<>(synonyms)));

            this.dictionary = mutableDict;
            logger.info("Loaded {} tasks from dictionary", dictionary.size());
        } catch (IOException e) {
            logger.error("Failed to load dictionary", e);
        }
    }

    public Map<String, List<String>> getDictionary() {
        return dictionary;
    }

    /**
     * Adds a synonym to an existing task in the dictionary (but does not duplicate).
     * If the task does not exist, the update will be cancelled.
     *
     * @param synonym the synonym to add
     * @param task the task name (dictionary key)
     */
    public void updateDictionary(String synonym, String task) {
        if (synonym == null || synonym.isBlank() || task == null || task.isBlank()) {
            logger.warn("Invalid mapping: synonym='{}', task='{}' – ignoring", synonym, task);
            return;
        }

        // If the task does not exist, don't update
        if (!dictionary.containsKey(task)) {
            logger.warn("Task '{}' does not exist in dictionary. Synonym '{}' not added.", task, synonym);
            return;
        }

        List<String> synonyms = dictionary.get(task);
        if (!synonyms.contains(synonym)) {
            synonyms.add(synonym);
            logger.info("Added synonym '{}' to task '{}'", synonym, task);
        } else {
            logger.info("Synonym '{}' already exists for task '{}'", synonym, task);
        }
    }

}
