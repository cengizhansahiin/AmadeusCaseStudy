package com.amadeus.casestudy.service.dtos;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightUpdateDTO {
    private Long id;
    FlightDTO flightDTO;
}
