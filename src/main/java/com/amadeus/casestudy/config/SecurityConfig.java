package com.amadeus.casestudy.config;


import com.amadeus.casestudy.security.DomainUserDetailsService;
import com.amadeus.casestudy.security.JwtAuthenticationEntryPoint;
import com.amadeus.casestudy.security.JwtRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtRequestFilter jwtRequestFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> requests
                                .requestMatchers(
                        new AntPathRequestMatcher("/api/user/authenticate")).permitAll()
                                .requestMatchers(
                        new AntPathRequestMatcher("/api/user/register")).permitAll()
                                .requestMatchers(
                        new AntPathRequestMatcher("/h2/**")).permitAll()
                                .requestMatchers(
                        new AntPathRequestMatcher("/swagger-ui/**")).permitAll()
                                .requestMatchers(
                        new AntPathRequestMatcher("/v3/api-docs/**")).permitAll()
                                .anyRequest().authenticated()

                )
                .headers(HeadersConfigurer::disable)
                .httpBasic(Customizer.withDefaults());
        http.exceptionHandling((exception)-> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint));
        http.sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
