package com.spacetime.journeys.controller;

import com.spacetime.journeys.domain.JourneyCreatedResponse;
import com.spacetime.journeys.domain.JourneyRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/journeys")
public class JourneyController {

    @PostMapping(produces = {"application/json"}, consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public JourneyCreatedResponse createJourney(@RequestBody JourneyRequest request) {
        return JourneyCreatedResponse.builder()
                .message("Journey planned successfully")
                .build();
    }
}
