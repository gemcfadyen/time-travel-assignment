package com.spacetime.journeys.repository.inmemory;

import com.spacetime.journeys.domain.Journey;
import com.spacetime.journeys.domain.exceptions.JourneyNotFoundException;
import com.spacetime.journeys.repository.JourneyRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemorySpaceTimeJourneyRepository implements JourneyRepository {
    private final Map<String, List<Journey>> travellerIdAndTheirJourneys = new HashMap<>();

    @Override
    public Journey save(Journey journey) {
        Long journeyId = new Random().nextLong();
        Journey journeyToSave = Journey.builder()
                .id(journeyId)
                .travellerId(journey.getTravellerId())
                .travelDate(journey.getTravelDate())
                .destination(journey.getDestination())
                .build();

        List<Journey> updatedJourneys = updatedJourneys(travellerIdAndTheirJourneys.get(journey.getTravellerId()),
                journeyToSave);
        travellerIdAndTheirJourneys.put(journey.getTravellerId(), updatedJourneys);
        return journeyToSave;
    }

    @Override
    public Optional<Journey> fetchByTravelDetails(String travellerId, String destination, LocalDate travelDate) {
        List<Journey> journeys = travellerIdAndTheirJourneys.get(travellerId);
        return findMatchingJourney(travellerId, destination, travelDate, journeys);
    }

    @Override
    public List<Journey> fetchJourneysFor(String travellerId) {
        List<Journey> journeys = travellerIdAndTheirJourneys.get(travellerId);
        return journeys == null ? new ArrayList<>() : journeys;
    }

    @Override
    public List<Journey> fetchJourneyFor(String travellerId, Long journeyId) {
        List<Journey> journeys = travellerIdAndTheirJourneys.get(travellerId);
        return findMatchingJourney(journeyId, journeys);
    }

    private List<Journey> updatedJourneys(List<Journey> existingJourneys, Journey journeyToSave) {
        if (existingJourneys != null) {
            List<Journey> copy = new ArrayList<>(existingJourneys);
            copy.add(journeyToSave);
            return copy;
        } else {
            return List.of(journeyToSave);
        }
    }

    private Optional<Journey> findMatchingJourney(String travellerId, String destination, LocalDate travelDate, List<Journey> journeys) {
        return journeys == null ?
                Optional.empty() :
                journeys.stream().filter(journey ->
                        journey.getTravellerId().equals(travellerId)
                                && journey.getDestination().equals(destination)
                                && journey.getTravelDate().equals(travelDate))
                        .findFirst();
    }

    private List<Journey> findMatchingJourney(Long journeyId, List<Journey> journeys) {
        if (journeys == null) {
            throw new JourneyNotFoundException();
        }

        List<Journey> matchingJourney = journeys
                .stream()
                .filter(journey -> journey.getId().equals(journeyId))
                .collect(Collectors.toList());

        if (matchingJourney.isEmpty()) {
            throw new JourneyNotFoundException();
        }
        return matchingJourney;
    }
}
