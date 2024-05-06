package com.pintor.api_gateway.filter;

import com.pintor.api_gateway.auth.response.MemberPassport;
import com.pintor.api_gateway.auth.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    private final AuthService authService;

    public AuthorizationHeaderFilter(AuthService authService) {
        super(Config.class);
        this.authService = authService;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            ServerHttpRequest request = exchange.getRequest();
            HttpHeaders headers = request.getHeaders();

            String bearerToken = headers.getFirst(HttpHeaders.AUTHORIZATION);
            MemberPassport passport = this.authService.getMemberPassport(bearerToken);

            request.mutate()
                    .header("X-Member-Id", passport.id().toString())
                    .header("X-Member-Email", passport.email())
                    .header("X-Member-Role", passport.role())
                    .build();

            exchange.mutate()
                    .request(request)
                    .build();

            return chain.filter(exchange);
        };
    }

    public static class Config {
    }
}
