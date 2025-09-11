package com.maoz.Nice_HW.config;

import com.maoz.Nice_HW.dto.SuggestTaskRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a concrete task for resetting a password.
 */
public class ResetPasswordTask extends AbstractTask {

    private static final Logger logger = LoggerFactory.getLogger(ResetPasswordTask.class);

    /**
     * Constructor initializes the task with its name.
     */
    public ResetPasswordTask() {
        this.taskName = "ResetPasswordTask";
    }

    /**
     * Activates the task.
     * In this example, it simply logs that the task was activated.
     */
    @Override
    public void activateTask(SuggestTaskRequestDTO request) {
        logger.info("ResetPasswordTask activated");
    }
}
