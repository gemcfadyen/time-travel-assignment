package com.spacetime.journeys.controller;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PersonalGalacticIdentifierValidator implements ConstraintValidator<ValidPersonalGalacticIdentifier, String> {
    @Override
    public boolean isValid(String identifierToValidate, ConstraintValidatorContext constraintValidatorContext) {
        String regex = "[a-zA-Z]([\\da-zA-Z]{4,9})";
        return identifierToValidate != null &&
                identifierToValidate.matches(regex);
    }
}

