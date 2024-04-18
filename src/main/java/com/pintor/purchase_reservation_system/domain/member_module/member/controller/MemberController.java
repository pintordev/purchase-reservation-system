package com.pintor.purchase_reservation_system.domain.member_module.member.controller;

import com.pintor.purchase_reservation_system.common.response.ResData;
import com.pintor.purchase_reservation_system.common.response.SuccessCode;
import com.pintor.purchase_reservation_system.domain.member_module.member.entity.Member;
import com.pintor.purchase_reservation_system.domain.member_module.member.request.MemberSignupRequest;
import com.pintor.purchase_reservation_system.domain.member_module.member.response.MemberSignupResponse;
import com.pintor.purchase_reservation_system.domain.member_module.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/api/members", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity signup(@Valid @RequestBody MemberSignupRequest request, BindingResult bindingResult) {

        Member member = this.memberService.signup(request, bindingResult);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResData.of(
                        SuccessCode.SIGNUP,
                        MemberSignupResponse.of(member)
                ));
    }
}
