package com.maoz.Nice_HW.controller;

import com.maoz.Nice_HW.dto.SuggestTaskRequest;
import com.maoz.Nice_HW.dto.SuggestTaskResponse;
import com.maoz.Nice_HW.service.SuggestTaskService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/suggestTask")
public class SuggestTaskController {
    private static final Logger logger = LoggerFactory.getLogger(SuggestTaskController.class);
    private final SuggestTaskService suggestTaskService;

    public SuggestTaskController(SuggestTaskService suggestTaskService) {
        this.suggestTaskService = suggestTaskService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuggestTaskResponse> suggestTask(@Valid @RequestBody SuggestTaskRequest request) {
        logger.info("Received request: userId={}, sessionId={}, utterance={}",
                request.userId(), request.sessionId(), request.utterance());
        String task;
        if (request.userId().equals("00000")){
            String[] texts = request.utterance().split("-");
            if (texts[0].equals("update")){
                String[] dictUpdate = texts[1].split(",");
                this.suggestTaskService.updateTaskDictionary(dictUpdate[0], dictUpdate[1]);
                logger.info("Updated the dictionary of the service");
            }
            task = "NoTaskFound";
        }

        else {
            task = suggestTaskService.suggestTask(request.utterance());
        }

        String timestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now());
        SuggestTaskResponse response = new SuggestTaskResponse(task, timestamp);

        logger.info("Responding with task={}, timestamp={}", response.task(), response.timestamp());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping
    public String healthCheck() {
        return "SuggestTask API is running!";
    }
}
