package com.amadeus.casestudy.service.dtos;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private String username;
    private String password;

}
