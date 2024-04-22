package com.pintor.purchase_reservation_system.domain.member_module.member.controller;

import com.pintor.purchase_reservation_system.common.response.ResData;
import com.pintor.purchase_reservation_system.common.response.SuccessCode;
import com.pintor.purchase_reservation_system.common.service.MailService;
import com.pintor.purchase_reservation_system.domain.member_module.auth.service.AuthService;
import com.pintor.purchase_reservation_system.domain.member_module.member.entity.Member;
import com.pintor.purchase_reservation_system.domain.member_module.member.request.MemberPasswordUpdateRequest;
import com.pintor.purchase_reservation_system.domain.member_module.member.request.MemberProfileUpdateRequest;
import com.pintor.purchase_reservation_system.domain.member_module.member.request.MemberSignupRequest;
import com.pintor.purchase_reservation_system.domain.member_module.member.response.MemberSignupResponse;
import com.pintor.purchase_reservation_system.domain.member_module.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping(value = "/api/members", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;
    private final AuthService authService;
    private final MailService mailService;

    @PostMapping
    public ResponseEntity signup(@Valid @RequestBody MemberSignupRequest request, BindingResult bindingResult) {

        log.info("signup request: {}", request);

        Member member = this.memberService.signup(request, bindingResult);
        String code = this.authService.saveMailToken(member.getId());
        this.mailService.sendVerificationCode(member.getEmail(), code);

        ResData resData = ResData.of(
                SuccessCode.SIGNUP,
                MemberSignupResponse.of(member)
        );
        return ResponseEntity
                .status(resData.getStatus())
                .body(resData);
    }

    @PatchMapping
    public ResponseEntity profileUpdate(@Valid @RequestBody MemberProfileUpdateRequest request, BindingResult bindingResult,
                                        @AuthenticationPrincipal User user) {

        log.info("profile update request: {}", request);

        this.memberService.profileUpdate(request, bindingResult, user);

        ResData resData = ResData.of(
                SuccessCode.PROFILE_UPDATE
        );
        return ResponseEntity
                .status(resData.getStatus())
                .body(resData);
    }

    @PatchMapping(value = "/password")
    public ResponseEntity passwordUpdate(@Valid @RequestBody MemberPasswordUpdateRequest request, BindingResult bindingResult,
                                         @AuthenticationPrincipal User user) {

        log.info("password update request: {}", request);

        this.memberService.passwordUpdate(request, bindingResult, user);

        ResData resData = ResData.of(
                SuccessCode.PASSWORD_UPDATE
        );
        return ResponseEntity
                .status(resData.getStatus())
                .body(resData);
    }
}
