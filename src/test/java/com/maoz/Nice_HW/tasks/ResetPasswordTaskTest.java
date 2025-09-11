package com.maoz.Nice_HW.tasks;

import com.maoz.Nice_HW.tasks.ResetPasswordTask;
import com.maoz.Nice_HW.dto.SuggestTaskRequestDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ResetPasswordTaskTest {

    @Test
    void getTaskName_returnsCorrectName() {
        ResetPasswordTask task = new ResetPasswordTask();
        assertEquals("ResetPasswordTask", task.getTaskName());
    }

    @Test
    void activateTask_runsWithoutException() {
        ResetPasswordTask task = new ResetPasswordTask();

        SuggestTaskRequestDTO dto = new SuggestTaskRequestDTO(
                "reset my password",
                "user123",
                "session456",
                "2025-09-09T14:10:32+03:00"
        );

        assertDoesNotThrow(() -> task.activateTask(dto));
    }
}
