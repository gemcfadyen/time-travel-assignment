package com.spacetime.journeys.service;

import java.time.LocalDate;

public interface JourneyService {
    String scheduleJourney(String id, String place, LocalDate date);
}
