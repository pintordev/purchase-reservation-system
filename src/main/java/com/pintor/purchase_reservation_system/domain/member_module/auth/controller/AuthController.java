package com.pintor.purchase_reservation_system.domain.member_module.auth.controller;

import com.pintor.purchase_reservation_system.common.response.ResData;
import com.pintor.purchase_reservation_system.common.response.SuccessCode;
import com.pintor.purchase_reservation_system.domain.member_module.auth.request.AuthLoginRequest;
import com.pintor.purchase_reservation_system.domain.member_module.auth.request.AuthVerifyMailRequest;
import com.pintor.purchase_reservation_system.domain.member_module.auth.response.AuthLoginResponse;
import com.pintor.purchase_reservation_system.domain.member_module.auth.service.AuthService;
import com.pintor.purchase_reservation_system.domain.member_module.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping(value = "/api/auth", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;
    private final MemberService memberService;

    @PostMapping(value = "/verify/mail", consumes = MediaType.ALL_VALUE)
    public ResponseEntity verifyMail(@Valid @RequestBody AuthVerifyMailRequest request, BindingResult bindingResult) {

        Long memberId = this.authService.verifyMailToken(request, bindingResult);
        this.memberService.verifyEmail(memberId);

        ResData resData = ResData.of(
                SuccessCode.VERIFY_MAIL
        );
        return ResponseEntity
                .status(resData.getStatus())
                .body(resData);
    }

    @PostMapping(value = "/login")
    public ResponseEntity login(@Valid @RequestBody AuthLoginRequest request, BindingResult bindingResult,
                                HttpServletResponse response) {

        log.info("login request: {}", request);

        AuthLoginResponse authLoginResponse = this.authService.login(request, bindingResult);

        response.setHeader("Authorization", authLoginResponse.getAccessToken());
        response.setHeader("Refresh", authLoginResponse.getRefreshToken());
        ResData resData = ResData.of(
                SuccessCode.LOGIN
        );
        return ResponseEntity
                .status(resData.getStatus())
                .body(resData);
    }

    @PostMapping(value = "/logout", consumes = MediaType.ALL_VALUE)
    public ResponseEntity logout(HttpServletRequest request) {

        String accessToken = request.getHeader("Authorization").substring("Bearer ".length());
        this.authService.logout(accessToken);

        ResData resData = ResData.of(
                SuccessCode.LOGOUT
        );
        return ResponseEntity
                .status(resData.getStatus())
                .body(resData);
    }
}
