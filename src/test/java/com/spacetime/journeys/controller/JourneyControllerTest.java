package com.spacetime.journeys.controller;

import com.spacetime.journeys.domain.responses.FetchTravellersJourneysResponse;
import com.spacetime.journeys.domain.Journey;
import com.spacetime.journeys.domain.responses.JourneyCreatedResponse;
import com.spacetime.journeys.domain.requests.JourneyRequest;
import com.spacetime.journeys.service.JourneyService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

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

    @Test
    public void getsAllJourneysForATraveller() {
        Journey earthJourney = Journey.builder()
                .id(1L)
                .travellerId("a1212ss")
                .destination("Earth")
                .travelDate(LocalDate.of(2020, 1, 1))
                .build();
        when(service.fetchAllJourneysFor("G234w33"))
                .thenReturn(List.of(earthJourney));

        FetchTravellersJourneysResponse response = controller.fetchJourneysFor("G234w33");

        assertThat(response.getJourneys()).isEqualTo(List.of(earthJourney));
    }

    @Test
    public void returnsEmptyListIfTravellerHasNotScheduledAnyJourneys() {
        when(service.fetchAllJourneysFor("G234w33")).thenReturn(Collections.emptyList());

        FetchTravellersJourneysResponse response = controller.fetchJourneysFor("G234w33", 1L);

        assertThat(response.getJourneys()).isEqualTo(Collections.emptyList());
    }

    @Test
    public void getsSpecificJourneyForTraveller() {
        Journey earthJourney = Journey.builder()
                .id(1L)
                .travellerId("G234w33")
                .destination("Earth")
                .travelDate(LocalDate.of(2020, 1, 1))
                .build();
        when(service.fetchJourneysDetailsFor("G234w33", 1L))
                .thenReturn(List.of(earthJourney));

        FetchTravellersJourneysResponse response = controller.fetchJourneysFor("G234w33", 1L);

        assertThat(response.getJourneys()).isEqualTo(List.of(earthJourney));
    }
}
