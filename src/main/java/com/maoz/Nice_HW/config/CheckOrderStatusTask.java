package com.maoz.Nice_HW.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckOrderStatusTask extends AbstractTask{
    private static final Logger logger = LoggerFactory.getLogger(CheckOrderStatusTask.class);
    public CheckOrderStatusTask(){
        this.taskName = "CheckOrderStatusTask";
    }
    @Override
    public void activateTask() {
        logger.info("CheckOrderStatusTask activated");
    }
}
