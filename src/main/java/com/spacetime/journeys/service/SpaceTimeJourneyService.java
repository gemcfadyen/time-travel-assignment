package com.spacetime.journeys.service;

import com.spacetime.journeys.domain.Journey;
import com.spacetime.journeys.domain.JourneyAlreadyScheduledException;
import com.spacetime.journeys.repository.JourneyRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SpaceTimeJourneyService implements JourneyService {
    private JourneyRepository repository;

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
}
