package com.amadeus.casestudy.service;


import com.amadeus.casestudy.repository.UserRepository;
import com.amadeus.casestudy.security.DomainUserDetailsService;
import com.amadeus.casestudy.security.SecurityUtil;
import com.amadeus.casestudy.service.dtos.UserDTO;
import com.amadeus.casestudy.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final SecurityUtil securityUtil;

    private final DomainUserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public Long create(UserDTO userDTO){
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return userRepository.save(userMapper.userDTOtoUser(userDTO)).getId();
    }
    public String authenticate(UserDTO userDTO) throws Exception {

        if(!isMatchingPasswords(userDTO)) throw new Exception("Incorrect password");

        var authenticationToken = new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword());
        var authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return createToken(authentication);

    }
    private String createToken(Authentication authentication){
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getName());
        return securityUtil.generateToken(userDetails);
    }
    private boolean isMatchingPasswords(UserDTO userDTO) throws Exception {
        var passwordFromDatabase = userRepository.findOneByUsername(userDTO.getUsername())
                .orElseThrow(() -> new Exception("Username not on the database"))
                .getPassword();
        return passwordEncoder.matches(userDTO.getPassword(), passwordFromDatabase);
    }
}
