package com.pintor.purchase_module.common.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EndpointEntries {

    private final List<AntPathRequestMatcher> permitAllEntries;
    private final AntPathRequestMatcher internalEntry;

    private EndpointEntries() {
        this.permitAllEntries = List.of();
        this.internalEntry = AntPathRequestMatcher.antMatcher("/api/internal/**");
    }

    public boolean isPermitAll(HttpServletRequest request) {
        return this.permitAllEntries.stream().anyMatch(matcher -> matcher.matches(request));
    }

    public boolean isInternal(HttpServletRequest request) {
        return this.internalEntry.matches(request);
    }
}