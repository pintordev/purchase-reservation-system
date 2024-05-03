package com.pintor.product_module.common.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EndpointEntries {

    private final List<AntPathRequestMatcher> permitAllEntries;
    private final AntPathRequestMatcher internalEntry;

    private EndpointEntries() {
        this.permitAllEntries = List.of(
                AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/products"),
                AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/products/*")
        );
        this.internalEntry = AntPathRequestMatcher.antMatcher("/api/internal/**");
    }

    public boolean isPermitAll(HttpServletRequest request) {
        return this.permitAllEntries.stream().anyMatch(matcher -> matcher.matches(request));
    }

    public boolean isInternal(HttpServletRequest request) {
        return this.internalEntry.matches(request);
    }
}