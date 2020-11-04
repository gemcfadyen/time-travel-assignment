package com.spacetime.journeys.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
@Data
@Builder
public class JourneyRequest {
    private String personalGalacticIdentifier;
    private LocalDate date;
    private String place;

    public Journey toJourney() {
       return Journey.builder()
               .travellerId(this.personalGalacticIdentifier)
               .destination(this.place)
               .travelDate(this.date)
               .build() ;
    }
}
