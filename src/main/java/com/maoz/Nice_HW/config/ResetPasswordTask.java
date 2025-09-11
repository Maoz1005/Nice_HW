package com.maoz.Nice_HW.config;

import com.maoz.Nice_HW.controller.SuggestTaskController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResetPasswordTask extends AbstractTask{
    private static final Logger logger = LoggerFactory.getLogger(ResetPasswordTask.class);
    public ResetPasswordTask(){
        this.taskName = "ResetPasswordTask";
    }
    @Override
    public void activateTask() {
        logger.info("ResetPasswordTask activated");
    }
}
