package com.pintor.payment_module.common.config;

import com.pintor.payment_module.common.filter.ExceptionHandlingFilter;
import com.pintor.payment_module.common.filter.ServerAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final ServerAuthFilter serverAuthFilter;
    private final ExceptionHandlingFilter exceptionHandlingFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
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
                        serverAuthFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
                .addFilterBefore(
                        exceptionHandlingFilter,
                        ServerAuthFilter.class
                )
        ;

        return http.build();
    }
}
