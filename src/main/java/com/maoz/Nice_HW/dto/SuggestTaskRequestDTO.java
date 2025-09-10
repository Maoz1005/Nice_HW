package com.maoz.Nice_HW.dto;

import com.maoz.Nice_HW.config.Constants;
import jakarta.validation.constraints.NotBlank;
import com.maoz.Nice_HW.validation.Iso8601;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import static com.maoz.Nice_HW.config.Constants.MAX_UTTERANCE_LENGTH;

/***
 * DTO representing a request to suggest a task based on user input.
 */
public record SuggestTaskRequestDTO(

        /**
         * The user input text (utterance) for which a task should be suggested.
         * Must not be blank and its length must not exceed MAX_UTTERANCE_LENGTH.
         */
        @NotBlank(message = "{utterance.notBlank}")
        @Size(max = Constants.MAX_UTTERANCE_LENGTH, message = "{utterance.size}")
        String utterance,

        /**
         * ID of the user making the request.
         * Must not be blank, contain only digits, and have length between 1 and 9.
         */
        @NotBlank(message = "{userId.notBlank}")
        @Size(min = 1, max = 9, message = "{userId.size}")
        @Pattern(regexp = "^[0-9]+$", message = "{userId.pattern}")
        String userId,

        /**
         * Session ID of the user request.
         * Must not be blank.
         */
        @NotBlank(message = "{sessionId.notBlank}")
        String sessionId,

        /**
         * Timestamp of the request.
         * Must not be blank and must follow ISO-8601 format.
         */
        @NotBlank(message = "{timestamp.notBlank}")
        @Iso8601(message = "{timestamp.invalid}")
        String timestamp
) {}
