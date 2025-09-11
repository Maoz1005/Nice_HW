package com.maoz.Nice_HW;

import com.maoz.Nice_HW.config.*;
import com.maoz.Nice_HW.dto.SuggestTaskRequestDTO;
import com.maoz.Nice_HW.dto.SuggestTaskResponseDTO;
import com.maoz.Nice_HW.service.SuggestTaskDictionaryService;
import com.maoz.Nice_HW.service.SuggestTaskClassifierService;

import com.maoz.Nice_HW.tasks.*;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

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
    private final SuggestTaskDictionaryService suggestTaskService;
    private final SuggestTaskClassifierService classifierService;
    private final DevUsers devUsers;
    private final Map<String, AbstractTask> taskNameToTaskClass= new HashMap<>();

    public SuggestTaskController(
            SuggestTaskDictionaryService suggestTaskService,
            SuggestTaskClassifierService classifierService,
            DevUsers devUsers
    ) {
        this.suggestTaskService = suggestTaskService;
        this.classifierService = classifierService;
        this.devUsers = devUsers;
        this.taskNameToTaskClass.put("ResetPasswordTask", new ResetPasswordTask());
        this.taskNameToTaskClass.put("CheckOrderStatusTask", new CheckOrderStatusTask());
        this.taskNameToTaskClass.put("MakeOrderTask", new MakeOrderTask());
        this.taskNameToTaskClass.put("CancelOrderTask", new CancelOrderTask());
    }

    /**
     * Suggests a task based on the user's utterance.
     * Supports two modes: "suggest" (dictionary) or "classifier".
     *
     * @param request The SuggestTaskRequestDTO containing userId, sessionId, utterance, timestamp
     * @param mode Optional parameter (default is "suggest")
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
        if (devUsers.getUsers().contains(request.userId()) && lowerUtterance.contains("update dictionary")
                && mode.equalsIgnoreCase("suggest")) {
            String[] symAndTask = getSymAndTask(request.utterance());
            suggestTaskService.updateDictionary(symAndTask);
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

        if (!task.equals(Constants.NO_TASK_FOUND)){
            AbstractTask taskObject = taskNameToTaskClass.get(task);
            taskObject.activateTask(request);
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
     * Parses an update-dictionary request string into a synonym-task pair.
     *
     * Expected format: "update dictionary - synonym,Task"
     *
     * @param s the raw request string to parse
     * @return a 2-element array containing {synonym, task}, or {null, null} if invalid
     */
    private String[] getSymAndTask(String s) {
        if (s == null || s.isBlank()) {
            logger.info("Invalid update dictionary request: input is null/blank");
            return new String[2]; // {null, null}
        }

        String[] split = s.split("-", 2);
        if (split.length < 2) {
            logger.info("Invalid update dictionary request: missing '-' separator");
            return new String[2];
        }

        String[] symAndTask = split[1].split(",", 2);
        if (symAndTask.length < 2 || symAndTask[0].isBlank() || symAndTask[1].isBlank()) {
            logger.info("Invalid update dictionary request: missing/empty synonym or task");
            return new String[2];
        }

        return new String[]{symAndTask[0].trim(), symAndTask[1].trim()};
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

