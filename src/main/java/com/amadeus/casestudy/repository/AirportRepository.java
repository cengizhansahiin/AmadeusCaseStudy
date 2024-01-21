package com.amadeus.casestudy.repository;

import com.amadeus.casestudy.domain.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AirportRepository extends JpaRepository<Airport, Long> {
}