package com.spacetime.journeys.controller;

import com.spacetime.journeys.controller.validation.PersonalGalacticIdentifierValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = PersonalGalacticIdentifierValidator.class)
@Documented
public @interface ValidPersonalGalacticIdentifier {
    String message() default "Invalid personal galactic identifier";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
