package com.maoz.Nice_HW.controller;

import com.maoz.Nice_HW.config.Constants;
import com.maoz.Nice_HW.forSuggestTaskDictionary.DevUsers;
import com.maoz.Nice_HW.sharedSuggestTask.TaskDictionary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SuggestTaskControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskDictionary taskDictionary;

    @Autowired
    private DevUsers devUsers;

    @BeforeEach
    void setUp() {
        devUsers.getUsers().add("22222"); // כדי לאפשר בדיקות dev
    }

    @Test
    void testSuggestTask_dictionaryMode() throws Exception {
        String json = """
            {
                "utterance": "please reset my password",
                "userId": "12345",
                "sessionId": "sess1",
                "timestamp": "2025-09-11T10:00:00Z"
            }
        """;

        mockMvc.perform(post("/suggestTask")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.task").exists())
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void testSuggestTask_devUpdateDictionary() throws Exception {
        String json = """
        {
            "utterance": "update dictionary - hello,ResetPasswordTask",
            "userId": "22222",
            "sessionId": "sess1",
            "timestamp": "2025-09-11T10:00:00Z"
        }
        """;

        mockMvc.perform(post("/suggestTask")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.task").value(Constants.NO_TASK_FOUND));
    }

    @Test
    void testHealthCheck() throws Exception {
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/suggestTask"))
                .andExpect(status().isOk())
                .andExpect(content().string("SuggestTask API is running!"));
    }
}
