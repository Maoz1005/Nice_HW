package com.maoz.Nice_HW.config;

import com.maoz.Nice_HW.dto.SuggestTaskRequestDTO;
import com.maoz.Nice_HW.tasks.CheckOrderStatusTask;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CheckOrderStatusTaskTest {

    @Test
    void getTaskName_returnsCorrectName() {
        CheckOrderStatusTask task = new CheckOrderStatusTask();
        assertEquals("CheckOrderStatusTask", task.getTaskName());
    }

    @Test
    void activateTask_runsWithoutException() {
        CheckOrderStatusTask task = new CheckOrderStatusTask();

        SuggestTaskRequestDTO dto = new SuggestTaskRequestDTO(
                "check my order",
                "user123",
                "session456",
                "2025-09-09T14:10:32+03:00"
        );

        assertDoesNotThrow(() -> task.activateTask(dto));
    }
}
