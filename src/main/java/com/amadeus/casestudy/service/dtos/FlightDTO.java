package com.amadeus.casestudy.service.dtos;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightDTO {

    private AirportDTO departureAirport;
    private AirportDTO arrivalAirport;
    private LocalDateTime departureTime;
    private LocalDateTime returnTime;
    private Long price;

}
