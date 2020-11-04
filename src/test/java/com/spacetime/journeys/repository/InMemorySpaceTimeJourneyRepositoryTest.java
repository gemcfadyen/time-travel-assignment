package com.spacetime.journeys.repository;

import com.spacetime.journeys.domain.Journey;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class InMemorySpaceTimeJourneyRepositoryTest {
    private JourneyRepository inMemoryRepository = new InMemorySpaceTimeJourneyRespository();

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
}
