package com.maoz.Nice_HW.tasks;

import com.maoz.Nice_HW.dto.SuggestTaskRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a concrete task for Checking a status of an order.
 */
public class CheckOrderStatusTask extends AbstractTask {
    private static final Logger logger = LoggerFactory.getLogger(CheckOrderStatusTask.class);
    /**
     * Constructor initializes the task with its name.
     */
    public CheckOrderStatusTask(){
        this.taskName = "CheckOrderStatusTask";
    }

    /**
     * Activates the task.
     * In this example, it simply logs that the task was activated.
     */
    @Override
    public void activateTask(SuggestTaskRequestDTO request) {
        logger.info("CheckOrderStatusTask activated");
    }
}
