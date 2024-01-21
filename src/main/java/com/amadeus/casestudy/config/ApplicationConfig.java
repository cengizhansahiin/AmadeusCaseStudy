package com.amadeus.casestudy.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableScheduling
public class ApplicationConfig {

    @Bean
    @Primary
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
