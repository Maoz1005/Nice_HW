package com.maoz.Nice_HW.dto;

/***
 * DTO representing the response for a suggested task.
 */
public record SuggestTaskResponseDTO(

        /**
         * The suggested task based on the user's input.
         * Can be a valid task name or a constant indicating that no task was found.
         */
        String task,

        /**
         * The timestamp (ISO-8601 format) when the response was generated.
         * Useful for logging and tracking request/response times.
         */
        String timestamp
) {}
