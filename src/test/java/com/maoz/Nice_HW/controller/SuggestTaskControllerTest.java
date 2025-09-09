package com.maoz.Nice_HW.controller;

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
class SuggestTaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testSuggestTask_success() throws Exception {
        String json = """
            {
                "utterance": "please reset my password",
                "userId": "12345",
                "sessionId": "abcde-67890",
                "timestamp": "2025-09-09T14:10:32+03:00"
            }
        """;

        mockMvc.perform(post("/suggestTask")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.task").value("ResetPasswordTask"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void testSuggestTask_missingField() throws Exception {
        String json = """
            {
                "utterance": "",
                "userId": "12345",
                "sessionId": "abcde-67890"
            }
        """;

        mockMvc.perform(post("/suggestTask")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.utterance").exists());
    }

    @Test
    void testSuggestTask_extraFieldsIgnored() throws Exception {
        String json = """
            {
                "utterance": "check my order",
                "userId": "12345",
                "sessionId": "abcde-67890",
                "timestamp": "2025-09-09T14:10:32+03:00",
                "extraField": "should be ignored"
            }
        """;

        mockMvc.perform(post("/suggestTask")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testHealthCheck() throws Exception {
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/suggestTask"))
                .andExpect(status().isOk())
                .andExpect(content().string("SuggestTask API is running!"));
    }
}
