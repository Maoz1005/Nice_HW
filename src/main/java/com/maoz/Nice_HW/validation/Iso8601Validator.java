package com.maoz.Nice_HW.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;

public class Iso8601Validator implements ConstraintValidator<Iso8601, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) return false;
        try {
            OffsetDateTime.parse(value);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
