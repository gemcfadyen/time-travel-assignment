package com.spacetime.journeys;

import com.spacetime.journeys.domain.*;
import com.spacetime.journeys.service.JourneyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JourneyApiContractTest {
    @LocalServerPort
    protected int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockBean
    private JourneyService service;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void successfullyCreatesAJourney() {
        when(service.scheduleJourney(
                Journey.builder()
                        .travellerId("G1234b561")
                        .destination("Mars")
                        .travelDate(LocalDate.of(2020, 11, 17))
                        .build()))
                .thenReturn(Journey.builder()
                        .id(2L)
                        .build());

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
        when(service.scheduleJourney(
                Journey.builder()
                        .travellerId("G123424")
                        .destination("Mars")
                        .travelDate(LocalDate.of(2020, 11, 17))
                        .build()))
                .thenReturn(Journey.builder()
                        .id(2L)
                        .build());

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

        assertThat(response.getBody()).isEqualTo(JourneyCreatedResponse.builder()
                .journeyId(2L)
                .message("Journey planned successfully")
                .build());
    }

    @Test
    public void returnsConflictStatusWhenJourneyAlreadyExists() {
        when(service.scheduleJourney(
                Journey.builder()
                        .travellerId("G123424")
                        .destination("Mars")
                        .travelDate(LocalDate.of(2020, 11, 17))
                        .build()))
                .thenThrow(new JourneyAlreadyScheduledException(2L));

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
        URI uri = URI.create("/travellers/a1212ss/journeys");
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
        URI uri = URI.create("/travellers/a234234/journeys");
        when(service.fetchAllJourneysFor("a234234")).thenReturn(List.of(
                Journey.builder()
                        .id(1L)
                        .destination("Earth")
                        .travelDate(LocalDate.of(2020, 1, 1))
                        .build()
        ));

        ResponseEntity<FetchTravellersJourneysResponse> response = testRestTemplate.getForEntity(
                uri,
                FetchTravellersJourneysResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void returnsDetailsForAllJourneysForATraveller() {
        URI uri = URI.create("/travellers/a1212ss/journeys");
        Journey earthJourney = Journey.builder()
                .id(1L)
                .travellerId("a1212ss")
                .destination("Earth")
                .travelDate(LocalDate.of(2020, 1, 1))
                .build();
        Journey saturnJourney = Journey.builder()
                .id(2L)
                .travellerId("a1212ss")
                .destination("Saturn")
                .travelDate(LocalDate.of(2021, 1, 1))
                .build();

        when(service.fetchAllJourneysFor("a1212ss")).thenReturn(
                List.of(
                        earthJourney,
                        saturnJourney
                )
        );

        ResponseEntity<FetchTravellersJourneysResponse> response = testRestTemplate.getForEntity(
                uri,
                FetchTravellersJourneysResponse.class
        );

        List<Journey> journeys = response.getBody().getJourneys();
        assertThat(journeys.size()).isEqualTo(2);
        assertThat(journeys).contains(earthJourney, saturnJourney);
    }

    @Test
    public void successfullyFetchesAParticularJourneyForAGivenTraveller() {
        URI uri = URI.create("/travellers/a1212ss/journeys/1");
        Journey earthJourney = Journey.builder()
                .id(1L)
                .travellerId("a1212ss")
                .destination("Earth")
                .travelDate(LocalDate.of(2020, 1, 1))
                .build();

        when(service.fetchJourneysDetailsFor("a1212ss", 1L)).thenReturn(
                List.of(earthJourney)
        );

        ResponseEntity<FetchTravellersJourneysResponse> response = testRestTemplate.getForEntity(
                uri,
                FetchTravellersJourneysResponse.class
        );

        List<Journey> journeys = response.getBody().getJourneys();
        assertThat(journeys.size()).isEqualTo(1);
        assertThat(journeys).contains(earthJourney);
    }

    @Test
    public void returnsNotFoundWhenSpecificJourneyCanNotBeFound() {
        URI uri = URI.create("/travellers/a1212ss/journeys/1");

        when(service.fetchJourneysDetailsFor("a1212ss", 1L)).thenThrow(
                new JourneyNotFoundException()
        );

        ResponseEntity<FetchTravellersJourneysResponse> response = testRestTemplate.getForEntity(
                uri,
                FetchTravellersJourneysResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
