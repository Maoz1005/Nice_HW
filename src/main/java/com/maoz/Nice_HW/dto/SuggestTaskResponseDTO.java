package com.maoz.Nice_HW.dto;

public record SuggestTaskResponseDTO(

        /**
         * The suggested task based on the user's input.
         */
        String task,
        String timestamp
) {}
