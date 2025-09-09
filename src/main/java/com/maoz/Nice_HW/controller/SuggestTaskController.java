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

        String task = suggestTaskService.suggestTask(request.utterance());
        SuggestTaskResponse response = new SuggestTaskResponse(task, Instant.now().toString());

        logger.info("Responding with task={}, timestamp={}", response.task(), response.timestamp());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/")
    public String healthCheck() {
        return "SuggestTask API is running!";
    }
}
