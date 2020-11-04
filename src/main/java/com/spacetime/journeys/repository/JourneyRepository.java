package com.spacetime.journeys.repository;

import com.spacetime.journeys.domain.Journey;

import java.time.LocalDate;
import java.util.Optional;

public interface JourneyRepository {
    Journey save(Journey journey);
    Optional<Journey> fetchByTravelDetails(String travellerId, String destination, LocalDate travelDate);
}
