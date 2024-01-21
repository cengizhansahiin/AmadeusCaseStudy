package com.amadeus.casestudy.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "flight")
@RequiredArgsConstructor
@Getter
@Setter
public class Flight extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "departure_airport_id")
    private Airport departureAirport;

    @ManyToOne
    @JoinColumn(name = "arrival_airport_id")
    private Airport arrivalAirport;

    @Column(name = "departure_time")
    private LocalDateTime departureTime;

    @Column(name = "arrival_time")
    private LocalDateTime returnTime;

    @Column(name = "price")
    private Long price;

}
