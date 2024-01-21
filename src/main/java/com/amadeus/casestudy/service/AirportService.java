package com.amadeus.casestudy.service;


import com.amadeus.casestudy.repository.AirportRepository;
import com.amadeus.casestudy.service.dtos.AirportDTO;
import com.amadeus.casestudy.service.dtos.AirportUpdateDTO;
import com.amadeus.casestudy.service.mapper.AirportMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AirportService {

    private final AirportRepository airportRepository;

    private final AirportMapper airportMapper;

    public Long create(AirportDTO airportDTO){
        return airportRepository.save(airportMapper.airportDTOtoAirport(airportDTO)).getId();
    }
    public void update(AirportUpdateDTO airportDTO) throws Exception {
        var airport = airportRepository.findById(airportDTO.getId()).orElseThrow(() -> new Exception("Cannot find the flight"));
        airport.setCity(airportDTO.getAirportDTO().getCity());
        airportRepository.save(airport);
    }
    public void delete(Long id){
        airportRepository.deleteById(id);
    }
    public List<AirportDTO> getAll(){
        return airportRepository.findAll().stream().map(airportMapper::airportToAirportDTO).toList();
    }

}
