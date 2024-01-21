package com.amadeus.casestudy.service;


import com.amadeus.casestudy.repository.AirportRepository;
import com.amadeus.casestudy.repository.FlightRepository;
import com.amadeus.casestudy.service.dtos.FlightDTO;
import com.amadeus.casestudy.service.dtos.FlightSearchRequest;
import com.amadeus.casestudy.service.dtos.FlightUpdateDTO;
import com.amadeus.casestudy.service.mapper.AirportMapper;
import com.amadeus.casestudy.service.mapper.FlightMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;

    private final AirportRepository airportRepository;

    private final FlightMapper flightMapper;

    private final AirportMapper airportMapper;



    public Long create(FlightDTO flightDTO){
        var flight = flightMapper.flightDTOtoFlight(flightDTO);
        airportRepository.save(flight.getDepartureAirport());
        airportRepository.save(flight.getArrivalAirport());
        return flightRepository.save(flight).getId();
    }


    public void update(FlightUpdateDTO flightDTO) throws Exception {

        var flight = flightRepository.findById(flightDTO.getId()).orElseThrow(() -> new Exception("Cannot find the flight"));

        if(flightDTO.getFlightDTO().getDepartureTime() != null) flight.setDepartureTime(flightDTO.getFlightDTO().getDepartureTime());
        if(flightDTO.getFlightDTO().getReturnTime() != null) flight.setReturnTime(flightDTO.getFlightDTO().getReturnTime());
        if(flightDTO.getFlightDTO().getArrivalAirport() != null){
            var airport = airportMapper.airportDTOtoAirport(flightDTO.getFlightDTO().getArrivalAirport());
            airportRepository.save(airport);
            flight.setArrivalAirport(airport);
        }
        if(flightDTO.getFlightDTO().getDepartureAirport() != null){
            var airport = airportMapper.airportDTOtoAirport(flightDTO.getFlightDTO().getDepartureAirport());
            airportRepository.save(airport);
            flight.setDepartureAirport(airport);
        }
        if(flightDTO.getFlightDTO().getPrice() != null) flight.setPrice(flightDTO.getFlightDTO().getPrice());
        flightRepository.save(flight);

    }
    public List<FlightDTO> getAll(){
        return flightRepository.findAll().stream().map(flightMapper::flightToFlightDTO).toList();
    }
    public void delete(Long id){
        flightRepository.deleteById(id);
    }
    public List<FlightDTO> search(FlightSearchRequest flightSearchRequest){
        if(flightSearchRequest.getReturnTime() != null){
            var returnFlight = FlightSearchRequest.builder()
                    .departureAirport(flightSearchRequest.getArrivalAirport())
                    .arrivalAirport(flightSearchRequest.getDepartureAirport())
                    .departureTime(flightSearchRequest.getReturnTime())
                    .build();
            var flights = flightRepository.findBySearch(flightSearchRequest).stream().map(flightMapper::flightToFlightDTO).toList();
            var returnFlights = flightRepository.findReturn(returnFlight).stream().map(flightMapper::flightToFlightDTO).toList();
            return Stream.concat(flights.stream(), returnFlights.stream()).toList();
        }
        return flightRepository.findBySearch(flightSearchRequest).stream().map(flightMapper::flightToFlightDTO).toList();

    }
}
