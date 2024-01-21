package com.amadeus.casestudy.web;

import com.amadeus.casestudy.domain.Airport;
import com.amadeus.casestudy.domain.Flight;
import com.amadeus.casestudy.repository.AirportRepository;
import com.amadeus.casestudy.repository.FlightRepository;
import com.amadeus.casestudy.repository.UserRepository;
import com.amadeus.casestudy.service.dtos.*;
import com.amadeus.casestudy.service.mapper.AirportMapper;
import com.amadeus.casestudy.service.mapper.FlightMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SoftAssertionsExtension.class)
@TestExecutionListeners(
        value = {
                MockitoTestExecutionListener.class,
                DependencyInjectionTestExecutionListener.class,
                SqlScriptsTestExecutionListener.class,
        }
)
public class AirportControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private final SoftAssertions softAssertions;
    @Autowired
    private AirportRepository airportRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AirportMapper airportMapper;

    public AirportControllerIT(){
        this.softAssertions = new SoftAssertions();
    }

    @Test
    public void shouldCreateAirportWhenUserSendCreateRequest() throws Exception {
        //Given (Valid flight information along with airport information)

        var airportDTO = AirportDTO.builder().city("Samsun").build();

        //When (User sends flight creation request

        var response = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/airport/create")
                .header("Authorization","Bearer " + authenticate())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(airportDTO))).andReturn().getResponse();


        //Then (Return code of created http status code)

        softAssertions.assertThat(response.getStatus()).isEqualTo(201);
        softAssertions.assertAll();

    }
    @Test
    public void shouldDeleteAirportWhenUserSendDeleteRequest() throws Exception {
        //Given (Pre-loaded flight information)

        var airportId = createMockAirport().getId();

        //When (User sends flight deletion request)

        var response = mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/airport/delete")
                .header("Authorization","Bearer " + authenticate())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(airportId))).andReturn().getResponse();

        //Then (Return code of ok status code)

        softAssertions.assertThat(response.getStatus()).isEqualTo(200);
        softAssertions.assertThat(airportRepository.findById(airportId).isPresent()).isEqualTo(false);
        softAssertions.assertAll();
    }

    @Test
    public void shouldUpdateAirportWhenUserSendUpdateRequest() throws Exception {
        //Given (Pre-loaded flight information and updated version of the flight information)

        var airport = createMockAirport();

        var airportUpdateDTO = AirportUpdateDTO.builder()
                .id(airport.getId())
                .airportDTO(AirportDTO.builder().city("Izmir").build())
                .build();

        //When (User sends flight update request)

        var response = mockMvc.perform(MockMvcRequestBuilders
                .put("/api/airport/update")
                .header("Authorization","Bearer " + authenticate())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(airportUpdateDTO))).andReturn().getResponse();

        //Then (Return code of ok status code)

        softAssertions.assertThat(response.getStatus()).isEqualTo(200);
        softAssertions.assertAll();

    }

    @Test
    public void shouldGetAirportWhenUserSendReadRequest() throws Exception {
        //Given (Pre-loaded flight information and updated version of the flight information)

        createMockAirport();

        //When (User sends flight update request)

        var response = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/airport/")
                .header("Authorization","Bearer " + authenticate())
        ).andReturn().getResponse();

        //Then (Response code of ok status code)

        softAssertions.assertThat(response.getStatus()).isEqualTo(200);
        softAssertions.assertThat(objectMapper.readValue(response.getContentAsString(), Airport[].class) != null)
                .isEqualTo(true);
        softAssertions.assertAll();
    }


    private Airport createMockAirport(){
        var airportDTO = AirportDTO.builder()
                .city("Istanbul")
                .build();

        return airportRepository.save(airportMapper.airportDTOtoAirport(airportDTO));
    }
    private Airport createMockAirport(AirportDTO airportDTO){

        return airportRepository.save(airportMapper.airportDTOtoAirport(airportDTO));
    }

    private String authenticate() throws Exception {
        var user = UserDTO.builder().username("cengizhan").password("password").build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/register").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user))).andReturn();

        return mockMvc.perform(MockMvcRequestBuilders.post("/api/user/authenticate").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user))).andReturn().getResponse().getContentAsString();
    }

    @BeforeEach
    public void beforeTests(){
        airportRepository.deleteAll();
        userRepository.deleteAll();
    }
}
