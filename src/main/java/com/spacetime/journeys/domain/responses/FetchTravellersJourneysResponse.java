package com.spacetime.journeys.domain.responses;

import com.spacetime.journeys.domain.Journey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FetchTravellersJourneysResponse {
    private List<Journey> journeys = new ArrayList<>();
}
