package com.maoz.Nice_HW.unit;

import com.maoz.Nice_HW.dto.SuggestTaskRequestDTO;
import com.maoz.Nice_HW.tasks.CancelOrderTask;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CancelOrderTaskTest {

    @Test
    void getTaskName_returnsCorrectName() {
        CancelOrderTask task = new CancelOrderTask();
        assertEquals("CancelOrderTask", task.getTaskName());
    }

    @Test
    void activateTask_runsWithoutException() {
        CancelOrderTask task = new CancelOrderTask();

        SuggestTaskRequestDTO dto = new SuggestTaskRequestDTO(
                "cancel my order",
                "user123",
                "session456",
                "2025-09-09T14:10:32+03:00"
        );

        assertDoesNotThrow(() -> task.activateTask(dto));
    }
}
