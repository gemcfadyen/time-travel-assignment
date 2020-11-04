package com.spacetime.journeys.service;

import com.spacetime.journeys.domain.Journey;

import java.util.List;

public interface JourneyService {
    Journey scheduleJourney(Journey journey);

    List<Journey> fetchAllJourneysFor(String personalGalacticIdentifier);

    List<Journey> fetchJourneysDetailsFor(String personalGalacticIdentifier, Long journeyId);
}
