package com.maoz.Nice_HW.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * DevUsers is a shared Component responsible for loading and providing a list of
 * "developer users" (users allowed to update the task dictionary).
 *
 * The users are loaded from an external JSON file (resources/devUsers.json),
 * so that it can be easily updated without changing the code.
 */
@Component
public class DevUsers {
    private static final Logger logger = LoggerFactory.getLogger(DevUsers.class);
    private List<String> users = new ArrayList<>(); // List of authorized user IDs

    /**
     * Constructor automatically loads the list of developer users
     * when the Spring context initializes this Component.
     */
    public DevUsers() {
        loadUsers();
    }

    /**
     * Loads the developer users from the JSON file.
     * If the file is missing or cannot be read, an empty list is used.
     */
    private void loadUsers() {
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream input = getClass().getResourceAsStream("/devUsers.json")) {
            if (input == null) {
                logger.warn("devUsers.json not found in resources, using empty list");
                return;
            }

            // Read the JSON as a map and extract the "users" list
            Map<String, List<String>> map = objectMapper.readValue(input, new TypeReference<>() {});
            this.users = map.getOrDefault("users", new ArrayList<>());
            logger.info("Loaded {} authorized users", users.size());
        } catch (IOException e) {
            logger.error("Failed to load authorized users", e);
        }
    }

    /**
     * Returns the list of developer user IDs.
     * This can be used by controllers or services to check authorization.
     *
     * @return List of authorized developer users
     */
    public List<String> getUsers() {
        return users;
    }

    /**
     * Reloads the developer users from the JSON file at runtime.
     * Can be called from a controller to refresh the list without restarting the application.
     */
    public void reloadUsers() {
        logger.info("Reloading developer users from devUsers.json...");
        loadUsers();
    }
}
