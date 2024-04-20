package com.pintor.purchase_reservation_system.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .securityMatcher("/api/**")
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                                .requestMatchers(HttpMethod.POST, "/api/members").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/auth/mail").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                                .anyRequest().authenticated()
                )
                .cors(cors -> cors
                        .disable()
                )
                .csrf(csrf -> csrf
                        .disable()
                )
                .httpBasic(httpBasic -> httpBasic
                        .disable()
                )
                .formLogin(formLogin -> formLogin
                        .disable()
                )
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
        ;

        return http.build();
    }
}
