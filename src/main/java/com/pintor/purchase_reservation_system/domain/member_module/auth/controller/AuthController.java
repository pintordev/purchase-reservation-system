package com.pintor.purchase_reservation_system.domain.member_module.auth.controller;

import com.pintor.purchase_reservation_system.common.response.ResData;
import com.pintor.purchase_reservation_system.common.response.SuccessCode;
import com.pintor.purchase_reservation_system.domain.member_module.auth.request.AuthLoginRequest;
import com.pintor.purchase_reservation_system.domain.member_module.auth.response.AuthLoginResponse;
import com.pintor.purchase_reservation_system.domain.member_module.auth.service.AuthService;
import com.pintor.purchase_reservation_system.domain.member_module.member.service.MemberService;
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

    @GetMapping(value = "/mail", consumes = MediaType.ALL_VALUE)
    public ResponseEntity verifyMail(@RequestParam(value = "code") String code) {

        Long memberId = this.authService.verifyMailToken(code);
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
}
