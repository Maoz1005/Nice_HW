package com.maoz.Nice_HW.dto;

import jakarta.validation.constraints.NotBlank;
import com.maoz.Nice_HW.validation.Iso8601;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import static com.maoz.Nice_HW.config.Constants.MAX_UTTERANCE_LENGTH;

public record SuggestTaskRequest(
        @NotBlank(message = "{utterance.notBlank}")
        @Size(max = MAX_UTTERANCE_LENGTH, message = "{utterance.size}")
        String utterance,

        @NotBlank(message = "{userId.notBlank}")
        @Size(min = 1, max = 9, message = "{userId.size}")
        @Pattern(regexp = "^[0-9]+$", message = "{userId.pattern}")
        String userId,

        @NotBlank(message = "{sessionId.notBlank}")
        String sessionId,

        @NotBlank(message = "{timestamp.notBlank}")
        @Iso8601(message = "{timestamp.invalid}")
        String timestamp
) {}
