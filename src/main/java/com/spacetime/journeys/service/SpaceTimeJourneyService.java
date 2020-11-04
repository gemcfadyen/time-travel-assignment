package com.spacetime.journeys.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class SpaceTimeJourneyService implements JourneyService {
    @Override
    public String scheduleJourney(String id, String place, LocalDate date) {
        return "J1";
    }
}
