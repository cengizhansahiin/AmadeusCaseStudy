package com.amadeus.casestudy.service.dtos;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightSearchRequest {

    private String departureAirport;
    private String arrivalAirport;
    private LocalDateTime departureTime;
    private LocalDateTime returnTime;
}
