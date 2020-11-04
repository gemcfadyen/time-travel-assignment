package com.spacetime.journeys.repository;

import com.spacetime.journeys.domain.Journey;
import com.spacetime.journeys.domain.exceptions.JourneyNotFoundException;
import com.spacetime.journeys.repository.inmemory.InMemorySpaceTimeJourneyRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class InMemorySpaceTimeJourneyRepositoryTest {
    private final JourneyRepository inMemoryRepository = new InMemorySpaceTimeJourneyRepository();

    @Test
    public void savesTravellersFirstJourney() {
        Journey journeyDetails = Journey.builder()
                .destination("Venus")
                .travelDate(LocalDate.of(1999, 7, 7))
                .travellerId("G234234")
                .build();
        Journey savedJourney = inMemoryRepository.save(journeyDetails);

        assertThat(savedJourney.getDestination()).isEqualTo(journeyDetails.getDestination());
        assertThat(savedJourney.getTravelDate()).isEqualTo(journeyDetails.getTravelDate());
        assertThat(savedJourney.getTravellerId()).isEqualTo(journeyDetails.getTravellerId());
        assertThat(savedJourney.getId()).isNotNull();
    }

    @Test
    public void savesTravellersJourneyWhenTheyAlreadyHaveOthersPlanned() {
        Journey firstJourneyDetails = Journey.builder()
                .destination("Venus")
                .travelDate(LocalDate.of(1999, 7, 7))
                .travellerId("G234234")
                .build();

        Journey subsequentJourneyDetails = Journey.builder()
                .destination("Earth")
                .travelDate(LocalDate.of(1996, 7, 7))
                .travellerId("G234234")
                .build();

        inMemoryRepository.save(firstJourneyDetails);
        inMemoryRepository.save(subsequentJourneyDetails);

        Journey earthJourney = inMemoryRepository.fetchByTravelDetails("G234234", "Earth", LocalDate.of(1996, 7, 7)).get();
        Journey venusJourney = inMemoryRepository.fetchByTravelDetails("G234234", "Venus", LocalDate.of(1999, 7, 7)).get();

        assertThat(earthJourney.getTravellerId()).isEqualTo("G234234");
        assertThat(venusJourney.getTravellerId()).isEqualTo("G234234");
    }

    @Test
    public void looksUpPreviouslySavedJourney() {
        Journey journeyDetails = Journey.builder()
                .destination("Venus")
                .travelDate(LocalDate.of(1999, 7, 7))
                .travellerId("G234234")
                .build();
        inMemoryRepository.save(journeyDetails);

        Optional<Journey> possibleSavedJourney = inMemoryRepository.fetchByTravelDetails("G234234",
                "Venus",
                LocalDate.of(1999, 7, 7));

        Journey savedJourney = possibleSavedJourney.get();
        assertThat(savedJourney.getDestination()).isEqualTo(journeyDetails.getDestination());
        assertThat(savedJourney.getTravelDate()).isEqualTo(journeyDetails.getTravelDate());
        assertThat(savedJourney.getTravellerId()).isEqualTo(journeyDetails.getTravellerId());
    }

    @Test
    public void looksUpJourneyWhichDoesntExist() {
        Optional<Journey> possibleSavedJourney = inMemoryRepository.fetchByTravelDetails("G234234",
                "Toyland",
                LocalDate.of(1999, 7, 7));

        assertThat(possibleSavedJourney).isEmpty();
    }

    @Test
    public void fetchesAllJourneysForATraveller() {
        Journey firstJourney = Journey.builder()
                .destination("Venus")
                .travelDate(LocalDate.of(1999, 7, 7))
                .travellerId("G234234")
                .build();

        Journey secondJourney = Journey.builder()
                .destination("Saturn")
                .travelDate(LocalDate.of(1833, 2, 11))
                .travellerId("G234234")
                .build();

        Journey journeyForUnrelatedTraveller = Journey.builder()
                .destination("Earth")
                .travelDate(LocalDate.of(1233, 11, 1))
                .travellerId("AB22324")
                .build();

        Journey firstSavedJourney = inMemoryRepository.save(firstJourney);
        Journey secondSavedJourney = inMemoryRepository.save(secondJourney);
        inMemoryRepository.save(journeyForUnrelatedTraveller);

        List<Journey> journeys = inMemoryRepository.fetchJourneysFor("G234234");

        assertThat(journeys).containsExactlyInAnyOrder(firstSavedJourney, secondSavedJourney);
    }

    @Test
    public void returnsEmptyListWhenTravellerHasNoScheduledJourneys() {
        Journey journeyForUnrelatedTraveller = Journey.builder()
                .destination("Earth")
                .travelDate(LocalDate.of(1233, 11, 1))
                .travellerId("AB22324")
                .build();

        inMemoryRepository.save(journeyForUnrelatedTraveller);

        List<Journey> journeys = inMemoryRepository.fetchJourneysFor("G234234");

        assertThat(journeys).isEmpty();
    }

    @Test
    public void returnsParticularJourneyForATraveller() {
        Journey firstJourney = Journey.builder()
                .destination("Saturn")
                .travelDate(LocalDate.of(1833, 2, 11))
                .travellerId("AB22324")
                .build();

        Journey secondJourney = Journey.builder()
                .destination("Earth")
                .travelDate(LocalDate.of(1233, 11, 1))
                .travellerId("AB22324")
                .build();

        Journey savedJourney = inMemoryRepository.save(firstJourney);
        inMemoryRepository.save(secondJourney);

        List<Journey> journeys = inMemoryRepository.fetchJourneyFor("AB22324", savedJourney.getId());

        assertThat(journeys).containsExactlyInAnyOrder(savedJourney);
    }

    @Test
    public void throwsExceptionWhenFetchingSpecificJourneyWhenTravellerHasNone() {
        assertThatExceptionOfType(JourneyNotFoundException.class)
                .isThrownBy(() -> {
                    inMemoryRepository.fetchJourneyFor("AB22324", -1L);
                });
    }

    @Test
    public void throwsExceptionWhenSpecificJourneyNotFoundForTraveller() {
        Journey firstJourney = Journey.builder()
                .destination("Saturn")
                .travelDate(LocalDate.of(1833, 2, 11))
                .travellerId("AB22324")
                .build();
        inMemoryRepository.save(firstJourney);

        assertThatExceptionOfType(JourneyNotFoundException.class)
                .isThrownBy(() -> {
                    inMemoryRepository.fetchJourneyFor("AB22324", -1L);
                });
    }
}
