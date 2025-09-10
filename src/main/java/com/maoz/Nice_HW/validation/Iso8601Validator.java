package com.maoz.Nice_HW.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;

/***
 * Validator for the @Iso8601 annotation.
 * Checks if a given String represents a valid ISO8601 timestamp.
 */
public class Iso8601Validator implements ConstraintValidator<Iso8601, String> {

    /**
     * Validates the given value (according to Iso8601).
     *
     * @param value - the String value to validate
     * @param context - the context in which the constraint is evaluated
     * @return true if the value is a valid ISO-8601 timestamp, false otherwise
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Null or blank values are considered invalid
        if (value == null || value.isBlank()) return false;

        try {
            // Try to parse the string as an ISO-8601 OffsetDateTime
            OffsetDateTime.parse(value);
            return true;
        } catch (DateTimeParseException e) {
            // If it failed - invalid format
            return false;
        }
    }
}
