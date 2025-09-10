package com.maoz.Nice_HW.service;

import com.maoz.Nice_HW.config.TaskDictionary;

/***
 * Interface for suggest task services.
 */
public interface SuggestTaskInterface {
    public String suggestTask(String utterance);
    public void updateDictionary(TaskDictionary taskDictionary);
}
