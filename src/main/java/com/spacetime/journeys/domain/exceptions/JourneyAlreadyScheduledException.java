package com.spacetime.journeys.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class JourneyAlreadyScheduledException extends RuntimeException {
    public JourneyAlreadyScheduledException(Long journeyId) {
        super("Journey is already scheduled, id: [" + journeyId + "]");
    }
}
