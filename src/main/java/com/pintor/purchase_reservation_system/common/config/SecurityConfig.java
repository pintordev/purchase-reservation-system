package com.pintor.purchase_reservation_system.common.config;

import com.pintor.purchase_reservation_system.common.errors.exception_hanlder.ApiAuthenticationExceptionHandler;
import com.pintor.purchase_reservation_system.common.errors.exception_hanlder.ApiAuthorizationExceptionHandler;
import com.pintor.purchase_reservation_system.common.filter.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final ApiAuthenticationExceptionHandler apiAuthenticationExceptionHandler;
    private final ApiAuthorizationExceptionHandler apiAuthorizationExceptionHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .securityMatcher("/api/**")
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                                .requestMatchers(HttpMethod.POST, "/api/members").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/auth/verify/mail").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                                .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(apiAuthenticationExceptionHandler)
                        .accessDeniedHandler(apiAuthorizationExceptionHandler)
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
                .addFilterBefore(
                        this.jwtAuthFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
        ;

        return http.build();
    }
}
