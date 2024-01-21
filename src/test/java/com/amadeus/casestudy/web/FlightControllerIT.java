package com.amadeus.casestudy.web;


import com.amadeus.casestudy.domain.Flight;
import com.amadeus.casestudy.repository.AirportRepository;
import com.amadeus.casestudy.repository.FlightRepository;
import com.amadeus.casestudy.repository.UserRepository;
import com.amadeus.casestudy.service.dtos.*;
import com.amadeus.casestudy.service.mapper.FlightMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
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
public class FlightControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private FlightMapper flightMapper;
    private final SoftAssertions softAssertions;
    @Autowired
    private AirportRepository airportRepository;
    @Autowired
    private UserRepository userRepository;

    public FlightControllerIT(){
        this.softAssertions = new SoftAssertions();
    }

    @Test
    public void shouldCreateFlightWhenUserSendCreateRequest() throws Exception {
        //Given (Valid flight information along with airport information)

        var flightDTO = FlightDTO.builder()
                .departureAirport(AirportDTO.builder().city("Izmir").build())
                .arrivalAirport(AirportDTO.builder().city("Istanbul").build())
                .departureTime(LocalDateTime.of(2024, 11, 10, 8, 0))
                .returnTime(LocalDateTime.of(2024, 11, 10, 9, 40))
                .price(1500L)
                .build();

        //When (User sends flight creation request

        var response = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/flight/create")
                .header("Authorization","Bearer " + authenticate())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(flightDTO))).andReturn().getResponse();


        //Then (Return code of created http status code)

        softAssertions.assertThat(response.getStatus()).isEqualTo(201);
        softAssertions.assertAll();

    }
    @Test
    public void shouldDeleteFlightWhenUserSendDeleteRequest() throws Exception {
        //Given (Pre-loaded flight information)

        var flightId = createMockFlight().getId();

        //When (User sends flight deletion request)

        var response = mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/flight/delete")
                .header("Authorization","Bearer " + authenticate())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(flightId))).andReturn().getResponse();

        //Then (Return code of ok status code)

        softAssertions.assertThat(response.getStatus()).isEqualTo(200);
        softAssertions.assertThat(flightRepository.findById(flightId).isPresent()).isEqualTo(false);
        softAssertions.assertAll();
    }

    @Test
    public void shouldUpdateFlightWhenUserSendUpdateRequest() throws Exception {
        //Given (Pre-loaded flight information and updated version of the flight information)

        var flight = createMockFlight();

        var flightUpdateDTO = FlightUpdateDTO.builder()
                .id(flight.getId())
                .flightDTO(FlightDTO.builder().arrivalAirport(AirportDTO.builder().city("Samsun").build()).build())
                .build();

        //When (User sends flight update request)

        var response = mockMvc.perform(MockMvcRequestBuilders
                .put("/api/flight/update")
                .header("Authorization","Bearer " + authenticate())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(flightUpdateDTO))).andReturn().getResponse();

        //Then (Return code of ok status code)

        softAssertions.assertThat(response.getStatus()).isEqualTo(200);
        softAssertions.assertAll();

    }

    @Test
    public void shouldGetFlightsWhenUserSendReadRequest() throws Exception {
        //Given (Pre-loaded flight information and updated version of the flight information)

        createMockFlight();

        //When (User sends flight update request)

        var response = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/flight/")
                .header("Authorization","Bearer " + authenticate())
        ).andReturn().getResponse();

        //Then (Response code of ok status code)

        softAssertions.assertThat(response.getStatus()).isEqualTo(200);
        softAssertions.assertThat(objectMapper.readValue(response.getContentAsString(), Flight[].class) != null)
                .isEqualTo(true);
        softAssertions.assertAll();
    }

    @Test
    public void shouldGetCorrespondingFlightsWhenUserSendSearchRequest() throws Exception {
        //Given (Pre-loaded flight information along with return flight

        var departureFlight = createMockFlight();

        var returnFlight = createMockFlight(FlightDTO.builder()
                .price(departureFlight.getPrice())
                .arrivalAirport(AirportDTO.builder().city(departureFlight.getDepartureAirport().getCity()).build())
                .departureAirport(AirportDTO.builder().city(departureFlight.getArrivalAirport().getCity()).build())
                .departureTime(departureFlight.getReturnTime())
                .returnTime(null).build());

        var flightSearchRequest = FlightSearchRequest.builder()
                .departureAirport(departureFlight.getDepartureAirport().getCity())
                .arrivalAirport(departureFlight.getArrivalAirport().getCity())
                .departureTime(departureFlight.getDepartureTime())
                .returnTime(departureFlight.getReturnTime())
                .build();

        //When (User sends flight search request)

        var response = mockMvc.perform(MockMvcRequestBuilders.post("/api/flight/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(flightSearchRequest))
                .header("Authorization","Bearer " + authenticate())
                )
                .andReturn().getResponse();

        //Then (Return code of ok status code)

        var shouldReturn = objectMapper.readValue(response.getContentAsString(), Flight[].class);
        softAssertions.assertThat(response.getStatus()).isEqualTo(200);
        softAssertions.assertAll();

    }

    private Flight createMockFlight(){
        var flightDTO = FlightDTO.builder()
                .departureAirport(AirportDTO.builder().city("Izmir").build())
                .arrivalAirport(AirportDTO.builder().city("Istanbul").build())
                .departureTime(LocalDateTime.of(2024, 11, 10, 8, 0))
                .returnTime(LocalDateTime.of(2024, 11, 10, 9, 40))
                .price(1500L)
                .build();

        var flight = flightMapper.flightDTOtoFlight(flightDTO);
        airportRepository.save(flight.getDepartureAirport());
        airportRepository.save(flight.getArrivalAirport());
        flightRepository.save(flight);

        return flight;
    }
    private Flight createMockFlight(FlightDTO flightDTO){

        var flight = flightMapper.flightDTOtoFlight(flightDTO);
        airportRepository.save(flight.getDepartureAirport());
        airportRepository.save(flight.getArrivalAirport());
        flightRepository.save(flight);

        return flight;
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
        flightRepository.deleteAll();
        airportRepository.deleteAll();
        userRepository.deleteAll();
    }

}
