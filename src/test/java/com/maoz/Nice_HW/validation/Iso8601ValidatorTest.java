package com.maoz.Nice_HW.validation;

import com.maoz.Nice_HW.dto.SuggestTaskRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class Iso8601ValidatorTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidTimestamp() {
        SuggestTaskRequest request = new SuggestTaskRequest(
                "check my order",
                "123",
                "session-1",
                "2025-09-09T14:10:32Z"  // תקין
        );

        Set<ConstraintViolation<SuggestTaskRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty(), "There should be no validation errors for a valid timestamp");
    }

    @Test
    void testInvalidTimestamp() {
        SuggestTaskRequest request = new SuggestTaskRequest(
                "check my order",
                "123",
                "session-1",
                "2025-09-09 14:10:32"  // לא תקין
        );

        Set<ConstraintViolation<SuggestTaskRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "There should be validation errors for an invalid timestamp");
    }
}
