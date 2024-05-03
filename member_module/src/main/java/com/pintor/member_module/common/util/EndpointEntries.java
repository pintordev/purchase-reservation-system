package com.pintor.member_module.common.util;

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
                AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/members"),
                AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/members/password"),
                AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/auth/verify/mail"),
                AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/auth/login"),
                AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/auth/login/mail"),
                AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/auth/refresh")
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