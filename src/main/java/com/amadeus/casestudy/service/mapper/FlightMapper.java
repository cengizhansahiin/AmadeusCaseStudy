package com.amadeus.casestudy.service.mapper;

import com.amadeus.casestudy.domain.Flight;
import com.amadeus.casestudy.service.dtos.FlightDTO;
import com.amadeus.casestudy.service.dtos.FlightUpdateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = AirportMapper.class)
public interface FlightMapper {

    @Mapping(target = "id", ignore = true)
    Flight flightDTOtoFlight(FlightDTO flightDTO);

    FlightDTO flightToFlightDTO(Flight flight);

}
