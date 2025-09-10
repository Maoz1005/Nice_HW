package com.maoz.Nice_HW.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * TaskDictionary is a shared component that loads and provides access
 * to the mapping of tasks → list of synonyms.
 *
 * The dictionary is loaded from an external JSON file (resources/tasksDictionary.json),
 * so that it can be easily updated without changing the code.
 */
@Component
public class TaskDictionary {
    private static final Logger logger = LoggerFactory.getLogger(TaskDictionary.class);
    // Map of TaskName → List of Synonyms
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
            this.dictionary = newDict;
            logger.info("Loaded {} tasks from dictionary", dictionary.size());

        } catch (IOException e) {
            logger.error("Failed to load dictionary", e);
        }
    }

    /**
     * Returns the current dictionary.
     * @return dictionary map
     */
    public Map<String, List<String>> getDictionary() {
        return dictionary;
    }

    /**
     * Reloads the dictionary from the JSON file at runtime.
     * Can be called from a controller to refresh the list without restarting the application.
     */
    public void reloadDictionary() {
        logger.info("Reloading dictionary from tasksDictionary.json...");
        loadDictionary();
    }
}
