package com.maoz.Nice_HW.validation;

import com.maoz.Nice_HW.dto.SuggestTaskRequestDTO;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;
import jakarta.validation.ConstraintViolation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SuggestTaskRequestDTOTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validRequest_passesValidation() {
        SuggestTaskRequestDTO dto = new SuggestTaskRequestDTO(
                "please reset my password",
                "12345",
                "abcde-67890",
                "2025-09-09T14:10:32+03:00"
        );

        Set<ConstraintViolation<SuggestTaskRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void missingUtterance_failsValidation() {
        SuggestTaskRequestDTO dto = new SuggestTaskRequestDTO(
                "",
                "12345",
                "abcde-67890",
                "2025-09-09T14:10:32+03:00"
        );

        Set<ConstraintViolation<SuggestTaskRequestDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("utterance must not be blank", violations.iterator().next().getMessage());
    }

    @Test
    void invalidUserId_failsValidation() {
        SuggestTaskRequestDTO dto = new SuggestTaskRequestDTO(
                "check my order",
                "abc",
                "abcde-67890",
                "2025-09-09T14:10:32+03:00"
        );

        Set<ConstraintViolation<SuggestTaskRequestDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("userId must contain only digits", violations.iterator().next().getMessage());
    }

    @Test
    void invalidTimestamp_failsValidation() {
        SuggestTaskRequestDTO dto = new SuggestTaskRequestDTO(
                "check my order",
                "12345",
                "abcde-67890",
                "2025-09-09 14:10:32"
        );

        Set<ConstraintViolation<SuggestTaskRequestDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("timestamp must be a valid ISO-8601 instant (for example: 2025-08-21T12:00:00Z)",
                violations.iterator().next().getMessage());
    }
}
