package com.maoz.Nice_HW.unit;

import com.maoz.Nice_HW.config.Constants;
import com.maoz.Nice_HW.service.SuggestTaskClassifierService;
import com.maoz.Nice_HW.suggestTaskUtils.TaskDictionary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SuggestTaskClassifierServiceTest {

    private TaskDictionary taskDictionary;
    private SuggestTaskClassifierService classifier;

    @BeforeEach
    void setUp() {
        taskDictionary = new TaskDictionary() {
            @Override
            public Map<String, List<String>> getDictionary() {
                return Map.of(
                        "ResetPasswordTask", List.of("reset password", "forgot password"),
                        "CheckOrderStatusTask", List.of("check order", "track order"),
                        "CancelOrderTask", List.of("cancel order"),
                        "MakeOrderTask", List.of("place an order")
                );
            }
        };

        classifier = new SuggestTaskClassifierService(taskDictionary);
        classifier.train();
    }

    @Test
    void suggestTask_knownUtterances_returnsCorrectTaskOrNoTask() {
        String[] utterances = {
                "please reset password",
                "I forgot my password",
                "track order now",
                "cancel order immediately",
                "I want to place an order"
        };

        String[] expectedTasks = {
                "ResetPasswordTask",
                "ResetPasswordTask",
                "CheckOrderStatusTask",
                "CancelOrderTask",
                "MakeOrderTask"
        };

        for (int i = 0; i < utterances.length; i++) {
            String task = classifier.suggestTask(utterances[i]);
            assertTrue(
                    task.equals(expectedTasks[i]) || task.equals(Constants.NO_TASK_FOUND),
                    "Utterance: '" + utterances[i] + "' returned unexpected task: " + task
            );
        }
    }


    @Test
    void suggestTask_unknownUtterance_returnsNoTaskFound() {
        assertEquals(Constants.NO_TASK_FOUND, classifier.suggestTask("do something else"));
    }

    @Test
    void suggestTask_emptyOrNull_returnsNoTaskFound() {
        assertEquals(Constants.NO_TASK_FOUND, classifier.suggestTask(""));
        assertEquals(Constants.NO_TASK_FOUND, classifier.suggestTask("   "));
        assertEquals(Constants.NO_TASK_FOUND, classifier.suggestTask(null));
    }

    @Test
    void train_doesNotThrowException() {
        assertDoesNotThrow(() -> classifier.train());
    }
}
