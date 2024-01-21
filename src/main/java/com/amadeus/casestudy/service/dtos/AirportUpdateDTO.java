package com.amadeus.casestudy.service.dtos;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AirportUpdateDTO {

    private Long id;

    private AirportDTO airportDTO;
}
