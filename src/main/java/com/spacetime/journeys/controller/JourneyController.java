package com.spacetime.journeys.controller;

import com.spacetime.journeys.domain.FetchTravellersJourneysResponse;
import com.spacetime.journeys.domain.Journey;
import com.spacetime.journeys.domain.JourneyCreatedResponse;
import com.spacetime.journeys.domain.JourneyRequest;
import com.spacetime.journeys.service.JourneyService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
@RequestMapping("/travellers")
public class JourneyController {
    private final JourneyService service;

    public JourneyController(JourneyService service) {
        this.service = service;
    }

    @ApiOperation(value = "Creates a journey for a traveller", response = JourneyCreatedResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Journey has been created", response = JourneyCreatedResponse.class),
            @ApiResponse(code = 409, message = "Journey already exists"),
            @ApiResponse(code = 400, message = "Invalid request")})
    @PostMapping(path = "/{pgi}/journeys", produces = {"application/json"}, consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public JourneyCreatedResponse createJourney(@PathVariable(name = "pgi") @ValidPersonalGalacticIdentifier String personalGalacticIdentifier, @Valid @RequestBody JourneyRequest request) {
        Journey scheduledJourney = service.scheduleJourney(request.toJourney(personalGalacticIdentifier));

        return JourneyCreatedResponse.builder()
                .journeyId(scheduledJourney.getId())
                .message("Journey planned successfully")
                .build();
    }

    @ApiOperation(value = "Retrieves a specific journey for a traveller", response = FetchTravellersJourneysResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Journey has been successfully retrieved", response = FetchTravellersJourneysResponse.class),
            @ApiResponse(code = 400, message = "Invalid Personal Galactic Identifier (pgi) provided"),
            @ApiResponse(code = 404, message = "Journey can not be found")
    })
    @GetMapping(path = "/{pgi}/journeys/{journeyId}", produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public FetchTravellersJourneysResponse fetchJourneysFor(@PathVariable(name = "pgi") @ValidPersonalGalacticIdentifier String personalGalacticIdentifier,
                                                            @PathVariable(name = "journeyId") Long journeyId) {
        List<Journey> journeys = service.fetchJourneysDetailsFor(personalGalacticIdentifier, journeyId);
        return FetchTravellersJourneysResponse.builder()
                .journeys(journeys)
                .build();
    }

    @ApiOperation(value = "Retrieves all journeys for a traveller", response = FetchTravellersJourneysResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Journeys have been successfully retrieved", response = FetchTravellersJourneysResponse.class),
            @ApiResponse(code = 400, message = "Invalid Personal Galactic Identifier (pgi) provided", response = FetchTravellersJourneysResponse.class)})
    @GetMapping(path = "/{pgi}/journeys", produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public FetchTravellersJourneysResponse fetchJourneysFor(@PathVariable(name = "pgi") @ValidPersonalGalacticIdentifier String personalGalacticIdentifier) {
        List<Journey> journeys = service.fetchAllJourneysFor(personalGalacticIdentifier);
        return FetchTravellersJourneysResponse.builder()
                .journeys(journeys)
                .build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> onValidationError(Exception constraintException) {
        return ResponseEntity.badRequest().body(constraintException.getMessage());
    }
}
