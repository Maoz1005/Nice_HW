package com.maoz.Nice_HW.controller;

import com.maoz.Nice_HW.dto.SuggestTaskRequest;
import com.maoz.Nice_HW.service.SuggestTaskService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/suggestTask")
public class SuggestTaskController {
    private static final Logger logger = LoggerFactory.getLogger(SuggestTaskController.class);

    private final SuggestTaskService suggestTaskService;

    public SuggestTaskController(SuggestTaskService suggestTaskService) {
        this.suggestTaskService = suggestTaskService;
    }

    @PostMapping
    public ResponseEntity<String> suggestTask(@Valid @RequestBody SuggestTaskRequest request) {
        logger.info("Received request: {}", request);

        String task = suggestTaskService.suggestTask(request.utterance());

        return ResponseEntity.ok(task);
    }

    @GetMapping("/")
    public String healthCheck() {
        return "SuggestTask API is running!";
    }
}
