package com.spacetime.journeys.controller;

import com.spacetime.journeys.domain.JourneyCreatedResponse;
import com.spacetime.journeys.domain.JourneyRequest;
import com.spacetime.journeys.service.JourneyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/journeys")
public class JourneyController {
    private JourneyService service;

    public JourneyController(JourneyService service) {
        this.service = service;
    }

    @PostMapping(produces = {"application/json"}, consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public JourneyCreatedResponse createJourney(@RequestBody JourneyRequest request) {
        String journeyId = service.scheduleJourney(request.getPersonalGalacticIdentifier(),
                request.getPlace(),
                request.getDate());

        return JourneyCreatedResponse.builder()
                .journeyId(journeyId)
                .message("Journey planned successfully")
                .build();
    }
}
