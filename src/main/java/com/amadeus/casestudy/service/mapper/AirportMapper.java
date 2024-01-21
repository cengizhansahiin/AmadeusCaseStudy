package com.amadeus.casestudy.service.mapper;

import com.amadeus.casestudy.domain.Airport;
import com.amadeus.casestudy.service.dtos.AirportDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AirportMapper {

    @Mapping(target = "id", ignore = true)
    Airport airportDTOtoAirport(AirportDTO airportDTO);

    AirportDTO airportToAirportDTO(Airport airport);
}
