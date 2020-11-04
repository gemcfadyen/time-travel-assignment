package com.spacetime.journeys;

import com.spacetime.journeys.domain.*;
import com.spacetime.journeys.domain.requests.JourneyRequest;
import com.spacetime.journeys.domain.responses.FetchTravellersJourneysResponse;
import com.spacetime.journeys.domain.responses.JourneyCreatedResponse;
import com.spacetime.journeys.repository.JourneyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JourneyApiContractTest {
    @LocalServerPort
    protected int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private JourneyRepository inMemorySpaceTimeJourneyRepository;

    @Test
    public void successfullyCreatesAJourney() {
        URI uri = URI.create("/travellers/G1234b561/journeys");
        JourneyRequest journeyRequest = JourneyRequest.builder()
                .date(LocalDate.of(2020, 11, 17))
                .place("Mars")
                .build();

        ResponseEntity<JourneyCreatedResponse> response = testRestTemplate.postForEntity(
                uri,
                journeyRequest,
                JourneyCreatedResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void successfulMessageReturnedInResponseBody() {
        URI uri = URI.create("/travellers/G123424/journeys");
        JourneyRequest journeyRequest = JourneyRequest.builder()
                .date(LocalDate.of(2020, 11, 17))
                .place("Mars")
                .build();

        ResponseEntity<JourneyCreatedResponse> response = testRestTemplate.postForEntity(
                uri,
                journeyRequest,
                JourneyCreatedResponse.class
        );

        Optional<Journey> expectedJourney = inMemorySpaceTimeJourneyRepository.fetchByTravelDetails("G123424", "Mars", LocalDate.of(2020, 11, 17));
        assertThat(response.getBody()).isEqualTo(
                JourneyCreatedResponse.builder()
                        .journeyId(expectedJourney.get().getId())
                        .message("Journey planned successfully")
                        .build());
    }

    @Test
    public void returnsConflictStatusWhenJourneyAlreadyExists() {
        inMemorySpaceTimeJourneyRepository.save(Journey.builder()
                .travellerId("B11223ss")
                .destination("Mars")
                .travelDate(LocalDate.of(2020, 11, 17))
                .build());

        URI uri = URI.create("/travellers/B11223ss/journeys");
        JourneyRequest journeyRequest = JourneyRequest.builder()
                .date(LocalDate.of(2020, 11, 17))
                .place("Mars")
                .build();

        ResponseEntity<JourneyCreatedResponse> response = testRestTemplate.postForEntity(
                uri,
                journeyRequest,
                JourneyCreatedResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    public void returnsBadRequestWhenPersonalGalacticIdentifierIsInvalid() {
        URI uri = URI.create("/travellers/1*2/journeys");
        JourneyRequest journeyRequest = JourneyRequest.builder()
                .date(LocalDate.of(2020, 11, 17))
                .place("Mars")
                .build();

        ResponseEntity<String> response = testRestTemplate.postForEntity(
                uri,
                journeyRequest,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("createJourney.personalGalacticIdentifier: Invalid personal galactic identifier");
    }

    @Test
    public void returnsNotFoundWhenPersonalGalacticIdentifierIsMissing() {
        URI uri = URI.create("/travellers//journeys");
        JourneyRequest journeyRequest = JourneyRequest.builder()
                .date(LocalDate.of(2020, 11, 17))
                .place("Mars")
                .build();

        ResponseEntity<JourneyCreatedResponse> response = testRestTemplate.postForEntity(
                uri,
                journeyRequest,
                JourneyCreatedResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void returnsBadRequestWhenPlaceIsMissingOnRequest() {
        URI uri = URI.create("/travellers/F12ssj/journeys");
        JourneyRequest journeyRequest = JourneyRequest.builder()
                .date(LocalDate.of(2020, 11, 17))
                .build();

        ResponseEntity<JourneyCreatedResponse> response = testRestTemplate.postForEntity(
                uri,
                journeyRequest,
                JourneyCreatedResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void returnsBadRequestWhenDateIsMissingOnRequest() {
        URI uri = URI.create("/travellers/a121AA/journeys");
        JourneyRequest journeyRequest = JourneyRequest.builder()
                .place("Earth")
                .build();

        ResponseEntity<JourneyCreatedResponse> response = testRestTemplate.postForEntity(
                uri,
                journeyRequest,
                JourneyCreatedResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void successfullyFetchesAllJourneysForATraveller() {
        URI uri = URI.create("/travellers/B2AA234/journeys");
        inMemorySpaceTimeJourneyRepository.save(Journey.builder()
                .destination("Earth")
                .travelDate(LocalDate.of(2020, 1, 1))
                .build());

        ResponseEntity<FetchTravellersJourneysResponse> response = testRestTemplate.getForEntity(
                uri,
                FetchTravellersJourneysResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void returnsDetailsForAllJourneysForATraveller() {
        URI uri = URI.create("/travellers/G44bA2ss/journeys");
        Journey earthJourney = Journey.builder()
                .id(1L)
                .travellerId("G44bA2ss")
                .destination("Earth")
                .travelDate(LocalDate.of(2020, 1, 1))
                .build();
        Journey saturnJourney = Journey.builder()
                .id(2L)
                .travellerId("G44bA2ss")
                .destination("Saturn")
                .travelDate(LocalDate.of(2021, 1, 1))
                .build();
        Journey savedEarthJourney = inMemorySpaceTimeJourneyRepository.save(earthJourney);
        Journey savedSaturnJourney = inMemorySpaceTimeJourneyRepository.save(saturnJourney);

        ResponseEntity<FetchTravellersJourneysResponse> response = testRestTemplate.getForEntity(
                uri,
                FetchTravellersJourneysResponse.class
        );

        List<Journey> journeys = response.getBody().getJourneys();
        assertThat(journeys).containsExactlyInAnyOrder(savedEarthJourney, savedSaturnJourney);
    }

    @Test
    public void successfullyFetchesAParticularJourneyForAGivenTraveller() {
        Journey earthJourney = Journey.builder()
                .travellerId("C00SSk")
                .destination("Earth")
                .travelDate(LocalDate.of(2020, 1, 1))
                .build();
        Journey savedEarthJourney = inMemorySpaceTimeJourneyRepository.save(earthJourney);
        URI uri = URI.create("/travellers/C00SSk/journeys/" + savedEarthJourney.getId());

        ResponseEntity<FetchTravellersJourneysResponse> response = testRestTemplate.getForEntity(
                uri,
                FetchTravellersJourneysResponse.class
        );

        List<Journey> journeys = response.getBody().getJourneys();
        assertThat(journeys).containsExactlyInAnyOrder(savedEarthJourney);
    }

    @Test
    public void returnsNotFoundWhenSpecificJourneyCanNotBeFound() {
        URI uri = URI.create("/travellers/a1212ss/journeys/1");
        ResponseEntity<FetchTravellersJourneysResponse> response = testRestTemplate.getForEntity(
                uri,
                FetchTravellersJourneysResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
