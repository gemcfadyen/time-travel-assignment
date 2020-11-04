package com.spacetime.journeys.domain;

import com.spacetime.journeys.controller.ValidPersonalGalacticIdentifier;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
@Data
@Builder
public class JourneyRequest {
    @ValidPersonalGalacticIdentifier
    private String personalGalacticIdentifier;
    @NotNull
    private LocalDate date;
    @NotNull
    private String place;

    public Journey toJourney() {
       return Journey.builder()
               .travellerId(this.personalGalacticIdentifier)
               .destination(this.place)
               .travelDate(this.date)
               .build() ;
    }
}
