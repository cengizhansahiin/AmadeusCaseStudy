package com.amadeus.casestudy.service.job;

import com.amadeus.casestudy.service.FlightService;
import com.amadeus.casestudy.service.dtos.FlightDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class FetchFlightJob {

    private final FlightService flightService;

    private final String apiUrl = "http://localhost:8081/flights/";

    private final RestTemplate restTemplate;

    @Scheduled(cron = "0 0 0 * * *")
    public void fetchFlights(){
        var flights = restTemplate.getForObject(apiUrl, FlightDTO[].class);
        if(flights != null) Arrays.stream(flights).map(flightService::create).toList();
    }

}
