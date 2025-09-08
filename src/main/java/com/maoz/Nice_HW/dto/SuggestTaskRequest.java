package com.maoz.Nice_HW.dto;

import jakarta.validation.constraints.NotBlank;
import com.maoz.Nice_HW.validation.Iso8601;
import jakarta.validation.constraints.Size;

import static com.maoz.Nice_HW.config.Constants.MAX_UTTERANCE_LENGTH;

public record SuggestTaskRequest(
        @NotBlank(message = "utterance must not be blank")
        @Size(max = MAX_UTTERANCE_LENGTH)
        String utterance,

        @NotBlank(message = "userId must not be blank")
        String userId,

        @NotBlank(message = "sessionId must not be blank")
        String sessionId,

        @NotBlank(message = "timestamp must not be blank")
        @Iso8601
        String timestamp
) {}
