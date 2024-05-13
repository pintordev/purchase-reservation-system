package com.pintor.api_gateway.config;

import com.pintor.api_gateway.filter.AuthorizationHeaderFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@RequiredArgsConstructor
@Configuration
public class FilterConfig {

    private final String memberModuleUrl = "lb://MEMBER-MODULE";
    private final String productModuleUrl = "lb://PRODUCT-MODULE";
    private final String purchaseModuleUrl = "lb://PURCHASE-MODULE";
    private final String paymentModuleUrl = "lb://PAYMENT-MODULE";

    private final AuthorizationHeaderFilter authorizationHeaderFilter;

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/member_module/members")
                        .and().method(HttpMethod.POST)
                        .filters(f -> f.rewritePath("/member_module/(?<segment>.*)", "/api/${segment}"))
                        .uri(memberModuleUrl)
                )
                .route(r -> r.path("/member_module/members/password")
                        .and().method(HttpMethod.POST)
                        .filters(f -> f.rewritePath("/member_module/(?<segment>.*)", "/api/${segment}"))
                        .uri(memberModuleUrl)
                )
                .route(r -> r.path("/member_module/auth/login")
                        .and().method(HttpMethod.POST)
                        .filters(f -> f.rewritePath("/member_module/(?<segment>.*)", "/api/${segment}"))
                        .uri(memberModuleUrl)
                )
                .route(r -> r.path("/member_module/auth/login/mail")
                        .and().method(HttpMethod.POST)
                        .filters(f -> f.rewritePath("/member_module/(?<segment>.*)", "/api/${segment}"))
                        .uri(memberModuleUrl)
                )
                .route(r -> r.path("/member_module/auth/refresh")
                        .and().method(HttpMethod.POST)
                        .filters(f -> f.rewritePath("/member_module/(?<segment>.*)", "/api/${segment}"))
                        .uri(memberModuleUrl)
                )
                .route(r -> r.path("/member_module/auth/verify/mail")
                        .and().method(HttpMethod.POST)
                        .filters(f -> f.rewritePath("/member_module/(?<segment>.*)", "/api/${segment}"))
                        .uri(memberModuleUrl)
                )
                .route(r -> r.path("/member_module/**")
                        .filters(f -> f.rewritePath("/member_module/(?<segment>.*)", "/api/${segment}")
                                .filter(authorizationHeaderFilter.apply(new AuthorizationHeaderFilter.Config()))
                                .removeRequestHeader("Authorization"))
                        .uri(memberModuleUrl)
                )
                .route(r -> r.path("/product_module/**")
                        .filters(f -> f.rewritePath("/product_module/(?<segment>.*)", "/api/${segment}"))
                        .uri(productModuleUrl)
                )
                .route(r -> r.path("/purchase_module/**")
                        .filters(f -> f.rewritePath("/purchase_module/(?<segment>.*)", "/api/${segment}")
                                .filter(authorizationHeaderFilter.apply(new AuthorizationHeaderFilter.Config()))
                                .removeRequestHeader("Authorization"))
                        .uri(purchaseModuleUrl)
                )
                .route(r -> r.path("/payment_module/**")
                        .filters(f -> f.rewritePath("/payment_module/(?<segment>.*)", "/api/${segment}"))
                        .uri(paymentModuleUrl)
                )
                .build();
    }
}
