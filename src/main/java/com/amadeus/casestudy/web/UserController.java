package com.amadeus.casestudy.web;


import com.amadeus.casestudy.service.UserService;
import com.amadeus.casestudy.service.dtos.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody UserDTO userDTO){
        try{
            String token = userService.authenticate(userDTO);
            return ResponseEntity.ok(token);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/register")
    public ResponseEntity<String> create(@RequestBody UserDTO userDTO){
        try{
            var userId = userService.create(userDTO);
            return ResponseEntity.created(URI.create("/api/user/" + userId)).body("Created");
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Bad request");
        }
    }


}
