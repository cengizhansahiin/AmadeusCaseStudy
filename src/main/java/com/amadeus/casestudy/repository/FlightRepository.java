package com.amadeus.casestudy.repository;

import com.amadeus.casestudy.domain.Flight;
import com.amadeus.casestudy.service.dtos.FlightSearchRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    @Query("SELECT f FROM Flight f WHERE " +
            "(f.arrivalAirport.city = :#{#searchFlight.arrivalAirport} AND " +
            "f.departureAirport.city = :#{#searchFlight.departureAirport} AND " +
            "f.departureTime = :#{#searchFlight.departureTime} AND " +
            "f.returnTime = :#{#searchFlight.returnTime})")
    List<Flight> findBySearch(@Param("searchFlight") FlightSearchRequest flightSearchRequest);

    @Query("SELECT f FROM Flight f WHERE " +
            "(f.arrivalAirport.city = :#{#searchFlight.arrivalAirport} AND " +
            "f.departureAirport.city = :#{#searchFlight.departureAirport} AND " +
            "f.departureTime = :#{#searchFlight.departureTime})")
    List<Flight> findReturn(@Param("searchFlight") FlightSearchRequest flightSearchRequest);


}
