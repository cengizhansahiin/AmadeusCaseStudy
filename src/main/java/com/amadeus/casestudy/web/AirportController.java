package com.amadeus.casestudy.web;


import com.amadeus.casestudy.service.AirportService;
import com.amadeus.casestudy.service.dtos.AirportDTO;
import com.amadeus.casestudy.service.dtos.AirportUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/airport")
@RequiredArgsConstructor
public class AirportController {

    private final AirportService airportService;

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody AirportDTO airportDTO){
        try{
            var airportId = airportService.create(airportDTO);
            return ResponseEntity.created(URI.create("/api/flight/" + airportId)).body("Created");
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestBody AirportUpdateDTO airportDTO){
        try{
            airportService.update(airportDTO);
            return ResponseEntity.ok().build();
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/")
    public List<AirportDTO> getAll(){
        return airportService.getAll();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestBody Long id){
        try{
            airportService.delete(id);
            return ResponseEntity.ok().build();
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
