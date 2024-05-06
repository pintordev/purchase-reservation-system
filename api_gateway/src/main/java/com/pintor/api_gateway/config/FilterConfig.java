package com.pintor.api_gateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class FilterConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/member_module/**")
                        .filters(f -> f.rewritePath("/member_module/(?<segment>.*)", "/api/${segment}"))
                        .uri("http://localhost:8081")
                )
                .route(r -> r.path("/product_module/**")
                        .filters(f -> f.rewritePath("/product_module/(?<segment>.*)", "/api/${segment}"))
                        .uri("http://localhost:8082")
                )
                .route(r -> r.path("/purchase_module/**")
                        .filters(f -> f.rewritePath("/purchase_module/(?<segment>.*)", "/api/${segment}"))
                        .uri("http://localhost:8083")
                )
                .build();
    }
}
