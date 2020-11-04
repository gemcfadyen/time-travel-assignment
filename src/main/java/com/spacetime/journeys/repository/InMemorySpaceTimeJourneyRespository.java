package com.spacetime.journeys.repository;

import com.spacetime.journeys.domain.Journey;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class InMemorySpaceTimeJourneyRespository implements JourneyRepository {

    @Override
    public Journey save(Journey journey) {
        return null;
    }

    @Override
    public Optional<Journey> fetchByTravelDetails(String travellerId, String destination, LocalDate travelDate) {
        return Optional.empty();
    }
}
