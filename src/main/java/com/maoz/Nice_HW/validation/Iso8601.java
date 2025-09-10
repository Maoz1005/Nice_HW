package com.maoz.Nice_HW.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/***
 * The annotation is suitable for validating timestamps coming from JSON requests in DTOs.
 * It relies on Iso8601Validator to perform the actual validation, and it can be applied to
 * fields or method/constructor parameters (except collections).
 */
@Documented // Adds to Javadoc
@Constraint(validatedBy = Iso8601Validator.class) // The annotation is validated by Iso8601Validator
@Target({ FIELD, PARAMETER }) // Specifies the elements where the annotation can be applied
@Retention(RUNTIME) // Indicates that the annotation is retained at runtime
public @interface Iso8601 {
    // The error message that will be sent
    String message() default "{timestamp.invalid}";

    // Bean Validation requires these elements even if we don't use them
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}