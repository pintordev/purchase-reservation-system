package com.pintor.purchase_reservation_system.domain.member_module.auth.controller;

import com.pintor.purchase_reservation_system.common.response.ResData;
import com.pintor.purchase_reservation_system.common.response.SuccessCode;
import com.pintor.purchase_reservation_system.domain.member_module.auth.service.AuthService;
import com.pintor.purchase_reservation_system.domain.member_module.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
