package com.amadeus.casestudy.service.mapper;


import com.amadeus.casestudy.domain.User;
import com.amadeus.casestudy.service.dtos.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User userDTOtoUser(UserDTO userDTO);
}
