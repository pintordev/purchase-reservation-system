package com.pintor.member_module.domain.auth.controller;

import com.pintor.member_module.domain.auth.service.AuthService;
import com.pintor.member_module.domain.member.response.MemberPrincipalResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping(value = "/api/internal/auth", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@RestController
public class InternalAuthController {

    private final AuthService authService;

    @PostMapping(value = "/passport", consumes = MediaType.ALL_VALUE)
    public MemberPrincipalResponse getMemberPrincipal(@RequestHeader("Authorization") String bearerToken) {
        log.info("get member principal: bearerToken={}", bearerToken);
        return this.authService.getMemberPrincipalByAuthToken(bearerToken);
    }
}
