package com.spacetime.journeys;

import com.spacetime.journeys.domain.JourneyCreatedResponse;
import com.spacetime.journeys.domain.JourneyRequest;
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
        URI uri = URI.create("/journeys");
        JourneyRequest journeyRequest = JourneyRequest.builder()
                .personalGalacticIdentifier("G1234b561")
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
        when(service.scheduleJourney("G123424", "Mars", LocalDate.of(2020, 11, 17)))
                .thenReturn("J1");
        URI uri = URI.create("/journeys");
        JourneyRequest journeyRequest = JourneyRequest.builder()
                .personalGalacticIdentifier("G123424")
                .date(LocalDate.of(2020, 11, 17))
                .place("Mars")
                .build();

        ResponseEntity<JourneyCreatedResponse> response = testRestTemplate.postForEntity(
                uri,
                journeyRequest,
                JourneyCreatedResponse.class
        );

        assertThat(response.getBody()).isEqualTo(JourneyCreatedResponse.builder()
                .journeyId("J1")
                .message("Journey planned successfully")
                .build());
    }
}
