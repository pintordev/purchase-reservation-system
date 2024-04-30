package com.pintor.product_module.common.config;

import com.pintor.product_module.common.errors.exception_hanlder.ApiAuthenticationExceptionHandler;
import com.pintor.product_module.common.errors.exception_hanlder.ApiAuthorizationExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final ApiAuthenticationExceptionHandler apiAuthenticationExceptionHandler;
    private final ApiAuthorizationExceptionHandler apiAuthorizationExceptionHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .securityMatcher("/api/**")
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers(HttpMethod.GET, "/api/internal/products/*").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/internal/stocks/decrease/all").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/internal/stocks/increase/all").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/products").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/products/*").permitAll()
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
        ;

        return http.build();
    }
}
