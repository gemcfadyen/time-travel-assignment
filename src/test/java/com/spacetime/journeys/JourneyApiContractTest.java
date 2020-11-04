package com.spacetime.journeys;

import com.spacetime.journeys.domain.JourneyCreatedResponse;
import com.spacetime.journeys.domain.JourneyRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JourneyApiContractTest {
    @LocalServerPort
    protected int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

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

        assertThat(response.getBody()).isEqualTo(JourneyCreatedResponse.builder().message("Journey planned successfully").build());
    }
}
