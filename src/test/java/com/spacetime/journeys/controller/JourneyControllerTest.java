package com.spacetime.journeys.controller;

import com.spacetime.journeys.domain.JourneyCreatedResponse;
import com.spacetime.journeys.domain.JourneyRequest;
import com.spacetime.journeys.service.JourneyService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

public class JourneyControllerTest {
    private JourneyController controller;
    @Mock
    private JourneyService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        controller = new JourneyController(service);
    }

    @Test
    public void invokesJourneyService() {
        JourneyRequest journeyRequest = JourneyRequest.builder()
                .date(LocalDate.of(2045, 4, 11))
                .place("Moon")
                .personalGalacticIdentifier("G23d")
                .build();

        controller.createJourney(journeyRequest);

        verify(service).scheduleJourney("G23d", "Moon", LocalDate.of(2045, 4, 11));
    }

    @Test
    public void returnsNewlyCreatedJourneyId() {
        when(service.scheduleJourney("G23d", "Moon", LocalDate.of(2045, 4, 11)))
                .thenReturn("J1");

        JourneyRequest journeyRequest = JourneyRequest.builder()
                .date(LocalDate.of(2045, 4, 11))
                .place("Moon")
                .personalGalacticIdentifier("G23d")
                .build();

        JourneyCreatedResponse response = controller.createJourney(journeyRequest);

        assertThat(response.getJourneyId()).isEqualTo("J1");
    }
}
