package com.maoz.Nice_HW.controller;

import com.maoz.Nice_HW.config.Constants;
import com.maoz.Nice_HW.config.DevUsers;
import com.maoz.Nice_HW.config.TaskDictionary;
import com.maoz.Nice_HW.dto.SuggestTaskRequestDTO;
import com.maoz.Nice_HW.dto.SuggestTaskResponseDTO;
import com.maoz.Nice_HW.service.SuggestTaskBaseService;
import com.maoz.Nice_HW.service.SuggestTaskClassifierService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;

/***
 * REST controller for the SuggestTask API.
 * Handle requests to suggest a task based on user input (utterance).
 *
 * Key features:
 * - Supports two modes of task suggestion: "suggest" (dictionary-based) and "classifier" (model-based).
 * - Allows updating the shared task dictionary for development purposes via a special userId.
 * - Returns structured JSON responses with task and timestamp.
 */
@RestController
@RequestMapping("/suggestTask")
public class SuggestTaskController {

    private static final Logger logger = LoggerFactory.getLogger(SuggestTaskController.class);

    private final SuggestTaskBaseService suggestTaskService;
    private final SuggestTaskClassifierService classifierService;
    private final DevUsers devUsers;
    private final TaskDictionary taskDictionary;

    public SuggestTaskController(
            SuggestTaskBaseService suggestTaskService,
            SuggestTaskClassifierService classifierService,
            DevUsers devUsers,
            TaskDictionary taskDictionary
    ) {
        this.suggestTaskService = suggestTaskService;
        this.classifierService = classifierService;
        this.devUsers = devUsers;
        this.taskDictionary = taskDictionary;
    }

    /**
     * Suggests a task based on the user's utterance.
     * Supports two modes: "suggest" (dictionary) or "classifier".
     *
     * @param request - The SuggestTaskRequestDTO containing userId, sessionId, utterance, timestamp
     * @param mode - Optional parameter (default is "suggest")
     * @return SuggestTaskResponseDTO with the task and timestamp
     */
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuggestTaskResponseDTO> suggestTask(
            @Valid @RequestBody SuggestTaskRequestDTO request,
            @RequestParam(defaultValue = "suggest") String mode
    ) {
        logger.info("Received request: userId={}, sessionId={}, utterance={}",
                request.userId(), request.sessionId(), request.utterance());

        String task;
        String lowerUtterance = request.utterance().toLowerCase();

        // Special handling for dev commands (update dictionary)
        if (devUsers.getUsers().contains(request.userId()) && lowerUtterance.contains("update dictionary")) {
            taskDictionary.reloadDictionary();
            classifierService.train();
            task = Constants.NO_TASK_FOUND;
        }
        // Normal suggestion
        else {
            if (mode.equalsIgnoreCase("classifier")) {
                task = classifierService.suggestTask(request.utterance());
            } else {
                task = suggestTaskService.suggestTask(request.utterance());
            }
        }

        String timestamp = ZonedDateTime.now(ZoneId.of("Asia/Jerusalem"))
                .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        SuggestTaskResponseDTO response = new SuggestTaskResponseDTO(task, timestamp);
        logger.info("Responding with task={}, timestamp={}", response.task(), response.timestamp());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    /**
     * Health check endpoint.
     *
     * @return A simple status message
     */
    @GetMapping
    public String healthCheck() {
        return "SuggestTask API is running!";
    }
}




/***
 * dict<String, TaskInterface> = {"remove": removeTask, "update":updateTask};
 *         taskClass = dict[task];
 *         response = taskClass.getResponse()
 */

