package com.spacetime.journeys.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
@Data
@Builder
public class Journey {
    private Long id;
    private String travellerId;
    private LocalDate travelDate;
    private String destination;
}
