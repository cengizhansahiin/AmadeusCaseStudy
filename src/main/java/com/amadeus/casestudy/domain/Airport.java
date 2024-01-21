package com.amadeus.casestudy.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "airport")
@RequiredArgsConstructor
@Getter
@Setter
public class Airport extends BaseEntity{

    @Column(name = "city")
    private String city;
}
