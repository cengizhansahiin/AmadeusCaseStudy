package com.amadeus.casestudy.web;


import com.amadeus.casestudy.domain.Flight;
import com.amadeus.casestudy.service.FlightService;
import com.amadeus.casestudy.service.dtos.FlightDTO;
import com.amadeus.casestudy.service.dtos.FlightSearchRequest;
import com.amadeus.casestudy.service.dtos.FlightUpdateDTO;
import com.amadeus.casestudy.service.mapper.AirportMapper;
import com.amadeus.casestudy.service.mapper.FlightMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/flight")
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;


    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody FlightDTO flightDTO){
        try{
            var flightId = flightService.create(flightDTO);
            return ResponseEntity.created(URI.create("/api/flight/" + flightId)).body("Created");
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestBody FlightUpdateDTO flightDTO){
        try{
            flightService.update(flightDTO);
            return ResponseEntity.ok().build();
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/")
    public List<FlightDTO> getAll(){
        return flightService.getAll();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestBody Long id){
        try{
            flightService.delete(id);
            return ResponseEntity.ok().build();
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/search")
    public List<FlightDTO> search(@RequestBody FlightSearchRequest flightSearchRequest){
        return flightService.search(flightSearchRequest);
    }


}
