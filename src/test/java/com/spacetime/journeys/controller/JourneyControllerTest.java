package com.spacetime.journeys.controller;

import com.spacetime.journeys.domain.Journey;
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
                .build();

        when(service.scheduleJourney(any())).thenReturn(Journey.builder()
                .id(1L)
                .build());

        controller.createJourney("G23d234", journeyRequest);

        verify(service).scheduleJourney(Journey.builder()
                .travellerId("G23d234")
                .destination("Moon")
                .travelDate(LocalDate.of(2045, 4, 11))
                .build());
    }

    @Test
    public void returnsNewlyCreatedJourneyId() {
        when(service.scheduleJourney(Journey.builder()
                .travellerId("G23d234")
                .destination("Moon")
                .travelDate(LocalDate.of(2045, 4, 11))
                .build()))
                .thenReturn(Journey.builder()
                        .id(1L)
                        .build());

        JourneyRequest journeyRequest = JourneyRequest.builder()
                .date(LocalDate.of(2045, 4, 11))
                .place("Moon")
                .build();

        JourneyCreatedResponse response = controller.createJourney("G23d234", journeyRequest);

        assertThat(response.getJourneyId()).isEqualTo(1L);
    }

}
