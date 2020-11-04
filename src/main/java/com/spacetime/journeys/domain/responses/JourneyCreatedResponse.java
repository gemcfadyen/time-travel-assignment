package com.spacetime.journeys.domain.responses;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class JourneyCreatedResponse {
    private Long journeyId;
    private String message;
}
