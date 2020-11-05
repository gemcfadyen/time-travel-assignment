package com.spacetime.journeys.domain.requests;

import com.spacetime.journeys.domain.Journey;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
public class JourneyRequest {
    @NotNull
    private LocalDate date;
    @NotNull
    private String place;

    public Journey toJourney(String personalGalacticIdentifier) {
        return Journey.builder()
                .travellerId(personalGalacticIdentifier)
                .destination(this.place)
                .travelDate(this.date)
                .build();
    }
}
