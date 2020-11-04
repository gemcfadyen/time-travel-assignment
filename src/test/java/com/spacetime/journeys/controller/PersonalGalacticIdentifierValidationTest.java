package com.spacetime.journeys.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.validation.ConstraintValidatorContext;

import static org.assertj.core.api.Assertions.assertThat;

public class PersonalGalacticIdentifierValidationTest {
    private final PersonalGalacticIdentifierValidator validator = new PersonalGalacticIdentifierValidator();
    @Mock
    private ConstraintValidatorContext context;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void nullIdentifierIsInvalid() {
        assertThat(validator.isValid(null, context)).isFalse();
    }

    @Test
    public void identifierStartingWithNumberIsInvalid() {
       assertThat(validator.isValid("3ABCDEF", context)).isFalse();
    }

    @Test
    public void identifierStartingWithSpecialCharacterIsInvalid() {
        assertThat(validator.isValid("*ABCDEF", context)).isFalse();
    }

    @Test
    public void identifierThatIsTooShortIsInvalid() {
        assertThat(validator.isValid("AB12", context)).isFalse();
    }

    @Test
    public void identifierThatIsTooLongIsInvalid() {
        assertThat(validator.isValid("AB1234567891011", context)).isFalse();
    }

    @Test
    public void identifierThatContainsSpecialCharacterIsInvalid() {
        assertThat(validator.isValid("AB12$kji!", context)).isFalse();
    }

    @Test
    public void identifierThatContainsWhiteSpaceIsInvalid() {
        assertThat(validator.isValid("AB12 12345", context)).isFalse();
    }

    @Test
    public void identifierThatAdheresToRulesIsValid() {
        assertThat(validator.isValid("aS23ab", context)).isTrue();
    }
}
