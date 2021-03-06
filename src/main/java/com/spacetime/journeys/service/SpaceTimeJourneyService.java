package com.spacetime.journeys.service;

import com.spacetime.journeys.domain.Journey;
import com.spacetime.journeys.domain.exceptions.JourneyAlreadyScheduledException;
import com.spacetime.journeys.domain.exceptions.JourneyNotFoundException;
import com.spacetime.journeys.repository.JourneyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpaceTimeJourneyService implements JourneyService {
    private final JourneyRepository repository;

    SpaceTimeJourneyService(JourneyRepository repository) {
        this.repository = repository;
    }

    @Override
    public Journey scheduleJourney(Journey journeyDetails) {
        Optional<Journey> possibleScheduledJourney = repository.fetchByTravelDetails(journeyDetails.getTravellerId(), journeyDetails.getDestination(), journeyDetails.getTravelDate());
        possibleScheduledJourney.ifPresent(journey -> {
                    throw new JourneyAlreadyScheduledException(journey.getId());
                }
        );
        return repository.save(journeyDetails);
    }

    @Override
    public List<Journey> fetchAllJourneysFor(String personalGalacticIdentifier) {
        return repository.fetchJourneysFor(personalGalacticIdentifier);
    }

    @Override
    public List<Journey> fetchJourneysDetailsFor(String personalGalacticIdentifier, Long journeyId) {
        List<Journey> journeys = repository.fetchJourneyFor(personalGalacticIdentifier, journeyId);
        if (journeys.isEmpty()) {
            throw new JourneyNotFoundException();
        }
        return journeys;
    }
}
