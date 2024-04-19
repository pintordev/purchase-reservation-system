package com.pintor.purchase_reservation_system.domain.member_module.member.controller;

import com.pintor.purchase_reservation_system.common.response.ResData;
import com.pintor.purchase_reservation_system.common.response.SuccessCode;
import com.pintor.purchase_reservation_system.domain.member_module.member.dto.MemberDto;
import com.pintor.purchase_reservation_system.domain.member_module.member.request.MemberSignupRequest;
import com.pintor.purchase_reservation_system.domain.member_module.member.response.MemberSignupResponse;
import com.pintor.purchase_reservation_system.domain.member_module.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping(value = "/api/members", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity signup(@Valid @RequestBody MemberSignupRequest request, BindingResult bindingResult) {

        log.info("signup request: {}", request);

        MemberDto memberDto = this.memberService.signup(request, bindingResult);

        ResData resData = ResData.of(
                SuccessCode.SIGNUP,
                MemberSignupResponse.of(memberDto)
        );
        return ResponseEntity
                .status(resData.getStatus())
                .body(resData);
    }
}
