package com.maoz.Nice_HW.dto;

import jakarta.validation.constraints.NotBlank;
import com.maoz.Nice_HW.validation.Iso8601;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import static com.maoz.Nice_HW.config.Constants.MAX_UTTERANCE_LENGTH;

public record SuggestTaskRequest(
        @NotBlank(message = "utterance must not be blank")
        String utterance,

        @NotBlank(message = "userId must not be blank")
        @Size(min = 1, max = 9, message = "userId length must be between 1 and 9 digits")
        @Pattern(regexp = "^[0-9]+$", message = "userId must contain only digits")
        String userId,

        @NotBlank(message = "sessionId must not be blank")
        String sessionId,

        @NotBlank(message = "timestamp must not be blank")
        @Iso8601
        String timestamp
) {}
