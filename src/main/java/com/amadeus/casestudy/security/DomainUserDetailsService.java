package com.amadeus.casestudy.security;


import com.amadeus.casestudy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component("userDetailService")
@RequiredArgsConstructor
public class DomainUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findOneByUsername(username)
                .map(x -> new User(x.getUsername(), x.getPassword(), new ArrayList<>()))
                .orElseThrow(() -> new UsernameNotFoundException("Username not found in the database"));
        }
    }

