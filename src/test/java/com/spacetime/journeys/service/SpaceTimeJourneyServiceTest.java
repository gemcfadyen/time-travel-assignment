package com.spacetime.journeys.service;

import com.spacetime.journeys.domain.Journey;
import com.spacetime.journeys.domain.exceptions.JourneyAlreadyScheduledException;
import com.spacetime.journeys.domain.exceptions.JourneyNotFoundException;
import com.spacetime.journeys.repository.JourneyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SpaceTimeJourneyServiceTest {
    private JourneyService service;
    @Mock
    private JourneyRepository repository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        service = new SpaceTimeJourneyService(repository);
    }

    @Test
    public void savesJourney() {
        when(repository.fetchByTravelDetails("G23334",
                "Jupiter",
                LocalDate.of(1988, 1, 23)))
                .thenReturn(Optional.empty());

        Journey journey = Journey.builder()
                .travellerId("G23334")
                .destination("Jupiter")
                .travelDate(LocalDate.of(1988, 1, 23))
                .build();

        service.scheduleJourney(journey);

        verify(repository).save(journey);
    }

    @Test
    public void returnsSuccessfullySavedJourney() {
        Journey journey = Journey.builder()
                .travellerId("G23334")
                .destination("Jupiter")
                .travelDate(LocalDate.of(1988, 1, 23))
                .build();

        when(repository.fetchByTravelDetails("G23334", "Jupiter", LocalDate.of(1988, 1, 23))).thenReturn(Optional.empty());
        when(repository.save(journey)).thenReturn(Journey.builder()
                .id(1L)
                .travellerId("G23334")
                .destination("Jupiter")
                .travelDate(LocalDate.of(1988, 1, 23))
                .build());

        Journey savedJourney = service.scheduleJourney(journey);
        assertThat(savedJourney.getId()).isEqualTo(1);
        assertThat(savedJourney.getTravelDate()).isEqualTo(LocalDate.of(1988, 1, 23));
        assertThat(savedJourney.getDestination()).isEqualTo("Jupiter");
    }

    @Test
    public void throwsExceptionIfJourneyAlreadyScheduled() {
        Journey journeyThatIsAlreadyScheduled = Journey.builder()
                .id(2L)
                .travellerId("G23432")
                .travelDate(LocalDate.of(1999, 7, 23))
                .destination("Saturn")
                .build();

        when(repository.fetchByTravelDetails("G23432", "Saturn", LocalDate.of(1999, 7, 23)))
                .thenReturn(Optional.of(journeyThatIsAlreadyScheduled));

        assertThatExceptionOfType(JourneyAlreadyScheduledException.class)
                .isThrownBy(() -> {
                    service.scheduleJourney(journeyThatIsAlreadyScheduled);
                }).withMessage("Journey is already scheduled, id: [2]");
    }

    @Test
    public void returnsAllJourneysScheduledForATraveller() {
        Journey firstScheduledJourney = Journey.builder()
                .id(2L)
                .travellerId("G23432")
                .travelDate(LocalDate.of(1999, 7, 23))
                .destination("Saturn")
                .build();

        Journey secondScheduledJourney = Journey.builder()
                .id(1L)
                .travellerId("G23432")
                .travelDate(LocalDate.of(1969, 2, 3))
                .destination("Neptune")
                .build();

        when(repository.fetchJourneysFor("G23432"))
                .thenReturn(List.of(firstScheduledJourney, secondScheduledJourney));

        List<Journey> journeys = service.fetchAllJourneysFor("G23432");

        assertThat(journeys).containsExactlyInAnyOrder(firstScheduledJourney, secondScheduledJourney);
    }

    @Test
    public void returnsSpecificJourneyForATraveller() {
        Journey scheduledJourney = Journey.builder()
                .id(2L)
                .travellerId("G23432")
                .travelDate(LocalDate.of(1999, 7, 23))
                .destination("Saturn")
                .build();

        when(repository.fetchJourneyFor("G23432", 2L))
                .thenReturn(List.of(scheduledJourney));

        List<Journey> journeys = service.fetchJourneysDetailsFor("G23432", 2L);

        assertThat(journeys).containsExactlyInAnyOrder(scheduledJourney);
    }

    @Test
    public void throwsExceptionWhenSpecificJourneyNotFound() {
        when(repository.fetchJourneyFor("G23432", 2L))
                .thenReturn(Collections.emptyList());

        assertThatExceptionOfType(JourneyNotFoundException.class)
                .isThrownBy(() -> {
                    service.fetchJourneysDetailsFor("G23432", 2L);
                });
    }
}
